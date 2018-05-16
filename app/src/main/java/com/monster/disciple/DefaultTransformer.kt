package com.monster.disciple

import android.content.Context
import com.monster.disciple.response.BaseResponse
import com.monster.disciple.transformer.AbsErrorCheckTransformer
import retrofit2.Response

class DefaultTransformer<T : Response<R>, R : BaseResponse<*>> : AbsErrorCheckTransformer<T, R> {

    constructor(context: Context?) : super(context)

    constructor(context: Context?, cacheBuilder: CacheBuilder?) : super(context, cacheBuilder)

    override fun isLoginInvalid(code: Int?, msg: String?): Boolean {
        return false
    }

    override fun loginInvalid() {

    }

    override fun isErrorResponse(code: Int?, msg: String?): Boolean {
        return false
    }
}