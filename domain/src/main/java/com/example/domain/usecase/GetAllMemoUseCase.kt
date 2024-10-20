package com.example.domain.usecase

import com.example.domain.model.MemoModel
import com.example.domain.repository.MemoRepository
import javax.inject.Inject

class GetAllMemoUseCase @Inject constructor(
    private val memoRepository: MemoRepository
) {
    suspend operator fun invoke(): List<MemoModel> = memoRepository.getAllMemo()
}