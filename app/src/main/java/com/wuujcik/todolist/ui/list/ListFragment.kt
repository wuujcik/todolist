package com.wuujcik.todolist.ui.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.wuujcik.todolist.R
import com.wuujcik.todolist.model.Todo
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_list.*
import kotlin.system.measureNanoTime


class ListFragment : Fragment() {

    private lateinit var firebaseDb: FirebaseDatabase
    private lateinit var itemsReference: DatabaseReference
    private var itemsEventListener: ChildEventListener? = null

    private lateinit var todoListAdapter: TodoListAdapter
    private val firebaseList = mutableListOf<Todo?>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        firebaseDb = Firebase.database
        itemsReference = firebaseDb.reference.child(ITEMS_KEY)

        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListAdapter()
        attachDatabaseReadListeners()

        create_new_item.setOnClickListener {
            findNavController().navigate(
                ListFragmentDirections.actionListFragmentToDetailsFragment(
                    null
                )
            )
        }
    }


    private fun setListAdapter() {
        todoListAdapter = TodoListAdapter(firebaseList, context, itemsReference)
        todoListAdapter.onItemClicked = { data ->
            findNavController().navigate(
                ListFragmentDirections.actionListFragmentToDetailsFragment(data)
            )
        }
        context?.let {
            list_recycler_viewer.adapter = todoListAdapter
            list_recycler_viewer.layoutManager = LinearLayoutManager(it)
        }
        progress_bar_overlay?.visibility = View.GONE
    }


    private fun attachDatabaseReadListeners() {
        if (itemsEventListener == null) {
            //  create and attach read listener
            itemsEventListener = object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val item = snapshot.getValue(Todo::class.java)
                    if (item !in firebaseList) {
                        firebaseList.add(item)
                    }
                    todoListAdapter.notifyDataSetChanged()
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    // An item has changed, use the key to determine if we are displaying this
                    // item and if so displayed the changed comment.
                    val updatedTodo = snapshot.getValue(Todo::class.java)

                    snapshot.key?.let { key ->
                        val index = getIndexForKey(key)
                        firebaseList[index] = updatedTodo
                        todoListAdapter.notifyItemChanged(index)
                    }

                    todoListAdapter.notifyDataSetChanged()
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    // An item has changed, use the key to determine if we are displaying this
                    // item and if so remove it.
                    snapshot.key?.let { key ->
                        val index = getIndexForKey(key)
                        firebaseList.removeAt(index)
                        todoListAdapter.notifyItemChanged(index)
                    }
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value. Error: $error")
                    Toast.makeText(context, R.string.failed_to_load_todos, Toast.LENGTH_SHORT).show()
                }
            }
        }
        itemsEventListener?.let {
            itemsReference.addChildEventListener(it)
        }
    }

    private fun detachDatabaseReadListener() {
        itemsEventListener?.let {
            itemsReference.removeEventListener(it)
        }
        itemsEventListener = null
    }


    override fun onPause() {
        super.onPause()
        detachDatabaseReadListener()
    }

    private fun getIndexForKey(key: String): Int {
        var index = 0
        for (item in firebaseList) {
            if (item?.timestamp.toString().equals(key)) {
                return index
            } else {
                index++
            }
        }
        return -1
    }


    companion object {
        const val TAG = "ListFragment"
        const val ITEMS_KEY = "items"
    }
}
