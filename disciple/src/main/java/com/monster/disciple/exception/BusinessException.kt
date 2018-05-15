package com.monster.disciple.exception

class BusinessException(override var code: Int?, override var msg: String?) : BaseException(code, msg)