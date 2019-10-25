package com.xh.common.ktextended

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.ToastUtils
import com.xh.common.R
import java.math.BigDecimal


inline fun EditText.text(): String {
    return this.text.toString()
}

inline fun String.isEmptyForMy(): Boolean {
    return StringUtils.isTrimEmpty(this)
}

inline fun String.showToast() {
    val view = ToastUtils.showCustomLong(R.layout.view_toast)
    view.findViewById<TextView>(R.id.tv_msg).setText(this)

}

inline fun View.show() {
    this.visibility = View.VISIBLE
}

inline fun View.hide() {
    this.visibility = View.GONE
}

fun TextView.addTextChangedListener(listener: (s: String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            listener.invoke(s?.toString() ?: "")
        }

    })
}


inline fun BigDecimal.toRoundTwoDouble(): Double {
    return this.setScale(2, BigDecimal.ROUND_HALF_UP).toDouble()
}

inline fun BigDecimal.toRoundTwoString(): String {
    return this.setScale(2, BigDecimal.ROUND_HALF_UP).toString()
}

inline fun BigDecimal.toRoundTwo(): BigDecimal {
    return this.setScale(2, BigDecimal.ROUND_HALF_UP)
}


inline fun BigDecimal.toDownTwoDouble(): Double {
    return this.setScale(2, BigDecimal.ROUND_DOWN).toDouble()
}

inline fun BigDecimal.toDownTwoString(): String {
    return this.setScale(2, BigDecimal.ROUND_DOWN).toString()
}

inline fun BigDecimal.toDownTwo(): BigDecimal {
    return this.setScale(2, BigDecimal.ROUND_DOWN)
}


inline fun TextView.setDrawableLeft(drawableId: Int) {
    val drawable = this.context.getResources().getDrawable(drawableId)
    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight())
    this.setCompoundDrawables(drawable, null, null, null)
}
