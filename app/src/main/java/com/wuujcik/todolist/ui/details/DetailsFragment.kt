package com.wuujcik.todolist.ui.details

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.wuujcik.todolist.R
import com.wuujcik.todolist.persistence.Todo
import com.wuujcik.todolist.model.isTodoValid
import com.wuujcik.todolist.ui.MainActivity
import kotlinx.android.synthetic.main.fragment_details.*
import java.util.*
import javax.inject.Inject

class DetailsFragment : Fragment() {

    @Inject
    lateinit var detailsViewModel: DetailsViewModel

    var originalItem: Todo? = null
    val args: DetailsFragmentArgs by navArgs()
    private var editingMode = false


    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).mainActivityComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        originalItem = args.data
        originalItem?.let { item ->
            editingMode = true
            title.setText(item.title)
            description.setText(item.description)
            icon.setText(item.iconUrl)
        }
        cancelButton.setOnClickListener { findNavController().navigateUp() }
        saveButton.setOnClickListener { saveItem() }
    }


    private fun saveItem() {
        val title = title.text.toString().trim()
        val description = description.text.toString().trim()
        val iconUrl = icon.text.toString().trim()
        val timestamp = originalItem?.timestamp ?: Date().time
        val item = Todo(title, description, timestamp, iconUrl)

        if (!isTodoValid(item)) {
            validateFields()
            return
        }
        
        if (editingMode) {
            detailsViewModel.updateItem(item)
            findNavController().navigateUp()
        } else {
            detailsViewModel.createItem(item)
            findNavController().navigateUp()
        }
    }


    private fun validateFields() {
        when {
            title.text.toString().trim().isEmpty() -> {
                title_layout.error = getString(R.string.error_field_empty)
            }
            title.text.toString().trim().length > 30 -> {
                title_layout.error = getString(R.string.error_field_too_long)
            }
            else -> {
                title_layout.error = null
            }
        }
        when {
            description.text.toString().trim().isEmpty() -> {
                description_layout.error = getString(R.string.error_field_empty)
            }
            title.text.toString().trim().length > 200 -> {
                description_layout.error = getString(R.string.error_field_too_long)
            }
            else -> {
                description_layout.error = null
            }
        }
    }
}
