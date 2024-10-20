package com.example.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.data.dao.MemoDao
import com.example.data.entity.MemoEntity

@Database(entities = [MemoEntity::class], version = 1)
@TypeConverters(com.example.data.TypeConverters.TypeConverters::class)
abstract class MemoDatabase: RoomDatabase() {
    abstract fun memoDao(): MemoDao
}