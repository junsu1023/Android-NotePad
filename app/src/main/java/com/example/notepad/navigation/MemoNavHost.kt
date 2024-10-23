package com.example.notepad.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.data.entity.MemoEntity
import com.example.notepad.ui.view.HomeScreen
import com.example.notepad.ui.view.WriteScreen
import com.example.notepad.viewmodel.MemoViewModel

@Composable
fun MemoNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    memoViewModel: MemoViewModel
) {
    NavHost(
        navController = navController,
        modifier = modifier,
        startDestination = MemoScreen.Home.name
    ) {
        val handleClickAddMemoButton = {
            navController.navigate(MemoScreen.Write.name) {
                popUpTo(MemoScreen.Home.name)
            }
        }

        val handleClickBackButton = {
            navController.popBackStack()
        }

        val handleClickMemoItem = { memo: MemoEntity ->
            navController.navigate("${MemoScreen.Detail.name}/${memo.id}") {
                popUpTo(MemoScreen.Home.name)
            }
        }

        composable(MemoScreen.Home.name) {
            HomeScreen(
                memoViewModel = memoViewModel,
                handleClickAddMemoButton = handleClickAddMemoButton,
                handleClickMemoItem = handleClickMemoItem
            )
        }

        composable(
            route = "${MemoScreen.Detail}/${Key.MEMO_ARGS_KEY}",
            arguments = listOf(
                navArgument((Key.MEMO_ARGS_KEY)) {
                     type = NavType.LongType
                }
            )
        ) { entry ->
            val memoId = entry.arguments?.getLong(Key.MEMO_ARGS_KEY) ?: -1L
            val memo = memoViewModel.getMemo(memoId)

            WriteScreen(
                memoViewModel = memoViewModel,
                memoEntity =  memo,
                handleBackButtonClick = { handleClickBackButton() }
            )
        }

        composable(MemoScreen.Write.name) {
            WriteScreen(
                memoViewModel = memoViewModel,
                handleBackButtonClick = { handleClickBackButton() }
            )
        }
    }
}

object Key {
    const val MEMO_ARGS_KEY = "memoId"
}