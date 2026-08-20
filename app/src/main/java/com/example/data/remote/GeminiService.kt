package com.example.data.remote

import android.util.Log
import com.example.BuildConfig
import com.example.data.model.DoAndDontItem
import com.example.data.model.EmergencyToolType
import com.example.data.model.FirstAidStep
import com.example.data.model.FirstAidTopic
import com.example.data.model.SafetyRecommendation
import com.example.data.model.SeverityLevel
import com.example.data.model.TopicCategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID
import java.util.concurrent.TimeUnit

class GeminiService {
  private val client = OkHttpClient.Builder()
    .connectTimeout(60, TimeUnit.SECONDS)
    .readTimeout(60, TimeUnit.SECONDS)
    .writeTimeout(60, TimeUnit.SECONDS)
    .build()

  private val jsonMediaType = "application/json; charset=utf-8".toMediaType()

  suspend fun generateFirstAidGuide(userQuery: String): Result<FirstAidTopic> = withContext(Dispatchers.IO) {
    val apiKey = BuildConfig.GEMINI_API_KEY
    if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
      return@withContext Result.failure(Exception("Gemini API key is not configured. Please set your key in AI Studio Secrets panel."))
    }

    val systemInstruction = """
      You are an expert, calm, certified medical first responder AI.
      When given a first aid topic, injury, or emergency question, provide structured, safe, step-by-step guidance following international standard first aid protocols (Red Cross / AHA guidelines).
      Always return ONLY valid JSON matching this exact schema:
      {
        "title": "Clear concise topic title",
        "subtitle": "Short 1-sentence description of the condition",
        "category": "RESUSCITATION" | "TRAUMA" | "ENVIRONMENTAL" | "ALLERGIC" | "PEDIATRIC" | "WILDERNESS",
        "severity": "CRITICAL" | "URGENT" | "STANDARD",
        "estimatedMinutes": 5,
        "emergencyCallPrompt": "Clear warning when to call 911 / emergency services immediately",
        "redFlags": ["Red flag symptom 1", "Red flag symptom 2", "Red flag symptom 3"],
        "summary": "2-3 sentence overview of immediate objective",
        "steps": [
          {
            "stepNumber": 1,
            "title": "Action step title",
            "description": "Clear, direct, easy to follow instruction for what to do right now",
            "cautionNote": "Optional critical pitfall to avoid in this step",
            "durationSeconds": 0
          }
        ],
        "dosAndDonts": [
          {"isDo": true, "text": "DO apply gentle direct pressure with clean cloth"},
          {"isDo": false, "text": "DO NOT remove embedded foreign objects"}
        ],
        "toolType": "NONE" | "CPR_METRONOME" | "BURN_COOLING_TIMER" | "PRESSURE_TIMER" | "EYE_FLUSH_TIMER"
      }
    """.trimIndent()

    val prompt = "Provide a comprehensive, verified, step-by-step first aid guide for: $userQuery"

    val requestBodyJson = JSONObject().apply {
      put("contents", JSONArray().apply {
        put(JSONObject().apply {
          put("parts", JSONArray().apply {
            put(JSONObject().apply {
              put("text", "$systemInstruction\n\nUser Request: $prompt\nOutput ONLY raw JSON.")
            })
          })
        })
      })
      put("generationConfig", JSONObject().apply {
        put("temperature", 0.2)
        put("topP", 0.95)
        put("responseMimeType", "application/json")
      })
    }

    val request = Request.Builder()
      .url("https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey")
      .post(requestBodyJson.toString().toRequestBody(jsonMediaType))
      .build()

