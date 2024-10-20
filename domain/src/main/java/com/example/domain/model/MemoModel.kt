package com.example.domain.model

import java.util.Date

data class MemoModel(
    val id: Long? = null,
    val updateDate: Date = Date(System.currentTimeMillis()),
    val title: String? = null,
    val contents: String? = null
)
