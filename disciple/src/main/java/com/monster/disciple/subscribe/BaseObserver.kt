package com.monster.disciple.subscribe

import android.content.Context
import com.monster.disciple.exception.BadNetException
import com.monster.disciple.exception.BusinessException
import com.monster.disciple.exception.LoginException
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import retrofit2.HttpException
import java.lang.ref.WeakReference
import java.net.ConnectException
import java.net.SocketTimeoutException

abstract class BaseObserver<T>(context: Context?) : Observer<T>, IDiscipleException {

    private var mContextRef: WeakReference<Context> = WeakReference<Context>(context)

    protected var disposable: Disposable? = null

    fun getContext(): Context? = mContextRef.get()

    override fun onSubscribe(d: Disposable) {
        disposable = d
    }

    override fun onError(e: Throwable) {
        if (e is LoginException) {//登录态失效观察者取消订阅
            disposable?.dispose()
        } else if (e is SocketTimeoutException || e is ConnectException || e is BadNetException) {
            onBadNetError(e)
        } else if (e is HttpException) {
            onServerError(e)
        } else if (e is BusinessException) {
            onBusinessError(e)
        } else {
            onOtherError(e)
        }

        onComplete()
    }

    override fun onBadNetError(t: Throwable) {

    }

    override fun onBusinessError(ex: BusinessException) {

    }

    override fun onOtherError(t: Throwable) {

    }

    override fun onServerError(t: Throwable) {

    }
}