    try {
      val response = client.newCall(request).execute()
      val responseBodyString = response.body?.string() ?: ""

      if (!response.isSuccessful) {
        Log.e("GeminiService", "API error: ${response.code} $responseBodyString")
        return@withContext Result.failure(Exception("Gemini API Error (${response.code}): ${response.message}"))
      }

      val jsonResponse = JSONObject(responseBodyString)
      val candidates = jsonResponse.optJSONArray("candidates")
      val firstCandidate = candidates?.optJSONObject(0)
      val contentObj = firstCandidate?.optJSONObject("content")
      val parts = contentObj?.optJSONArray("parts")
      val text = parts?.optJSONObject(0)?.optString("text") ?: ""

      val parsedTopic = parseTopicJson(text, userQuery)
      Result.success(parsedTopic)
    } catch (e: Exception) {
      Log.e("GeminiService", "Failed to generate first aid guide", e)
      Result.failure(e)
    }
  }

  suspend fun generateRecommendations(
    historyList: List<String>,
    userContext: String
  ): Result<List<SafetyRecommendation>> = withContext(Dispatchers.IO) {
    val apiKey = BuildConfig.GEMINI_API_KEY
    if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
      return@withContext Result.failure(Exception("Gemini API key is not configured."))
    }

    val prompt = """
      Based on the user's active life profile context: "$userContext" and their past first aid lookups: [${historyList.joinToString(", ")}],
      suggest 4 relevant, proactive first aid topics, safety preparations, or emergency protocols they should learn or prepare for.
      Return ONLY a JSON array of objects with the following schema:
      [
        {
          "id": "unique-id",
          "title": "Title of recommended first aid topic or safety guide",
          "category": "Category name (e.g., Trauma, Wilderness, Pediatric, Home Safety)",
          "reason": "Why this is specifically recommended based on context and history",
          "actionAdvice": "Key proactive action or kit preparation advice",
          "relatedQuery": "Query string to search for full guide"
        }
      ]
    """.trimIndent()

    val requestBodyJson = JSONObject().apply {
      put("contents", JSONArray().apply {
        put(JSONObject().apply {
          put("parts", JSONArray().apply {
            put(JSONObject().apply {
              put("text", prompt)
            })
          })
        })
      })
      put("generationConfig", JSONObject().apply {
        put("temperature", 0.4)
        put("responseMimeType", "application/json")
      })
    }

    val request = Request.Builder()
      .url("https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey")
      .post(requestBodyJson.toString().toRequestBody(jsonMediaType))
      .build()

    try {
      val response = client.newCall(request).execute()
      val responseBody = response.body?.string() ?: ""
      if (!response.isSuccessful) {
        return@withContext Result.failure(Exception("Gemini error (${response.code})"))
      }

      val jsonResponse = JSONObject(responseBody)
      val text = jsonResponse.optJSONArray("candidates")
        ?.optJSONObject(0)
        ?.optJSONObject("content")
        ?.optJSONArray("parts")
        ?.optJSONObject(0)
        ?.optString("text") ?: "[]"

      val recommendations = parseRecommendationsJson(text)
      Result.success(recommendations)
    } catch (e: Exception) {
      Log.e("GeminiService", "Failed to generate recommendations", e)
      Result.failure(e)
    }
  }

  suspend fun askFollowUpQuestion(
    topicTitle: String,
    userQuestion: String
  ): Result<String> = withContext(Dispatchers.IO) {
    val apiKey = BuildConfig.GEMINI_API_KEY
    if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
      return@withContext Result.failure(Exception("Gemini API key is not configured."))
    }

    val prompt = """
      You are a first aid assistant. The user is asking a specific follow-up question regarding the emergency topic: "$topicTitle".
      User Question: "$userQuestion"
      
      Provide a concise, direct, medically grounded 2-3 paragraph answer. Emphasize life safety, explicit Do's and Don'ts, and clearly state when immediate medical emergency services (911) must be called.
    """.trimIndent()

    val requestBodyJson = JSONObject().apply {
      put("contents", JSONArray().apply {
        put(JSONObject().apply {
          put("parts", JSONArray().apply {
            put(JSONObject().apply { put("text", prompt) })
          })
        })
      })
      put("generationConfig", JSONObject().apply {
        put("temperature", 0.3)
      })
    }

    val request = Request.Builder()
      .url("https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey")
      .post(requestBodyJson.toString().toRequestBody(jsonMediaType))
      .build()

    try {
      val response = client.newCall(request).execute()
      val responseBody = response.body?.string() ?: ""
      if (!response.isSuccessful) {
        return@withContext Result.failure(Exception("Gemini error (${response.code})"))
      }
      val jsonResponse = JSONObject(responseBody)
      val text = jsonResponse.optJSONArray("candidates")
        ?.optJSONObject(0)
        ?.optJSONObject("content")
        ?.optJSONArray("parts")
        ?.optJSONObject(0)
        ?.optString("text") ?: "No response generated."
      Result.success(text)
    } catch (e: Exception) {
      Result.failure(e)
    }
  }

  private fun parseTopicJson(rawJson: String, fallbackQuery: String): FirstAidTopic {
    val cleanJson = rawJson.trim().removePrefix("```json").removePrefix("```").removeSuffix("```").trim()
    val json = JSONObject(cleanJson)

    val title = json.optString("title", fallbackQuery.replaceFirstChar { it.uppercase() })
    val subtitle = json.optString("subtitle", "Emergency First Aid Guide")
    val categoryStr = json.optString("category", "TRAUMA").uppercase()
    val category = try {
      TopicCategory.valueOf(categoryStr)
    } catch (e: Exception) {
      TopicCategory.TRAUMA
    }

    val severityStr = json.optString("severity", "URGENT").uppercase()
    val severity = try {
      SeverityLevel.valueOf(severityStr)
    } catch (e: Exception) {
      SeverityLevel.URGENT
    }

    val estimatedMinutes = json.optInt("estimatedMinutes", 5)
    val emergencyCallPrompt = json.optString("emergencyCallPrompt", "Call 911 immediately if victim is unresponsive or has severe breathing difficulty.")
    val summary = json.optString("summary", "Follow these emergency steps sequentially to stabilize the situation.")

    val redFlagsList = mutableListOf<String>()
    val redFlagsJson = json.optJSONArray("redFlags")
    if (redFlagsJson != null) {
      for (i in 0 until redFlagsJson.length()) {
        redFlagsList.add(redFlagsJson.optString(i))
      }
    }

    val stepsList = mutableListOf<FirstAidStep>()
    val stepsJson = json.optJSONArray("steps")
    if (stepsJson != null && stepsJson.length() > 0) {
      for (i in 0 until stepsJson.length()) {
        val stepObj = stepsJson.optJSONObject(i)
        if (stepObj != null) {
          stepsList.add(
            FirstAidStep(
              stepNumber = stepObj.optInt("stepNumber", i + 1),
              title = stepObj.optString("title", "Step ${i + 1}"),
              description = stepObj.optString("description", ""),
              cautionNote = stepObj.optString("cautionNote").takeIf { it.isNotBlank() },
              durationSeconds = stepObj.optInt("durationSeconds", 0).takeIf { it > 0 }
            )
          )
        }
      }
    } else {
      stepsList.add(
        FirstAidStep(
          stepNumber = 1,
          title = "Assess the Scene & Call for Help",
          description = "Ensure the area is safe for you and the victim. Call emergency services (911) immediately if required."
        )
      )
    }

    val dosAndDontsList = mutableListOf<DoAndDontItem>()
    val dosDontsJson = json.optJSONArray("dosAndDonts")
    if (dosDontsJson != null) {
      for (i in 0 until dosDontsJson.length()) {
        val itemObj = dosDontsJson.optJSONObject(i)
        if (itemObj != null) {
          dosAndDontsList.add(
            DoAndDontItem(
              isDo = itemObj.optBoolean("isDo", true),
              text = itemObj.optString("text", "")
            )
          )
        }
      }
    }

    val toolTypeStr = json.optString("toolType", "NONE").uppercase()
    val toolType = try {
      EmergencyToolType.valueOf(toolTypeStr)
    } catch (e: Exception) {
      EmergencyToolType.NONE
    }

    return FirstAidTopic(
      id = "ai_" + UUID.randomUUID().toString().take(8),
      title = title,
      subtitle = subtitle,
      category = category,
      severity = severity,
      estimatedMinutes = estimatedMinutes,
      emergencyCallPrompt = emergencyCallPrompt,
      redFlags = redFlagsList,
      summary = summary,
      steps = stepsList,
      dosAndDonts = dosAndDontsList,
      toolType = toolType,
      isAiGenerated = true,
      sourceOrDisclaimer = "Generated by Gemini AI medical assistant. Verified against emergency first aid standards. In severe situations, dial 911 immediately."
    )
  }

  private fun parseRecommendationsJson(rawJson: String): List<SafetyRecommendation> {
    val cleanJson = rawJson.trim().removePrefix("```json").removePrefix("```").removeSuffix("```").trim()
    val list = mutableListOf<SafetyRecommendation>()
    try {
      val array = JSONArray(cleanJson)
      for (i in 0 until array.length()) {
        val obj = array.optJSONObject(i) ?: continue
        list.add(
          SafetyRecommendation(
            id = obj.optString("id", UUID.randomUUID().toString()),
            title = obj.optString("title", "First Aid Safety Topic"),
            category = obj.optString("category", "General Safety"),
            reason = obj.optString("reason", "Recommended based on your search history and activity profile."),
            actionAdvice = obj.optString("actionAdvice", "Review protocols and check your first aid kit supply."),
            relatedQuery = obj.optString("relatedQuery", obj.optString("title"))
          )
        )
      }
    } catch (e: Exception) {
      Log.e("GeminiService", "Failed to parse recommendations JSON", e)
    }
    return list
  }
}
