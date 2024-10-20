package com.example.data.mapper

import com.example.data.entity.MemoEntity
import com.example.domain.model.MemoModel

fun MemoEntity.convertMemoModel(): MemoModel
= MemoModel(
    this.id, this.updateDate, this.title, this.contents
)

fun MemoModel.convertMemoEntity(): MemoEntity
= MemoEntity(
    this.id, this.updateDate, this.title, this.contents
)