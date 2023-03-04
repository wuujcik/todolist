package com.wuujcik.todolist.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.wuujcik.todolist.R
import com.wuujcik.todolist.databinding.FragmentDetailsBinding
import com.wuujcik.todolist.model.TodoProvider.Companion.DESCRIPTION_MAX_LENGTH
import com.wuujcik.todolist.model.TodoProvider.Companion.TITLE_MAX_LENGTH
import com.wuujcik.todolist.model.isTodoValid
import com.wuujcik.todolist.persistence.Todo
import com.wuujcik.todolist.utils.textToTrimString
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class DetailsFragment : Fragment() {

    private val detailsViewModel: DetailsViewModel by viewModel()

    private lateinit var binding: FragmentDetailsBinding

    private var originalItem: Todo? = null
    private val args: DetailsFragmentArgs by navArgs()
    private var editingMode = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentDetailsBinding.inflate(inflater, container, false)
            .also {
                binding = it
            }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        originalItem = args.data
        originalItem?.let { item ->
            editingMode = true
            binding.title.setText(item.title)
            binding.description.setText(item.description)
        }
        binding.cancelButton.setOnClickListener { findNavController().navigateUp() }
        binding.saveButton.setOnClickListener { saveItem() }
    }


    private fun saveItem() {
        val title = binding.title.text.textToTrimString()
        val description = binding.description.text.textToTrimString()
        val timestamp = originalItem?.timestamp ?: Date().time
        val item = Todo(title, description, timestamp)

        if (!isTodoValid(item)) {
            validateFields()
            return
        }

        if (editingMode) {
            detailsViewModel.updateItem(item)
        } else {
            detailsViewModel.createItem(item)
        }
        findNavController().navigateUp()
    }


    private fun validateFields() {
        when {
            binding.title.text.textToTrimString().isEmpty() -> {
                binding.titleLayout.error = getString(R.string.error_field_empty)
            }
            binding.title.text.textToTrimString().length > TITLE_MAX_LENGTH -> {
                binding.titleLayout.error = getString(R.string.error_field_too_long)
            }
            else -> {
                binding.titleLayout.error = null
            }
        }
        when {
            binding.description.text.textToTrimString().length > DESCRIPTION_MAX_LENGTH -> {
                binding.descriptionLayout.error = getString(R.string.error_field_too_long)
            }
            else -> {
                binding.descriptionLayout.error = null
            }
        }
    }
}
