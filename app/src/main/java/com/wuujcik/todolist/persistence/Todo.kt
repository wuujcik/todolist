package com.wuujcik.todolist.persistence

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "todo")
data class Todo(

    @ColumnInfo(name = "title")
    var title: String? = null,

    @ColumnInfo(name = "description")
    var description: String? = null,

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "timestamp")
    var timestamp: Long? = null,

    @ColumnInfo(name = "iconUrl")
    var iconUrl: String? = null
) : Serializable
