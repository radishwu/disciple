package com.monster.disciple

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import com.monster.disciple.response.BaseResponse
import com.monster.disciple.subscribe.ToastObserver
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.button).setOnClickListener({ v ->
            Disciple.create(ApiService::class.java)
                    .getText()
                    .compose(DefaultTransformer<Response<BaseResponse<TestData>>, BaseResponse<TestData>>(this,
                            CacheBuilder.newBuilder()
                                    .withCacheKey("mainid")
                                    .addType(BaseResponse::class.java, TestData::class.java)
                                    .build()
                    ))
                    .map { it.data }
                    .subscribe(object : ToastObserver<TestData>(this) {
                        override fun onComplete() {
                            Log.e("DISCIPLE TEST", "onComplete")
                        }

                        override fun onNext(t: TestData) {
                            Log.e("DISCIPLE TEST", "codeUrl:" + t.codeUrl + ",updateUrl:" + t.updateUrl)
                        }
                    })
        })
    }
}
