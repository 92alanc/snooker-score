package com.alancamargo.snookerscore.core.arch.extensions

import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment

private const val ARG_ARGUMENTS = "com.alancamargo.snookerscore.core.arch.ARG_ARGUMENTS"

fun <T : Parcelable> Fragment.args(): Lazy<T> = lazy {
    arguments?.getParcelable(EXTRA_ARGUMENTS) as? T ?: throw IllegalStateException(
        "Missing arguments"
    )
}

fun <T : Fragment> T.putArguments(args: Parcelable): T {
    return this.apply {
        arguments = Bundle().also {
            it.putParcelable(ARG_ARGUMENTS, args)
        }
    }
}