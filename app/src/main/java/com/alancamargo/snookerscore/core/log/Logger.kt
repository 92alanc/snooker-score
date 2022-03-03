package com.alancamargo.snookerscore.core.log

interface Logger {

    fun debug(message: String)

    fun error(throwable: Throwable)

}
