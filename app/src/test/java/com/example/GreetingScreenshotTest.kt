package com.example

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import com.example.data.datasource.LocalFirstAidData
import com.example.ui.components.EmergencyQuickDialBanner
import com.example.ui.components.FirstAidTopicCard
import com.example.ui.theme.MyApplicationTheme
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = RobolectricDeviceQualifiers.Pixel8, sdk = [34])
class GreetingScreenshotTest {

  @get:Rule val composeTestRule = createComposeRule()

  @Test
  fun emergency_banner_screenshot() {
    composeTestRule.setContent {
      MyApplicationTheme {
        EmergencyQuickDialBanner()
      }
    }

    composeTestRule.onRoot().captureRoboImage(filePath = "src/test/screenshots/emergency_banner.png")
  }

  @Test
  fun topic_card_screenshot() {
    val sampleTopic = LocalFirstAidData.predefinedTopics.first()
    composeTestRule.setContent {
      MyApplicationTheme {
        FirstAidTopicCard(topic = sampleTopic, onClick = {})
      }
    }

    composeTestRule.onRoot().captureRoboImage(filePath = "src/test/screenshots/topic_card.png")
  }
}
