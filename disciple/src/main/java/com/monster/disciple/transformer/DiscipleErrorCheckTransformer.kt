package com.monster.disciple.transformer

import android.content.Context
import com.monster.disciple.response.BaseResponse
import com.monster.disciple.util.CacheBuilder
import retrofit2.Response

class DiscipleErrorCheckTransformer<T : Response<R>, R : BaseResponse<*>> : AbsErrorCheckTransformer<T, R> {

    constructor(context: Context?) : super(context)

    constructor(context: Context?, cacheBuilder: CacheBuilder?) : super(context, cacheBuilder)

    override fun isLoginInvalid(code: Int?, msg: String?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loginInvalid() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isErrorResponse(code: Int?, msg: String?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}