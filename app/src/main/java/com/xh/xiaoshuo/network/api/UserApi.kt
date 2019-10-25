package com.xh.xiaoshuo.network.api

import com.xh.common.network.NetworkApi
import com.xh.xiaoshuo.bean.*
import com.xh.xiaoshuo.network.UserService

class UserApi {
    private val mService = NetworkApi.retrofit.create(UserService::class.java)


    suspend fun login(userReqBean: UserReqBean): UserResBean? {
        return NetworkApi.getData(mService.login(userReqBean))
    }

    suspend fun register(bean: RegisterReqBean):String? {
        return  NetworkApi.getData(mService.register(bean))
    }

    suspend fun getVfCode(bean: VfCodeReqBean) :String?{
        return  NetworkApi.getData(mService.getVfCode(bean))
    }

    suspend fun modifyPassword(bean: ModifyPasswordReqBean) :String? {
        return  NetworkApi.getData(mService.modifyPassword(bean))
    }

    suspend fun forgetPassword(bean: ForgetPasswordReqBean) :String? {
        return  NetworkApi.getData(mService.forgetPassword(bean))
    }

    suspend fun updateUesr(bean: UpdateUserReqBean)  :String?{
        return  NetworkApi.getData(mService.updateUesr(bean))
    }
}
