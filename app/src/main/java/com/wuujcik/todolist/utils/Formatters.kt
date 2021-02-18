package com.wuujcik.todolist.utils

import android.content.Context
import android.text.format.DateUtils

/**
 * formats the date to display day, numeric month, year in a localized format
 */
fun formatShortDate(context: Context, date: Long): String {

    return DateUtils.formatDateTime(
        context,
        date,
        DateUtils.FORMAT_SHOW_YEAR or DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_NUMERIC_DATE
    )
}