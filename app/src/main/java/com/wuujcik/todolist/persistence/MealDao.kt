package com.wuujcik.todolist.persistence

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*


@Dao
interface MealDao {

    @Query("SELECT * FROM meal ORDER BY timestamp ASC")
    fun getAll(): DataSource.Factory<Int, Meal>

    @Query("SELECT timestamp FROM meal")
    fun getAllTimestampts(): LiveData<List<Long>>

    @Query("SELECT * FROM meal WHERE timestamp = :itemTimestamp")
    suspend fun getItemByTimestamp(itemTimestamp: Long?): Meal?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: Meal)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(item: Meal)

    @Query("DELETE FROM meal WHERE timestamp = :key")
    suspend fun delete(key: Long)

    @Query("DELETE FROM meal")
    suspend fun deleteAll()
}
