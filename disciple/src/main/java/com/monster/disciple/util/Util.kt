package com.monster.disciple.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat
import com.google.gson.Gson
import com.monster.disciple.entity.CacheEntity
import com.monster.disciple.exception.BadNetException
import com.monster.disciple.exception.BusinessException
import com.monster.disciple.exception.LoginException
import com.monster.disciple.response.BaseResponse
import com.monster.disciple.transformer.IErrorCheckTransformer
import io.reactivex.functions.Function
import retrofit2.Response
import java.io.*
import java.lang.reflect.Type

fun <T : Response<R>, R : BaseResponse<*>> newInstanceFunction(errorCheck: IErrorCheckTransformer): Function<T, R> {
    return Function { t ->
        if (!t.isSuccessful || null == t.body()) {
            throw BadNetException(t.code(), "")
        } else if (errorCheck.isLoginInvalid(t.body()!!.code, t.body()!!.msg)) {
            errorCheck.loginInvalid()
            throw LoginException(t.body()!!.code, t.body()!!.msg)
        } else if (errorCheck.isErrorResponse(t.body()!!.code, t.body()!!.msg)) {
            throw BusinessException(t.body()!!.code, t.body()!!.msg)
        }

        t.body()
    }
}

/**
 * 将网络请求数据缓存
 *
 * @param cacheKey 缓存key
 */
fun <R> saveCacheFunction(context: Context?, cacheKey: String?): Function<R, R> {
    return Function { r ->

        if (checkPermission(context) && null != r) {
            val fileOutputStream: FileOutputStream
            val file = File(context?.filesDir, cacheKey)
            val gson = Gson()
            val cache = gson.toJson(r)

            try {
                fileOutputStream = FileOutputStream(file)
                fileOutputStream.write(cache.toByteArray())
                fileOutputStream.close()
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        r
    }
}

fun <R : BaseResponse<*>> getCacheFunction(context: Context?, type: Type?): Function<String, CacheEntity<R>> {
    return Function { cacheKey ->
        var result: R? = null

        if (checkPermission(context)) {
            val file = File(context?.filesDir, cacheKey)
            try {
                val fileInputStream = FileInputStream(file)
                val bytes = ByteArray(fileInputStream.available())
                fileInputStream.read(bytes)
                fileInputStream.close()
                val str = String(bytes)
                val gson = Gson()
                result = gson.fromJson<R>(str, type)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        CacheEntity(result)
    }
}

private fun checkPermission(context: Context?): Boolean {
    return null != context && ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
}