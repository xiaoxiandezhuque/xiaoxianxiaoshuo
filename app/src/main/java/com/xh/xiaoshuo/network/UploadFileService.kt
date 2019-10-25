package com.xh.xiaoshuo.network


import com.xh.common.network.HttpResBean
import com.xh.xiaoshuo.bean.UploadResBean

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UploadFileService {


//    @POST("/multipleUpload")
//    fun uploads(@Body body: MultipartBody): Observable<HttpResBean<String>>

    @POST("/upload/avatar")
    fun uploadAvatar(@Body body: MultipartBody): Call<HttpResBean<UploadResBean>>
}