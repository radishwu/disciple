package com.monster.disciple.exception

open class BaseException(open var code: Int?, open var msg: String?) : RuntimeException()