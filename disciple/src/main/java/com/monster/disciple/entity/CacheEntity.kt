package com.monster.disciple.entity

import com.monster.disciple.response.BaseResponse

data class CacheEntity<T : BaseResponse<*>>(var response: T?) {

    fun isNil(): Boolean = null == response
}