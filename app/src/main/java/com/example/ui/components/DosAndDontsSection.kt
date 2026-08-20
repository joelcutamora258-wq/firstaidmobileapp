package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DoAndDontItem
import com.example.ui.theme.CriticalRed
import com.example.ui.theme.CriticalRedBg
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.SuccessGreenBg

@Composable
fun DosAndDontsSection(
  items: List<DoAndDontItem>,
  modifier: Modifier = Modifier
) {
  if (items.isEmpty()) return

  val doItems = items.filter { it.isDo }
  val dontItems = items.filter { !it.isDo }

  Column(modifier = modifier.fillMaxWidth()) {
    Text(
      text = "Crucial DOs & DON'Ts",
      style = MaterialTheme.typography.titleMedium,
      fontWeight = FontWeight.Bold
    )

    Spacer(modifier = Modifier.height(10.dp))

    if (doItems.isNotEmpty()) {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
          containerColor = SuccessGreenBg,
          contentColor = SuccessGreen
        ),
        border = BorderStroke(1.dp, SuccessGreen.copy(alpha = 0.3f))
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.CheckCircle,
              contentDescription = "DOs",
              tint = SuccessGreen,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "WHAT TO DO",
              fontWeight = FontWeight.Bold,
              fontSize = 12.sp,
              color = SuccessGreen
            )
          }

          Spacer(modifier = Modifier.height(8.dp))

          doItems.forEach { item ->
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 3.dp),
              verticalAlignment = Alignment.Top
            ) {
              Text(
                text = "✓",
                color = SuccessGreen,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = item.text,
                fontSize = 12.sp,
                color = Color.Black.copy(alpha = 0.85f),
                lineHeight = 17.sp
              )
            }
          }
        }
      }
    }

    if (dontItems.isNotEmpty()) {
      Spacer(modifier = Modifier.height(10.dp))

      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
          containerColor = CriticalRedBg,
          contentColor = CriticalRed
        ),
        border = BorderStroke(1.dp, CriticalRed.copy(alpha = 0.25f))
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Cancel,
              contentDescription = "DON'Ts",
              tint = CriticalRed,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "WHAT NOT TO DO (AVOID)",
              fontWeight = FontWeight.Bold,
              fontSize = 12.sp,
              color = CriticalRed
            )
          }

          Spacer(modifier = Modifier.height(8.dp))

          dontItems.forEach { item ->
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 3.dp),
              verticalAlignment = Alignment.Top
            ) {
              Text(
                text = "✕",
                color = CriticalRed,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = item.text,
                fontSize = 12.sp,
                color = Color.Black.copy(alpha = 0.85f),
                lineHeight = 17.sp
              )
            }
          }
        }
      }
    }
  }
}

