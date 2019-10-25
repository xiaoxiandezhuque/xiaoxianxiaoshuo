package com.xh.xiaoshuo.mode

import android.os.Handler
import android.os.Looper
import android.os.Message

import cn.smssdk.EventHandler
import cn.smssdk.SMSSDK

class MOBCode(private val sendCodeListener: ((isSuccess: Boolean) -> Unit),
              private val vfCodeListener: ((isSuccess: Boolean) -> Unit)) {


    private val mainHandler = Handler(Looper.getMainLooper(), {
        val event = it.arg1
        val result = it.arg2
        val data = it.obj
        if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
            if (result == SMSSDK.RESULT_COMPLETE) {
                sendCodeListener.invoke(true)
            } else {
                sendCodeListener.invoke(false)
                (data as Throwable).printStackTrace()
            }
        } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
            if (result == SMSSDK.RESULT_COMPLETE) {
                vfCodeListener.invoke(true)
            } else {
                vfCodeListener.invoke(false)
                (data as Throwable).printStackTrace()
            }
        }
        false
    })

    private val eventHandler = object : EventHandler() {
        override fun afterEvent(event: Int, result: Int, data: Any?) {
            // afterEvent会在子线程被调用，因此如果后续有UI相关操作，需要将数据发送到UI线程
            val msg = Message()
            msg.arg1 = event
            msg.arg2 = result
            msg.obj = data
            mainHandler.sendMessage(msg)
        }
    }


    init {
        SMSSDK.registerEventHandler(eventHandler)
    }

    fun getCode(phone: String) {
        // 请求验证码，其中country表示国家代码，如“86”；phone表示手机号码，如“13800138000”
        SMSSDK.getVerificationCode("86", phone)

    }

    fun vfCode(phone: String, code: String) {
        SMSSDK.submitVerificationCode("86", phone, code)
    }


    fun onDestory(){
        mainHandler.removeCallbacksAndMessages(null)
        SMSSDK.unregisterEventHandler(eventHandler);
    }
}
