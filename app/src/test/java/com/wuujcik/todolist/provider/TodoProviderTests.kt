package com.wuujcik.todolist.provider

import com.wuujcik.todolist.model.TodoProvider
import com.wuujcik.todolist.persistence.Todo
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.robolectric.RobolectricTestRunner
import java.util.*

@RunWith(RobolectricTestRunner::class)
class TodoProviderTests {

    private lateinit var provider: TodoProvider


    @Before
    fun initProvider() {
        provider = mock(TodoProvider::class.java)
    }

    @Test
    @Throws(Exception::class)
    fun writeTodoAndReadFromLocalDB() {
        // GIVEN
        val time = Date().time
        val item = Todo("title", "description", time, "url")

        // WHEN
        provider.addItem(item)

        // THEN
        provider.getItemByTimestamp(time) {
            assertThat(it, equalTo(item))
        }
    }

    @Test
    @Throws(Exception::class)
    fun deleteFromLocalDB() {
        // GIVEN
        val time = Date().time
        val item = Todo("title", "description", time, "url")
        provider.addItem(item)

        // WHEN
        provider.deleteItem(item)

        // THEN
        provider.getItemByTimestamp(time) {
            assertThat(it, null)
        }
    }

    @Test
    @Throws(Exception::class)
    fun insertIncorrectItemToLocalDB() {
        // GIVEN
        val time = Date().time
        val itemEmptyTitle = Todo("", "description", time, "url")
        val itemNullTitle = Todo(null, "description", time, "url")
        val itemEmptyDescription = Todo("title", "", time, "url")
        val itemNullDescription = Todo("title", null, time, "url")

        // WHEN
        provider.addItem(itemEmptyTitle)
        provider.addItem(itemNullTitle)
        provider.addItem(itemEmptyDescription)
        provider.addItem(itemNullDescription)

        // THEN
        provider.getItemByTimestamp(time) {
            assertThat(it, equalTo(null))
        }
    }

    @Test
    @Throws(Exception::class)
    fun insertAndUpdateInLocalDB() {
        // GIVEN
        val time = Date().time
        val item = Todo("title", "description", time, "url")
        provider.addItem(item)

        // WHEN
        val updatedItem = Todo("updatedTitle", "updatedDescription", time, "ipdatedUrl")
        provider.updateItem(updatedItem)

        // THEN
        provider.getItemByTimestamp(time) {
            assertThat(it, equalTo(updatedItem))
        }
    }
}
