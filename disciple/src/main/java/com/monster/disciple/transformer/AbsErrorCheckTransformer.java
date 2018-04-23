package com.monster.disciple.transformer;

import android.content.Context;

import com.monster.disciple.response.BaseResponse;
import com.monster.disciple.util.Util;

import java.lang.ref.WeakReference;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public abstract class AbsErrorCheckTransformer<T extends Response<R>, R extends BaseResponse>
        implements ObservableTransformer<T, R>, IErrorCheckTransformer {

    protected WeakReference<Context> mContextRef;

    public AbsErrorCheckTransformer(final Context context) {
        mContextRef = new WeakReference<>(context);
    }

    @Override
    public ObservableSource<R> apply(Observable<T> upstream) {
        return upstream
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(Util.<T, R>newInstanceFunction(this));
    }
}
