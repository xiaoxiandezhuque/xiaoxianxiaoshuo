package com.xh.common.network

/**
 * Created by Administrator on 2017/6/20.
 */
class LoginExpiredException : RuntimeException()

class ApiException(error: String) : RuntimeException(error)

class KeyErrorException : RuntimeException()