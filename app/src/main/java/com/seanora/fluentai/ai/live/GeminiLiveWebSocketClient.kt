package com.seanora.fluentai.ai.live

import com.seanora.fluentai.ai.live.auth.LiveCredential
import com.seanora.fluentai.ai.live.auth.LiveCredentialProvider
import com.seanora.fluentai.ai.live.auth.LiveEndpoint
import com.seanora.fluentai.ai.live.protocol.LiveClientMessageFactory
import com.seanora.fluentai.ai.live.protocol.LiveCoachingTools
import com.seanora.fluentai.ai.live.protocol.LiveConnectionErrorMapper
import com.seanora.fluentai.ai.live.protocol.LiveServerMessageParser
import com.seanora.fluentai.ai.live.protocol.LiveServerSignal
import com.seanora.fluentai.ai.live.protocol.LiveSetupMessageFactory
import com.seanora.fluentai.core.common.logging.AppLog
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withTimeout
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import java.io.IOException
import java.util.Base64
import java.util.concurrent.CancellationException
import java.util.concurrent.TimeoutException

class GeminiLiveWebSocketClient(
    dispatcher: CoroutineDispatcher,
    private val credentialProvider: LiveCredentialProvider,
    private val webSocketFactory: WebSocket.Factory,
    private val setupTimeoutMs: Long = DEFAULT_SETUP_TIMEOUT_MS,
    private val reconnectPolicy: LiveReconnectPolicy = LiveReconnectPolicy(),
    modelName: String = DEFAULT_MODEL
) : LiveAiClient {

    companion object {
        const val DEFAULT_MODEL = "models/gemini-3.8-live"
        const val DEFAULT_SETUP_TIMEOUT_MS = 15_000L
        private const val TAG = "GeminiLiveClient"
        private const val EVENT_BUFFER_CAPACITY = 512
        private const val TOOL_RESULT_LOGGED = "logged"
        private const val TOOL_RESULT_IGNORED = "ignored"
    }

    private val scope = CoroutineScope(SupervisorJob() + dispatcher)
    private val setupMessageFactory = LiveSetupMessageFactory(modelName)
    private val messageParser = LiveServerMessageParser()
    private val connectionMutex = Mutex()
    private val lock = Any()

    private var activeSocket: WebSocket? = null
    private var generation: Long = 0L
    private var setupDeferred: CompletableDeferred<Result<Unit>>? = null
    private var currentConfig: LiveScenarioConfig? = null
    private var resumptionHandle: String? = null
    private var sessionEstablished = false
    private var clientClosing = false
    private var reconnectJob: Job? = null

    @Volatile
    private var discardModelOutput = false

    @Volatile
    private var droppedEventCount = 0

    private val _connectionState = MutableStateFlow(LiveConnectionState.DISCONNECTED)
    override val connectionState: StateFlow<LiveConnectionState> = _connectionState.asStateFlow()

    private val _sessionEvents = MutableSharedFlow<LiveSessionEvent>(extraBufferCapacity = EVENT_BUFFER_CAPACITY)
    override val sessionEvents: SharedFlow<LiveSessionEvent> = _sessionEvents.asSharedFlow()

    override suspend fun connect(config: LiveScenarioConfig): Result<Unit> {
        val deferred = connectionMutex.withLock {
            if (_connectionState.value.isActive()) {
                AppLog.w(TAG) { "Ignoring duplicate connect while ${_connectionState.value}" }
                return Result.success(Unit)
            }
            val credential = credentialProvider.obtainCredential()
            if (credential == null) {
                _connectionState.value = LiveConnectionState.FAILED
                emit(LiveSessionEvent.Error(LiveConnectionErrorMapper.MISSING_CREDENTIAL, isFatal = true))
                return Result.failure(IllegalStateException("No Gemini credential available"))
            }
            synchronized(lock) {
                currentConfig = config
                resumptionHandle = null
                sessionEstablished = false
                clientClosing = false
                discardModelOutput = false
            }
            openSocket(credential, config, handle = null, openingState = LiveConnectionState.CONNECTING)
        }
        return awaitSetup(deferred)
    }

    override suspend fun sendAudioChunk(pcmData: ByteArray) {
        if (pcmData.isEmpty()) return
        sendWhenReady(LiveClientMessageFactory.audioChunk(Base64.getEncoder().encodeToString(pcmData)).toString())
    }

    override suspend fun sendAudioStreamEnd() {
        sendWhenReady(LiveClientMessageFactory.audioStreamEnd().toString())
    }

    override suspend fun sendTextMessage(text: String) {
        if (!sendWhenReady(LiveClientMessageFactory.userText(text).toString())) {
            AppLog.w(TAG) { "Text message dropped: state=${_connectionState.value}" }
        }
    }

    override suspend fun interruptModelTurn() {
        discardModelOutput = true
    }

    override suspend fun disconnect() {
        synchronized(lock) {
            clientClosing = true
            reconnectJob?.cancel()
            reconnectJob = null
            releaseSocket(reason = "Clean session termination")
        }
        _connectionState.value = LiveConnectionState.CLOSED
        emit(LiveSessionEvent.Disconnected("Session ended"))
    }

    private fun openSocket(
        credential: LiveCredential,
        config: LiveScenarioConfig,
        handle: String?,
        openingState: LiveConnectionState
    ): CompletableDeferred<Result<Unit>> = synchronized(lock) {
        releaseSocket(reason = "Opening a new socket")
        val socketGeneration = ++generation
        val deferred = CompletableDeferred<Result<Unit>>()
        setupDeferred = deferred
        _connectionState.value = openingState
        AppLog.i(TAG) { "Opening socket [gen=$socketGeneration, resuming=${handle != null}]" }
        val request = Request.Builder().url(LiveEndpoint.urlFor(credential)).build()
        activeSocket = webSocketFactory.newWebSocket(request, SocketListener(socketGeneration, config, handle))
        deferred
    }

    private suspend fun awaitSetup(deferred: CompletableDeferred<Result<Unit>>): Result<Unit> = try {
        withTimeout(setupTimeoutMs) { deferred.await() }
    } catch (timeout: TimeoutCancellationException) {
        AppLog.e(TAG) { "Setup timed out after ${setupTimeoutMs}ms" }
        synchronized(lock) { releaseSocket(reason = "Setup timeout") }
        _connectionState.value = LiveConnectionState.FAILED
        emit(LiveSessionEvent.Error(LiveConnectionErrorMapper.CONNECTION_TIMED_OUT, isFatal = true))
        Result.failure(TimeoutException("Live session setup timed out waiting for setupComplete"))
    }

    private fun sendWhenReady(payload: String): Boolean {
        if (!_connectionState.value.acceptsInput()) return false
        val socket = activeSocket ?: return false
        return socket.send(payload)
    }

    private fun isCurrent(socketGeneration: Long): Boolean = synchronized(lock) { socketGeneration == generation }

    private fun releaseSocket(reason: String) {
        generation++
        setupDeferred?.takeIf { it.isActive }?.complete(Result.failure(CancellationException(reason)))
        setupDeferred = null
        activeSocket?.cancel()
        activeSocket = null
    }

    private fun completeSetup(result: Result<Unit>) {
        synchronized(lock) { setupDeferred }?.takeIf { it.isActive }?.complete(result)
    }

    private fun handleServerMessage(text: String, socketGeneration: Long) {
        val signals = runCatching { messageParser.parse(text) }.getOrElse { error ->
            AppLog.w(TAG, error) { "Unparseable server frame (length=${text.length})" }
            return
        }
        signals.forEach { signal -> handleSignal(signal, socketGeneration) }
    }

    private fun handleSignal(signal: LiveServerSignal, socketGeneration: Long) {
        when (signal) {
            LiveServerSignal.SetupComplete -> onSetupComplete(socketGeneration)
            is LiveServerSignal.ServerError -> onServerError(signal)
            is LiveServerSignal.GoAway -> onGoAway(signal)
            is LiveServerSignal.ResumptionUpdate -> onResumptionUpdate(signal)
            is LiveServerSignal.Usage -> emit(LiveSessionEvent.UsageUpdated(signal.usage))
            is LiveServerSignal.ToolCall -> onToolCall(signal)
            is LiveServerSignal.Event -> forwardModelEvent(signal.event)
        }
    }

    private fun onSetupComplete(socketGeneration: Long) {
        synchronized(lock) { sessionEstablished = true }
        _connectionState.value = LiveConnectionState.SESSION_READY
        AppLog.i(TAG) { "Session ready [gen=$socketGeneration]" }
        emit(LiveSessionEvent.Connected("gemini-live-$socketGeneration"))
        completeSetup(Result.success(Unit))
    }

    private fun onServerError(signal: LiveServerSignal.ServerError) {
        AppLog.e(TAG) { "Server error code=${signal.code} status=${signal.status}" }
        _connectionState.value = LiveConnectionState.FAILED
        emit(LiveSessionEvent.Error(LiveConnectionErrorMapper.fromServerError(signal), isFatal = true))
        completeSetup(Result.failure(IOException("Server error: code=${signal.code}, status=${signal.status}")))
        synchronized(lock) { releaseSocket(reason = "Server error") }
    }

    private fun onGoAway(signal: LiveServerSignal.GoAway) {
        AppLog.i(TAG) { "goAway received, timeLeftMs=${signal.timeLeftMs}" }
        if (canResume()) scheduleResume()
    }

    private fun onResumptionUpdate(signal: LiveServerSignal.ResumptionUpdate) {
        if (signal.resumable && signal.handle != null) {
            synchronized(lock) { resumptionHandle = signal.handle }
        }
    }

    private fun forwardModelEvent(event: LiveSessionEvent) {
        when (event) {
            is LiveSessionEvent.AudioOutputChunk, is LiveSessionEvent.OutputTranscription ->
                if (discardModelOutput) return
            LiveSessionEvent.TurnComplete, LiveSessionEvent.Interrupted -> discardModelOutput = false
            else -> Unit
        }
        emit(event)
    }

    private fun onRemoteClose(code: Int, reason: String, socketGeneration: Long) {
        AppLog.i(TAG) { "Socket closed by server code=$code [gen=$socketGeneration]" }
        val pendingSetup = synchronized(lock) { setupDeferred?.isActive == true && !sessionEstablished }
        when {
            pendingSetup && reconnectJob?.isActive != true -> {
                _connectionState.value = LiveConnectionState.CLOSED
                emit(LiveSessionEvent.Disconnected(reason))
                completeSetup(Result.failure(IOException("WebSocket closed before setup completed: code=$code")))
                synchronized(lock) { releaseSocket(reason = "Closed during setup") }
            }
            code != LiveConnectionErrorMapper.NORMAL_CLOSURE_CODE && canResume() -> {
                completeSetup(Result.failure(IOException("Socket closed during resume: code=$code")))
                scheduleResume()
            }
            code == LiveConnectionErrorMapper.NORMAL_CLOSURE_CODE -> {
                synchronized(lock) { releaseSocket(reason = "Server closed normally") }
                _connectionState.value = LiveConnectionState.CLOSED
                emit(LiveSessionEvent.Disconnected(reason))
            }
            else -> failSession(LiveConnectionErrorMapper.fromClose(code, reason))
        }
    }

    private fun onSocketFailure(throwable: Throwable, response: Response?, socketGeneration: Long) {
        AppLog.e(TAG, throwable) { "Socket failure http=${response?.code} [gen=$socketGeneration]" }
        if (canResume()) {
            completeSetup(Result.failure(IOException("Socket failure during resume", throwable)))
            scheduleResume()
            return
        }
        val message = LiveConnectionErrorMapper.fromFailure(throwable, response?.code)
        completeSetup(Result.failure(IOException(message, throwable)))
        failSession(message)
    }

    private fun canResume(): Boolean = synchronized(lock) {
        !clientClosing && resumptionHandle != null && (sessionEstablished || reconnectJob?.isActive == true)
    }

    private fun scheduleResume() {
        synchronized(lock) {
            if (clientClosing || reconnectJob?.isActive == true) return
            _connectionState.value = LiveConnectionState.RECONNECTING
            reconnectJob = scope.launch { runResumeLoop() }
        }
    }

    private suspend fun runResumeLoop() {
        for (attempt in 1..reconnectPolicy.maxAttempts) {
            emit(LiveSessionEvent.Reconnecting(attempt))
            delay(reconnectPolicy.delayBeforeAttempt(attempt))
            val (config, handle) = synchronized(lock) {
                if (clientClosing) return
                currentConfig to resumptionHandle
            }
            val credential = credentialProvider.obtainCredential() ?: break
            if (config == null) return
            val deferred = openSocket(credential, config, handle, LiveConnectionState.RECONNECTING)
            val result = runCatching { withTimeout(setupTimeoutMs) { deferred.await() } }.getOrNull()
            if (result?.isSuccess == true) return
        }
        failSession(LiveConnectionErrorMapper.CONNECTION_LOST)
    }

    private fun failSession(message: String) {
        synchronized(lock) { releaseSocket(reason = message) }
        _connectionState.value = LiveConnectionState.FAILED
        emit(LiveSessionEvent.Error(message, isFatal = true))
    }

    private fun onToolCall(signal: LiveServerSignal.ToolCall) {
        signal.calls.forEach { call ->
            val correction = LiveCoachingTools.toCorrection(call)
            correction?.let { emit(LiveSessionEvent.CorrectionLogged(it)) }
            val result = if (correction != null) TOOL_RESULT_LOGGED else TOOL_RESULT_IGNORED
            sendWhenReady(LiveClientMessageFactory.toolResponse(call.id, call.name, result).toString())
        }
    }

    private fun emit(event: LiveSessionEvent) {
        if (!_sessionEvents.tryEmit(event)) {
            droppedEventCount++
            AppLog.w(TAG) { "Event buffer full; dropped ${event::class.simpleName} (total=$droppedEventCount)" }
        }
    }

    private fun LiveConnectionState.isActive(): Boolean = this == LiveConnectionState.SESSION_READY ||
        this == LiveConnectionState.CONNECTED ||
        this == LiveConnectionState.SOCKET_OPEN ||
        this == LiveConnectionState.CONNECTING ||
        this == LiveConnectionState.RECONNECTING

    private fun LiveConnectionState.acceptsInput(): Boolean =
        this == LiveConnectionState.SESSION_READY || this == LiveConnectionState.CONNECTED

    private inner class SocketListener(
        private val socketGeneration: Long,
        private val config: LiveScenarioConfig,
        private val handle: String?
    ) : WebSocketListener() {

        override fun onOpen(webSocket: WebSocket, response: Response) {
            if (!isCurrent(socketGeneration)) {
                webSocket.cancel()
                return
            }
            _connectionState.value = LiveConnectionState.SOCKET_OPEN
            val sent = webSocket.send(setupMessageFactory.create(config, handle).toString())
            if (!sent) {
                completeSetup(Result.failure(IOException("Failed to send setup message")))
                failSession(LiveConnectionErrorMapper.SETUP_FAILED)
            }
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            if (isCurrent(socketGeneration)) handleServerMessage(text, socketGeneration)
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
            if (!isCurrent(socketGeneration)) return
            val text = runCatching { bytes.utf8() }.getOrNull()
            if (!text.isNullOrBlank()) handleServerMessage(text, socketGeneration)
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            if (!isCurrent(socketGeneration)) return
            webSocket.close(LiveConnectionErrorMapper.NORMAL_CLOSURE_CODE, null)
            onRemoteClose(code, reason, socketGeneration)
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            if (isCurrent(socketGeneration)) onRemoteClose(code, reason, socketGeneration)
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            if (isCurrent(socketGeneration)) onSocketFailure(t, response, socketGeneration)
        }
    }
}
