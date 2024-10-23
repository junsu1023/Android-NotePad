package com.example.notepad

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.notepad.navigation.MemoNavHost
import com.example.notepad.ui.theme.NotePadTheme
import com.example.notepad.viewmodel.MemoViewModel

@Composable
fun MemoApp(
    memoViewModel: MemoViewModel = hiltViewModel()
) {
    val navController = rememberNavController()

    NotePadTheme {
        Scaffold {
            MemoNavHost(
                modifier = Modifier.padding(it),
                navController = navController,
                memoViewModel = memoViewModel
            )
        }
    }
}