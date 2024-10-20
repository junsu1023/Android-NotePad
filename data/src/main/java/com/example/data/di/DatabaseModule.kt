package com.example.data.di

import android.content.Context
import androidx.room.Room
import com.example.data.database.MemoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideMemoDatabase(@ApplicationContext context: Context): MemoDatabase
    = Room.databaseBuilder(context, MemoDatabase::class.java, "memo.db").build()

    @Provides
    fun provideMemoDao(memoDatabase: MemoDatabase) = memoDatabase.memoDao()
}