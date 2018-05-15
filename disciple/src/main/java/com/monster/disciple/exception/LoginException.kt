package com.monster.disciple.exception

class LoginException(override var code: Int?, override var msg: String?) : BaseException(code, msg)