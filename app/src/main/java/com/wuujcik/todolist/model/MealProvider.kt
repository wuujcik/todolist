package com.wuujcik.todolist.model

import android.util.Log
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.wuujcik.todolist.di.ActivityScope
import com.wuujcik.todolist.persistence.Meal
import com.wuujcik.todolist.persistence.MealDao
import com.wuujcik.todolist.ui.list.ListFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ActivityScope
class MealProvider @Inject constructor(val mealDao: MealDao, firebaseDb: FirebaseDatabase) {

    private var itemsEventListener: ChildEventListener? = null
    private val itemsReference = firebaseDb.reference.child(ITEMS_KEY)


    val getAll = mealDao.getAll()


    fun addItem(item: Meal, scope: CoroutineScope) {
        addItemToFirebase(item)
        addItemToRoom(item, scope)
    }

    private fun addItemToRoom(item: Meal, scope: CoroutineScope) {
        if (isMealValid(item)) {
            scope.launch {
                mealDao.insert(item)
            }
        }
    }

    private fun addItemToFirebase(item: Meal) {
        itemsReference.child(item.timestamp.toString()).setValue(item)
    }

    fun updateItem(item: Meal?, scope: CoroutineScope) {
        item ?: return
        updateItemInRoom(item, scope)
        updateItemInFirebase(item)
    }

    private fun updateItemInRoom(item: Meal, scope: CoroutineScope) {
        if (isMealValid(item)) {
            scope.launch {
                mealDao.update(item)
            }
        }
    }

    private fun updateItemInFirebase(item: Meal) {
        itemsReference.child(item.timestamp.toString()).setValue(item)
    }

    fun deleteItem(item: Meal?, scope: CoroutineScope) {
        item ?: return
        deleteItemInRoom(item.timestamp, scope)
        deleteItemInFirebase(item)
    }

    private fun deleteItemInRoom(itemTimestamp: Long?, scope: CoroutineScope) {
        itemTimestamp ?: return
        scope.launch {
            mealDao.delete(itemTimestamp)
        }
    }

    private fun deleteItemInFirebase(item: Meal) {
        itemsReference.child(item.timestamp.toString()).removeValue()
    }

    fun refreshFromFirebase(scope: CoroutineScope) {
        scope.launch {
            mealDao.deleteAll()
            getAllItemsFromFirebase(scope)
        }
    }

    suspend fun getItemByTimestamp(itemTimestamp: Long?, scope: CoroutineScope): Meal? {
        itemTimestamp ?: return null
        return withContext(scope.coroutineContext) {
            mealDao.getItemByTimestamp(itemTimestamp)
        }
    }

    fun attachDatabaseReadListeners(coroutineScope: CoroutineScope) {
        if (itemsEventListener == null) {
            itemsEventListener = object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val item = snapshot.getValue(Meal::class.java)
                    item?.let {
                        val allTimestamps = mealDao.getAllTimestampts().value ?: listOf()
                        if (item.timestamp !in allTimestamps) {
                            addItemToRoom(item, coroutineScope)
                        }
                    }
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    val updatedMeal = snapshot.getValue(Meal::class.java)
                    updatedMeal ?: return
                    updateItemInRoom(updatedMeal, coroutineScope)
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    snapshot.key?.let { key ->
                        try {
                            val itemTimestamp = key.toLong()
                            deleteItemInRoom(itemTimestamp, coroutineScope)
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

    private fun getAllItemsFromFirebase(coroutineScope: CoroutineScope) {
        itemsReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (itemSnapshot in dataSnapshot.children) {
                    val item = itemSnapshot.getValue(Meal::class.java)
                    item?.let {
                        addItemToRoom(it, coroutineScope)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        })
    }


    companion object {
        const val TAG = "MealProvider"
        const val ITEMS_KEY = "items"
        const val TITLE_MAX_LENGTH = 40
        const val DESCRIPTION_MAX_LENGTH = 500
    }
}
