package com.example.domain.repository

import com.example.domain.model.MemoModel

interface MemoRepository {
    suspend fun getAllMemo(): List<MemoModel>

    suspend fun addMemo(memoModel: MemoModel): Result<Unit>

    suspend fun deleteMemo(memoModel: MemoModel): Result<Unit>
}