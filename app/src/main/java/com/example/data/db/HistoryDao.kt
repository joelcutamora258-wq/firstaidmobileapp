package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {
  @Query("SELECT * FROM first_aid_history ORDER BY timestamp DESC")
  fun getAllHistory(): Flow<List<HistoryEntity>>

  @Query("SELECT * FROM first_aid_history WHERE isFavorite = 1 ORDER BY timestamp DESC")
  fun getFavoriteHistory(): Flow<List<HistoryEntity>>

  @Query("SELECT * FROM first_aid_history WHERE title LIKE '%' || :query || '%' OR category LIKE '%' || :query || '%' ORDER BY timestamp DESC")
  fun searchHistory(query: String): Flow<List<HistoryEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(history: HistoryEntity): Long

  @Update
  suspend fun update(history: HistoryEntity)

  @Query("UPDATE first_aid_history SET isFavorite = NOT isFavorite WHERE id = :id")
  suspend fun toggleFavorite(id: Long)

  @Query("UPDATE first_aid_history SET notes = :notes WHERE id = :id")
  suspend fun updateNotes(id: Long, notes: String)

  @Query("DELETE FROM first_aid_history WHERE id = :id")
  suspend fun deleteById(id: Long)

  @Query("DELETE FROM first_aid_history")
  suspend fun clearAll()

  @Query("SELECT COUNT(*) FROM first_aid_history")
  suspend fun getHistoryCount(): Int
}
