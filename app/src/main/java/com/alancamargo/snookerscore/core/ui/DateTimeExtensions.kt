package com.alancamargo.snookerscore.core.ui

import java.text.SimpleDateFormat
import java.util.Locale

fun Long.formatDateTime(): String {
    val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy HH:MM", Locale.UK)
    return simpleDateFormat.format(this)
}
