package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import com.example.ui.theme.CriticalRed
import com.example.ui.theme.CriticalRedBg

@Composable
fun CPRMetronomeWidget(
  isActive: Boolean,
  compressionCount: Int,
  onToggleMetronome: () -> Unit,
  onResetCount: () -> Unit,
  modifier: Modifier = Modifier
) {
  val infiniteTransition = rememberInfiniteTransition(label = "heartbeat")
  val pulseScale by infiniteTransition.animateFloat(
    initialValue = 1.0f,
    targetValue = if (isActive) 1.25f else 1.0f,
    animationSpec = infiniteRepeatable(
      animation = tween(durationMillis = 270, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "scale"
  )

  // 30 compressions per cycle prompt
  val cycleNumber = (compressionCount / 30) + 1
  val countInCycle = compressionCount % 30

  Card(
    modifier = modifier
      .fillMaxWidth()
      .testTag("cpr_metronome_card"),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(
      containerColor = CriticalRedBg,
      contentColor = CriticalRed
    ),
    border = BorderStroke(1.dp, CriticalRed.copy(alpha = 0.3f))
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "CPR Rhythm Metronome (110 BPM)",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = CriticalRed
          )
          Text(
            text = "Match beat: 100-120 compressions/min (2 inches deep)",
            style = MaterialTheme.typography.bodySmall,
            color = CriticalRed.copy(alpha = 0.8f)
          )
        }

        IconButton(
          onClick = onResetCount,
          modifier = Modifier.testTag("reset_cpr_counter_button")
        ) {
          Icon(
            imageVector = Icons.Default.Refresh,
            contentDescription = "Reset Compression Counter",
            tint = CriticalRed
          )
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
      ) {
        Box(
          modifier = Modifier
            .size(68.dp)
            .scale(if (isActive) pulseScale else 1.0f)
            .clip(CircleShape)
            .background(CriticalRed),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Favorite,
            contentDescription = "CPR Pulse Heartbeat",
            tint = Color.White,
            modifier = Modifier.size(34.dp)
          )
        }

        Spacer(modifier = Modifier.width(20.dp))

        Column(
          horizontalAlignment = Alignment.Start
        ) {
          Text(
            text = "Cycle $cycleNumber: $countInCycle / 30",
            fontSize = 22.sp,
            fontWeight = FontWeight.Black,
            color = CriticalRed
          )
          Text(
            text = if (countInCycle == 0 && compressionCount > 0) "Give 2 Rescue Breaths NOW!" else "Keep pushing hard and fast",
            fontSize = 12.sp,
            fontWeight = if (countInCycle == 0 && compressionCount > 0) FontWeight.Bold else FontWeight.Normal,
            color = if (countInCycle == 0 && compressionCount > 0) CriticalRed else Color.Black.copy(alpha = 0.7f)
          )
          Text(
            text = "Total: $compressionCount compressions",
            fontSize = 11.sp,
            color = Color.Black.copy(alpha = 0.6f)
          )
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      Button(
        onClick = onToggleMetronome,
        modifier = Modifier
          .fillMaxWidth()
          .height(44.dp)
          .testTag("toggle_cpr_metronome_button"),
        colors = ButtonDefaults.buttonColors(
          containerColor = CriticalRed,
          contentColor = Color.White
        ),
        shape = CircleShape
      ) {
        Icon(
          imageVector = if (isActive) Icons.Default.Stop else Icons.Default.PlayArrow,
          contentDescription = if (isActive) "Stop Metronome" else "Start Metronome",
          modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = if (isActive) "Stop CPR Metronome" else "Start CPR Rhythm Beat (110 BPM)",
          fontWeight = FontWeight.Bold,
          fontSize = 13.sp
        )
      }
    }
  }
}

