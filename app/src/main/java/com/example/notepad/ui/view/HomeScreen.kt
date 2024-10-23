package com.example.notepad.ui.view

import com.example.notepad.ui.theme.khaki

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.toast.ToastUtil
import com.example.data.entity.MemoEntity
import com.example.notepad.R
import com.example.notepad.ui.theme.black
import com.example.notepad.ui.theme.darkKhaki
import com.example.notepad.viewmodel.MemoViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun HomeScreen(
    memoViewModel: MemoViewModel,
    handleClickAddMemoButton: () -> Unit,
    handleClickMemoItem: (MemoEntity) -> Unit
) {
    val context = LocalContext.current

    BackHandler {
        if(memoViewModel.selectedMemoList.value.isEmpty()) {
            (context as Activity).finish()
        } else {
            memoViewModel.clearSelectedMemo()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(khaki)
    ) {
        TopBarLayer(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.07f),
            handleClickAddMemoButton = handleClickAddMemoButton,
            memoViewModel = memoViewModel
        )

        DivideLine()

        MemoListLayer(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.92f),
            memoViewModel = memoViewModel,
            handleClickMemoItem = handleClickMemoItem
        )
    }
}

@Composable
private fun TopBarLayer(
    modifier: Modifier,
    handleClickAddMemoButton: () -> Unit,
    memoViewModel: MemoViewModel
) {
    val coroutineScope = rememberCoroutineScope()

    SideEffect {
        val deleteContinuation = memoViewModel.deleteMemoContinuationState

        coroutineScope.launch {
            deleteContinuation.collectLatest {
                ToastUtil.makeShort("메모 삭제 실패")
            }
        }
    }

    Row(
        modifier = modifier.padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier.wrapContentSize(),
            text = stringResource(R.string.main_title),
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )

        Row(
            modifier = Modifier.wrapContentSize()
        ) {
            Image(
                painter = painterResource(R.drawable.add),
                contentDescription = null,
                modifier = Modifier.clickable(
                    onClick = {
                        handleClickAddMemoButton()
                        memoViewModel.clearSelectedMemo()
                    }
                )
            )

            Spacer(modifier = Modifier.width(20.dp))

            Image(
                painter = painterResource(R.drawable.trashcan),
                contentDescription = null,
                modifier = Modifier.clickable(
                    onClick = {
                        if(memoViewModel.selectedMemoList.value.isNotEmpty()) {
                            memoViewModel.deleteMemos()
                            memoViewModel.clearSelectedMemo()
                        }
                    }
                )
            )
        }
    }
}

@Composable
fun DivideLine() {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(black)
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MemoListLayer(
    modifier: Modifier,
    memoViewModel: MemoViewModel,
    handleClickMemoItem: (MemoEntity) -> Unit
) {
    val memoList by memoViewModel.memoList.collectAsStateWithLifecycle()
    val selectedMemoList = memoViewModel.selectedMemoList.collectAsStateWithLifecycle()

    val handleLongClickMemoItem = { memo: MemoEntity ->
        memoViewModel.selectedMemo(memo)
    }

    val handleReClickMemoItem = { memo: MemoEntity ->
        memoViewModel.cancelSelectedMemo(memo)
    }

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(10.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        itemsIndexed(
            items = memoList.sortedByDescending { it.updateDate }
        ) { index, memo ->
            MemoItem(
                modifier = Modifier
                    .combinedClickable(
                        onClick = {
                            if (selectedMemoList.value.isEmpty()) {
                                handleClickMemoItem(memo)
                            } else {
                                if (selectedMemoList.value.contains(memo)) {
                                    handleReClickMemoItem(memo)
                                } else {
                                    handleLongClickMemoItem(memo)
                                }
                            }
                        },
                        onLongClick = {
                            handleLongClickMemoItem(memo)
                        }
                    )
                    .background(if (selectedMemoList.value.contains(memo)) darkKhaki else khaki),
                memo = memo
            )
        }
    }
}

@Composable
private fun MemoItem(
    modifier: Modifier,
    memo: MemoEntity
) {
    val title = memo.title ?: stringResource(R.string.blank_title)
    val updateDate = memo.updateDate

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
            .border(width = 1.dp, color = Color.Black, shape = RectangleShape)
            .padding(horizontal = 20.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = if(title.length < 10) title else title.substring(0, 10))
        Text(text = SimpleDateFormat("yyyy-MM-dd kk:mm", Locale("ko", "KR")).format(updateDate))
    }
}