package com.xh.xiaoshuo.network


import com.xh.common.network.HttpResBean
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UserCenterService {

    @POST("/userCenter/feedback")
    fun feedback(@Body content: String): Call<HttpResBean<String>>



}
