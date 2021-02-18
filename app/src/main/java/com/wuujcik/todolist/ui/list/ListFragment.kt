package com.wuujcik.todolist.ui.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.wuujcik.todolist.R
import com.wuujcik.todolist.model.Todo
import com.wuujcik.todolist.ui.MainViewModel
import com.wuujcik.todolist.ui.MainViewModelFactory
import com.wuujcik.todolist.utils.getApplication
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_list.*
import java.util.*


class ListFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var mainViewModelFactory: MainViewModelFactory


    private var firebaseDb: FirebaseDatabase? = null
    private var itemsReference: DatabaseReference? = null
    private var childEventListener: ChildEventListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainViewModelFactory = MainViewModelFactory(getApplication())
        mainViewModel = ViewModelProvider(this, mainViewModelFactory)
            .get(MainViewModel::class.java)
        firebaseDb = Firebase.database
        itemsReference = firebaseDb?.reference?.child(ITEMS_KEY)

        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListAdapter()
        attachDatabaseReadListener()

        create_new_item.setOnClickListener {
            findNavController().navigate(ListFragmentDirections.actionListFragmentToDetailsFragment(null))
        }
    }


    private fun setListAdapter() {
        val todoListAdapter = TodoListAdapter()
        todoListAdapter.onItemClicked = { data ->
            findNavController().navigate(
                ListFragmentDirections.actionListFragmentToDetailsFragment(data)
            )
        }
        context?.let {
            list_recycler_viewer.adapter = todoListAdapter
            list_recycler_viewer.layoutManager = LinearLayoutManager(it)
        }


        val firebaseList = mutableListOf<Todo>()
        firebaseList.add(Todo("tytuł", "opis", Date().time))
        firebaseList.add(Todo("kasza", "opis", Date().time))
        firebaseList.add(Todo("skarpety", "opis", Date().time))

        progress_bar_overlay?.visibility = View.GONE
        todoListAdapter.submitList(firebaseList)

    }


    private fun attachDatabaseReadListener() {
        if (childEventListener == null) {
            //  create and attach read listener
            childEventListener = object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val item = snapshot.getValue(Todo::class.java)
                    Log.e("DEBUGGING", "ListFragment, onChildAdded: new item = $item")
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    val item = snapshot.getValue(Todo::class.java)
                    Log.e("DEBUGGING", "ListFragment, onChildChanged: changed item = $item")
                }
                override fun onChildRemoved(snapshot: DataSnapshot) {
                    val item = snapshot.getValue(Todo::class.java)
                    Log.e("DEBUGGING", "ListFragment, onChildRemoved: removed item = $item")
                }
                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onCancelled(error: DatabaseError) {}
            }
        }
        childEventListener?.let {
            itemsReference?.addChildEventListener(it)
        }
    }

    private fun detachDatabaseReadListener() {
        childEventListener?.let {
            itemsReference?.removeEventListener(it)
        }
        childEventListener = null
    }


    override fun onPause() {
        super.onPause()
        detachDatabaseReadListener()
    }


    companion object{
        const val TAG = "ListFragment"
        const val ITEMS_KEY = "items"
    }
}
