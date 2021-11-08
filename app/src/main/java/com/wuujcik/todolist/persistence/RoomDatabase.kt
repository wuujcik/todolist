package com.wuujcik.todolist.persistence

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [Meal::class], version = 1, exportSchema = false)
abstract class RoomDatabase : RoomDatabase() {
    abstract fun mealDao(): MealDao
}