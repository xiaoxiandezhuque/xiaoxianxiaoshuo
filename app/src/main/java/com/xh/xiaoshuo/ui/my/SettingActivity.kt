package com.xh.xiaoshuo.ui.my

import android.content.Intent
import androidx.appcompat.app.AlertDialog
import com.blankj.utilcode.util.SPUtils
import com.xh.common.base.BaseActivity
import com.xh.common.util.GlideCacheUtil
import com.xh.xiaoshuo.App
import com.xh.xiaoshuo.R
import com.xh.xiaoshuo.constant.SPConstant
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class SettingActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_setting
    }

    override fun initView() {
        tv_phone.setText(SPUtils.getInstance().getString(SPConstant.USER_PHONE))

        ll_people.setOnClickListener {
            startActivity(Intent(this, PersonalInfoActivity::class.java))
        }

        ll_modify_password.setOnClickListener {
            startActivity(Intent(this, ModifyPasswordActivity::class.java))
        }
        ll_cache.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("清除缓存？")
                    .setPositiveButton("确定", { dialog, which ->
                        tv_cache_size.setText("0M")
                        GlideCacheUtil.getInstance().clearImageAllCache(this)
                        dialog.dismiss()
                    })
                    .setNegativeButton("取消", { dialog, which ->
                        dialog.dismiss()
                    })
            dialog.show()
        }

        btn_exit_login.setOnClickListener {
            App.toLoginAndClean()
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        getCacheSize()
    }


    private fun getCacheSize() = mMainScope.launch {
        val size = withContext(Dispatchers.IO) {
            GlideCacheUtil.getInstance().getCacheSize(this@SettingActivity)
        }
        tv_cache_size.setText(size)

    }

}