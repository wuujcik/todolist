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
class TodoDaoTests {

    private lateinit var database: RoomDatabase
    private lateinit var todoDao: TodoDao

    @Before
    fun initDbAndDao() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RoomDatabase::class.java
        ).allowMainThreadQueries().build()
        todoDao = database.todoDao()
    }

    @After
    fun closeDb() = database.close()

    @Test
    fun insertItemAndGetById() = runBlocking {
        // GIVEN
        val time = Date().time
        val item = Todo("title", "description", time, "iconUrl")
        todoDao.insert(item)

        // WHEN
        val loaded = todoDao.getItemByTimestamp(item.timestamp)

        // THEN
        assertThat<Todo>(loaded as Todo, CoreMatchers.notNullValue())
        assertThat(loaded.timestamp, `is`(item.timestamp))
        assertThat(loaded.title, `is`(item.title))
        assertThat(loaded.description, `is`(item.description))
        assertThat(loaded.iconUrl, `is`(item.iconUrl))
    }

    @Test
    @Throws(Exception::class)
    fun insertAndDelete() = runBlocking {
        // GIVEN
        val time = Date().time
        val item = Todo("title", "description", time, "url")
        todoDao.insert(item)

        // WHEN
        todoDao.delete(time)

        // THEN
        val result = todoDao.getItemByTimestamp(time)
        assertThat(result, CoreMatchers.`is`(CoreMatchers.nullValue()))
    }

    @Test
    @Throws(Exception::class)
    fun insertAndUpdate() = runBlocking {
        // GIVEN
        val time = Date().time
        val item = Todo("title", "description", time, "url")
        todoDao.insert(item)

        // WHEN
        val updatedItem = Todo("updatedTitle", "updatedDescription", time, "ipdatedUrl")
        todoDao.update(updatedItem)

        // THEN
        val result = todoDao.getItemByTimestamp(time)
        assertThat(result, equalTo(updatedItem))
    }
}