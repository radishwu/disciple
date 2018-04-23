package com.monster.disciple.transformer;

public interface IErrorCheckTransformer {

    /**
     * 登录态是否失效
     *
     * @param code response code
     * @param msg  response msg
     * @return true 失效；false 未失效
     */
    boolean isLoginInvalid(Integer code, String msg);

    /**
     * 登录态失效逻辑
     */
    void loginInvalid();

    /**
     * 是否错误的返回
     *
     * @param code response code
     * @param msg  response msg
     * @return true 错误response；正确response
     */
    boolean isErrorResponse(Integer code, String msg);
}
