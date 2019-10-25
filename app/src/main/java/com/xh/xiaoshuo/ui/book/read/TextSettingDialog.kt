package com.xh.xiaoshuo.ui.book.read

import android.content.Context

import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import com.qy.reader.common.widgets.reader.annotation.SlideMode
import com.xh.xiaoshuo.R
import com.xh.xiaoshuo.bean.TextBean


class TextSettingDialog(private val mContext: Context, private val textBean: TextBean) {


    private var mDialog: AlertDialog? = null

    private var onSizeChangeListener: ((bool: Boolean, left: View, right: View) -> Unit)? = null
    private var onSpacingChangeListener: ((bool: Boolean, left: View, right: View) -> Unit)? = null
    private var onSlideChangeListener: ((mode: Int, left: View, right: View) -> Unit)? = null
    private var onBgChangeListener: ((state: Int) -> Unit)? = null

    fun show() {
        if (mDialog == null) {
            mDialog = AlertDialog.Builder(mContext).create()
            mDialog!!.show()
            val window = mDialog!!.window ?: return
            window.setBackgroundDrawableResource(R.drawable.shape_transparent)
            val view = window.layoutInflater.inflate(R.layout.dialog_text_setting, null)
            window.setContentView(view)
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
            window.setGravity(Gravity.BOTTOM)

            val btn_enlarge_size = view.findViewById<Button>(R.id.btn_enlarge_size)
            val btn_reduce_size = view.findViewById<Button>(R.id.btn_reduce_size)

            val btn_spacing_enlarge = view.findViewById<Button>(R.id.btn_spacing_enlarge)
            val btn_spacing_reduce = view.findViewById<Button>(R.id.btn_spacing_reduce)

            val btn_follow = view.findViewById<Button>(R.id.btn_follow)
            val btn_cover = view.findViewById<Button>(R.id.btn_cover)

            val btn_white = view.findViewById<Button>(R.id.btn_white)
            val btn_brown = view.findViewById<Button>(R.id.btn_brown)
            val btn_red = view.findViewById<Button>(R.id.btn_red)
            val btn_green = view.findViewById<Button>(R.id.btn_green)
            val btn_black = view.findViewById<Button>(R.id.btn_black)

            val btnColor = arrayOf(btn_white, btn_brown, btn_red, btn_green, btn_black)

            btn_enlarge_size.setOnClickListener {
                onSizeChangeListener?.invoke(true, btn_reduce_size, btn_enlarge_size)
            }
            btn_reduce_size.setOnClickListener {
                onSizeChangeListener?.invoke(false, btn_reduce_size, btn_enlarge_size)
            }
            btn_spacing_enlarge.setOnClickListener {
                onSpacingChangeListener?.invoke(true, btn_spacing_reduce, btn_spacing_enlarge)
            }
            btn_spacing_reduce.setOnClickListener {
                onSpacingChangeListener?.invoke(false, btn_spacing_reduce, btn_spacing_enlarge)
            }
            btn_follow.setOnClickListener {
                onSlideChangeListener?.invoke(SlideMode.FOLLOW, btn_follow, btn_cover)
            }
            btn_cover.setOnClickListener {
                onSlideChangeListener?.invoke(SlideMode.OVERLAP, btn_follow, btn_cover)
            }
            for (i in btnColor.indices) {
                if (textBean.color == i) {
                    btnColor[i].isEnabled = false
                }
                btnColor[i].setTag(i)
                btnColor[i].setOnClickListener {
                    val i = it.getTag() as Int
                    onBgChangeListener?.invoke(i)
                    it.isEnabled = false
//                    设置当前选中不能用
                    for (j in btnColor.indices) {
                        if (i == j) {
                            btnColor[j].isEnabled = false
                        } else {
                            btnColor[j].isEnabled = true
                        }
                    }
                }

            }

            if (textBean.size <= 10) {
                btn_reduce_size.isEnabled = false
            } else if (textBean.size >= 32) {
                btn_enlarge_size.isEnabled = false
            }

            if (textBean.spacing >= 18) {
                btn_spacing_enlarge.isEnabled = false
            } else if (textBean.spacing <= 4) {
                btn_spacing_reduce.isEnabled = false
            }
            if (textBean.mode == SlideMode.FOLLOW) {
                btn_follow.isEnabled = false
            } else {
                btn_cover.isEnabled = false
            }


        } else if (!mDialog!!.isShowing) {
            mDialog!!.show()
        }
    }

    fun addSizeChangeListener(onSizeChangeListener: ((bool: Boolean, left: View, right: View) -> Unit)) {
        this.onSizeChangeListener = onSizeChangeListener
    }

    fun addSpacingChangeListener(onSpacingChangeListener: ((bool: Boolean, left: View, right: View) -> Unit)) {
        this.onSpacingChangeListener = onSpacingChangeListener
    }

    fun addSlideChangeListener(onSlideChangeListener: ((mode: Int, left: View, right: View) -> Unit)) {
        this.onSlideChangeListener = onSlideChangeListener
    }

    fun addBgChangeListener(onBgChangeListener: ((state: Int) -> Unit)) {
        this.onBgChangeListener = onBgChangeListener
    }

}


