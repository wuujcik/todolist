package com.wuujcik.todolist.ui.details

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.wuujcik.todolist.R
import com.wuujcik.todolist.model.Todo
import com.wuujcik.todolist.ui.list.ListFragment
import kotlinx.android.synthetic.main.fragment_details.*
import java.util.*

class DetailsFragment : Fragment() {

    var item: Todo? = null
    val args: DetailsFragmentArgs by navArgs()
    private var firebaseDb: FirebaseDatabase? = null
    private var itemReference: DatabaseReference? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        firebaseDb = Firebase.database
        itemReference = firebaseDb?.reference?.child(ListFragment.ITEMS_KEY)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        item = args.data
        title.setText(item?.title)
        description.setText(item?.description)
        icon.setText(item?.iconUrl)

        cancelButton.setOnClickListener { findNavController().navigateUp() }
        saveButton.setOnClickListener { saveItem() }
    }

    private fun saveItem(){
        if(isValid()){
            val todo = Todo(title.text.toString().trim(), description.text.toString().trim(), Date().time, icon.text.toString().trim())
            itemReference?.push()?.setValue(todo)
            findNavController().navigateUp()
        }
    }

    private fun isValid() : Boolean{
        return true
    }
}
