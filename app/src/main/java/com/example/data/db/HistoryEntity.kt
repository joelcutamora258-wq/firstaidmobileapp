package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "first_aid_history")
data class HistoryEntity(
  @PrimaryKey(autoGenerate = true)
  val id: Long = 0L,
  val topicId: String,
  val title: String,
  val category: String,
  val severity: String,
  val summary: String,
  val timestamp: Long = System.currentTimeMillis(),
  val isAiGenerated: Boolean = false,
  val notes: String = "",
  val isFavorite: Boolean = false
)
