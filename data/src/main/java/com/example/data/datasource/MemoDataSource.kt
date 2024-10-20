package com.example.data.datasource

import com.example.data.dao.MemoDao
import com.example.data.entity.MemoEntity

class MemoDataSource(
    private val memoDao: MemoDao
) {
    suspend fun getAllMemo(): List<MemoEntity> = memoDao.getAllMemo()

    suspend fun addMemo(memoEntity: MemoEntity): Result<Unit> = memoDao.addMemoEntity(memoEntity)

    suspend fun deleteMemo(memoEntity: MemoEntity): Result<Unit> = memoDao.deleteMemoEntity(memoEntity)
}