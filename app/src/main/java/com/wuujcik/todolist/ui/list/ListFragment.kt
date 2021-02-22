package com.wuujcik.todolist.ui.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.PagedList
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.wuujcik.todolist.R
import com.wuujcik.todolist.persistence.Todo
import kotlinx.android.synthetic.main.fragment_list.*


class ListFragment : Fragment() {

    private val listViewModel by viewModels<ListViewModel>()
    private var todoListAdapter: TodoListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listViewModel.attachDatabaseReadListeners()
        setListAdapter()

        initSwipeToDelete()
        create_new_item.setOnClickListener {
            findNavController().navigate(
                ListFragmentDirections.actionListFragmentToDetailsFragment(
                    null
                )
            )
        }
    }


    override fun onPause() {
        super.onPause()
        listViewModel.detachDatabaseReadListener()
    }


    private fun setListAdapter() {
        this.activity?.let {
            todoListAdapter = TodoListAdapter(it)
        }

        listViewModel.allTodos.observe(viewLifecycleOwner, { list: PagedList<Todo>? ->
            todoListAdapter?.placeholderView = empty_recycler_view
            todoListAdapter?.submitList(list)

            todoListAdapter?.onItemClicked = { data ->
                findNavController().navigate(
                    ListFragmentDirections.actionListFragmentToDetailsFragment(data)
                )
            }
            todoListAdapter?.onItemLongClicked = { item ->
                AlertDialog.Builder(this.context, R.style.AlertDialog)
                    .setTitle(R.string.delete)
                    .setMessage(R.string.delete_confirm_text)
                    .setNegativeButton(R.string.yes) { _, _ ->
                        listViewModel.deleteTodo(item)
                        listViewModel.invalidateTodos()
                    }
                    .setNeutralButton(R.string.button_cancel) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }
            todoListAdapter?.onErrorMessage = { message ->
                view?.let {
                    val snackbar = Snackbar.make(it, message, Snackbar.LENGTH_LONG)
                    snackbar.show()
                }
            }
        })

        list_recycler_viewer.adapter = todoListAdapter
        list_recycler_viewer.layoutManager = LinearLayoutManager(this.context)
    }


    private fun initSwipeToDelete() {
        ItemTouchHelper(object : ItemTouchHelper.Callback() {
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
                    listViewModel.invalidateTodos()
                    listViewModel.deleteTodo(it)
                }
            }
        }).attachToRecyclerView(list_recycler_viewer)
    }


    companion object {
        const val TAG = "ListFragment"
    }
}
