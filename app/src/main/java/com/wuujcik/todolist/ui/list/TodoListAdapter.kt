package com.wuujcik.todolist.ui.list

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.wuujcik.todolist.R
import com.wuujcik.todolist.model.Todo
import com.wuujcik.todolist.utils.formatShortDate
import kotlinx.android.synthetic.main.item_todo.view.*

class TodoListAdapter(
    val items: MutableList<Todo?>,
    val context: Context?,
    val ref: DatabaseReference
) : RecyclerView.Adapter<TodoListAdapter.ViewHolder>() {

    /** Callback when user click on holder */
    var onItemClicked: (item: Todo) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_todo, parent, false)
        return ViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        item?.let {
            holder.bind(it, onItemClicked)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: Todo, listener: (Todo) -> Unit) = with(itemView) {
            title.text = item.title
            item.timestamp?.let { date ->
                date_created.text = formatShortDate(context, date)
            }
            setOnLongClickListener {
                handleLongClick(item)
                true
            }
            //TODO: set the icon
            setOnClickListener { listener(item) }
        }

        private fun handleLongClick(item: Todo) {
            AlertDialog.Builder(context)
                .setTitle(R.string.delete)
                .setMessage(R.string.delete_confirm_text)
                .setNegativeButton(R.string.yes) { _, _ ->
                    ref.child(item.timestamp.toString()).removeValue()
                    items.remove(item)
                    notifyDataSetChanged()
                }
                .setNeutralButton(R.string.button_cancel) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }


    companion object {
        const val TAG = "TodoListAdapter"
    }
}
