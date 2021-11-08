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
import com.wuujcik.todolist.databinding.ItemMealBinding
import com.wuujcik.todolist.persistence.Meal
import com.wuujcik.todolist.utils.formatShortDate


class MealListAdapter : PagedListAdapter<Meal, MealListAdapter.TodoViewHolder>(DIFF_CALLBACK) {

    /** Callback when user click on holder */
    var onItemClicked: (item: Meal) -> Unit = {}
    var onItemLongClicked: (item: Meal) -> Unit = {}
    var onErrorMessage: (message: String) -> Unit = {}

    /** Optional placeholder that is visible when there is no data */
    var placeholderView: View? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        return TodoViewHolder(
            ItemMealBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val item = getItem(position) ?: return
        holder.bind(item)
    }

    override fun onCurrentListChanged(
        previousList: PagedList<Meal>?,
        currentList: PagedList<Meal>?
    ) {
        val count = currentList?.size ?: 0
        placeholderView?.visibility = if (count > 0) View.GONE else View.VISIBLE
        super.onCurrentListChanged(previousList, currentList)
    }


    inner class TodoViewHolder(private val binding: ItemMealBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var meal: Meal? = null

        fun bind(item: Meal) = with(binding) {
            meal = item
            title.text = item.title
            item.timestamp?.let { date ->
                dateCreated.text = formatShortDate(binding.root.context, date)
            }
            when (item.mealType) {
                0 -> {
                    iconImg.load(R.drawable.ic_test)
                }
                1 -> {
                    iconImg.load(R.drawable.ic_now)
                }
                2 -> {
                    iconImg.load(R.drawable.ic_good)
                }
            }

            binding.root.setOnLongClickListener {
                onItemLongClicked(item)
                true
            }
            binding.root.setOnClickListener { onItemClicked(item) }
        }
    }


    companion object {
        const val TAG = "MealListAdapter"
        private val DIFF_CALLBACK = object :
            DiffUtil.ItemCallback<Meal>() {
            override fun areItemsTheSame(
                oldMeal: Meal,
                newMeal: Meal
            ) = oldMeal.timestamp == newMeal.timestamp

            override fun areContentsTheSame(
                oldMeal: Meal,
                newMeal: Meal
            ) = oldMeal == newMeal
        }
    }
}
