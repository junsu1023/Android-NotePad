package com.example.notepad.ui.view

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.toast.ToastUtil
import com.example.data.entity.MemoEntity
import com.example.notepad.ui.theme.gold
import com.example.notepad.ui.theme.khaki
import com.example.notepad.util.ContentTextField
import com.example.notepad.util.TitleTextField
import com.example.notepad.viewmodel.MemoViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun WriteScreen(
    memoViewModel: MemoViewModel,
    memoEntity: MemoEntity? = null,
    handleBackButtonClick: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    if(memoEntity != null) {
        memoViewModel.setTitle(memoEntity.title ?: "")
        memoViewModel.setContent(memoEntity.contents ?: "")
    }

    BackHandler {
        memoViewModel.makeMemoEntity(memoEntity?.id)

        coroutineScope.launch {
            memoViewModel.prevSaveMemo.collectLatest {
                memoViewModel.saveMemo(it)
            }
        }

        handleBackButtonClick()
    }

    SideEffect {
        val throwable = memoViewModel.saveMemoContinuationState

        coroutineScope.launch {
            throwable.collectLatest {
                ToastUtil.makeShort("메모 저장 실패")
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(khaki)
    ) {
        EditTitleLayer(
            memoViewModel = memoViewModel,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.1f)
        )

        DivideLine()

        EditContentLayer(
            memoViewModel = memoViewModel,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.89f)
        )
    }
}

@Composable
private fun EditTitleLayer(
    memoViewModel: MemoViewModel,
    modifier: Modifier
) {
    val title = memoViewModel.title.collectAsStateWithLifecycle()

    Row(
        modifier = modifier.background(gold)
    ) {
        TitleTextField(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            value = title.value,
            onValueChange = { newTitle -> memoViewModel.setTitle(newTitle) },
        )
    }
}

@Composable
private fun EditContentLayer(
    memoViewModel: MemoViewModel,
    modifier: Modifier
) {
    val content = memoViewModel.content.collectAsStateWithLifecycle()

    Row(
        modifier = modifier.background(khaki)
    ) {
        ContentTextField(
            modifier = Modifier.fillMaxSize(),
            value = content.value,
            onValueChange = { newContent -> memoViewModel.setContent(newContent) }
        )
    }
}