package com.alancamargo.snookerscore.core.arch.extensions

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

inline fun <reified T : AppCompatActivity> Context.createIntent(): Intent {
    return Intent(this, T::class.java)
}