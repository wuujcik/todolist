package com.wuujcik.todolist.persistence

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.*
import kotlin.jvm.Throws


@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28])
class MealDaoTests {

    private lateinit var database: RoomDatabase
    private lateinit var mealDao: MealDao

    @Before
    fun initDbAndDao() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RoomDatabase::class.java
        ).allowMainThreadQueries().build()
        mealDao = database.mealDao()
    }

    @After
    fun closeDb() = database.close()

    @Test
    fun insertItemAndGetById() = runBlocking {
        // GIVEN
        val time = Date().time
        val item = Meal("title", "description", time, 0)
        mealDao.insert(item)

        // WHEN
        val loaded = mealDao.getItemByTimestamp(item.timestamp)

        // THEN
        assertThat<Meal>(loaded as Meal, CoreMatchers.notNullValue())
        assertThat(loaded.timestamp, `is`(item.timestamp))
        assertThat(loaded.title, `is`(item.title))
        assertThat(loaded.description, `is`(item.description))
        assertThat(loaded.mealType, `is`(item.mealType))
    }

    @Test
    @Throws(Exception::class)
    fun insertAndDelete() = runBlocking {
        // GIVEN
        val time = Date().time
        val item = Meal("title", "description", time, 0)
        mealDao.insert(item)

        // WHEN
        mealDao.delete(time)

        // THEN
        val result = mealDao.getItemByTimestamp(time)
        assertThat(result, CoreMatchers.`is`(CoreMatchers.nullValue()))
    }

    @Test
    @Throws(Exception::class)
    fun insertAndUpdate() = runBlocking {
        // GIVEN
        val time = Date().time
        val item = Meal("title", "description", time, 0)
        mealDao.insert(item)

        // WHEN
        val updatedItem = Meal("updatedTitle", "updatedDescription", time, 1)
        mealDao.update(updatedItem)

        // THEN
        val result = mealDao.getItemByTimestamp(time)
        assertThat(result, equalTo(updatedItem))
    }
}