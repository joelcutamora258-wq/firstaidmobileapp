package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FirstAidTopic
import com.example.data.model.SeverityLevel
import com.example.data.model.TopicCategory
import com.example.ui.theme.CriticalRed
import com.example.ui.theme.CriticalRedBg
import com.example.ui.theme.CriticalRedText
import com.example.ui.theme.HdSurfaceCard
import com.example.ui.theme.InfoBlue
import com.example.ui.theme.InfoBlueBg
import com.example.ui.theme.InfoBlueText
import com.example.ui.theme.NeutralBadgeBg
import com.example.ui.theme.NeutralBadgeText
import com.example.ui.theme.ResuscitationBlue
import com.example.ui.theme.ResuscitationBlueBg
import com.example.ui.theme.ResuscitationBlueText
import com.example.ui.theme.WarningAmber
import com.example.ui.theme.WarningAmberBg
import com.example.ui.theme.WarningAmberText

@Composable
fun SeverityBadge(
  severity: SeverityLevel,
  modifier: Modifier = Modifier
) {
  val (bgColor, textColor, label) = when (severity) {
    SeverityLevel.CRITICAL -> Triple(CriticalRedBg, CriticalRedText, "CRITICAL")
    SeverityLevel.URGENT -> Triple(WarningAmberBg, WarningAmberText, "URGENT")
    SeverityLevel.STANDARD -> Triple(InfoBlueBg, InfoBlueText, "FIRST AID")
  }

  Box(
    modifier = modifier
      .clip(RoundedCornerShape(8.dp))
      .background(bgColor)
      .padding(horizontal = 8.dp, vertical = 3.dp),
    contentAlignment = Alignment.Center
  ) {
    Text(
      text = label,
      color = textColor,
      fontSize = 10.sp,
      fontWeight = FontWeight.Bold,
      letterSpacing = 0.5.sp
    )
  }
}

@Composable
fun FirstAidTopicCard(
  topic: FirstAidTopic,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val iconBgColor = when (topic.category) {
    TopicCategory.RESUSCITATION -> ResuscitationBlueBg
    TopicCategory.TRAUMA -> CriticalRedBg
    TopicCategory.ALLERGIC -> WarningAmberBg
    else -> NeutralBadgeBg
  }
  val iconTint = when (topic.category) {
    TopicCategory.RESUSCITATION -> ResuscitationBlueText
    TopicCategory.TRAUMA -> CriticalRedText
    TopicCategory.ALLERGIC -> WarningAmberText
    else -> NeutralBadgeText
  }

  Card(
    modifier = modifier
      .fillMaxWidth()
      .clickable(onClick = onClick)
      .testTag("topic_card_${topic.id}"),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
      contentColor = MaterialTheme.colorScheme.onSurface
    ),
    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.8f))
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Category leading icon block (High Density Style)
      Box(
        modifier = Modifier
          .size(42.dp)
          .clip(RoundedCornerShape(12.dp))
          .background(iconBgColor),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.MedicalServices,
          contentDescription = null,
          tint = iconTint,
          modifier = Modifier.size(20.dp)
        )
      }

      Spacer(modifier = Modifier.width(12.dp))

      Column(modifier = Modifier.weight(1f)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = topic.title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f, fill = false)
          )

          Spacer(modifier = Modifier.width(6.dp))

          SeverityBadge(severity = topic.severity)
        }

        Spacer(modifier = Modifier.height(2.dp))

        Text(
          text = topic.subtitle,
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
          fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(4.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = "${topic.steps.size} steps",
              fontSize = 11.sp,
              color = MaterialTheme.colorScheme.primary,
              fontWeight = FontWeight.Medium
            )
            Text(
              text = " • ~${topic.estimatedMinutes} min",
              fontSize = 11.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }

          if (topic.isAiGenerated) {
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  imageVector = Icons.Default.AutoAwesome,
                  contentDescription = "AI Guide",
                  modifier = Modifier.size(10.dp),
                  tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                  text = "AI Guide",
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onPrimaryContainer
                )
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.width(8.dp))

      Icon(
        imageVector = Icons.Default.ChevronRight,
        contentDescription = "Open guide",
        tint = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.size(18.dp)
      )
    }
  }
}

@Composable
fun CategoryChipRow(
  selectedCategory: TopicCategory,
  onCategorySelected: (TopicCategory) -> Unit,
  modifier: Modifier = Modifier
) {
  LazyRow(
    modifier = modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    items(TopicCategory.values()) { category ->
      val isSelected = category == selectedCategory
      FilterChip(
        selected = isSelected,
        onClick = { onCategorySelected(category) },
        shape = CircleShape,
        label = {
          Text(
            text = category.label,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            fontSize = 12.sp
          )
        },
        border = FilterChipDefaults.filterChipBorder(
          enabled = true,
          selected = isSelected,
          borderColor = MaterialTheme.colorScheme.outlineVariant,
          selectedBorderColor = Color.Transparent
        ),
        modifier = Modifier.testTag("category_chip_${category.name}"),
        colors = FilterChipDefaults.filterChipColors(
          selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
          selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
          containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
          labelColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
      )
    }
  }
}

