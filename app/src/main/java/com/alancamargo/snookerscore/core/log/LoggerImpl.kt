package com.alancamargo.snookerscore.core.log

import android.util.Log

private const val TAG = "DEBUG_ALAN"

class LoggerImpl : Logger {

    override fun error(throwable: Throwable) {
        Log.e(TAG, throwable.message, throwable)
    }

}
