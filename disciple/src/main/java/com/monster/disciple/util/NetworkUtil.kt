package com.monster.disciple.util

import android.content.Context
import android.net.ConnectivityManager

fun isAvailable(context: Context?): Boolean {
    if (context != null) {
        val connectivityManager = context
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val mNetworkInfo = connectivityManager.activeNetworkInfo
        if (mNetworkInfo != null) {
            return mNetworkInfo.isAvailable
        }
    }
    return false
}