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
import com.wuujcik.todolist.persistence.Todo
import com.wuujcik.todolist.utils.formatShortDate
import kotlinx.android.synthetic.main.item_todo.view.*


class TodoListAdapter : PagedListAdapter<Todo, TodoListAdapter.TodoViewHolder>(DIFF_CALLBACK) {

    /** Callback when user click on holder */
    var onItemClicked: (item: Todo) -> Unit = {}
    var onItemLongClicked: (item: Todo) -> Unit = {}
    var onErrorMessage: (message: String) -> Unit = {}

    /** Optional placeholder that is visible when there is no data */
    var placeholderView: View? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_todo, parent, false)
        return TodoViewHolder(itemView)
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


    inner class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var todo: Todo? = null

        fun bind(item: Todo) = with(itemView) {
            todo = item
            title.text = item.title
            item.timestamp?.let { date ->
                date_created.text = formatShortDate(context, date)
            }
            if (item.iconUrl != null && item.iconUrl != "") {
                icon_img.load(item.iconUrl)
            } else {
                icon_img.load(  R.drawable.ic_placeholder)
            }

            setOnLongClickListener {
                onItemLongClicked(item)
                true
            }
            setOnClickListener { onItemClicked(item) }
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
