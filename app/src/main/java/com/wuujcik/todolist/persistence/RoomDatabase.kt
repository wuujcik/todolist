package com.wuujcik.todolist.persistence

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [Todo::class], version = 2, exportSchema = false)
abstract class RoomDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
    companion object {
        const val DB_NAME = "offline_database"
    }
}