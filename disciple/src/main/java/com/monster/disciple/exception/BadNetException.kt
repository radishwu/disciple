package com.monster.disciple.exception

class BadNetException(override var code: Int?, override var msg: String?) : BaseException(code, msg)