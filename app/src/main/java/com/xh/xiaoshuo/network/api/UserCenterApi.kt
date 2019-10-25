package com.xh.xiaoshuo.network.api

import com.xh.common.network.NetworkApi
import com.xh.xiaoshuo.network.UserCenterService

class UserCenterApi {
    private val mService = NetworkApi.retrofit.create(UserCenterService::class.java)


    suspend fun feedback(content: String): String? {
        return NetworkApi.getData(mService.feedback(content))
    }

}
