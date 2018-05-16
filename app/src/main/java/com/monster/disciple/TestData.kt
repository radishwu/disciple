package com.monster.disciple

import com.google.gson.annotations.SerializedName

data class TestData(@SerializedName("code_url") var codeUrl: String?, @SerializedName("update_url") var updateUrl: String?)