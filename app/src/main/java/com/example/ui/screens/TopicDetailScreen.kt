package com.example.ui.screens

import android.content.Intent
import android.net.Uri
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.EmergencyToolType
import com.example.data.model.FirstAidStep
import com.example.data.model.FirstAidTopic
import com.example.ui.components.AiAssistantBox
import com.example.ui.components.CPRMetronomeWidget
import com.example.ui.components.DosAndDontsSection
import com.example.ui.components.FirstAidTimerWidget
import com.example.ui.components.SeverityBadge
import com.example.ui.theme.CriticalRed
import com.example.ui.theme.CriticalRedBg
import com.example.ui.theme.WarningAmber
import com.example.ui.theme.WarningAmberBg

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopicDetailScreen(
  topic: FirstAidTopic,
  stepChecklist: Map<Int, Boolean>,
  isTimerRunning: Boolean,
  timerSecondsRemaining: Int,
  cprMetronomeActive: Boolean,
  cprCompressionCount: Int,
  followUpAnswer: String?,
  isFollowUpLoading: Boolean,
  onBackClick: () -> Unit,
  onToggleStepCompletion: (Int) -> Unit,
  onStartTimer: (Int) -> Unit,
  onPauseTimer: () -> Unit,
  onResetTimer: (Int) -> Unit,
  onToggleCprMetronome: () -> Unit,
  onResetCprCount: () -> Unit,
  onAskFollowUp: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current

  val totalSteps = topic.steps.size
  val completedSteps = stepChecklist.count { it.value }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = topic.title,
            maxLines = 1,
            fontWeight = FontWeight.Bold,
            fontSize = 17.sp
          )
        },
        navigationIcon = {
          IconButton(
            onClick = onBackClick,
            modifier = Modifier.testTag("topic_detail_back_button")
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Back"
            )
          }
        },
        actions = {
          IconButton(
            onClick = {
              val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, "First Aid: ${topic.title}")
                putExtra(
                  Intent.EXTRA_TEXT,
                  "First Aid Guide: ${topic.title}\n\n${topic.summary}\n\nSteps:\n" +
                    topic.steps.joinToString("\n") { "${it.stepNumber}. ${it.title}: ${it.description}" }
                )
              }
              context.startActivity(Intent.createChooser(shareIntent, "Share First Aid Guide"))
            },
            modifier = Modifier.testTag("share_topic_button")
          ) {
            Icon(
              imageVector = Icons.Default.Share,
              contentDescription = "Share Guide"
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
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .testTag("topic_detail_list"),
      contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      // 1. Topic Header & Severity
      item {
        Column(modifier = Modifier.fillMaxWidth()) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            SeverityBadge(severity = topic.severity)
            Text(
              text = "${topic.category.label} • ~${topic.estimatedMinutes} min",
              fontSize = 12.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              fontWeight = FontWeight.Medium
            )
          }

          Spacer(modifier = Modifier.height(8.dp))

          Text(
            text = topic.title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
          )

          Spacer(modifier = Modifier.height(4.dp))

          Text(
            text = topic.subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )

          Spacer(modifier = Modifier.height(10.dp))

          Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.7f))
          ) {
            Text(
              text = topic.summary,
              fontSize = 13.sp,
              lineHeight = 18.sp,
              modifier = Modifier.padding(14.dp)
            )
          }
        }
      }

      // 2. Emergency 911 Callout Banner
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(
            containerColor = CriticalRedBg,
            contentColor = CriticalRed
          ),
          border = BorderStroke(1.dp, CriticalRed.copy(alpha = 0.25f))
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.Warning,
              contentDescription = "Emergency Alert",
              tint = CriticalRed,
              modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "When to Call Emergency Services:",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = CriticalRed
              )
              Text(
                text = topic.emergencyCallPrompt,
                fontSize = 12.sp,
                color = Color.Black.copy(alpha = 0.85f),
                lineHeight = 16.sp
              )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
              onClick = {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:911"))
                context.startActivity(intent)
              },
              colors = ButtonDefaults.buttonColors(
                containerColor = CriticalRed,
                contentColor = Color.White
              ),
              shape = CircleShape,
              contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
            ) {
              Icon(
                imageVector = Icons.Default.Call,
                contentDescription = null,
                modifier = Modifier.size(14.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text("911", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
          }
        }
      }

      // 3. Red Flags Warning Box
      if (topic.redFlags.isNotEmpty()) {
        item {
          Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
              containerColor = WarningAmberBg,
              contentColor = WarningAmber
            ),
            border = BorderStroke(1.dp, WarningAmber.copy(alpha = 0.3f))
          ) {
            Column(modifier = Modifier.padding(14.dp)) {
              Text(
                text = "⚠️ Red Flag Symptoms (Seek Emergency Care):",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = WarningAmber
              )
              Spacer(modifier = Modifier.height(6.dp))
              topic.redFlags.forEach { flag ->
                Row(
                  modifier = Modifier.padding(vertical = 2.dp),
                  verticalAlignment = Alignment.Top
                ) {
                  Text("• ", fontWeight = FontWeight.Bold, color = WarningAmber)
                  Text(
                    text = flag,
                    fontSize = 12.sp,
                    color = Color.Black.copy(alpha = 0.85f),
                    lineHeight = 16.sp
                  )
                }
              }
            }
          }
        }
      }

      // 4. Interactive Emergency Tools (Metronome / Timers)
      if (topic.toolType == EmergencyToolType.CPR_METRONOME) {
        item {
          CPRMetronomeWidget(
            isActive = cprMetronomeActive,
            compressionCount = cprCompressionCount,
            onToggleMetronome = onToggleCprMetronome,
            onResetCount = onResetCprCount
          )
        }
      } else if (topic.toolType != EmergencyToolType.NONE) {
        val totalSec = topic.steps.firstOrNull { it.durationSeconds != null }?.durationSeconds ?: 600
        item {
          FirstAidTimerWidget(
            toolType = topic.toolType,
            secondsRemaining = if (timerSecondsRemaining > 0) timerSecondsRemaining else totalSec,
            totalSeconds = totalSec,
            isRunning = isTimerRunning,
            onStart = { onStartTimer(totalSec) },
            onPause = onPauseTimer,
            onReset = { onResetTimer(totalSec) }
          )
        }
      }

      // 5. Steps Section Header & Progress
      item {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Step-by-Step Action Guide",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = "$completedSteps of $totalSteps completed",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold
          )
        }
      }

      // 6. Steps List
      items(topic.steps, key = { it.stepNumber }) { step ->
        val isCompleted = stepChecklist[step.stepNumber] ?: false

        StepItemCard(
          step = step,
          isCompleted = isCompleted,
          onToggle = { onToggleStepCompletion(step.stepNumber) }
        )
      }

      // 7. Dos & Don'ts Section
      if (topic.dosAndDonts.isNotEmpty()) {
        item {
          DosAndDontsSection(items = topic.dosAndDonts)
        }
      }

      // 8. Gemini AI Follow-Up Box
      item {
        AiAssistantBox(
          topicTitle = topic.title,
          answer = followUpAnswer,
          isLoading = isFollowUpLoading,
          onAskQuestion = onAskFollowUp
        )
      }

      // 9. Disclaimer
      item {
        Text(
          text = topic.sourceOrDisclaimer,
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          fontSize = 11.sp,
          modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
        )
      }
    }
  }
}

