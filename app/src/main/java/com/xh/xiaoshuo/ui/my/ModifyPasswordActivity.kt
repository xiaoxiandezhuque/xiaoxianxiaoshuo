package com.xh.xiaoshuo.ui.my

import com.qy.reader.common.utils.StringUtils
import com.xh.common.base.BaseActivity
import com.xh.common.ktextended.addTextChangedListener
import com.xh.common.ktextended.showToast
import com.xh.xiaoshuo.App
import com.xh.xiaoshuo.R
import com.xh.xiaoshuo.bean.ModifyPasswordReqBean
import com.xh.xiaoshuo.network.api.UserApi
import kotlinx.android.synthetic.main.activity_modify_password.*
import kotlinx.coroutines.launch


class ModifyPasswordActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_modify_password
    }

    override fun initView() {

        et_old_password.addTextChangedListener {
            btn_submit.isEnabled = btnIsUse()
        }
        et_new_password.addTextChangedListener {
            btn_submit.isEnabled = btnIsUse()
        }
        et_new_password_agagin.addTextChangedListener {
            btn_submit.isEnabled = btnIsUse()
        }
        btn_submit.setOnClickListener {
            submit()
        }

    }

    private fun submit() {
        if (et_new_password.text.toString() != et_new_password_agagin.text.toString()) {
            "2次输入的新密码不一致".showToast()
            return
        }
        mMainScope.launch {
            mLoadingDialog.show()
            val data = UserApi().modifyPassword(ModifyPasswordReqBean(et_old_password.text.toString(),
                    et_new_password.text.toString()))
            if (!StringUtils.isEmpty(data)) {
                App.toLogin()
                finish()
            }
            mLoadingDialog.dismiss()
        }
    }

    private fun btnIsUse(): Boolean {

        return et_old_password.text.toString().length >= 6 &&
                et_new_password.text.toString().length >= 6 &&
                et_new_password_agagin.text.toString().length >= 6
    }


}