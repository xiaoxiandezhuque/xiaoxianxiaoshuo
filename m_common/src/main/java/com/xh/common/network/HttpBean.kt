package com.xh.common.network

/**
 * Created by Administrator on 2017/6/13.
 */


data class HttpWeatherBean<T>(var result: Int = 0,
                              var content: T? = null)

data class HttpResBean<T>(var code: Int = 0,
                          var data: T? = null,
                          var msg: String = "")
//
//data class HttpBeanGson(var code: Int = 0,
//                        var msg: String = "",
//                        var isSuccess: Boolean = false)
//
//data class AESResponse(var success: Boolean = false,
//                       var sData: String = "")
//
//data class RequestJsonBean(var url: String = "",
//                           var json: String = "")
//
//data class ResultJsonBean(var url: String = "",
//                          var reqJson: String = "",
//                          var resJson: String = "")




