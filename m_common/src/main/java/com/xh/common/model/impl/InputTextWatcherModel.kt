package com.xh.common.model.impl

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

/**
 * Created by Administrator on 2017/5/9.
 */

open class InputTextWatcherModel(private val et: EditText,
                            private val regex: String = "[\\u4e00-\\u9fa5]") : TextWatcher {

    /**
     * 默认的筛选条件(正则:只能输入中文)
     */



    override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

    }

    override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

    }

    override fun afterTextChanged(editable: Editable) {
        val str = editable.toString()
        val inputStr = clearLimitStr(regex, str)
        et.removeTextChangedListener(this)
        // et.setText方法可能会引起键盘变化,所以用editable.replace来显示内容
        editable.replace(0, editable.length, inputStr.trim { it <= ' ' })
        et.addTextChangedListener(this)

    }

    /**
     * 清除不符合条件的内容

     * @param regex
     * *
     * @return
     */
    private fun clearLimitStr(regex: String, str: String): String {
        return str.replace(regex.toRegex(), "")
    }

}
