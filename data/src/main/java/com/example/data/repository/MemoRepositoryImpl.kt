package com.example.data.repository

import com.example.data.datasource.MemoDataSource
import com.example.data.mapper.convertMemoEntity
import com.example.data.mapper.convertMemoModel
import com.example.domain.model.MemoModel
import com.example.domain.repository.MemoRepository
import javax.inject.Inject

class MemoRepositoryImpl @Inject constructor(
    private val memoDataSource: MemoDataSource
): MemoRepository {
    override suspend fun getAllMemo(): List<MemoModel> {
        return memoDataSource.getAllMemo().map { it.convertMemoModel() }
    }

    override suspend fun addMemo(memoModel: MemoModel): Result<Unit> {
        return memoDataSource.addMemo(memoModel.convertMemoEntity())
    }

    override suspend fun deleteMemo(memoModel: MemoModel): Result<Unit> {
        return memoDataSource.deleteMemo(memoModel.convertMemoEntity())
    }
}