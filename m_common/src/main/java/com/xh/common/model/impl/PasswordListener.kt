package com.xh.common.model.impl

import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import com.xh.common.R

/**
 * Created by Administrator on 2017/7/5.
 */
class PasswordListener(vararg var edits: EditText) : View.OnClickListener {

    private var isShowPassword = false

    override fun onClick(v: View?) {
        for (edit in edits) {
            val select = edit.selectionStart
            if (isShowPassword) {
                (v as ImageView).setImageResource(R.drawable.icon_input_pw_hide)
                edit.setInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)
            } else {
                (v as ImageView).setImageResource(R.drawable.icon_input_pw_show)
                edit.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD)
            }
            edit.setSelection(select)
        }
        isShowPassword = !isShowPassword
    }
}