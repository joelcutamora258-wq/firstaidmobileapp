package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.datasource.LocalFirstAidData
import com.example.data.db.AppDatabase
import com.example.data.db.HistoryEntity
import com.example.data.model.FirstAidTopic
import com.example.data.model.SafetyRecommendation
import com.example.data.model.TopicCategory
import com.example.data.model.UserProfileContext
import com.example.data.repository.FirstAidRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FirstAidViewModel(application: Application) : AndroidViewModel(application) {

  private val repository: FirstAidRepository

  init {
    val db = AppDatabase.getDatabase(application)
    repository = FirstAidRepository(db.historyDao())
  }

  // Active Navigation Screen
  enum class Screen {
    HOME,
    TOPIC_DETAIL,
    HISTORY,
    RECOMMENDATIONS,
    AI_SEARCH_RESULT
  }

  private val _currentScreen = MutableStateFlow(Screen.HOME)
  val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

  // Search & Filtering
  private val _searchQuery = MutableStateFlow("")
  val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

  private val _selectedCategory = MutableStateFlow(TopicCategory.ALL)
  val selectedCategory: StateFlow<TopicCategory> = _selectedCategory.asStateFlow()

  // User Profile Context for Personalized AI Recommendations
  private val _userProfile = MutableStateFlow(LocalFirstAidData.userProfiles[0])
  val userProfile: StateFlow<UserProfileContext> = _userProfile.asStateFlow()

  // Active Topic & Guides
  private val _activeTopic = MutableStateFlow<FirstAidTopic?>(null)
  val activeTopic: StateFlow<FirstAidTopic?> = _activeTopic.asStateFlow()

  // Step Checklist for active guide
  private val _stepChecklist = MutableStateFlow<Map<Int, Boolean>>(emptyMap())
  val stepChecklist: StateFlow<Map<Int, Boolean>> = _stepChecklist.asStateFlow()

  // Emergency Tools State
  private val _isTimerRunning = MutableStateFlow(false)
  val isTimerRunning: StateFlow<Boolean> = _isTimerRunning.asStateFlow()

  private val _timerSecondsRemaining = MutableStateFlow(0)
  val timerSecondsRemaining: StateFlow<Int> = _timerSecondsRemaining.asStateFlow()

  private val _cprMetronomeActive = MutableStateFlow(false)
  val cprMetronomeActive: StateFlow<Boolean> = _cprMetronomeActive.asStateFlow()

  private val _cprCompressionCount = MutableStateFlow(0)
  val cprCompressionCount: StateFlow<Int> = _cprCompressionCount.asStateFlow()

  private var timerJob: Job? = null
  private var metronomeJob: Job? = null

  // AI Generation State
  private val _isAiGenerating = MutableStateFlow(false)
  val isAiGenerating: StateFlow<Boolean> = _isAiGenerating.asStateFlow()

  private val _aiGenerationError = MutableStateFlow<String?>(null)
  val aiGenerationError: StateFlow<String?> = _aiGenerationError.asStateFlow()

  // AI Follow-up Q&A
  private val _followUpAnswer = MutableStateFlow<String?>(null)
  val followUpAnswer: StateFlow<String?> = _followUpAnswer.asStateFlow()

  private val _isFollowUpLoading = MutableStateFlow(false)
  val isFollowUpLoading: StateFlow<Boolean> = _isFollowUpLoading.asStateFlow()

  // Recommendations State
  private val _recommendations = MutableStateFlow<List<SafetyRecommendation>>(LocalFirstAidData.defaultRecommendations)
  val recommendations: StateFlow<List<SafetyRecommendation>> = _recommendations.asStateFlow()

  private val _isRefreshingRecommendations = MutableStateFlow(false)
  val isRefreshingRecommendations: StateFlow<Boolean> = _isRefreshingRecommendations.asStateFlow()

  // History State
  private val _historySearchQuery = MutableStateFlow("")
  val historySearchQuery: StateFlow<String> = _historySearchQuery.asStateFlow()

  private val _showFavoritesOnly = MutableStateFlow(false)
  val showFavoritesOnly: StateFlow<Boolean> = _showFavoritesOnly.asStateFlow()

  val filteredHistory: StateFlow<List<HistoryEntity>> = combine(
    repository.allHistory,
    _historySearchQuery,
    _showFavoritesOnly
  ) { historyList, query, favsOnly ->
    historyList.filter { entity ->
      val matchesQuery = query.isBlank() ||
        entity.title.contains(query, ignoreCase = true) ||
        entity.category.contains(query, ignoreCase = true) ||
        entity.summary.contains(query, ignoreCase = true) ||
        entity.notes.contains(query, ignoreCase = true)

      val matchesFav = !favsOnly || entity.isFavorite
      matchesQuery && matchesFav
    }
  }.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = emptyList()
  )

  init {
    loadRecommendations()
  }

  fun navigateTo(screen: Screen) {
    _currentScreen.value = screen
  }

  fun onSearchQueryChanged(query: String) {
    _searchQuery.value = query
  }

  fun onCategorySelected(category: TopicCategory) {
    _selectedCategory.value = category
  }

  fun selectTopic(topic: FirstAidTopic) {
    _activeTopic.value = topic
    _stepChecklist.value = emptyMap()
    _followUpAnswer.value = null
    resetTimer(topic.steps.firstOrNull { it.durationSeconds != null }?.durationSeconds ?: 0)
    stopCprMetronome()

    viewModelScope.launch {
      repository.recordTopicView(topic)
      loadRecommendations()
    }

    _currentScreen.value = Screen.TOPIC_DETAIL
  }

  fun searchAndGenerateAiGuide(query: String) {
    if (query.isBlank()) return
    _isAiGenerating.value = true
    _aiGenerationError.value = null
    _currentScreen.value = Screen.AI_SEARCH_RESULT

    viewModelScope.launch {
      val result = repository.generateAiFirstAidGuide(query)
      _isAiGenerating.value = false
      if (result.isSuccess) {
        val topic = result.getOrNull()
        if (topic != null) {
          _activeTopic.value = topic
          _stepChecklist.value = emptyMap()
          _followUpAnswer.value = null
          repository.recordTopicView(topic)
          loadRecommendations()
        }
      } else {
        _aiGenerationError.value = result.exceptionOrNull()?.localizedMessage ?: "Failed to generate first aid guide. Check internet connection or API configuration."
      }
    }
  }

  fun setUserProfile(profile: UserProfileContext) {
    _userProfile.value = profile
    loadRecommendations()
  }

  fun refreshRecommendations() {
    loadRecommendations()
  }

  private fun loadRecommendations() {
    viewModelScope.launch {
      _isRefreshingRecommendations.value = true
      val historySnapshot = repository.allHistory.stateIn(viewModelScope).value
      val historyTitles = historySnapshot.take(5).map { it.title }
      val recs = repository.getSafetyRecommendations(historyTitles, _userProfile.value.name)
      _recommendations.value = recs
      _isRefreshingRecommendations.value = false
    }
  }

  fun toggleStepCompletion(stepNumber: Int) {
    val current = _stepChecklist.value.toMutableMap()
    val isDone = current[stepNumber] ?: false
    current[stepNumber] = !isDone
    _stepChecklist.value = current
  }

  fun submitFollowUpQuestion(question: String) {
    val topic = _activeTopic.value ?: return
    if (question.isBlank()) return

    _isFollowUpLoading.value = true
    _followUpAnswer.value = null

    viewModelScope.launch {
      val result = repository.askFollowUpQuestion(topic.title, question)
      _isFollowUpLoading.value = false
      if (result.isSuccess) {
        _followUpAnswer.value = result.getOrNull()
      } else {
        _followUpAnswer.value = "Unable to reach Gemini assistant: ${result.exceptionOrNull()?.localizedMessage}"
      }
    }
  }

  // Timer Controls
  fun startTimer(totalSeconds: Int) {
    if (_timerSecondsRemaining.value <= 0) {
      _timerSecondsRemaining.value = totalSeconds
    }
    _isTimerRunning.value = true
    timerJob?.cancel()
    timerJob = viewModelScope.launch {
      while (_timerSecondsRemaining.value > 0 && _isTimerRunning.value) {
        delay(1000)
        _timerSecondsRemaining.value -= 1
      }
      _isTimerRunning.value = false
    }
  }

  fun pauseTimer() {
    _isTimerRunning.value = false
    timerJob?.cancel()
  }

  fun resetTimer(totalSeconds: Int) {
    _isTimerRunning.value = false
    timerJob?.cancel()
    _timerSecondsRemaining.value = totalSeconds
  }

  // CPR Metronome & Compression Counter (110 BPM = ~545ms per beat)
  fun toggleCprMetronome() {
    if (_cprMetronomeActive.value) {
      stopCprMetronome()
    } else {
      startCprMetronome()
    }
  }

  private fun startCprMetronome() {
    _cprMetronomeActive.value = true
    metronomeJob?.cancel()
    metronomeJob = viewModelScope.launch {
      val intervalMs = 545L // 110 BPM
      while (_cprMetronomeActive.value) {
        delay(intervalMs)
        _cprCompressionCount.value += 1
      }
    }
  }

  private fun stopCprMetronome() {
    _cprMetronomeActive.value = false
    metronomeJob?.cancel()
  }

  fun resetCprCount() {
    _cprCompressionCount.value = 0
  }

  // History Actions
  fun onHistorySearchChanged(query: String) {
    _historySearchQuery.value = query
  }

  fun toggleShowFavoritesOnly() {
    _showFavoritesOnly.value = !_showFavoritesOnly.value
  }

  fun toggleHistoryFavorite(id: Long) {
    viewModelScope.launch {
      repository.toggleFavorite(id)
    }
  }

  fun updateHistoryNotes(id: Long, notes: String) {
    viewModelScope.launch {
      repository.updateHistoryNotes(id, notes)
    }
  }

  fun deleteHistoryItem(id: Long) {
    viewModelScope.launch {
      repository.deleteHistory(id)
    }
  }

  fun clearAllHistory() {
    viewModelScope.launch {
      repository.clearAllHistory()
    }
  }

  override fun onCleared() {
    super.onCleared()
    timerJob?.cancel()
    metronomeJob?.cancel()
  }
}
