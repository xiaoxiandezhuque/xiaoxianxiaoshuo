package com.xh.xiaoshuo.network


import com.xh.common.network.HttpResBean
import com.xh.xiaoshuo.bean.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {

    @POST("/user/login")
    fun login(@Body userReqBean: UserReqBean): Call<HttpResBean<UserResBean>>

    @POST("/user/getVfCode")
    fun getVfCode(@Body vfCodeReqBean: VfCodeReqBean):  Call<HttpResBean<String>>

    @POST("/user/register")
    fun register(@Body registerReqBean: RegisterReqBean):  Call<HttpResBean<String>>

    @POST("/user/modifyPassword")
    fun modifyPassword(@Body modifyPasswordReqBean: ModifyPasswordReqBean):  Call<HttpResBean<String>>

    @POST("/user/forgetPassword")
    fun forgetPassword(@Body forgetPasswordReqBean: ForgetPasswordReqBean):  Call<HttpResBean<String>>

    @POST("/user/updateUesr")
    fun updateUesr(@Body updateUserReqBean: UpdateUserReqBean):  Call<HttpResBean<String>>

}
