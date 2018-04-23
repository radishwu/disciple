package com.monster.disciple;

import android.content.Context;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.monster.disciple.interceptor.HttpCacheInterceptor;
import com.monster.disciple.interceptor.MultiHostInterceptor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public final class Disciple {

    public final static String MULTI_DOMAIN_NAME = "Multi-Domain-Name";
    /**
     * 多baseURL策略Header标识
     */
    public final static String MULTI_DOMAIN_NAME_HEADER = MULTI_DOMAIN_NAME + ":";

    private volatile static Retrofit retrofit = null;
    private volatile static Disciple instance = null;
    private volatile static OkHttpClient clientInstance = null;
    private final Map<String, String> multiHostMap = new HashMap<>();
    private String baseUrl;

    private Disciple() {
    }

    public static void init(Context context, Builder builder) {
        instance = new Disciple();

        clientInstance = new OkHttpClient.Builder()
                .addInterceptor(new MultiHostInterceptor())
                .addInterceptor(new HttpCacheInterceptor(context.getApplicationContext(), builder.cacheInvalidSec))
                .cache(HttpCacheInterceptor.getCache(context.getApplicationContext(), builder.cacheSize))
                .addInterceptor(builder.isDebug ? new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY) : new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE))
                .addInterceptor(builder.loginInterceptor)
                .addNetworkInterceptor(new StethoInterceptor())
                .connectTimeout(builder.timeout, TimeUnit.SECONDS)
                .writeTimeout(builder.timeout, TimeUnit.SECONDS)
                .build();

        instance.setBaseUrl(builder.baseUrl);
    }

    public static Disciple getInstance() {
        if (null == instance) {
            throw new RuntimeException("must be init first!");
        }
        return instance;
    }

    public static <T> T create(Class<T> serviceClazz) {
        if (null == retrofit) {
            throw new RuntimeException("must be init first!");
        }
        return retrofit.create(serviceClazz);
    }

    public void addBaseUrl(String key, String baseUrl) {
        multiHostMap.put(key, baseUrl);
    }

    public void removeBaseUrl(String key) {
        multiHostMap.remove(key);
    }

    public Map<String, String> getMultiHostMap() {
        return multiHostMap;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    private void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        initRetrofit(baseUrl);
    }

    private static void initRetrofit(String baseUrl) {
        retrofit = new Retrofit.Builder()
                .client(clientInstance)
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public final static class Builder {

        String baseUrl;

        long timeout;

        Interceptor loginInterceptor;

        boolean isDebug;

        /**
         * 缓存过期时间，默认1天
         */
        int cacheInvalidSec = 60 * 60 * 24;

        /**
         * 缓存空间，默认10m
         */
        long cacheSize = 10 * 1024 * 1024;

        public Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder timeout(long timeout) {
            this.timeout = timeout;
            return this;
        }

        public Builder loginInterceptor(Interceptor loginInterceptor) {
            this.loginInterceptor = loginInterceptor;
            return this;
        }

        public Builder isDebug(boolean isDebug) {
            this.isDebug = isDebug;
            return this;
        }

        public Builder cacheInvalidSec(int cacheInvalidSec) {
            this.cacheInvalidSec = cacheInvalidSec;
            return this;
        }

        public Builder cacheSize(long cacheSize) {
            this.cacheSize = cacheSize;
            return this;
        }
    }
}
