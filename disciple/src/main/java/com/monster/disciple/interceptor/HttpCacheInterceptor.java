package com.monster.disciple.interceptor;

import android.content.Context;

import com.monster.disciple.util.NetworkUtil;

import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 设置http请求缓存策略
 * 无网络使用缓存并设置失效时间
 * 有网络不适用缓存
 */
public class HttpCacheInterceptor implements Interceptor {

    private Context context;
    private int cacheInvalidSec;

    public HttpCacheInterceptor(Context context, int cacheInvalidSec) {
        this.context = context;
        this.cacheInvalidSec = cacheInvalidSec;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (!NetworkUtil.isAvailable(context)) {
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }
        Response response = chain.proceed(request);
        if (NetworkUtil.isAvailable(context)) {
            int maxAge = 0;                     // 有网络时 设置缓存超时时间0个小时
            response.newBuilder()
                    .header("Cache-Control", "public, max-age=" + maxAge)
                    .removeHeader("Pragma")     // 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                    .build();
        } else {

            response.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + cacheInvalidSec)
                    .removeHeader("Pragma")
                    .build();
        }
        return response;
    }

    public static Cache getCache(Context context, long cacheSize) {
        File httpCacheDirectory = new File(context.getCacheDir(), "responses");
        return new Cache(httpCacheDirectory, cacheSize);         // 缓存空间10M
    }
}

