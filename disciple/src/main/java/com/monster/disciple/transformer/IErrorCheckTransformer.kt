package com.monster.disciple.transformer

interface IErrorCheckTransformer {

    /**
     * 登录态是否失效
     *
     * @param code response code
     * @param msg  response msg
     * @return true 失效；false 未失效
     */
    fun isLoginInvalid(code: Int?, msg: String?): Boolean

    /**
     * 登录态失效逻辑
     */
    fun loginInvalid()

    /**
     * 是否错误的返回
     *
     * @param code response code
     * @param msg  response msg
     * @return true 错误response；正确response
     */
    fun isErrorResponse(code: Int?, msg: String?): Boolean
}