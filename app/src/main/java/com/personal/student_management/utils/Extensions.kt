package com.personal.student_management.utils

import java.text.SimpleDateFormat
import java.util.*

fun Long.toStringDate(): String {
    val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    return formatter.format(Date(this))
}