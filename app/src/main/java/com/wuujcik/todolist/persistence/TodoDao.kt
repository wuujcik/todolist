package com.wuujcik.todolist.persistence

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface TodoDao {

    @Query("SELECT * FROM todo")
    fun getAll(): LiveData<List<Todo>>

    @Query("SELECT timestamp FROM todo")
    fun getAllTimestampts(): LiveData<List<Long>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: Todo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun update(item: Todo)

    @Query("DELETE FROM todo WHERE timestamp = :key")
    fun delete(key: Long)

    @Query("DELETE FROM todo")
    fun deleteAll()
}
