package com.seanora.fluentai.data.repository

import com.seanora.fluentai.core.model.ChunkCategory
import com.seanora.fluentai.core.model.LexicalChunk
import com.seanora.fluentai.data.content.dao.ChunkDao
import com.seanora.fluentai.data.content.mapper.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

interface ChunkRepository {
    fun getAllChunks(): Flow<List<LexicalChunk>>
    fun getChunkById(id: String): Flow<LexicalChunk?>
    fun getChunksByLevel(level: String): Flow<List<LexicalChunk>>
    fun getChunksByCategory(category: ChunkCategory): Flow<List<LexicalChunk>>
    fun searchChunks(query: String): Flow<List<LexicalChunk>>
    suspend fun getChunkCount(): Int
}

@Singleton
class ChunkRepositoryImpl @Inject constructor(
    private val chunkDao: ChunkDao,
) : ChunkRepository {

    override fun getAllChunks(): Flow<List<LexicalChunk>> =
        chunkDao.getAllChunks().map { list -> list.map { it.toDomain() } }

    override fun getChunkById(id: String): Flow<LexicalChunk?> =
        chunkDao.getChunkById(id).map { it?.toDomain() }

    override fun getChunksByLevel(level: String): Flow<List<LexicalChunk>> =
        chunkDao.getChunksByLevel(level).map { list -> list.map { it.toDomain() } }

    override fun getChunksByCategory(category: ChunkCategory): Flow<List<LexicalChunk>> =
        chunkDao.getChunksByCategory(category.wireValue).map { list -> list.map { it.toDomain() } }

    override fun searchChunks(query: String): Flow<List<LexicalChunk>> =
        chunkDao.searchChunks(query).map { list -> list.map { it.toDomain() } }

    override suspend fun getChunkCount(): Int = chunkDao.getChunkCount()
}
