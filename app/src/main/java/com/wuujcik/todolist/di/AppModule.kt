package com.wuujcik.todolist.di

import android.content.Context
import androidx.room.Room
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.wuujcik.todolist.persistence.TodoDao
import com.wuujcik.todolist.persistence.RoomDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun provideRoomDb(context: Context): RoomDatabase {
        return Room
            .databaseBuilder(context, RoomDatabase::class.java, "todo")
            .allowMainThreadQueries()
            .build()
    }

    @Provides
    fun provideTodoDao(todoDb: RoomDatabase): TodoDao {
        return todoDb.todoDao()
    }

    @Provides
    fun provideFirebaseDb(): FirebaseDatabase {
        return Firebase.database
    }
}
