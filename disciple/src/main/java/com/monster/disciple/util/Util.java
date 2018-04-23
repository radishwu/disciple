package com.monster.disciple.util;

import com.monster.disciple.exception.BadNetException;
import com.monster.disciple.exception.BusinessException;
import com.monster.disciple.exception.LoginException;
import com.monster.disciple.response.BaseResponse;
import com.monster.disciple.transformer.IErrorCheckTransformer;

import io.reactivex.functions.Function;
import retrofit2.Response;

public class Util {

    public static <T extends Response<R>, R extends BaseResponse> Function<T, R> newInstanceFunction(final IErrorCheckTransformer errorCheck) {
        return new Function<T, R>() {
            @Override
            public R apply(T t) {

                if (!t.isSuccessful() || null == t.body()) {
                    throw new BadNetException(t.code(), "");
                } else if (errorCheck.isLoginInvalid(t.body().getCode(), t.body().getMsg())) {
                    errorCheck.loginInvalid();
                    throw new LoginException(t.body().getCode(), t.body().getMsg());
                } else if (errorCheck.isErrorResponse(t.body().getCode(), t.body().getMsg())) {
                    throw new BusinessException(t.body().getCode(), t.body().getMsg());
                }

                return t.body();
            }
        };
    }
}
