package com.monster.disciple.subscribe

import android.content.Context
import com.monster.disciple.exception.BadNetException
import com.monster.disciple.exception.BusinessException
import com.monster.disciple.exception.LoginException
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import retrofit2.HttpException
import java.lang.ref.WeakReference
import java.net.ConnectException
import java.net.SocketTimeoutException

abstract class BaseSubscriber<T>(context: Context?) : Subscriber<T>, IDiscipleException {

    private val mContextRef: WeakReference<Context> = WeakReference<Context>(context)
    protected var subscription: Subscription? = null

    fun getContext(): Context? = mContextRef.get()

    override fun onSubscribe(s: Subscription) {
        this.subscription = s
        s.request(Integer.MAX_VALUE.toLong())
    }

    override fun onError(t: Throwable) {
        if (t is LoginException) {//登录态失效观察者取消订阅
            subscription?.cancel()
        } else if (t is SocketTimeoutException || t is ConnectException || t is BadNetException) {
            onBadNetError(t)
        } else if (t is HttpException) {
            onServerError(t)
        } else if (t is BusinessException) {
            onBusinessError(t)
        } else {
            onOtherError(t)
        }

        onComplete()
    }

    override fun onBadNetError(t: Throwable) {

    }

    override fun onServerError(t: Throwable) {

    }

    override fun onBusinessError(ex: BusinessException) {

    }

    override fun onOtherError(t: Throwable) {

    }
}