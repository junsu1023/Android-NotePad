package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class MemoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long?,
    val updateDate: Date = Date(System.currentTimeMillis()),
    val title: String? = null,
    val contents: String? = null
)
