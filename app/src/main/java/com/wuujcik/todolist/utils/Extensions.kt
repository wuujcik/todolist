package com.wuujcik.todolist.utils

import android.app.Application
import androidx.fragment.app.Fragment

fun Fragment.getApplication(): Application {
    return this.requireActivity().application
}