package com.example.notepad.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.core.viewmodel.BaseViewModel
import com.example.data.entity.MemoEntity
import com.example.data.mapper.convertMemoEntity
import com.example.data.mapper.convertMemoModel
import com.example.domain.usecase.AddMemoUseCase
import com.example.domain.usecase.DeleteMemoUseCase
import com.example.domain.usecase.GetAllMemoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class MemoViewModel @Inject constructor(
    private val getAllMemoUseCase: GetAllMemoUseCase,
    private val insertMemoUseCase: AddMemoUseCase,
    private val deleteMemoUseCase: DeleteMemoUseCase
): BaseViewModel() {
    private val _memoList = MutableStateFlow<List<MemoEntity>>(emptyList())
    val memoList: StateFlow<List<MemoEntity>> get() = _memoList.asStateFlow()

    private val _saveMemoContinuationState = MutableSharedFlow<Throwable>()
    val saveMemoContinuationState: SharedFlow<Throwable> get() = _saveMemoContinuationState.asSharedFlow()

    private val _deleteMemoContinuationState = MutableSharedFlow<Throwable>()
    val deleteMemoContinuationState: SharedFlow<Throwable> get() = _deleteMemoContinuationState.asSharedFlow()

    private val _selectedMemoList = MutableStateFlow<List<MemoEntity>>(emptyList())
    val selectedMemoList: StateFlow<List<MemoEntity>> get() = _selectedMemoList.asStateFlow()

    private val _title = MutableStateFlow("")
    val title: StateFlow<String> get() = _title.asStateFlow()

    private val _content = MutableStateFlow("")
    val content: StateFlow<String> get() = _content.asStateFlow()

    private val _prevSaveMemo = MutableStateFlow(MemoEntity())
    val prevSaveMemo: StateFlow<MemoEntity> get() = _prevSaveMemo.asStateFlow()

    init {
        getAllMemo()
    }

    fun getAllMemo() {
        viewModelScope.launchWithLoading {
            _memoList.update { getAllMemoUseCase().map { it.convertMemoEntity() } }
        }
    }

    fun saveMemo(memoEntity: MemoEntity) {
        viewModelScope.launchWithLoading {
            insertMemoUseCase(memoEntity.convertMemoModel()).onSuccess {
                val memoList = _memoList.value.toMutableList()

                memoList.removeIf { it.id == memoEntity.id }
                memoList.add(memoEntity)

                _memoList.update { memoList }
            }.onFailure {
                _saveMemoContinuationState.emit(it)
            }
        }
    }

    fun deleteMemo(memoEntity: MemoEntity) {
        viewModelScope.launchWithLoading {
            deleteMemoUseCase(memoEntity.convertMemoModel()).onSuccess {
                val memoList = _memoList.value.toMutableList()
                memoList.remove(memoEntity)

                _memoList.update { memoList }
            }.onFailure {
                _deleteMemoContinuationState.emit(it)
            }
        }
    }

    fun selectedMemo(memoEntity: MemoEntity) {
        viewModelScope.launchWithLoading {
            val selectedMemoList = _selectedMemoList.value.toMutableList()
            selectedMemoList.add(memoEntity)

            _selectedMemoList.update { selectedMemoList }
        }
    }

    fun cancelSelectedMemo(memoEntity: MemoEntity) {
        viewModelScope.launchWithLoading {
            val selectedMemoList = _selectedMemoList.value.toMutableList()
            selectedMemoList.removeIf { it.id == memoEntity.id }

            _selectedMemoList.update { selectedMemoList }
        }
    }

    fun deleteMemos() {
        viewModelScope.launchWithLoading {
            val selectedMemoList = _selectedMemoList.value

            for(selectedMemo in selectedMemoList) {
                deleteMemo(selectedMemo)
            }
        }
    }

    fun makeMemoEntity(id: Long? = null) {
        viewModelScope.launchWithLoading {
            val updateDate = Date(System.currentTimeMillis())
            val title = _title.value
            val content = _content.value

            val newMemo = if(id == null) MemoEntity(updateDate = updateDate, title = title, contents = content)
            else MemoEntity(id = id, updateDate = updateDate, title = title, contents = content)
            _prevSaveMemo.update { newMemo }
        }
    }

    fun setTitle(title: String) {
        viewModelScope.launchWithLoading {
            _title.update { title }
        }
    }

    fun setContent(content: String) {
        viewModelScope.launchWithLoading {
            _content.update { content }
        }
    }

    fun getMemo(id: Long) = _memoList.value.find { it.id == id }

    fun clearSelectedMemo() {
        viewModelScope.launchWithLoading {
            val selectedMemoList = _selectedMemoList.value.toMutableList()
            selectedMemoList.clear()

            _selectedMemoList.update { selectedMemoList }
        }
    }
}