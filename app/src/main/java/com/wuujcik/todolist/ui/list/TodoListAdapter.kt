package com.wuujcik.todolist.ui.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wuujcik.todolist.R
import com.wuujcik.todolist.persistence.Todo
import com.wuujcik.todolist.utils.formatShortDate
import kotlinx.android.synthetic.main.item_todo.view.*


class TodoListAdapter(
    private val items: List<Todo>,
    val context: Context
) : RecyclerView.Adapter<TodoListAdapter.ViewHolder>() {

    /** Callback when user click on holder */
    var onItemClicked: (item: Todo) -> Unit = {}
    var onItemLongClicked: (item: Todo) -> Unit = {}


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_todo, parent, false)
        return ViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }


    override fun getItemCount(): Int {
        return items.size
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: Todo) = with(itemView) {
            title.text = item.title
            item.timestamp?.let { date ->
                date_created.text = formatShortDate(context, date)
            }
            setOnLongClickListener {
                onItemLongClicked(item)
                true
            }
            //TODO: set the icon
            setOnClickListener { onItemClicked(item) }
        }
    }


    companion object {
        const val TAG = "TodoListAdapter"
    }
}
