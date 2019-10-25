package com.xh.xiaoshuo.ui.my

import com.qy.reader.common.utils.StringUtils
import com.xh.common.base.BaseActivity
import com.xh.common.ktextended.addTextChangedListener
import com.xh.common.ktextended.showToast
import com.xh.xiaoshuo.R
import com.xh.xiaoshuo.network.api.UserCenterApi
import kotlinx.android.synthetic.main.activity_feedback.*
import kotlinx.coroutines.launch

//意见反馈界面
class FeedbackActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_feedback
    }

    override fun initView() {
        et_content.addTextChangedListener({
            tv_num.setText("${it?.toString()?.length}/500")
            if (it?.toString()?.length == 0) {
                btn_submit.isEnabled = false
            } else {
                btn_submit.isEnabled = true
            }
        })
        btn_submit.setOnClickListener {
            mMainScope.launch {
                mLoadingDialog.show()
                val content = et_content.text.toString()
                val data = UserCenterApi().feedback(content)
                if (!StringUtils.isEmpty(data)) {
                    "提交成功，感谢您提的建议".showToast()
                    finish()
                }
                mLoadingDialog.dismiss()
            }


        }
    }

}