package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FirstAidTopic

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AISearchResultScreen(
  query: String,
  topic: FirstAidTopic?,
  isGenerating: Boolean,
  errorMessage: String?,
  stepChecklist: Map<Int, Boolean>,
  isTimerRunning: Boolean,
  timerSecondsRemaining: Int,
  cprMetronomeActive: Boolean,
  cprCompressionCount: Int,
  followUpAnswer: String?,
  isFollowUpLoading: Boolean,
  onBackClick: () -> Unit,
  onRetry: () -> Unit,
  onToggleStepCompletion: (Int) -> Unit,
  onStartTimer: (Int) -> Unit,
  onPauseTimer: () -> Unit,
  onResetTimer: (Int) -> Unit,
  onToggleCprMetronome: () -> Unit,
  onResetCprCount: () -> Unit,
  onAskFollowUp: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = if (isGenerating) "Generating AI Guide..." else (topic?.title ?: "AI Search"),
            maxLines = 1,
            fontWeight = FontWeight.Bold,
            fontSize = 17.sp
          )
        },
        navigationIcon = {
          IconButton(
            onClick = onBackClick,
            modifier = Modifier.testTag("ai_result_back_button")
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Back"
            )
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = MaterialTheme.colorScheme.surface
        )
      )
    },
    modifier = modifier.fillMaxSize()
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) {
      if (isGenerating) {
        val infiniteTransition = rememberInfiniteTransition(label = "pulsing")
        val pulseScale by infiniteTransition.animateFloat(
          initialValue = 0.95f,
          targetValue = 1.12f,
          animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
          ),
          label = "scale"
        )

        Column(
          modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .testTag("ai_loading_view"),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Box(
            modifier = Modifier
              .size(80.dp)
              .scale(pulseScale)
              .clip(CircleShape)
              .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.AutoAwesome,
              contentDescription = "Gemini AI",
              tint = MaterialTheme.colorScheme.primary,
              modifier = Modifier.size(40.dp)
            )
          }

          Spacer(modifier = Modifier.height(20.dp))

          Text(
            text = "Consulting Gemini AI Medical Knowledge...",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
          )

          Spacer(modifier = Modifier.height(6.dp))

          Text(
            text = "Synthesizing step-by-step emergency protocol for \"$query\"",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )

          Spacer(modifier = Modifier.height(20.dp))

          CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = 3.dp
          )
        }
      } else if (errorMessage != null && topic == null) {
        Column(
          modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .testTag("ai_error_view"),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Icon(
            imageVector = Icons.Default.ErrorOutline,
            contentDescription = "Error",
            modifier = Modifier.size(52.dp),
            tint = MaterialTheme.colorScheme.error
          )

          Spacer(modifier = Modifier.height(14.dp))

          Text(
            text = "Could Not Generate Guide",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
          )

          Spacer(modifier = Modifier.height(6.dp))

          Text(
            text = errorMessage,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp)
          )

          Spacer(modifier = Modifier.height(18.dp))

          Button(
            onClick = onRetry,
            shape = CircleShape,
            modifier = Modifier.testTag("retry_ai_generation_button")
          ) {
            Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Retry with Gemini AI", fontSize = 13.sp)
          }
        }
      } else if (topic != null) {
        TopicDetailScreen(
          topic = topic,
          stepChecklist = stepChecklist,
          isTimerRunning = isTimerRunning,
          timerSecondsRemaining = timerSecondsRemaining,
          cprMetronomeActive = cprMetronomeActive,
          cprCompressionCount = cprCompressionCount,
          followUpAnswer = followUpAnswer,
          isFollowUpLoading = isFollowUpLoading,
          onBackClick = onBackClick,
          onToggleStepCompletion = onToggleStepCompletion,
          onStartTimer = onStartTimer,
          onPauseTimer = onPauseTimer,
          onResetTimer = onResetTimer,
          onToggleCprMetronome = onToggleCprMetronome,
          onResetCprCount = onResetCprCount,
          onAskFollowUp = onAskFollowUp
        )
      }
    }
  }
}

