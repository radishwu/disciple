package com.monster.disciple.subscribe;

import android.content.Context;
import android.widget.Toast;

import com.monster.disciple.R;
import com.monster.disciple.exception.BusinessException;

public abstract class ToastObserver<T> extends BaseObserver<T> {

    public ToastObserver(Context context) {
        super(context);
    }

    @Override
    public void onOtherError(Throwable t) {
        showErrorToast(t.getMessage());
    }

    @Override
    public void onBusinessError(BusinessException ex) {
        showErrorToast(ex.getMsg());
    }

    @Override
    public void onBadNetError(Throwable t) {
        showErrorToast(R.string.disciple_http_bad_net);
    }

    @Override
    public void onServerError(Throwable t) {
        showErrorToast(R.string.disciple_http_request_failure);
    }

    private void showErrorToast(int resId) {
        Context context = getContext();
        if (null == context) return;
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
    }

    private void showErrorToast(String msg) {
        Context context = getContext();
        if (null == context) return;
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
