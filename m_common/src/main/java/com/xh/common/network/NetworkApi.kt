package com.xh.common.network

import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.SPUtils
import com.xh.common.config.BaseConfig
import com.xh.common.ktextended.showToast
import com.xh.common.network.tfdata.GsonConverterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import java.io.File
import java.util.concurrent.TimeUnit


object NetworkApi {

    const val BASE_URL = "http://192.168.10.58:8080/"
    const val DISCLAIMER_URL = BASE_URL + "apk/disclaimer.html"

    private val HTTP_TIME_OUT = 10L
    private val cacheSize = (1024 * 1024 * 20).toLong()// 缓存文件最大限制大小20M
    private val cacheDirectory = BaseConfig.CACHE_PATH + "/http" // 设置缓存文件路径

    val retrofit: Retrofit
    var mLoginTimeOut: (() -> Unit)? = null


    init {
        val okHttpClient = OkHttpClient()
                .newBuilder()
                .addNetworkInterceptor(HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY))
                .addInterceptor { chain ->
                    var request = chain?.request()!!
                    request = request.newBuilder()
                            .header("Authorization", SPUtils.getInstance().getString("token", ""))
                            .build()
                    var originalResponse = chain?.proceed(request)!!
                    return@addInterceptor originalResponse
                }
                .connectTimeout(HTTP_TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(HTTP_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(HTTP_TIME_OUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .cache(Cache(File(cacheDirectory), cacheSize))
                .build()


        retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }

    suspend inline fun <T> getData(call: Call<HttpResBean<T>>,
                                   crossinline errorListener: (e: Throwable?) -> Unit = {},
                                   isShowErrorHint: Boolean = true): T? {
        var throwable: Throwable? = null
        val result = withContext(Dispatchers.IO) {
            try {
                val bean = call.execute().body()!!
                when (bean.code) {
                    1 -> bean.data ?: bean.data as? T
                    ?: throw ApiException("报错")
                    2 -> throw LoginExpiredException()
                    else -> throw ApiException("报错")
                }
                return@withContext bean.data!!
            } catch (e: Throwable) {
                throwable = e
            }
            return@withContext null
        }
        if (throwable != null) {
            if (isShowErrorHint) {
                val hint = getErrorHintHelper(throwable!!)
                hint.showToast()
                LogUtils.e(hint)
            }
            if (throwable is LoginExpiredException) {
                mLoginTimeOut?.invoke()
            } else {
                errorListener.invoke(throwable)
            }
        }
        return result
    }

    suspend inline fun <T> getData1(call: Call<HttpWeatherBean<T>>,
                                    crossinline errorListener: (e: Throwable?) -> Unit,
                                    isShowErrorHint: Boolean = true): T? {
        var throwable: Throwable? = null
        val result = withContext(Dispatchers.IO) {
            try {
                val httpResBean = call.execute().body()!!
                when (httpResBean.result) {
                    0 -> httpResBean.content ?: httpResBean.content as? T
                    ?: throw ApiException("报错")
                    2 -> throw LoginExpiredException()
                    else -> throw ApiException("报错")
                }
                return@withContext httpResBean.content!!
            } catch (e: Throwable) {
                throwable = e
            }
            return@withContext null
        }
        if (throwable != null) {
            if (isShowErrorHint) {
                val hint = getErrorHintHelper(throwable!!)
                hint.showToast()
                LogUtils.e(hint)
            }
            if (throwable is LoginExpiredException) {
                mLoginTimeOut?.invoke()
            } else {
                errorListener.invoke(throwable)
            }
        }
        return result
    }

}


