package com.alancamargo.snookerscore.core.arch.extensions

import android.content.Intent
import android.os.Parcelable

const val EXTRA_ARGUMENTS = "com.alancamargo.snookerscore.core.arch.EXTRA_ARGUMENTS"
const val EXTRA_RESPONSE = "com.alancamargo.snookerscore.core.arch.EXTRA_RESPONSE"

fun Intent.putArguments(args: Parcelable): Intent = putExtra(EXTRA_ARGUMENTS, args)

fun Intent.putResponse(response: Parcelable): Intent = putExtra(EXTRA_RESPONSE, response)

fun <T : Parcelable> Intent?.getResponse(): T {
    if (this == null) {
        throw IllegalStateException("Intent must not be null")
    }

    return getParcelableExtra(EXTRA_RESPONSE) ?: throw IllegalStateException(
        "Missing EXTRA_RESPONSE"
    )
}
