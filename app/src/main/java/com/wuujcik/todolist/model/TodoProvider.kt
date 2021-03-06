package com.wuujcik.todolist.model

import android.util.Log
import com.google.firebase.database.*
import com.wuujcik.todolist.di.ActivityScope
import com.wuujcik.todolist.persistence.*
import com.wuujcik.todolist.ui.list.ListFragment
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@ActivityScope
class TodoProvider @Inject constructor(val todoDao: TodoDao, firebaseDb: FirebaseDatabase) {

    private var itemsEventListener: ChildEventListener? = null
    private val itemsReference = firebaseDb.reference.child(ITEMS_KEY)
    private val scope = kotlinx.coroutines.GlobalScope // TODO: maybe some more specific scope


    val getAll = todoDao.getAll()


    fun addItem(item: Todo) {
        addItemToFirebase(item)
    }


    private fun addItemToRoom(item: Todo) {
        if (isTodoValid(item)) {
            scope.launch {
                todoDao.insert(item)
            }
        }
    }


    private fun addItemToFirebase(item: Todo) {
        itemsReference.child(item.timestamp.toString()).setValue(item)
    }


    fun updateItem(item: Todo?) {
        item ?: return
        updateItemInRoom(item)
        updateItemInFirebase(item)
    }


    private fun updateItemInRoom(item: Todo) {
        if (isTodoValid(item)) {
            scope.launch {
                todoDao.update(item)
            }
        }
    }


    private fun updateItemInFirebase(item: Todo) {
        itemsReference.child(item.timestamp.toString()).setValue(item)
    }


    fun deleteItem(item: Todo?) {
        item ?: return
        deleteItemInRoom(item.timestamp)
        deleteItemInFirebase(item)
    }


    private fun deleteItemInRoom(itemTimestamp: Long?) {
        itemTimestamp ?: return
        scope.launch {
            todoDao.delete(itemTimestamp)
        }
    }


    private fun deleteItemInFirebase(item: Todo) {
        itemsReference.child(item.timestamp.toString()).removeValue()
    }


    fun getItemByTimestamp(itemTimestamp: Long?, completion: (todo: Todo?) -> Unit) { //TODO: this shouldn't be a callback
        itemTimestamp ?: return completion(null)
        scope.launch {
            completion(todoDao.getItemByTimestamp(itemTimestamp))
        }
    }


    fun attachDatabaseReadListeners() {
        if (itemsEventListener == null) {
            itemsEventListener = object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val item = snapshot.getValue(Todo::class.java)
                    item?.let {
                        val allTimestamps = todoDao.getAllTimestampts().value ?: listOf()
                        if (item.timestamp !in allTimestamps) {
                            addItemToRoom(item)
                        }
                    }
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    val updatedTodo = snapshot.getValue(Todo::class.java)
                    updatedTodo ?: return
                    updateItemInRoom(updatedTodo)
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    snapshot.key?.let { key ->
                        try {
                            val itemTimestamp = key.toLong()
                            deleteItemInRoom(itemTimestamp)
                        } catch (e: Exception) {
                            Log.w(TAG, "Couldn't parse key to Long for $key, e: $e")
                        }
                    }
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onCancelled(error: DatabaseError) {
                    Log.w(ListFragment.TAG, "Failed to read value. Error: $error")
                }
            }
        }

        itemsEventListener?.let {
            itemsReference.addChildEventListener(it)
        }

        itemsReference.keepSynced(true)
    }


    fun detachDatabaseReadListener() {
        itemsEventListener?.let {
            itemsReference.removeEventListener(it)
        }
        itemsEventListener = null
    }


    companion object {
        const val TAG = "TodoProvider"
        const val ITEMS_KEY = "items"
        const val TITLE_MAX_LENGTH = 30
        const val DESCRIPTION_MAX_LENGTH = 200
    }
}
