package com.example.data.model

enum class SeverityLevel(val label: String) {
  CRITICAL("Critical Emergency"),
  URGENT("Urgent Attention"),
  STANDARD("Standard First Aid")
}

enum class TopicCategory(val label: String, val iconName: String) {
  ALL("All Topics", "emergency"),
  RESUSCITATION("CPR & Breathing", "heart_check"),
  TRAUMA("Trauma & Bleeding", "bandage"),
  ENVIRONMENTAL("Environmental", "thermostat"),
  ALLERGIC("Allergies & Poison", "medical_services"),
  PEDIATRIC("Child & Infant", "child_care"),
  WILDERNESS("Outdoor & Bites", "forest")
}

data class FirstAidStep(
  val stepNumber: Int,
  val title: String,
  val description: String,
  val cautionNote: String? = null,
  val durationSeconds: Int? = null,
  val isCompleted: Boolean = false
)

data class DoAndDontItem(
  val isDo: Boolean,
  val text: String
)

enum class EmergencyToolType {
  NONE,
  CPR_METRONOME,
  BURN_COOLING_TIMER,
  PRESSURE_TIMER,
  EYE_FLUSH_TIMER
}

data class FirstAidTopic(
  val id: String,
  val title: String,
  val subtitle: String,
  val category: TopicCategory,
  val severity: SeverityLevel,
  val estimatedMinutes: Int,
  val emergencyCallPrompt: String = "Call 911 immediately if victim is unresponsive or not breathing.",
  val redFlags: List<String> = emptyList(),
  val summary: String,
  val steps: List<FirstAidStep>,
  val dosAndDonts: List<DoAndDontItem> = emptyList(),
  val toolType: EmergencyToolType = EmergencyToolType.NONE,
  val isAiGenerated: Boolean = false,
  val sourceOrDisclaimer: String = "Based on standard emergency first aid protocols. Always contact professional emergency services."
)

data class UserProfileContext(
  val id: String,
  val name: String,
  val description: String,
  val iconName: String
)

data class SafetyRecommendation(
  val id: String,
  val title: String,
  val category: String,
  val reason: String,
  val actionAdvice: String,
  val relatedQuery: String
)
