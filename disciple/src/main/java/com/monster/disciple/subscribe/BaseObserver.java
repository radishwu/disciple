package com.monster.disciple.subscribe;

import android.content.Context;

import com.monster.disciple.exception.BadNetException;
import com.monster.disciple.exception.BusinessException;
import com.monster.disciple.exception.LoginException;

import java.lang.ref.WeakReference;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;

public abstract class BaseObserver<T> implements Observer<T>, IDiscipleException {

    private WeakReference<Context> mContextRef;
    protected Disposable disposable;

    public BaseObserver(final Context context) {
        mContextRef = new WeakReference<>(context);
    }

    public Context getContext() {
        return mContextRef.get();
    }

    @Override
    public void onSubscribe(Disposable d) {
        this.disposable = d;
    }

    @Override
    public void onError(Throwable t) {

        if (t instanceof LoginException) {//登录态失效观察者取消订阅
            disposable.dispose();
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
    public void onServerError(Throwable t) {

    }

    @Override
    public void onBadNetError(Throwable t) {

    }

    @Override
    public void onOtherError(Throwable t) {

    }

    @Override
    public void onBusinessError(BusinessException ex) {

    }
}
