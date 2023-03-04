package com.wuujcik.todolist.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.wuujcik.todolist.R
import com.wuujcik.todolist.databinding.ItemTodoBinding
import com.wuujcik.todolist.persistence.Todo
import com.wuujcik.todolist.utils.formatShortDate


class TodoListAdapter : PagedListAdapter<Todo, TodoListAdapter.TodoViewHolder>(DIFF_CALLBACK) {

    /** Callback when user click on holder */
    var onItemClicked: (item: Todo) -> Unit = {}
    var onItemLongClicked: (item: Todo) -> Unit = {}
    var onErrorMessage: (message: String) -> Unit = {}

    /** Optional placeholder that is visible when there is no data */
    var placeholderView: View? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        return TodoViewHolder(  ItemTodoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }


    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val item = getItem(position) ?: return
        holder.bind(item)
    }

    override fun onCurrentListChanged(
        previousList: PagedList<Todo>?,
        currentList: PagedList<Todo>?
    ) {
        val count = currentList?.size ?: 0
        placeholderView?.visibility = if (count > 0) View.GONE else View.VISIBLE
        super.onCurrentListChanged(previousList, currentList)
    }


    inner class TodoViewHolder(private val binding: ItemTodoBinding) : RecyclerView.ViewHolder(binding.root) {
        var todo: Todo? = null

        fun bind(item: Todo) = with(binding) {
            todo = item
            title.text = item.title

            if(item.description.isNullOrEmpty()) {
                description.visibility = View.GONE
            }  else {
                description.visibility = View.VISIBLE
                description.text = item.description
            }


            binding.root.setOnLongClickListener {
                onItemLongClicked(item)
                true
            }
            binding.root.setOnClickListener { onItemClicked(item) }
        }
    }


    companion object {
        const val TAG = "TodoListAdapter"
        private val DIFF_CALLBACK = object :
            DiffUtil.ItemCallback<Todo>() {
            override fun areItemsTheSame(
                oldTodo: Todo,
                newTodo: Todo
            ) = oldTodo.timestamp == newTodo.timestamp

            override fun areContentsTheSame(
                oldTodo: Todo,
                newTodo: Todo
            ) = oldTodo == newTodo
        }
    }
}
