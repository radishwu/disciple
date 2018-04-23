package com.monster.disciple.subscribe;

import android.content.Context;

import com.monster.disciple.exception.BadNetException;
import com.monster.disciple.exception.BusinessException;
import com.monster.disciple.exception.LoginException;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.lang.ref.WeakReference;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

import retrofit2.HttpException;

public abstract class BaseSubscriber<T> implements Subscriber<T>, IDiscipleException {

    private WeakReference<Context> mContextRef;
    protected Subscription subscription;

    public BaseSubscriber(final Context context) {
        mContextRef = new WeakReference<>(context);
    }

    public Context getContext() {
        return mContextRef.get();
    }

    @Override
    public void onSubscribe(Subscription s) {
        this.subscription = s;
        s.request(Integer.MAX_VALUE);
    }

    @Override
    public void onError(Throwable t) {
        if (t instanceof LoginException) {//登录态失效观察者取消订阅
            subscription.cancel();
        } else if (t instanceof SocketTimeoutException || t instanceof ConnectException || t instanceof BadNetException) {
            onBadNetError(t);
        } else if (t instanceof HttpException) {
            onServerError(t);
        } else if (t instanceof BusinessException) {
            onBusinessError((BusinessException) t);
        } else {
            onOtherError(t);
        }

        onComplete();
    }

    @Override
    public void onBadNetError(Throwable t) {

    }

    @Override
    public void onServerError(Throwable t) {

    }

    @Override
    public void onBusinessError(BusinessException ex) {

    }

    @Override
    public void onOtherError(Throwable t) {

    }
}
