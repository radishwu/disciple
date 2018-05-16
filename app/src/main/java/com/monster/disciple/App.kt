package com.monster.disciple

import android.app.Application

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        Disciple.init(
                this,
                Disciple.Builder()
                        .timeout(30)
                        .isDebug(true)
                        .cacheInvalidSec(60 * 60 * 24)
                        .cacheSize(10 * 1024 * 1024)
                        .addInterceptor(LoginInterceptor())
                        .baseUrl("http://192.168.199.161/")
        )
    }
}