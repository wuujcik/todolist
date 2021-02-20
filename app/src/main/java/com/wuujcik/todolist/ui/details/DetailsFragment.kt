package com.wuujcik.todolist.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.wuujcik.todolist.R
import com.wuujcik.todolist.persistence.Todo
import com.wuujcik.todolist.model.TodoProvider
import com.wuujcik.todolist.utils.getApplication
import kotlinx.android.synthetic.main.fragment_details.*
import java.util.*

class DetailsFragment : Fragment() {

    var originalItem: Todo? = null
    val args: DetailsFragmentArgs by navArgs()
    private var editingMode = false

    private val todoProvider: TodoProvider
        get() {
            return TodoProvider(getApplication())
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

        if (isValid() && editingMode) {
            val timestamp = originalItem?.timestamp ?: return
            todoProvider.updateItem(Todo(title, description, timestamp, iconUrl))
            todoProvider.updateItemInFirebase(Todo(title, description, timestamp, iconUrl))
            findNavController().navigateUp()

        } else if (isValid()) {
            todoProvider.addItemToFirebase(Todo(title, description, Date().time, iconUrl))
            findNavController().navigateUp()
        }
    }


    private fun isValid(): Boolean {
        var isValid = true

        when {
            title.text.toString().trim().isEmpty() -> {
                title_layout.error = getString(R.string.error_field_empty)
                isValid = false
            }
            title.text.toString().trim().length > 30 -> {
                title_layout.error = getString(R.string.error_field_too_long)
                isValid = false
            }
            else -> {
                title_layout.error = null
            }
        }

        when {
            description.text.toString().trim().isEmpty() -> {
                description_layout.error = getString(R.string.error_field_empty)
                isValid = false
            }
            title.text.toString().trim().length > 200 -> {
                description_layout.error = getString(R.string.error_field_too_long)
                isValid = false
            }
            else -> {
                description_layout.error = null
            }
        }

        return isValid
    }
}
