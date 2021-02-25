package com.wuujcik.todolist.utils
import android.text.Editable


fun Editable?.textToTrimString(): String {
    return this.toString().trim()
}
