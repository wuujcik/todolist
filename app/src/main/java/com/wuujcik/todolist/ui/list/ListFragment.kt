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
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.wuujcik.todolist.R
import com.wuujcik.todolist.databinding.FragmentListBinding
import com.wuujcik.todolist.persistence.Meal
import com.wuujcik.todolist.ui.MainActivity
import javax.inject.Inject


class ListFragment : Fragment() {

    @Inject  lateinit var listViewModel: ListViewModel
    private var mealListAdapter: MealListAdapter? = null

    private lateinit var binding: FragmentListBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).mainActivityComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentListBinding.inflate(inflater, container, false)
            .also {
                binding = it
            }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listViewModel.attachDatabaseReadListeners()
        setListAdapter()

        binding.createNewItem.setOnClickListener {
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
        mealListAdapter = MealListAdapter()

        listViewModel.allMeals.observe(viewLifecycleOwner, { list: PagedList<Meal>? ->
            mealListAdapter?.apply {
                placeholderView = binding.emptyRecyclerView
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
        with(binding.listRecyclerViewer) {
            adapter = mealListAdapter
            layoutManager = LinearLayoutManager(this.context)
        }
    }

    private fun showDialog(item: Meal) {
        AlertDialog.Builder(this.context, R.style.AlertDialog)
            .setTitle(R.string.delete)
            .setMessage(R.string.delete_confirm_text)
            .setNegativeButton(R.string.yes) { _, _ ->
                listViewModel.deleteMeal(item)
                listViewModel.invalidateMeals()
            }
            .setNeutralButton(R.string.button_cancel) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }


    companion object {
        const val TAG = "ListFragment"
    }
}
