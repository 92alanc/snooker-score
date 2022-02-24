package com.alancamargo.snookerscore.core.arch.extensions

import android.content.Intent
import android.os.Parcelable

const val EXTRA_ARGUMENTS = "com.alancamargo.snookerscore.core.arch.EXTRA_ARGUMENTS"

fun Intent.putArguments(args: Parcelable): Intent = putExtra(EXTRA_ARGUMENTS, args)
