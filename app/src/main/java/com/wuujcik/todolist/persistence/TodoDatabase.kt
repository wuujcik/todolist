package com.wuujcik.todolist.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Todo::class], version = 1, exportSchema = false)
abstract class TodoDatabase : RoomDatabase() {

    abstract fun todoDao(): TodoDao

    companion object {

        @Volatile
        private var mInstance: TodoDatabase? = null

        fun getDatabase(context: Context): TodoDatabase {
            synchronized(this) {
                if (mInstance == null) {
                    // Create database
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        TodoDatabase::class.java,
                        "todo"
                    )
                        .fallbackToDestructiveMigration()
                        .build()

                    mInstance = instance
                }
                return mInstance!!
            }
        }
    }
}