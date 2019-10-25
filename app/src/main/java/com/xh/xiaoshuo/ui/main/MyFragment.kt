package com.yx.jjs.ui.home.main

import android.content.Intent
import com.blankj.utilcode.util.SPUtils
import com.xh.common.base.BaseFragment
import com.xh.common.ktextended.isEmptyForMy
import com.xh.common.ktextended.showToast
import com.xh.common.network.NetworkApi
import com.xh.common.util.displayImageAvatar
import com.xh.xiaoshuo.R
import com.xh.xiaoshuo.constant.SPConstant
import com.xh.xiaoshuo.ui.WebActivity
import com.xh.xiaoshuo.ui.account.LoginActivity
import com.xh.xiaoshuo.ui.history.HistoryActivity
import com.xh.xiaoshuo.ui.my.AboutActivity
import com.xh.xiaoshuo.ui.my.FeedbackActivity
import com.xh.xiaoshuo.ui.my.PersonalInfoActivity
import com.xh.xiaoshuo.ui.my.SettingActivity
import com.xh.xiaoshuo.util.ClipUtil
import kotlinx.android.synthetic.main.fragment_my.*


class MyFragment : BaseFragment() {

    companion object {
        private const val IMAGE_ITEM_ADD = -1
        private const val REQUEST_CODE_SELECT = 100
        private const val REQUEST_CODE_PREVIEW = 101
    }

    private var mAvatarPath = ""

    override fun getLayoutId(): Int {
        return R.layout.fragment_my
    }

    override fun initView() {
        title_view.hideBack()

        ll_user.setOnClickListener {
            if (SPUtils.getInstance().getString(SPConstant.TOKEN).isEmptyForMy()) {
                startActivity(Intent(activity, LoginActivity::class.java))
            } else {
                startActivity(Intent(activity, PersonalInfoActivity::class.java))
            }
        }
        ll_history.setOnClickListener {
            if (SPUtils.getInstance().getString(SPConstant.TOKEN).isEmptyForMy()) {
                startActivity(Intent(activity, LoginActivity::class.java))
            } else {
                startActivity(Intent(activity, HistoryActivity::class.java))
            }
        }

        ll_feedback.setOnClickListener {
            if (SPUtils.getInstance().getString(SPConstant.TOKEN).isEmptyForMy()) {
                startActivity(Intent(activity, LoginActivity::class.java))
            } else {
                startActivity(Intent(activity, FeedbackActivity::class.java))
            }
        }
        ll_disclaimer.setOnClickListener {
            WebActivity.intentToThis(activity!!, "免责声明", NetworkApi.DISCLAIMER_URL)
        }

        ll_about.setOnClickListener {
            startActivity(Intent(activity, AboutActivity::class.java))
        }

        title_view.setOnClickRightListener {
            if (SPUtils.getInstance().getString(SPConstant.TOKEN).isEmptyForMy()) {
                startActivity(Intent(activity, LoginActivity::class.java))
            } else {
                startActivity(Intent(activity, SettingActivity::class.java))
            }
        }

        btn_share.setOnClickListener {
            ClipUtil.copy(activity, SPUtils.getInstance().getString(SPConstant.LINK))
            "下载地址已复制到剪切板".showToast()
        }

    }

    override fun onResume() {
        super.onResume()

        val invitationStr = SPUtils.getInstance().getString(SPConstant.USER_INVITATION_CODE, "")
        if (!invitationStr.isEmptyForMy()) {
            tv_invitation.setText("邀请码:${invitationStr}")
            tv_invitation.setOnClickListener {
                ClipUtil.copy(activity, SPUtils.getInstance().getString(SPConstant.USER_INVITATION_CODE, ""))
                "邀请码已复制到剪切板".showToast()
            }
        } else {
            tv_invitation.setText("")
        }
        val avatar = SPUtils.getInstance().getString(SPConstant.USER_AVATAR, "")
        val name = SPUtils.getInstance().getString(SPConstant.USER_NAME, "")
        if (!avatar.isEmptyForMy()) {
            iv_avatar.displayImageAvatar(NetworkApi.BASE_URL + avatar, R.drawable.ic_avatar)
        }
        if (!name.isEmptyForMy()) {
            tv_name.setText(name)
        }

//        tv_name.setText(SPUtils.getInstance().getString(SPConstant.USER_NAME, " "))
//        siv_avatar.displayImageAvatar(SPUtils.getInstance().getString(SPConstant.USER_AVATAR, ""))
    }


}