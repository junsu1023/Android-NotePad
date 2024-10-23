package com.example.data.di

import com.example.domain.repository.MemoRepository
import com.example.domain.usecase.AddMemoUseCase
import com.example.domain.usecase.DeleteMemoUseCase
import com.example.domain.usecase.GetAllMemoUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    fun provideGetAllMemo(memoRepository: MemoRepository) = GetAllMemoUseCase(memoRepository)

    @Provides
    fun provideAddMemo(memoRepository: MemoRepository) = AddMemoUseCase(memoRepository)

    @Provides
    fun provideDeleteMemo(memoRepository: MemoRepository) = DeleteMemoUseCase(memoRepository)
}