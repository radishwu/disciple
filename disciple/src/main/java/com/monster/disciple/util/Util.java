package com.monster.disciple.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import com.google.gson.Gson;
import com.monster.disciple.entity.CacheEntity;
import com.monster.disciple.exception.BadNetException;
import com.monster.disciple.exception.BusinessException;
import com.monster.disciple.exception.LoginException;
import com.monster.disciple.response.BaseResponse;
import com.monster.disciple.transformer.IErrorCheckTransformer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;

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

    /**
     * 将网络请求数据缓存
     *
     * @param cacheKey 缓存key
     */
    public static <R> Function<R, R> saveCacheFunction(final Context context, final String cacheKey) {
        return new Function<R, R>() {
            @Override
            public R apply(R r) {

                if (checkPermission(context) && null != r) {
                    FileOutputStream fileOutputStream;
                    File file = new File(context.getFilesDir(), cacheKey);
                    Gson gson = new Gson();
                    String cache = gson.toJson(r);

                    try {
                        fileOutputStream = new FileOutputStream(file);
                        fileOutputStream.write(cache.getBytes());
                        fileOutputStream.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                return r;
            }
        };
    }

    public static <R extends BaseResponse> Function<String, CacheEntity<R>> getCacheFunction(final Context context, final Type type) {
        return new Function<String, CacheEntity<R>>() {
            @Override
            public CacheEntity<R> apply(String cacheKey) {

                R result = null;

                if (checkPermission(context)) {
                    File file = new File(context.getFilesDir(), cacheKey);
                    try {
                        FileInputStream fileInputStream = new FileInputStream(file);
                        byte[] bytes = new byte[fileInputStream.available()];
                        fileInputStream.read(bytes);
                        fileInputStream.close();
                        String str = new String(bytes);
                        Gson gson = new Gson();
                        result = gson.fromJson(str, type);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                return new CacheEntity<>(result);
            }
        };
    }

    private static boolean checkPermission(final Context context) {
        return null != context && ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }
}
