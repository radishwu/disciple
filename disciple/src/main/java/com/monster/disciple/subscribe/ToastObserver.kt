package com.monster.disciple.subscribe

import android.content.Context
import android.widget.Toast
import com.monster.disciple.R
import com.monster.disciple.exception.BusinessException

abstract class ToastObserver<T>(context: Context?) : BaseObserver<T>(context) {

    override fun onOtherError(t: Throwable) {
        if (null != t.message) {
            showErrorToast(t.message!!)
        }
    }

    override fun onBusinessError(ex: BusinessException) {
        if (null != ex.msg) {
            showErrorToast(ex.msg!!)
        }
    }

    override fun onBadNetError(t: Throwable) {
        showErrorToast(R.string.disciple_http_bad_net)
    }

    override fun onServerError(t: Throwable) {
        showErrorToast(R.string.disciple_http_request_failure)
    }

    private fun showErrorToast(resId: Int) {
        val context = getContext() ?: return
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show()
    }

    private fun showErrorToast(msg: String) {
        val context = getContext() ?: return
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
}