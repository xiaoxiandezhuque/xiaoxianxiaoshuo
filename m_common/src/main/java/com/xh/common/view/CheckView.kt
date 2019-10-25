package com.xh.common.view

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.xh.common.R


/**
 * Created by Administrator on 2017/5/9.
 */

class CheckView(context: Context, attrs: AttributeSet?) : AppCompatImageView(context, attrs) {


    var isCheck: Boolean = false
        set(check) {
            field = check
            val imgId = if (check) imgCheck else img
            setImageResource(imgId)
        }
    private var onCheckChangeListener: OnCheckChangeListener? = null

    var img: Int
    var imgCheck: Int

    init {
        val array = context.obtainStyledAttributes(attrs, R.styleable.CheckView)
        img = array.getResourceId(R.styleable.CheckView_imgNormal, 0)
        imgCheck = array.getResourceId(R.styleable.CheckView_imgCheck, 0)
        array.recycle()//回收

        setImageResource(img)
        setOnClickListener {
            isCheck = !this.isCheck
            if (onCheckChangeListener != null) {
                onCheckChangeListener!!.onChange(this.isCheck)
            }
        }
    }


    fun setOnCheckChangeListener(onCheckChangeListener: OnCheckChangeListener) {
        this.onCheckChangeListener = onCheckChangeListener
    }

    interface OnCheckChangeListener {
        fun onChange(bool: Boolean)
    }
}
