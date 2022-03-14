package com.alancamargo.snookerscore.core.data.log

interface Logger {

    fun debug(message: String)

    fun error(throwable: Throwable)

}
