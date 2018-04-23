package com.monster.disciple.transformer;

import android.content.Context;

import com.monster.disciple.response.BaseResponse;
import com.monster.disciple.util.Util;

import org.reactivestreams.Publisher;

import java.lang.ref.WeakReference;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public abstract class AbsFlowableErrorCheckTransformer<T extends Response<R>, R extends BaseResponse>
        implements FlowableTransformer<T, R>, IErrorCheckTransformer {

    protected WeakReference<Context> mContextRef;

    public AbsFlowableErrorCheckTransformer(final Context context) {
        mContextRef = new WeakReference<>(context);
    }

    @Override
    public Publisher<R> apply(Flowable<T> upstream) {
        return upstream
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(Util.<T, R>newInstanceFunction(this));
    }
}
