package com.xh.xiaoshuo.ui

import android.app.Activity
import android.content.Intent
import com.blankj.utilcode.util.KeyboardUtils
import com.xh.common.base.BaseActivity
import com.xh.common.ktextended.isEmptyForMy
import com.xh.common.ktextended.showToast
import com.xh.xiaoshuo.R
import kotlinx.android.synthetic.main.activity_input.*


class InputActivity : BaseActivity() {
    companion object {
        private const val EXTRE_TITLE = "extre_title"
        private const val EXTRE_CONTENT = "extre_content"
        const val EXTRE_ET_STRING = "extre_et_string"
        const val INTENT_RESULT = 1324


        fun intentToThis(activity: Activity, title: String, content: String, requestCode: Int) {

            activity.startActivityForResult(Intent(activity, InputActivity::class.java)
                    .putExtra(EXTRE_TITLE, title)
                    .putExtra(EXTRE_CONTENT, content), requestCode)
        }
    }


    override fun getLayoutId(): Int {
        return R.layout.activity_input
    }

    override fun initView() {
        val title = intent.getStringExtra(EXTRE_TITLE)
        val content = intent.getStringExtra(EXTRE_CONTENT)
        et_content.setText(content)
        title_view.setTitleText(title)
        title_view.setOnClickRightListener {
            KeyboardUtils.hideSoftInput(this)
            val content = et_content.text.toString();
            if (content.isEmptyForMy()) {
                "您还没有输入任何内容".showToast()
                return@setOnClickRightListener
            }
            val intent = Intent()
            intent.putExtra(EXTRE_ET_STRING, content)
            setResult(INTENT_RESULT, intent)
            finish()
        }
        iv_cancel.setOnClickListener {
            et_content.setText("")
        }
    }


}