package com.wuujcik.todolist.di

import androidx.room.Room
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.wuujcik.todolist.persistence.RoomDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

/**
 * Module holding databases.
 */
val databaseModule = module {
    single<RoomDatabase> {
        Room
            .databaseBuilder(
                androidApplication(),
                RoomDatabase::class.java,
                RoomDatabase.DB_NAME
            )
            .fallbackToDestructiveMigration()
            .build()
    }
    single <FirebaseDatabase>{
        Firebase.database
    }
}
