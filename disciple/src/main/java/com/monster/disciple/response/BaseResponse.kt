package com.monster.disciple.response

import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(
        @SerializedName("code") var code: Int?,
        @SerializedName("msg") var msg: String?,
        @SerializedName("data") var data: T?)