package com.wuujcik.todolist.model

import com.wuujcik.todolist.persistence.Todo


fun isTodoValid(item: Todo?): Boolean {

    item ?: return false

    if (item.title.isNullOrEmpty()) {
        return false
    }
    if (item.title!!.length > 30) {
        return false
    }

    if (item.description.isNullOrEmpty()) {
        return false
    }
    if (item.description!!.length > 200) {
        return false
    }

    if (item.timestamp == null) {
        return false
    }

    return true
}
