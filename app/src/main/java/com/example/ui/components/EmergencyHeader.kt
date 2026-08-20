package com.example.ui.components

import android.content.Intent
import android.net.Uri
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
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
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
import com.example.ui.theme.CriticalRed
import com.example.ui.theme.CriticalRedBg
import com.example.ui.theme.CriticalRedText

@Composable
fun EmergencyQuickDialBanner(
  modifier: Modifier = Modifier,
  onCustomCall: ((String) -> Unit)? = null
) {
  val context = LocalContext.current

  Card(
    modifier = modifier
      .fillMaxWidth()
      .testTag("emergency_banner_card"),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(
      containerColor = CriticalRedBg,
      contentColor = CriticalRedText
    ),
    border = BorderStroke(1.dp, CriticalRed.copy(alpha = 0.2f))
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp)
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(34.dp)
            .clip(CircleShape)
            .background(CriticalRed),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = "Emergency Warning",
            tint = Color.White,
            modifier = Modifier.size(18.dp)
          )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
          Text(
            text = "Emergency Quick Response",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = CriticalRedText
          )
          Text(
            text = "If someone is unresponsive or bleeding heavily, call EMS immediately.",
            style = MaterialTheme.typography.bodySmall,
            color = CriticalRedText.copy(alpha = 0.85f),
            fontSize = 11.sp
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Button(
          onClick = {
            if (onCustomCall != null) {
              onCustomCall("911")
            } else {
              val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:911"))
              context.startActivity(intent)
            }
          },
          modifier = Modifier
            .weight(1f)
            .height(42.dp)
            .testTag("call_911_button"),
          colors = ButtonDefaults.buttonColors(
            containerColor = CriticalRed,
            contentColor = Color.White
          ),
          shape = CircleShape
        ) {
          Icon(
            imageVector = Icons.Default.Call,
            contentDescription = "Call 911",
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "Dial 911 / 112",
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp
          )
        }

        OutlinedButton(
          onClick = {
            if (onCustomCall != null) {
              onCustomCall("18002221222")
            } else {
              val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:18002221222"))
              context.startActivity(intent)
            }
          },
          modifier = Modifier
            .weight(1f)
            .height(42.dp)
            .testTag("call_poison_button"),
          shape = CircleShape,
          border = BorderStroke(1.dp, CriticalRed.copy(alpha = 0.5f)),
          colors = ButtonDefaults.outlinedButtonColors(
            contentColor = CriticalRedText
          )
        ) {
          Text(
            text = "Poison Control",
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp
          )
        }
      }
    }
  }
}

