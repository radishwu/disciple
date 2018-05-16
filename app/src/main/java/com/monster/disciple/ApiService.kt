package com.monster.disciple

import com.monster.disciple.response.BaseResponse
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("test.php")
    fun getText(): Observable<Response<BaseResponse<TestData>>>
}