@Composable
fun StepItemCard(
  step: FirstAidStep,
  isCompleted: Boolean,
  onToggle: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .clickable(onClick = onToggle)
      .testTag("step_card_${step.stepNumber}"),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(
      containerColor = if (isCompleted) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
      contentColor = MaterialTheme.colorScheme.onSurface
    ),
    border = BorderStroke(1.dp, if (isCompleted) Color.Transparent else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.7f))
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
      verticalAlignment = Alignment.Top
    ) {
      Box(
        modifier = Modifier
          .size(30.dp)
          .clip(CircleShape)
          .background(
            if (isCompleted) MaterialTheme.colorScheme.primary.copy(alpha = 0.6f) else MaterialTheme.colorScheme.primary
          ),
        contentAlignment = Alignment.Center
      ) {
        if (isCompleted) {
          Icon(
            imageVector = Icons.Default.Check,
            contentDescription = "Completed",
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.size(16.dp)
          )
        } else {
          Text(
            text = "${step.stepNumber}",
            color = MaterialTheme.colorScheme.onPrimary,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp
          )
        }
      }

      Spacer(modifier = Modifier.width(12.dp))

      Column(modifier = Modifier.weight(1f)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = step.title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = if (isCompleted) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f) else MaterialTheme.colorScheme.onSurface
          )

          Checkbox(
            checked = isCompleted,
            onCheckedChange = { onToggle() },
            modifier = Modifier
              .size(24.dp)
              .testTag("step_checkbox_${step.stepNumber}"),
            colors = CheckboxDefaults.colors(
              checkedColor = MaterialTheme.colorScheme.primary,
              checkmarkColor = MaterialTheme.colorScheme.onPrimary
            )
          )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
          text = step.description,
          style = MaterialTheme.typography.bodyMedium,
          fontSize = 13.sp,
          lineHeight = 18.sp,
          color = if (isCompleted) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f) else MaterialTheme.colorScheme.onSurface
        )

        if (step.cautionNote != null) {
          Spacer(modifier = Modifier.height(6.dp))
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(8.dp))
              .background(CriticalRedBg)
              .padding(8.dp)
          ) {
            Row(verticalAlignment = Alignment.Top) {
              Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "Caution",
                tint = CriticalRed,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "Caution: ${step.cautionNote}",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = CriticalRed,
                lineHeight = 15.sp
              )
            }
          }
        }
      }
    }
  }
}

