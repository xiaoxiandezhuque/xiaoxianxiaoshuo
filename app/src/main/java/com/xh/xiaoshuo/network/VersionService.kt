package com.xh.xiaoshuo.network


import com.xh.common.network.HttpResBean
import com.xh.xiaoshuo.bean.VersionReqBean
import com.xh.xiaoshuo.bean.VersionResBean
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface VersionService {

    @POST("/version/getVersion")
    fun getVersion(@Body versionReqBean: VersionReqBean): Call<HttpResBean<VersionResBean>>


}
