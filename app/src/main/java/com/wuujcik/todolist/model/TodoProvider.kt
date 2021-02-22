package com.wuujcik.todolist.model

import android.content.Context
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.wuujcik.todolist.persistence.*
import com.wuujcik.todolist.ui.list.ListFragment
import java.lang.Exception


class TodoProvider(private val context: Context) {

    private val handlerThread = HandlerThread("TODO_PROVIDER_HANDLER_THREAD")
    private var itemsEventListener: ChildEventListener? = null


    private val firebaseDb: FirebaseDatabase
        get() {
            return Firebase.database
        }

    private val itemsReference: DatabaseReference
        get() {
            return firebaseDb.reference.child(ITEMS_KEY)
        }

    private val todoDao: TodoDao
        get() {
            return TodoDatabase.getDatabase(context.applicationContext).todoDao()
        }

    init {
        handlerThread.start()
    }

    fun attachDatabaseReadListeners() {
        if (itemsEventListener == null) {
            //  create and attach read listener
            itemsEventListener = object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val item = snapshot.getValue(Todo::class.java)
                    item?.let {
                        val allTimestamps = todoDao.getAllTimestampts().value ?: listOf()
                        if (item.timestamp !in allTimestamps) {
                            addItem(item)
                        }
                    }
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    val updatedTodo = snapshot.getValue(Todo::class.java)
                    updateItem(updatedTodo)
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    snapshot.key?.let { key ->
                        try {
                            val a = key.toLong()
                            deleteItem(a)
                        } catch (e: Exception) {
                            Log.w(TAG, "Couldn't parse key to Long for $key, e: $e")
                        }
                    }
                }
                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
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


    fun addItem(item: Todo) {
        Handler(handlerThread.looper).post {
            todoDao.insert(item)
        }
    }


    fun addItemToFirebase(item: Todo) {
        itemsReference.child(item.timestamp.toString()).setValue(item)
    }


    fun updateItem(item: Todo?) {
        item ?: return
        Handler(handlerThread.looper).post {
            todoDao.update(item)
        }
    }


    fun updateItemInFirebase(item: Todo) {
        itemsReference.child(item.timestamp.toString()).setValue(item)
    }


    fun deleteItem(itemTimestamp: Long?) {
        itemTimestamp ?: return
        Handler(handlerThread.looper).post {
            todoDao.delete(itemTimestamp)
        }
    }


    fun deleteItemInFirebase(item: Todo) {
        itemsReference.child(item.timestamp.toString()).removeValue()
    }


    companion object {
        const val TAG = "TodoProvider"
        const val ITEMS_KEY = "items"
    }
}
