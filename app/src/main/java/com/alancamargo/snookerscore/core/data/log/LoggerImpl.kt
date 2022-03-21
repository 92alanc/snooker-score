package com.alancamargo.snookerscore.core.data.log

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics

private const val TAG = "DEBUG_ALAN"

class LoggerImpl(private val crashlytics: FirebaseCrashlytics) : Logger {

    override fun debug(message: String) {
        Log.d(TAG, message)
        crashlytics.log(message)
    }

    override fun error(throwable: Throwable) {
        Log.e(TAG, throwable.message, throwable)
        crashlytics.recordException(throwable)
    }

}
