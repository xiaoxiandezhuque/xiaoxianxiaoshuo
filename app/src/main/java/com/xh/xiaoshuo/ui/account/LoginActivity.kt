package com.xh.xiaoshuo.ui.account

import android.content.Intent
import android.text.Editable
import com.blankj.utilcode.util.RegexUtils
import com.blankj.utilcode.util.SPUtils

import com.xh.common.base.BaseActivity
import com.xh.common.ktextended.addTextChangedListener
import com.xh.common.model.impl.InputTextWatcherModel
import com.xh.xiaoshuo.BuildConfig
import com.xh.xiaoshuo.R
import com.xh.xiaoshuo.bean.UserReqBean
import com.xh.xiaoshuo.constant.SPConstant
import com.xh.xiaoshuo.network.api.UserApi
import com.xh.xiaoshuo.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.launch

class LoginActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_login
    }


    override fun initView() {
        title_view.hideRight()
        title_view.setOnClickRightListener {
            et_phone.setText("")
            et_password.setText("")
        }
        et_phone.addTextChangedListener {
            btn_login.isEnabled = isbtn_loginEnabled()
            isShowRightImage()
        }
        et_password.addTextChangedListener(object : InputTextWatcherModel(et_password) {
            override fun afterTextChanged(editable: Editable) {
                super.afterTextChanged(editable)
                btn_login.isEnabled = isbtn_loginEnabled()
                isShowRightImage()
            }
        })
        btn_register.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        btn_login.setOnClickListener {
            mMainScope.launch {
                mLoadingDialog.show()
                val data = UserApi().login(UserReqBean(et_phone.text.toString(), et_password.text.toString()))

                if (data != null) {
                    SPUtils.getInstance().put(SPConstant.TOKEN, data.token ?: "")
                    SPUtils.getInstance().put(SPConstant.USER_ID, data.id)
                    SPUtils.getInstance().put(SPConstant.USER_NAME, data.name ?: "")
                    SPUtils.getInstance().put(SPConstant.USER_PHONE, data.phone ?: "")
                    SPUtils.getInstance().put(SPConstant.USER_INVITATION_CODE, data.invitationCode
                            ?: "")
                    SPUtils.getInstance().put(SPConstant.USER_INVITATION_NUM, data.invitationNum)
                    SPUtils.getInstance().put(SPConstant.USER_AVATAR, data.avatar ?: "")
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                }
                mLoadingDialog.dismiss()
            }

        }
        tv_forget_password.setOnClickListener {
            startActivity(Intent(this@LoginActivity, ForgetPasswordActivity::class.java))
        }


        if (BuildConfig.DEBUG) {
            et_phone.setText("18444444445")
            et_password.setText("123456")

        }


    }

    private fun isShowRightImage() {
        if (et_password.text.toString().length > 0 || et_phone.text.toString().length > 0) {
            title_view.showRrightImage()
        } else {
            title_view.hideRight()
        }
    }

    private fun isbtn_loginEnabled(): Boolean {
        return et_password.text.toString().length >= 6 && isPhone()
    }

    private fun isPhone(): Boolean {
        return RegexUtils.isMobileExact(et_phone.text.toString())
    }
}