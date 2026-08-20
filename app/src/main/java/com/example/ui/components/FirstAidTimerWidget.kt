package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.EmergencyToolType
import com.example.ui.theme.InfoBlue
import com.example.ui.theme.InfoBlueBg
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.WarningAmber
import com.example.ui.theme.WarningAmberBg
import java.util.Locale

@Composable
fun FirstAidTimerWidget(
  toolType: EmergencyToolType,
  secondsRemaining: Int,
  totalSeconds: Int,
  isRunning: Boolean,
  onStart: () -> Unit,
  onPause: () -> Unit,
  onReset: () -> Unit,
  modifier: Modifier = Modifier
) {
  val title = when (toolType) {
    EmergencyToolType.BURN_COOLING_TIMER -> "Burn Cool-Water Timer (15 Min)"
    EmergencyToolType.PRESSURE_TIMER -> "Continuous Direct Pressure Timer (10 Min)"
    EmergencyToolType.EYE_FLUSH_TIMER -> "Chemical Eye Flush Timer (15 Min)"
    else -> "Emergency Protocol Timer"
  }

  val description = when (toolType) {
    EmergencyToolType.BURN_COOLING_TIMER -> "Hold under cool gentle tap water. Do not remove until timer finishes."
    EmergencyToolType.PRESSURE_TIMER -> "Maintain firm, unbroken pressure with gauze. Do not lift to check wound."
    EmergencyToolType.EYE_FLUSH_TIMER -> "Keep eyelids held open and continuously flush with lukewarm running water."
    else -> "Follow protocol until full duration completes."
  }

  val minutes = secondsRemaining / 60
  val seconds = secondsRemaining % 60
  val formattedTime = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)

  val progress = if (totalSeconds > 0) {
    (totalSeconds - secondsRemaining).toFloat() / totalSeconds.toFloat()
  } else 0f

  val isFinished = secondsRemaining == 0 && totalSeconds > 0

  Card(
    modifier = modifier
      .fillMaxWidth()
      .testTag("emergency_timer_card"),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(
      containerColor = if (isFinished) MaterialTheme.colorScheme.primaryContainer else InfoBlueBg,
      contentColor = if (isFinished) MaterialTheme.colorScheme.onPrimaryContainer else InfoBlue
    ),
    border = BorderStroke(1.dp, if (isFinished) MaterialTheme.colorScheme.primary.copy(alpha = 0.3f) else InfoBlue.copy(alpha = 0.3f))
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
          Icon(
            imageVector = Icons.Default.Timer,
            contentDescription = "Timer",
            modifier = Modifier.size(18.dp),
            tint = if (isFinished) SuccessGreen else InfoBlue
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = if (isFinished) SuccessGreen else InfoBlue
          )
        }

        IconButton(
          onClick = onReset,
          modifier = Modifier.testTag("reset_timer_button")
        ) {
          Icon(
            imageVector = Icons.Default.Refresh,
            contentDescription = "Reset Timer",
            tint = if (isFinished) SuccessGreen else InfoBlue
          )
        }
      }

      Text(
        text = description,
        style = MaterialTheme.typography.bodySmall,
        color = Color.Black.copy(alpha = 0.75f),
        fontSize = 12.sp,
        modifier = Modifier.padding(vertical = 4.dp)
      )

      Spacer(modifier = Modifier.height(6.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = formattedTime,
          fontSize = 30.sp,
          fontWeight = FontWeight.Black,
          color = if (isFinished) SuccessGreen else InfoBlue
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          if (!isRunning) {
            Button(
              onClick = onStart,
              modifier = Modifier
                .height(40.dp)
                .testTag("start_timer_button"),
              colors = ButtonDefaults.buttonColors(
                containerColor = if (isFinished) SuccessGreen else InfoBlue,
                contentColor = Color.White
              ),
              shape = CircleShape
            ) {
              Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Start", modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text(if (isFinished) "Restart" else "Start Timer", fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
          } else {
            OutlinedButton(
              onClick = onPause,
              modifier = Modifier
                .height(40.dp)
                .testTag("pause_timer_button"),
              shape = CircleShape,
              border = BorderStroke(1.dp, InfoBlue)
            ) {
              Icon(imageVector = Icons.Default.Pause, contentDescription = "Pause", modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Pause", fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      LinearProgressIndicator(
        progress = { progress.coerceIn(0f, 1f) },
        modifier = Modifier
          .fillMaxWidth()
          .height(6.dp)
          .clip(RoundedCornerShape(3.dp)),
        color = if (isFinished) SuccessGreen else InfoBlue,
        trackColor = Color.White.copy(alpha = 0.6f)
      )
    }
  }
}

