package com.xh.xiaoshuo.network.api

import com.blankj.utilcode.util.AppUtils
import com.xh.common.network.NetworkApi
import com.xh.xiaoshuo.bean.VersionReqBean
import com.xh.xiaoshuo.bean.VersionResBean
import com.xh.xiaoshuo.network.VersionService

class VersionApi {
    private val mService = NetworkApi.retrofit.create(VersionService::class.java)

    suspend fun getVersion(): VersionResBean? {
        return NetworkApi.getData(mService.getVersion(
                VersionReqBean(AppUtils.getAppVersionCode())))
    }

}
