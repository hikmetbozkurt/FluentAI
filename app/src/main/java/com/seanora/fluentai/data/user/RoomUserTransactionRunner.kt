package com.seanora.fluentai.data.user

import androidx.room.withTransaction
import com.seanora.fluentai.core.common.transaction.TransactionRunner
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomUserTransactionRunner @Inject constructor(
    private val database: UserDatabase
) : TransactionRunner {
    override suspend fun <T> runInTransaction(block: suspend () -> T): T = database.withTransaction { block() }
}
