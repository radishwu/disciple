package com.monster.disciple.subscribe

import com.monster.disciple.exception.BusinessException

interface IDiscipleException {

    /**
     * 网络 异常
     */
    fun onBadNetError(t: Throwable)

    /**
     * http 异常
     */
    fun onServerError(t: Throwable)

    /**
     * 业务 异常
     */
    fun onBusinessError(ex: BusinessException)

    /**
     * 其他 异常
     */
    fun onOtherError(t: Throwable)
}