package com.xh.common.util

import com.blankj.utilcode.util.EncodeUtils
import com.blankj.utilcode.util.EncryptUtils

object AESUtil {

    private val key = byteArrayOf(6, 89, 71, 40, -21, 43, 66, 15, -127, -59, -22, 42, -66, -117, 112, -102)
    fun encode(str: String): String {
        return String(EncodeUtils.base64Encode(
                EncryptUtils.encryptAES(str.toByteArray(), key, "AES", null)))
    }

    fun decrypt(str: String): String {

        return String(EncryptUtils.decryptAES(EncodeUtils.base64Decode(str), key, "AES", null))
    }


}