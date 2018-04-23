package com.monster.disciple.interceptor;

import android.text.TextUtils;

import com.monster.disciple.Disciple;

import java.io.IOException;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 多baseURL拦截器
 * 实现通过约定Header动态切换baseURL
 */
public class MultiHostInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request originalRequest = chain.request();
        List<String> multiHostKeys = originalRequest.headers(Disciple.MULTI_DOMAIN_NAME);

        if (null == multiHostKeys || 0 == multiHostKeys.size()) {
            return chain.proceed(originalRequest);
        }

        Request.Builder newBuilder = originalRequest.newBuilder();
        newBuilder.removeHeader(Disciple.MULTI_DOMAIN_NAME);
        String baseUrlKey = multiHostKeys.get(0);
        String newBaseUrl = Disciple.getInstance().getMultiHostMap().get(baseUrlKey);
        if (TextUtils.isEmpty(newBaseUrl)) {
            return chain.proceed(originalRequest);
        }
        HttpUrl baseUrl = HttpUrl.parse(newBaseUrl);
        if (null == baseUrl) {
            return chain.proceed(originalRequest);
        }

        HttpUrl newHttpUrl = originalRequest.url().newBuilder()
                .scheme(baseUrl.scheme())
                .host(baseUrl.host())
                .port(baseUrl.port())
                .build();

        return chain.proceed(newBuilder.url(newHttpUrl).build());
    }
}