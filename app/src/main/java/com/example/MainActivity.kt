package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.datasource.LocalFirstAidData
import com.example.data.model.TopicCategory
import com.example.ui.FirstAidViewModel
import com.example.ui.screens.AISearchResultScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.RecommendationsScreen
import com.example.ui.screens.TopicDetailScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

  private val viewModel: FirstAidViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        FirstAidApp(viewModel = viewModel)
      }
    }
  }
}

@Composable
fun FirstAidApp(viewModel: FirstAidViewModel) {
  val currentScreen by viewModel.currentScreen.collectAsState()
  val searchQuery by viewModel.searchQuery.collectAsState()
  val selectedCategory by viewModel.selectedCategory.collectAsState()
  val userProfile by viewModel.userProfile.collectAsState()
  val activeTopic by viewModel.activeTopic.collectAsState()
  val stepChecklist by viewModel.stepChecklist.collectAsState()
  val isTimerRunning by viewModel.isTimerRunning.collectAsState()
  val timerSecondsRemaining by viewModel.timerSecondsRemaining.collectAsState()
  val cprMetronomeActive by viewModel.cprMetronomeActive.collectAsState()
  val cprCompressionCount by viewModel.cprCompressionCount.collectAsState()
  val isAiGenerating by viewModel.isAiGenerating.collectAsState()
  val aiGenerationError by viewModel.aiGenerationError.collectAsState()
  val followUpAnswer by viewModel.followUpAnswer.collectAsState()
  val isFollowUpLoading by viewModel.isFollowUpLoading.collectAsState()
  val recommendations by viewModel.recommendations.collectAsState()
  val isRefreshingRecommendations by viewModel.isRefreshingRecommendations.collectAsState()
  val filteredHistory by viewModel.filteredHistory.collectAsState()
  val historySearchQuery by viewModel.historySearchQuery.collectAsState()
  val showFavoritesOnly by viewModel.showFavoritesOnly.collectAsState()

  // Handle system back navigation
  BackHandler(enabled = currentScreen != FirstAidViewModel.Screen.HOME) {
    viewModel.navigateTo(FirstAidViewModel.Screen.HOME)
  }

  val localFilteredTopics = if (searchQuery.isBlank()) {
    if (selectedCategory == TopicCategory.ALL) {
      LocalFirstAidData.predefinedTopics
    } else {
      LocalFirstAidData.predefinedTopics.filter { it.category == selectedCategory }
    }
  } else {
    LocalFirstAidData.predefinedTopics.filter { topic ->
      val matchesCategory = selectedCategory == TopicCategory.ALL || topic.category == selectedCategory
      val matchesQuery = topic.title.contains(searchQuery, ignoreCase = true) ||
        topic.subtitle.contains(searchQuery, ignoreCase = true) ||
        topic.summary.contains(searchQuery, ignoreCase = true) ||
        topic.steps.any { it.title.contains(searchQuery, ignoreCase = true) || it.description.contains(searchQuery, ignoreCase = true) }
      matchesCategory && matchesQuery
    }
  }

  val showBottomBar = currentScreen == FirstAidViewModel.Screen.HOME ||
    currentScreen == FirstAidViewModel.Screen.RECOMMENDATIONS ||
    currentScreen == FirstAidViewModel.Screen.HISTORY

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    bottomBar = {
      if (showBottomBar) {
        NavigationBar(
          modifier = Modifier.testTag("main_navigation_bar"),
          containerColor = MaterialTheme.colorScheme.surfaceVariant,
          tonalElevation = 0.dp
        ) {
          NavigationBarItem(
            selected = currentScreen == FirstAidViewModel.Screen.HOME,
            onClick = { viewModel.navigateTo(FirstAidViewModel.Screen.HOME) },
            icon = {
              Icon(
                imageVector = if (currentScreen == FirstAidViewModel.Screen.HOME) Icons.Filled.Home else Icons.Outlined.Home,
                contentDescription = "Protocols"
              )
            },
            label = {
              Text(
                "Protocols",
                fontWeight = if (currentScreen == FirstAidViewModel.Screen.HOME) FontWeight.Bold else FontWeight.Normal,
                fontSize = 12.sp
              )
            },
            colors = androidx.compose.material3.NavigationBarItemDefaults.colors(
              selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
              selectedTextColor = MaterialTheme.colorScheme.onSurface,
              indicatorColor = MaterialTheme.colorScheme.primaryContainer,
              unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
              unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            modifier = Modifier.testTag("nav_item_home")
          )

          NavigationBarItem(
            selected = currentScreen == FirstAidViewModel.Screen.RECOMMENDATIONS,
            onClick = { viewModel.navigateTo(FirstAidViewModel.Screen.RECOMMENDATIONS) },
            icon = {
              Icon(
                imageVector = if (currentScreen == FirstAidViewModel.Screen.RECOMMENDATIONS) Icons.Filled.Star else Icons.Outlined.StarOutline,
                contentDescription = "Recommendations"
              )
            },
            label = {
              Text(
                "AI Advice",
                fontWeight = if (currentScreen == FirstAidViewModel.Screen.RECOMMENDATIONS) FontWeight.Bold else FontWeight.Normal,
                fontSize = 12.sp
              )
            },
            colors = androidx.compose.material3.NavigationBarItemDefaults.colors(
              selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
              selectedTextColor = MaterialTheme.colorScheme.onSurface,
              indicatorColor = MaterialTheme.colorScheme.primaryContainer,
              unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
              unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            modifier = Modifier.testTag("nav_item_recommendations")
          )

          NavigationBarItem(
            selected = currentScreen == FirstAidViewModel.Screen.HISTORY,
            onClick = { viewModel.navigateTo(FirstAidViewModel.Screen.HISTORY) },
            icon = {
              Icon(
                imageVector = if (currentScreen == FirstAidViewModel.Screen.HISTORY) Icons.Filled.History else Icons.Outlined.History,
                contentDescription = "History"
              )
            },
            label = {
              Text(
                "History",
                fontWeight = if (currentScreen == FirstAidViewModel.Screen.HISTORY) FontWeight.Bold else FontWeight.Normal,
                fontSize = 12.sp
              )
            },
            colors = androidx.compose.material3.NavigationBarItemDefaults.colors(
              selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
              selectedTextColor = MaterialTheme.colorScheme.onSurface,
              indicatorColor = MaterialTheme.colorScheme.primaryContainer,
              unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
              unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            modifier = Modifier.testTag("nav_item_history")
          )
        }
      }
    }
  ) { innerPadding ->
    when (currentScreen) {
      FirstAidViewModel.Screen.HOME -> {
        HomeScreen(
          searchQuery = searchQuery,
          selectedCategory = selectedCategory,
          userProfile = userProfile,
          topics = localFilteredTopics,
          onSearchQueryChanged = viewModel::onSearchQueryChanged,
          onCategorySelected = viewModel::onCategorySelected,
          onTopicSelected = viewModel::selectTopic,
          onAiSearchRequested = viewModel::searchAndGenerateAiGuide,
          onNavigateToRecommendations = { viewModel.navigateTo(FirstAidViewModel.Screen.RECOMMENDATIONS) },
          modifier = Modifier.padding(innerPadding)
        )
      }

      FirstAidViewModel.Screen.TOPIC_DETAIL -> {
        activeTopic?.let { topic ->
          TopicDetailScreen(
            topic = topic,
            stepChecklist = stepChecklist,
            isTimerRunning = isTimerRunning,
            timerSecondsRemaining = timerSecondsRemaining,
            cprMetronomeActive = cprMetronomeActive,
            cprCompressionCount = cprCompressionCount,
            followUpAnswer = followUpAnswer,
            isFollowUpLoading = isFollowUpLoading,
            onBackClick = { viewModel.navigateTo(FirstAidViewModel.Screen.HOME) },
            onToggleStepCompletion = viewModel::toggleStepCompletion,
            onStartTimer = viewModel::startTimer,
            onPauseTimer = viewModel::pauseTimer,
            onResetTimer = viewModel::resetTimer,
            onToggleCprMetronome = viewModel::toggleCprMetronome,
            onResetCprCount = viewModel::resetCprCount,
            onAskFollowUp = viewModel::submitFollowUpQuestion,
            modifier = Modifier.padding(innerPadding)
          )
        }
      }

      FirstAidViewModel.Screen.HISTORY -> {
        HistoryScreen(
          historyList = filteredHistory,
          searchQuery = historySearchQuery,
          showFavoritesOnly = showFavoritesOnly,
          onSearchChanged = viewModel::onHistorySearchChanged,
          onToggleFavoritesOnly = viewModel::toggleShowFavoritesOnly,
          onSelectHistoryItem = { entity ->
            // Try to find matching predefined topic or generate dynamic topic from entity
            val matched = LocalFirstAidData.predefinedTopics.find { it.id == entity.topicId || it.title.equals(entity.title, ignoreCase = true) }
            if (matched != null) {
              viewModel.selectTopic(matched)
            } else {
              viewModel.searchAndGenerateAiGuide(entity.title)
            }
          },
          onToggleFavorite = viewModel::toggleHistoryFavorite,
          onUpdateNotes = viewModel::updateHistoryNotes,
          onDeleteItem = viewModel::deleteHistoryItem,
          onClearAll = viewModel::clearAllHistory,
          modifier = Modifier.padding(innerPadding)
        )
      }

      FirstAidViewModel.Screen.RECOMMENDATIONS -> {
        RecommendationsScreen(
          userProfile = userProfile,
          recommendations = recommendations,
          isRefreshing = isRefreshingRecommendations,
          onProfileSelected = viewModel::setUserProfile,
          onRefreshRecommendations = viewModel::refreshRecommendations,
          onSelectRecommendation = { rec ->
            val matched = LocalFirstAidData.predefinedTopics.find {
              it.title.equals(rec.relatedQuery, ignoreCase = true) ||
                it.title.equals(rec.title, ignoreCase = true)
            }
            if (matched != null) {
              viewModel.selectTopic(matched)
            } else {
              viewModel.searchAndGenerateAiGuide(rec.relatedQuery.ifBlank { rec.title })
            }
          },
          modifier = Modifier.padding(innerPadding)
        )
      }

      FirstAidViewModel.Screen.AI_SEARCH_RESULT -> {
        AISearchResultScreen(
          query = searchQuery,
          topic = activeTopic,
          isGenerating = isAiGenerating,
          errorMessage = aiGenerationError,
          stepChecklist = stepChecklist,
          isTimerRunning = isTimerRunning,
          timerSecondsRemaining = timerSecondsRemaining,
          cprMetronomeActive = cprMetronomeActive,
          cprCompressionCount = cprCompressionCount,
          followUpAnswer = followUpAnswer,
          isFollowUpLoading = isFollowUpLoading,
          onBackClick = { viewModel.navigateTo(FirstAidViewModel.Screen.HOME) },
          onRetry = { viewModel.searchAndGenerateAiGuide(searchQuery) },
          onToggleStepCompletion = viewModel::toggleStepCompletion,
          onStartTimer = viewModel::startTimer,
          onPauseTimer = viewModel::pauseTimer,
          onResetTimer = viewModel::resetTimer,
          onToggleCprMetronome = viewModel::toggleCprMetronome,
          onResetCprCount = viewModel::resetCprCount,
          onAskFollowUp = viewModel::submitFollowUpQuestion,
          modifier = Modifier.padding(innerPadding)
        )
      }
    }
  }
}
