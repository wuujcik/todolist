package com.wuujcik.todolist.persistence

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.util.*


@RunWith(RobolectricTestRunner::class)
class TodoDaoTests {

    private lateinit var database: RoomDatabase
    private lateinit var todoDao: TodoDao

    @Before
    fun initDbAndDao() {
        // using an in-memory database because the information stored here disappears when the
        // process is killed
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RoomDatabase::class.java
        ).allowMainThreadQueries().build()
        todoDao = database.todoDao()
    }

    @After
    fun closeDb() = database.close()

    @Test
    fun insertItemAndGetById() {
        // GIVEN - insert a task
        val time = Date().time
        val item = Todo("title", "description", time, "iconUrl")
        todoDao.insert(item)

        // WHEN - Get the task by id from the database
        val loaded = todoDao.getItemByTimestamp(item.timestamp)

        // THEN - The loaded data contains the expected values
        assertThat<Todo>(loaded as Todo, CoreMatchers.notNullValue())
        assertThat(loaded.timestamp, `is`(item.timestamp))
        assertThat(loaded.title, `is`(item.title))
        assertThat(loaded.description, `is`(item.description))
        assertThat(loaded.iconUrl, `is`(item.iconUrl))
    }

    @Test
    @Throws(Exception::class)
    fun insertAndDelete() {
        // GIVEN
        val time = Date().time
        val item = Todo("title", "description", time, "url")
        todoDao.insert(item)

        // WHEN
        todoDao.delete(time)

        // THEN
        val result = todoDao.getItemByTimestamp(time)
        assertThat(result, equalTo(null))
    }

    @Test
    @Throws(Exception::class)
    fun insertAndUpdate() {
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