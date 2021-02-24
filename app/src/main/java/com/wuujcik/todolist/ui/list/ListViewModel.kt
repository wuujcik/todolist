package com.wuujcik.todolist.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import androidx.paging.toLiveData
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.wuujcik.todolist.persistence.*
import com.wuujcik.todolist.model.TodoProvider
import javax.inject.Inject


class ListViewModel @Inject constructor(private val todoProvider: TodoProvider) : ViewModel() {


    val allTodos: LiveData<PagedList<Todo>> = todoProvider.getAll.toLiveData(30)


    fun deleteTodo(item: Todo) {
        todoProvider.deleteItem(item)
    }


    fun invalidateTodos() {
        allTodos.value?.dataSource?.invalidate()
    }


    fun attachDatabaseReadListeners() {
        todoProvider.attachDatabaseReadListeners()
    }


    fun detachDatabaseReadListener() {
        todoProvider.detachDatabaseReadListener()
    }


    val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.Callback() {
        // enable the items to swipe to the left or right
        override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ): Int =
            makeMovementFlags(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)

        override fun onMove(
            recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean = false

        // When an item is swiped, remove the item via the view model. The list item will be
        // automatically removed in response, because the adapter is observing the live list.
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            (viewHolder as TodoListAdapter.TodoViewHolder).todo?.let {
                invalidateTodos()
                deleteTodo(it)
            }
        }
    })

}
