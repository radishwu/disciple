package com.monster.disciple.transformer

import android.content.Context
import android.text.TextUtils
import com.monster.disciple.CacheBuilder
import com.monster.disciple.response.BaseResponse
import com.monster.disciple.util.getCacheFunction
import com.monster.disciple.util.newInstanceFunction
import com.monster.disciple.util.saveCacheFunction
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import java.lang.ref.WeakReference

abstract class AbsErrorCheckTransformer<T : Response<R>, R : BaseResponse<*>>(context: Context?) : ObservableTransformer<T, R>, IErrorCheckTransformer {

    private var mContextRef: WeakReference<Context>? = WeakReference<Context>(context)

    private var mCacheBuilder: CacheBuilder? = null

    constructor(context: Context?, cacheBuilder: CacheBuilder?) : this(context) {
        mCacheBuilder = cacheBuilder
    }

    override fun apply(upstream: Observable<T>): ObservableSource<R> {
        return if (mCacheBuilder == null || TextUtils.isEmpty(mCacheBuilder?.cacheKey)) {
            upstream
                    .map(newInstanceFunction<T,R>(this))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        } else {
            upstream
                    .map(newInstanceFunction<T,R>(this))
                    .map(saveCacheFunction<R>(mContextRef?.get(), mCacheBuilder?.cacheKey))
                    .startWith(
                            Observable
                                    .just<String>(mCacheBuilder?.cacheKey)
                                    .map(getCacheFunction<R>(mContextRef?.get(), mCacheBuilder?.type))
                                    .filter { rCacheEntity -> !rCacheEntity.isNil() }
                                    .map { rCacheEntity -> rCacheEntity.response })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        }
    }
}

