package com.wuujcik.todolist.ui.details

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.wuujcik.todolist.R
import com.wuujcik.todolist.databinding.FragmentDetailsBinding
import com.wuujcik.todolist.model.MealProvider.Companion.DESCRIPTION_MAX_LENGTH
import com.wuujcik.todolist.model.MealProvider.Companion.TITLE_MAX_LENGTH
import com.wuujcik.todolist.persistence.Meal
import com.wuujcik.todolist.model.isMealValid
import com.wuujcik.todolist.ui.MainActivity
import com.wuujcik.todolist.utils.textToTrimString
import java.util.*
import javax.inject.Inject


class DetailsFragment : Fragment() {

    @Inject
    lateinit var detailsViewModel: DetailsViewModel

    private lateinit var binding: FragmentDetailsBinding

    private var originalItem: Meal? = null
    private val args: DetailsFragmentArgs by navArgs()
    private var editingMode = false


    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).mainActivityComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
            val radioButtonId = when (item.mealType) {
                0 -> R.id.test_rb
                1 -> R.id.now_rb
                2 -> R.id.good_rb
                else -> -1
            }
            binding.radioGroup.check(radioButtonId)
        }
        binding.cancelButton.setOnClickListener { findNavController().navigateUp() }
        binding.saveButton.setOnClickListener { saveItem() }
    }


    private fun saveItem() {
        val title = binding.title.text.textToTrimString()
        val description = binding.description.text.textToTrimString()
        val mealType = getMealTypeIndex(binding.radioGroup)
        val timestamp = originalItem?.timestamp ?: Date().time
        val item = Meal(title, description, timestamp, mealType)

        if (!isMealValid(item)) {
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

    private fun getMealTypeIndex(radioGroup: RadioGroup): Int? {
        return when (radioGroup.checkedRadioButtonId) {
            R.id.test_rb -> 0
            R.id.now_rb ->  1
            R.id.good_rb ->  2
            else -> null
        }
    }
}
