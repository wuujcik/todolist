package com.wuujcik.todolist.ui.list

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.paging.PagedList
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.wuujcik.todolist.R
import com.wuujcik.todolist.persistence.Todo
import com.wuujcik.todolist.ui.MainActivity
import kotlinx.android.synthetic.main.fragment_list.*
import javax.inject.Inject


class ListFragment : Fragment() {

    @Inject
    lateinit var listViewModel: ListViewModel
    private var todoListAdapter: TodoListAdapter? = null


    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).mainActivityComponent.inject(this)
    }

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

        create_new_item.setOnClickListener {
            findNavController().navigate(
                ListFragmentDirections.actionListFragmentToDetailsFragment(
                    null
                )
            )
        }
    }

    override fun onStop() {
        super.onStop()
        listViewModel.detachDatabaseReadListener()
    }

    private fun setListAdapter() {
        todoListAdapter = TodoListAdapter()

        listViewModel.allTodos.observe(viewLifecycleOwner, { list: PagedList<Todo>? ->
            todoListAdapter?.apply {
                placeholderView = empty_recycler_view
                submitList(list)
                onItemClicked = { data ->
                    findNavController().navigate(
                        ListFragmentDirections.actionListFragmentToDetailsFragment(data)
                    )
                }
                onItemLongClicked = { item ->
                    showDialog(item)
                }
                onErrorMessage = { message ->
                    view?.let {
                        val snackbar = Snackbar.make(it, message, Snackbar.LENGTH_LONG)
                        snackbar.show()
                    }
                }
            }

        })
        list_recycler_viewer.apply {
            adapter = todoListAdapter
            layoutManager = LinearLayoutManager(this.context)
        }
        swipeToDelete()
    }

    private fun showDialog(item: Todo) {
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

    private fun swipeToDelete() {
        ItemTouchHelper(object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int =
                makeMovementFlags(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)

            override fun onMove(
                recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

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
