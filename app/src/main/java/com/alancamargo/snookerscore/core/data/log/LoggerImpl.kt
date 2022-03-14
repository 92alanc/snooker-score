package com.alancamargo.snookerscore.core.data.log

import android.util.Log

private const val TAG = "DEBUG_ALAN"

class LoggerImpl : Logger {

    override fun debug(message: String) {
        Log.d(TAG, message)
    }

    override fun error(throwable: Throwable) {
        Log.e(TAG, throwable.message, throwable)
    }

}
