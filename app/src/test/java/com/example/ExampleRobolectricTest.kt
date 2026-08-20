package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.datasource.LocalFirstAidData
import com.example.data.db.AppDatabase
import com.example.data.db.HistoryDao
import com.example.data.db.HistoryEntity
import com.example.data.model.TopicCategory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  private lateinit var db: AppDatabase
  private lateinit var dao: HistoryDao

  @Before
  fun setup() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
      .allowMainThreadQueries()
      .build()
    dao = db.historyDao()
  }

  @After
  fun tearDown() {
    db.close()
  }

  @Test
  fun `read app_name string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("First Aid AI", appName)
  }

  @Test
  fun `verify predefined topics contain critical life saving protocols`() {
    val topics = LocalFirstAidData.predefinedTopics
    assertTrue("Should have predefined emergency topics", topics.isNotEmpty())

    val cprTopic = topics.find { it.id == "cpr_adult" }
    assertNotNull("CPR topic must be present", cprTopic)
    assertTrue("CPR should have action steps", cprTopic!!.steps.isNotEmpty())
    assertTrue("CPR should have red flags", cprTopic.redFlags.isNotEmpty())

    val chokingTopic = topics.find { it.id == "choking_adult" }
    assertNotNull("Choking topic must be present", chokingTopic)

    val bleedingTopic = topics.find { it.id == "severe_bleeding" }
    assertNotNull("Severe Bleeding topic must be present", bleedingTopic)
  }

  @Test
  fun `test room database history flow and favorite toggle`() = runBlocking {
    val entity = HistoryEntity(
      topicId = "cpr_adult",
      title = "Adult CPR & AED",
      category = "Resuscitation",
      severity = "CRITICAL",
      summary = "Chest compressions and AED guidance.",
      timestamp = System.currentTimeMillis()
    )

    val id = dao.insert(entity)
    assertTrue("Inserted ID should be valid", id > 0)

    val allHistory = dao.getAllHistory().first()
    assertEquals(1, allHistory.size)
    assertEquals("Adult CPR & AED", allHistory[0].title)
    assertFalse(allHistory[0].isFavorite)

    // Toggle favorite
    dao.toggleFavorite(id)
    val favHistory = dao.getFavoriteHistory().first()
    assertEquals(1, favHistory.size)
    assertTrue(favHistory[0].isFavorite)

    // Update note
    dao.updateNotes(id, "Review with CPR trainer")
    val updated = dao.getAllHistory().first()[0]
    assertEquals("Review with CPR trainer", updated.notes)

    // Delete
    dao.deleteById(id)
    val emptyHistory = dao.getAllHistory().first()
    assertTrue(emptyHistory.isEmpty())
  }
}
