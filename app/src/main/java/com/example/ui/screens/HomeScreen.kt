package com.example.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.datasource.LocalFirstAidData
import com.example.data.model.FirstAidTopic
import com.example.data.model.TopicCategory
import com.example.data.model.UserProfileContext
import com.example.ui.components.CategoryChipRow
import com.example.ui.components.EmergencyQuickDialBanner
import com.example.ui.components.FirstAidTopicCard
import com.example.ui.theme.CriticalRed

@Composable
fun HomeScreen(
  searchQuery: String,
  selectedCategory: TopicCategory,
  userProfile: UserProfileContext,
  topics: List<FirstAidTopic>,
  onSearchQueryChanged: (String) -> Unit,
  onCategorySelected: (TopicCategory) -> Unit,
  onTopicSelected: (FirstAidTopic) -> Unit,
  onAiSearchRequested: (String) -> Unit,
  onNavigateToRecommendations: () -> Unit,
  modifier: Modifier = Modifier
) {
  val keyboardController = LocalSoftwareKeyboardController.current

  val emergencySuggestedQueries = listOf(
    "Infant choking on coin",
    "Boiling oil splash on forearm",
    "Stung by jellyfish at beach",
    "Fell off ladder broken arm",
    "Elderly sudden face drooping"
  )

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .testTag("home_screen_list"),
    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // 1. Hero Header Banner
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("hero_banner_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Box(modifier = Modifier.fillMaxWidth()) {
          Image(
            painter = painterResource(id = R.drawable.hero_first_aid),
            contentDescription = "First Aid Emergency Response",
            modifier = Modifier
              .fillMaxWidth()
              .height(150.dp),
            contentScale = ContentScale.Crop
          )
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .height(150.dp)
              .background(
                Brush.verticalGradient(
                  colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.75f))
                )
              )
          )
          Column(
            modifier = Modifier
              .align(Alignment.BottomStart)
              .padding(16.dp)
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(24.dp)
                  .clip(CircleShape)
                  .background(CriticalRed),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.MedicalServices,
                  contentDescription = "Medical",
                  tint = Color.White,
                  modifier = Modifier.size(14.dp)
                )
              }
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "First Aid AI Assistant",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
              )
            }
            Text(
              text = "Instant step-by-step emergency protocols & AI guidance",
              color = Color.White.copy(alpha = 0.9f),
              fontSize = 12.sp
            )
          }
        }
      }
    }

    // 2. Emergency Quick Dial Callout
    item {
      EmergencyQuickDialBanner()
    }

    // 3. AI Search Bar (High Density Rounded-Full Container)
    item {
      Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
          value = searchQuery,
          onValueChange = onSearchQueryChanged,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("main_search_input"),
          placeholder = {
            Text("Search first aid topics (e.g. burn, choking, CPR)...", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          },
          leadingIcon = {
            Icon(
              imageVector = Icons.Default.Search,
              contentDescription = "Search",
              tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
          },
          trailingIcon = {
            if (searchQuery.isNotBlank()) {
              IconButton(onClick = { onSearchQueryChanged("") }) {
                Icon(
                  imageVector = Icons.Default.Clear,
                  contentDescription = "Clear Search",
                  tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            } else {
              Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = "Gemini AI",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
              )
            }
          },
          shape = CircleShape,
          singleLine = true,
          keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
          keyboardActions = KeyboardActions(
            onSearch = {
              keyboardController?.hide()
              if (searchQuery.isNotBlank()) {
                onAiSearchRequested(searchQuery)
              }
            }
          ),
          colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = Color.Transparent
          )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // AI Search Button
        if (searchQuery.isNotBlank()) {
          Button(
            onClick = {
              keyboardController?.hide()
              onAiSearchRequested(searchQuery)
            },
            modifier = Modifier
              .fillMaxWidth()
              .height(44.dp)
              .testTag("gemini_ai_search_button"),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(
              containerColor = MaterialTheme.colorScheme.primary,
              contentColor = MaterialTheme.colorScheme.onPrimary
            )
          ) {
            Icon(
              imageVector = Icons.Default.AutoAwesome,
              contentDescription = "Generate AI Guide",
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "Generate AI Step-by-Step for \"$searchQuery\"",
              fontWeight = FontWeight.SemiBold,
              fontSize = 13.sp
            )
          }
        }
      }
    }

    // 4. Quick Emergency Prompt Suggestions
    item {
      Column(modifier = Modifier.fillMaxWidth()) {
        Text(
          text = "Quick Scenarios (AI Instant Lookup)",
          style = MaterialTheme.typography.labelMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(6.dp))
        LazyRow(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          items(emergencySuggestedQueries) { query ->
            SuggestionChip(
              onClick = {
                onSearchQueryChanged(query)
                onAiSearchRequested(query)
              },
              label = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    modifier = Modifier.size(12.dp),
                    tint = MaterialTheme.colorScheme.secondary
                  )
                  Spacer(modifier = Modifier.width(4.dp))
                  Text(text = query, fontSize = 12.sp)
                }
              }
            )
          }
        }
      }
    }

    // 5. Category Chips Filter
    item {
      Column(modifier = Modifier.fillMaxWidth()) {
        Text(
          text = "Browse by Category",
          style = MaterialTheme.typography.titleSmall,
          fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        CategoryChipRow(
          selectedCategory = selectedCategory,
          onCategorySelected = onCategorySelected
        )
      }
    }

    // 6. Topics Section Header
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = if (searchQuery.isBlank()) "Essential First Aid Protocols (${topics.size})" else "Search Results (${topics.size})",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold
        )
      }
    }

    // 7. Topic Cards
    if (topics.isEmpty()) {
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Icon(
              imageVector = Icons.Default.AutoAwesome,
              contentDescription = "No Results",
              modifier = Modifier.size(40.dp),
              tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
              text = "No offline protocol matches \"$searchQuery\"",
              fontWeight = FontWeight.Bold,
              fontSize = 15.sp
            )
            Text(
              text = "Tap below to let Gemini AI generate custom verified first aid steps for this emergency.",
              fontSize = 13.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.padding(vertical = 6.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
              onClick = { onAiSearchRequested(searchQuery) },
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier.testTag("ai_fallback_generate_button")
            ) {
              Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null)
              Spacer(modifier = Modifier.width(6.dp))
              Text("Generate with Gemini AI")
            }
          }
        }
      }
    } else {
      items(topics, key = { it.id }) { topic ->
        FirstAidTopicCard(
          topic = topic,
          onClick = { onTopicSelected(topic) }
        )
      }
    }

    // 8. Bottom Recommendation Callout Banner (High Density Tonal Container)
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .clickable { onNavigateToRecommendations() }
          .testTag("recommendation_callout_banner"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
          containerColor = MaterialTheme.colorScheme.primaryContainer,
          contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "AI RECOMMENDATION",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.8.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer
              )
            }
            Text(
              text = "Based on ${userProfile.name}",
              fontSize = 11.sp,
              color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
            )
          }

          Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
              text = "Proactive Safety Recommendations",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
              text = "Tailored preparedness guides and preventative advice for your active profile.",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              lineHeight = 16.sp
            )
          }

          Button(
            onClick = { onNavigateToRecommendations() },
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(
              containerColor = MaterialTheme.colorScheme.primary,
              contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            modifier = Modifier.height(36.dp)
          ) {
            Text(
              text = "View Emergency Steps",
              fontSize = 12.sp,
              fontWeight = FontWeight.Medium
            )
          }
        }
      }
    }
  }
}
