package com.xh.xiaoshuo.ui.my

import android.content.Intent
import android.net.Uri
import com.blankj.utilcode.util.AppUtils
import com.xh.common.base.BaseActivity
import com.xh.xiaoshuo.R
import kotlinx.android.synthetic.main.activity_about.*


class AboutActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_about
    }

    override fun initView() {
        tv_version.setText("版本号:${AppUtils.getAppVersionName()}")
        ll_score.setOnClickListener {
            val uri = Uri.parse("market://details?id=$packageName")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

}