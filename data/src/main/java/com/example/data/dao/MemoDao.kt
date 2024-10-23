package com.example.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.entity.MemoEntity

@Dao
interface MemoDao {
    @Query("select * from MemoEntity")
    fun getAllMemo(): List<MemoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addMemoEntity(memoEntity: MemoEntity): Result<Unit>

    @Delete
    fun deleteMemoEntity(memoEntity: MemoEntity): Result<Unit>
}