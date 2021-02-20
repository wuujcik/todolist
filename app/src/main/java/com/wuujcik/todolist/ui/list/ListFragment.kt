package com.wuujcik.todolist.ui.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.wuujcik.todolist.R
import com.wuujcik.todolist.persistence.Todo
import com.wuujcik.todolist.utils.getApplication
import kotlinx.android.synthetic.main.fragment_list.*


class ListFragment : Fragment() {

    private lateinit var listViewModel: ListViewModel
    private lateinit var listViewModelFactory: ListViewModelFactory


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        listViewModelFactory = ListViewModelFactory(getApplication())
        listViewModel = ViewModelProvider(this, listViewModelFactory)
            .get(ListViewModel::class.java)

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


    override fun onPause() {
        super.onPause()
        listViewModel.detachDatabaseReadListener()
    }


    private fun setListAdapter() {
        listViewModel.allTodos.observe(viewLifecycleOwner, { list: List<Todo>? ->
            if (list.isNullOrEmpty()) {
                empty_recycler_view?.visibility = View.VISIBLE
                list_recycler_viewer.visibility = View.GONE

            } else {
                context?.let { ctx ->
                    list_recycler_viewer.visibility = View.VISIBLE
                    empty_recycler_view?.visibility = View.GONE
                    val todoListAdapter = TodoListAdapter(list, ctx)
                    todoListAdapter.onItemClicked = { data ->
                        findNavController().navigate(
                            ListFragmentDirections.actionListFragmentToDetailsFragment(data)
                        )
                    }
                    list_recycler_viewer.adapter = todoListAdapter
                    list_recycler_viewer.layoutManager = LinearLayoutManager(ctx)

                    todoListAdapter.onItemLongClicked = { item ->
                        AlertDialog.Builder(ctx, R.style.AlertDialog)
                            .setTitle(R.string.delete)
                            .setMessage(R.string.delete_confirm_text)
                            .setNegativeButton(R.string.yes) { _, _ ->
                                listViewModel.deleteTodo(item)
                            }
                            .setNeutralButton(R.string.button_cancel) { dialog, _ ->
                                dialog.dismiss()
                            }
                            .show()
                    }
                }
            }
        })
    }


    companion object {
        const val TAG = "ListFragment"
    }
}
