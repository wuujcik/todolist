package com.wuujcik.todolist.model

import com.wuujcik.todolist.model.MealProvider.Companion.DESCRIPTION_MAX_LENGTH
import com.wuujcik.todolist.model.MealProvider.Companion.TITLE_MAX_LENGTH
import com.wuujcik.todolist.persistence.Meal


fun isMealValid(item: Meal?): Boolean {

    item ?: return false

    if (item.title.isNullOrEmpty()) {
        return false
    }

    if ((item.title?.length ?: 0) > TITLE_MAX_LENGTH) {
        return false
    }
    
    if ((item.description?.length ?: 0) > DESCRIPTION_MAX_LENGTH) {
        return false
    }

    if (item.timestamp == null) {
        return false
    }

    return true
}
