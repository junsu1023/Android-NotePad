package com.example.domain.usecase

import com.example.domain.model.MemoModel
import com.example.domain.repository.MemoRepository
import javax.inject.Inject

class AddMemoUseCase @Inject constructor(
    private val memoRepository: MemoRepository
) {
    suspend operator fun invoke(memoModel: MemoModel): Result<Unit> {
        return try {
            memoRepository.addMemo(memoModel)
            Result.success(Unit)
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }
}