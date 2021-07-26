package com.wuujcik.todolist.persistence

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*


@Dao
interface TodoDao {

    @Query("SELECT * FROM todo ORDER BY timestamp ASC")
    fun getAll(): DataSource.Factory<Int, Todo>

    @Query("SELECT timestamp FROM todo")
    fun getAllTimestampts(): LiveData<List<Long>>

    @Query("SELECT * FROM todo WHERE timestamp = :itemTimestamp")
    suspend fun getItemByTimestamp(itemTimestamp: Long?): Todo?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: Todo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(item: Todo)

    @Query("DELETE FROM todo WHERE timestamp = :key")
    suspend fun delete(key: Long)

    @Query("DELETE FROM todo")
    suspend fun deleteAll()
}
