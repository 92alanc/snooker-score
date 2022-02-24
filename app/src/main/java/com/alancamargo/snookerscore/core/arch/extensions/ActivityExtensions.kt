package com.alancamargo.snookerscore.core.arch.extensions

import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity

fun <T : Parcelable> AppCompatActivity.args(): Lazy<T> = lazy {
    intent.getParcelableExtra(EXTRA_ARGUMENTS) as? T ?: throw IllegalStateException(
        "Missing arguments"
    )
}
