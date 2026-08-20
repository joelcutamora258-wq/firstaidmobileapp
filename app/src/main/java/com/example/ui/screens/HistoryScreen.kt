package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.db.HistoryEntity
import com.example.ui.theme.CriticalRed
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoryScreen(
  historyList: List<HistoryEntity>,
  searchQuery: String,
  showFavoritesOnly: Boolean,
  onSearchChanged: (String) -> Unit,
  onToggleFavoritesOnly: () -> Unit,
  onSelectHistoryItem: (HistoryEntity) -> Unit,
  onToggleFavorite: (Long) -> Unit,
  onUpdateNotes: (Long, String) -> Unit,
  onDeleteItem: (Long) -> Unit,
  onClearAll: () -> Unit,
  modifier: Modifier = Modifier
) {
  var showClearDialog by remember { mutableStateOf(false) }
  var editingItem by remember { mutableStateOf<HistoryEntity?>(null) }
  var noteInputText by remember { mutableStateOf("") }

  if (showClearDialog) {
    AlertDialog(
      onDismissRequest = { showClearDialog = false },
      title = { Text("Clear All Search History?", fontWeight = FontWeight.Bold) },
      text = { Text("This will permanently remove all your past first aid lookups and search history.") },
      confirmButton = {
        Button(
          onClick = {
            onClearAll()
            showClearDialog = false
          },
          colors = ButtonDefaults.buttonColors(containerColor = CriticalRed),
          shape = CircleShape,
          modifier = Modifier.testTag("confirm_clear_history_button")
        ) {
          Text("Clear All")
        }
      },
      dismissButton = {
        TextButton(onClick = { showClearDialog = false }) {
          Text("Cancel")
        }
      }
    )
  }

  if (editingItem != null) {
    AlertDialog(
      onDismissRequest = { editingItem = null },
      title = { Text("Edit Note for \"${editingItem?.title}\"", fontWeight = FontWeight.Bold, fontSize = 16.sp) },
      text = {
        OutlinedTextField(
          value = noteInputText,
          onValueChange = { noteInputText = it },
          placeholder = { Text("Add personal note (e.g., 'Pack extra bandages', 'Review with family')") },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("note_input_field"),
          shape = RoundedCornerShape(12.dp)
        )
      },
      confirmButton = {
        Button(
          onClick = {
            editingItem?.let { onUpdateNotes(it.id, noteInputText) }
            editingItem = null
          },
          shape = CircleShape,
          modifier = Modifier.testTag("save_note_button")
        ) {
          Text("Save Note")
        }
      },
      dismissButton = {
        TextButton(onClick = { editingItem = null }) {
          Text("Cancel")
        }
      }
    )
  }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .testTag("history_screen_list"),
    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    // Header & Filter Bar
    item {
      Column(modifier = Modifier.fillMaxWidth()) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = "Search & Lookup History",
              style = MaterialTheme.typography.titleLarge,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = "${historyList.size} topics recorded on device",
              fontSize = 12.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }

          if (historyList.isNotEmpty()) {
            IconButton(
              onClick = { showClearDialog = true },
              modifier = Modifier.testTag("clear_history_icon_button")
            ) {
              Icon(
                imageVector = Icons.Default.DeleteOutline,
                contentDescription = "Clear All History",
                tint = MaterialTheme.colorScheme.error
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Search in History (High Density Pill)
        OutlinedTextField(
          value = searchQuery,
          onValueChange = onSearchChanged,
          placeholder = { Text("Filter past lookups...", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant) },
          leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = "Search", tint = MaterialTheme.colorScheme.onSurfaceVariant)
          },
          trailingIcon = {
            if (searchQuery.isNotBlank()) {
              IconButton(onClick = { onSearchChanged("") }) {
                Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear", tint = MaterialTheme.colorScheme.onSurfaceVariant)
              }
            }
          },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("history_search_input"),
          shape = CircleShape,
          singleLine = true,
          colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = Color.Transparent
          )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          FilterChip(
            selected = !showFavoritesOnly,
            onClick = { if (showFavoritesOnly) onToggleFavoritesOnly() },
            shape = CircleShape,
            label = { Text("All History", fontSize = 12.sp) },
            border = FilterChipDefaults.filterChipBorder(
              enabled = true,
              selected = !showFavoritesOnly,
              borderColor = MaterialTheme.colorScheme.outlineVariant,
              selectedBorderColor = Color.Transparent
            ),
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
              selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
              containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
              labelColor = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            modifier = Modifier.testTag("history_filter_all")
          )
          FilterChip(
            selected = showFavoritesOnly,
            onClick = { if (!showFavoritesOnly) onToggleFavoritesOnly() },
            shape = CircleShape,
            label = {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  imageVector = Icons.Default.Star,
                  contentDescription = null,
                  modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Saved / Favorites", fontSize = 12.sp)
              }
            },
            border = FilterChipDefaults.filterChipBorder(
              enabled = true,
              selected = showFavoritesOnly,
              borderColor = MaterialTheme.colorScheme.outlineVariant,
              selectedBorderColor = Color.Transparent
            ),
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
              selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
              containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
              labelColor = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            modifier = Modifier.testTag("history_filter_favorites")
          )
        }
      }
    }

    if (historyList.isEmpty()) {
      item {
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Box(
              modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.History,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(28.dp)
              )
            }
            Spacer(modifier = Modifier.height(14.dp))
            Text(
              text = if (showFavoritesOnly) "No saved favorites yet" else "No search history recorded",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = if (showFavoritesOnly) "Star any topic to quickly access it offline." else "Topics you view or search with Gemini AI will automatically appear here.",
              fontSize = 12.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.padding(top = 6.dp)
            )
          }
        }
      }
    } else {
      items(historyList, key = { it.id }) { item ->
        val formattedDate = remember(item.timestamp) {
          val sdf = SimpleDateFormat("MMM d, yyyy • h:mm a", Locale.getDefault())
          sdf.format(Date(item.timestamp))
        }

        Card(
          modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelectHistoryItem(item) }
            .testTag("history_item_${item.id}"),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            contentColor = MaterialTheme.colorScheme.onSurface
          ),
          border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.7f))
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(14.dp)
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                  Text(
                    text = item.category,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                  )
                }

                if (item.isAiGenerated) {
                  Spacer(modifier = Modifier.width(6.dp))
                  Box(
                    modifier = Modifier
                      .clip(RoundedCornerShape(6.dp))
                      .background(MaterialTheme.colorScheme.secondaryContainer)
                      .padding(horizontal = 6.dp, vertical = 2.dp)
                  ) {
                    Text(
                      text = "AI Guide",
                      fontSize = 10.sp,
                      fontWeight = FontWeight.Bold,
                      color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                  }
                }
              }

              Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                  onClick = { onToggleFavorite(item.id) },
                  modifier = Modifier
                    .size(28.dp)
                    .testTag("fav_button_${item.id}")
                ) {
                  Icon(
                    imageVector = if (item.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = if (item.isFavorite) CriticalRed else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(16.dp)
                  )
                }

                IconButton(
                  onClick = {
                    editingItem = item
                    noteInputText = item.notes
                  },
                  modifier = Modifier
                    .size(28.dp)
                    .testTag("edit_note_button_${item.id}")
                ) {
                  Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Notes",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(16.dp)
                  )
                }

                IconButton(
                  onClick = { onDeleteItem(item.id) },
                  modifier = Modifier
                    .size(28.dp)
                    .testTag("delete_history_button_${item.id}")
                ) {
                  Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Item",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    modifier = Modifier.size(16.dp)
                  )
                }
              }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
              text = item.title,
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
              text = item.summary,
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              maxLines = 2,
              overflow = TextOverflow.Ellipsis,
              fontSize = 12.sp
            )

            if (item.notes.isNotBlank()) {
              Spacer(modifier = Modifier.height(6.dp))
              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(8.dp))
                  .background(MaterialTheme.colorScheme.surface)
                  .padding(8.dp)
              ) {
                Text(
                  text = "Note: ${item.notes}",
                  fontSize = 11.sp,
                  color = MaterialTheme.colorScheme.onSurface,
                  fontWeight = FontWeight.Medium
                )
              }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = formattedDate,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
              )

              Text(
                text = "Re-open Guide →",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
              )
            }
          }
        }
      }
    }
  }
}

