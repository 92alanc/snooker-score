package com.alancamargo.snookerscore.core.ui.tools

import java.text.SimpleDateFormat
import java.util.Locale

fun Long.formatDateTime(): String {
    val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.UK)
    return simpleDateFormat.format(this)
}
