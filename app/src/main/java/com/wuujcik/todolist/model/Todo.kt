package com.wuujcik.todolist.model

import java.io.Serializable

data class Todo(
    var title: String? = null,
    var description: String? = null,
    var timestamp: Long? = null,
    var iconUrl: String? = null
) : Serializable
