package com.example.data.repository

import com.example.data.datasource.LocalFirstAidData
import com.example.data.db.HistoryDao
import com.example.data.db.HistoryEntity
import com.example.data.model.FirstAidTopic
import com.example.data.model.SafetyRecommendation
import com.example.data.model.TopicCategory
import com.example.data.remote.GeminiService
import kotlinx.coroutines.flow.Flow

class FirstAidRepository(
  private val historyDao: HistoryDao,
  private val geminiService: GeminiService = GeminiService()
) {

  val allHistory: Flow<List<HistoryEntity>> = historyDao.getAllHistory()

  val favoriteHistory: Flow<List<HistoryEntity>> = historyDao.getFavoriteHistory()

  fun searchHistory(query: String): Flow<List<HistoryEntity>> = historyDao.searchHistory(query)

  fun getPredefinedTopics(category: TopicCategory = TopicCategory.ALL): List<FirstAidTopic> {
    return if (category == TopicCategory.ALL) {
      LocalFirstAidData.predefinedTopics
    } else {
      LocalFirstAidData.predefinedTopics.filter { it.category == category }
    }
  }

  fun searchLocalTopics(query: String, category: TopicCategory = TopicCategory.ALL): List<FirstAidTopic> {
    val cleanQuery = query.trim().lowercase()
    val baseList = if (category == TopicCategory.ALL) {
      LocalFirstAidData.predefinedTopics
    } else {
      LocalFirstAidData.predefinedTopics.filter { it.category == category }
    }

    if (cleanQuery.isBlank()) return baseList

    return baseList.filter { topic ->
      topic.title.lowercase().contains(cleanQuery) ||
        topic.subtitle.lowercase().contains(cleanQuery) ||
        topic.summary.lowercase().contains(cleanQuery) ||
        topic.steps.any { it.title.lowercase().contains(cleanQuery) || it.description.lowercase().contains(cleanQuery) }
    }
  }

  fun getTopicById(id: String): FirstAidTopic? {
    return LocalFirstAidData.predefinedTopics.find { it.id == id }
  }

  suspend fun recordTopicView(topic: FirstAidTopic) {
    val entity = HistoryEntity(
      topicId = topic.id,
      title = topic.title,
      category = topic.category.label,
      severity = topic.severity.name,
      summary = topic.summary,
      timestamp = System.currentTimeMillis(),
      isAiGenerated = topic.isAiGenerated
    )
    historyDao.insert(entity)
  }

  suspend fun toggleFavorite(historyId: Long) {
    historyDao.toggleFavorite(historyId)
  }

  suspend fun updateHistoryNotes(historyId: Long, notes: String) {
    historyDao.updateNotes(historyId, notes)
  }

  suspend fun deleteHistory(historyId: Long) {
    historyDao.deleteById(historyId)
  }

  suspend fun clearAllHistory() {
    historyDao.clearAll()
  }

  suspend fun generateAiFirstAidGuide(userQuery: String): Result<FirstAidTopic> {
    // Check if query matches any predefined topic closely first for instant response
    val matched = LocalFirstAidData.predefinedTopics.find {
      it.title.equals(userQuery, ignoreCase = true) ||
        userQuery.lowercase().contains(it.title.lowercase()) ||
        it.title.lowercase().contains(userQuery.lowercase())
    }

    // Call Gemini for dynamic, tailored and deep-dive emergency responses
    val aiResult = geminiService.generateFirstAidGuide(userQuery)
    if (aiResult.isSuccess) {
      return aiResult
    }

    // If Gemini fails (e.g. offline or no key), return matched local topic or fallback
    if (matched != null) {
      return Result.success(matched)
    }

    return aiResult
  }

  suspend fun getSafetyRecommendations(
    historyTitles: List<String>,
    userContext: String
  ): List<SafetyRecommendation> {
    val aiResult = geminiService.generateRecommendations(historyTitles, userContext)
    if (aiResult.isSuccess) {
      val generated = aiResult.getOrNull()
      if (!generated.isNullOrEmpty()) {
        return generated
      }
    }
    // Fallback to intelligent local recommendations if offline or error
    return generateLocalContextRecommendations(userContext)
  }

  suspend fun askFollowUpQuestion(topicTitle: String, question: String): Result<String> {
    return geminiService.askFollowUpQuestion(topicTitle, question)
  }

  private fun generateLocalContextRecommendations(userContext: String): List<SafetyRecommendation> {
    return when {
      userContext.contains("Outdoor", ignoreCase = true) || userContext.contains("Hiking", ignoreCase = true) -> listOf(
        SafetyRecommendation(
          id = "rec_wild_1",
          title = "Snake & Spider Bites",
          category = "Wilderness",
          reason = "Crucial for trail safety and remote areas where EMS response times may be extended.",
          actionAdvice = "Pack wide elastic bandages and know not to cut or tourniquet bite sites.",
          relatedQuery = "Snake & Spider Bites"
        ),
        SafetyRecommendation(
          id = "rec_wild_2",
          title = "Fractures & Sprains Splinting",
          category = "Trauma",
          reason = "Uneven terrain increases risk of ankle rolls and limb trauma.",
          actionAdvice = "Carry a SAM splint or triangular bandage in your pack.",
          relatedQuery = "Fractures, Dislocations & Sprains"
        ),
        SafetyRecommendation(
          id = "rec_wild_3",
          title = "Heat Stroke & Dehydration",
          category = "Environmental",
          reason = "Sun exposure and physical exertion on long hikes.",
          actionAdvice = "Carry electrolyte packets and recognize confusion as a critical sign.",
          relatedQuery = "Heat Stroke & Heat Exhaustion"
        ),
        SafetyRecommendation(
          id = "rec_wild_4",
          title = "Severe Bleeding & Hemorrhage",
          category = "Trauma",
          reason = "Sharp rock lacerations or tool injuries outdoors.",
          actionAdvice = "Know proper direct pressure technique and tourniquet usage.",
          relatedQuery = "Severe Bleeding & Hemorrhage"
        )
      )
      userContext.contains("Parent", ignoreCase = true) || userContext.contains("Toddler", ignoreCase = true) -> listOf(
        SafetyRecommendation(
          id = "rec_pedia_1",
          title = "Choking (Heimlich Maneuver)",
          category = "Resuscitation",
          reason = "Small toy parts and food are the leading pediatric hazards.",
          actionAdvice = "Know the difference between back blows for infants vs abdominal thrusts.",
          relatedQuery = "Choking (Heimlich Maneuver)"
        ),
        SafetyRecommendation(
          id = "rec_pedia_2",
          title = "Burns & Scalds Protocol",
          category = "Trauma",
          reason = "Hot bath water, soups, and stove surfaces in home environments.",
          actionAdvice = "Cool with tap water for 15 minutes immediately. Never use ice or butter.",
          relatedQuery = "Burns & Scalds"
        ),
        SafetyRecommendation(
          id = "rec_pedia_3",
          title = "Poisoning & Chemical Ingestion",
          category = "Allergic & Poison",
          reason = "Household detergents, medication bottles, and pods.",
          actionAdvice = "Save Poison Help 1-800-222-1222 in speed dial. Never induce vomiting.",
          relatedQuery = "Poisoning & Chemical Ingestion"
        ),
        SafetyRecommendation(
          id = "rec_pedia_4",
          title = "Concussion & Head Trauma",
          category = "Trauma",
          reason = "Falls from playground equipment, beds, or stairs.",
          actionAdvice = "Watch for vomiting, lethargy, or pupil changes after a head bump.",
          relatedQuery = "Concussion & Head Trauma"
        )
      )
      else -> LocalFirstAidData.defaultRecommendations
    }
  }
}
