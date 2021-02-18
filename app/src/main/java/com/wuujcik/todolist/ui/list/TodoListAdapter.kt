package com.wuujcik.todolist.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wuujcik.todolist.R
import com.wuujcik.todolist.model.Todo
import com.wuujcik.todolist.utils.formatShortDate
import kotlinx.android.synthetic.main.item_todo.view.*

class TodoListAdapter : ListAdapter<Todo, TodoListAdapter.ViewHolder>(diffCallback) {

    /** Callback when user click on holder */
    var onItemClicked: (item: Todo) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_todo, parent, false)
        return ViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onItemClicked)
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(todo: Todo, listener: (Todo) -> Unit) = with(itemView) {
            title.text = todo.title
            todo.timestamp?.let { date ->
                date_created.text = formatShortDate(context, date)
            }

            setOnClickListener { listener(todo) }
        }


    }


    companion object {
        const val TAG = "TodoListAdapter"
        var diffCallback: DiffUtil.ItemCallback<Todo> =
            object : DiffUtil.ItemCallback<Todo>() {

                override fun areItemsTheSame(oldItem: Todo, newItem: Todo): Boolean {
                    return oldItem.timestamp == newItem.timestamp
                }

                override fun areContentsTheSame(oldItem: Todo, newItem: Todo): Boolean {
                    return oldItem.equals(newItem)
                }
            }
    }
}
