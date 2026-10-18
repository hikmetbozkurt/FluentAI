package com.seanora.fluentai.core.common.transaction

interface TransactionRunner {
    suspend fun <T> runInTransaction(block: suspend () -> T): T
}

object ImmediateTransactionRunner : TransactionRunner {
    override suspend fun <T> runInTransaction(block: suspend () -> T): T = block()
}
