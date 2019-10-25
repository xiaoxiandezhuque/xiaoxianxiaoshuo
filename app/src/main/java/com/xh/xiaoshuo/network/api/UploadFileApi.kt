package com.xh.xiaoshuo.network.api


import com.xh.common.network.NetworkApi
import com.xh.common.util.Bimp
import com.xh.xiaoshuo.bean.UploadResBean
import com.xh.xiaoshuo.network.UploadFileService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


class UploadFileApi {


    private val mService = NetworkApi.retrofit.create(UploadFileService::class.java)


    suspend fun uploadAvatar(filePath: String): UploadResBean? {
        val call = withContext(Dispatchers.IO) {
            val bitmapStr = Bimp.revitionImageSize(filePath)
            val builder = MultipartBody.Builder()
            builder.setType(MultipartBody.FORM)
            val file = File(bitmapStr)
            val body = RequestBody.create(MediaType.parse("image/*"), file)
            val filename = file.name
            builder.addFormDataPart("file", filename, body)
            mService.uploadAvatar(builder.build())
        }
        return NetworkApi.getData(call)

    }

}