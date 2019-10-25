package com.xh.xiaoshuo

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AlertDialog
import androidx.multidex.MultiDex
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.SPUtils
import com.lzy.imagepicker.ImagePicker
import com.lzy.imagepicker.view.CropImageView
import com.mob.MobSDK
import com.qy.reader.common.Global
import com.umeng.commonsdk.UMConfigure
import com.xh.common.base.BaseLibApp
import com.xh.xiaoshuo.constant.SPConstant
import com.xh.xiaoshuo.ui.account.LoginActivity
import com.xh.xiaoshuo.util.GlideImageLoader

/**
 * Created by yuyuhang on 2018/1/8.
 */
class App : BaseLibApp() {
    override fun init() {
        Global.init(this)

        UMConfigure.init(this, "5cce88db3fc1951deb0005e9", "gf", 1, null)
        if (BuildConfig.DEBUG) {
            UMConfigure.setLogEnabled(true)
        }

        MobSDK.init(this);
        initImagePicker()

//        NetworkApi.loginTimeOutListener = {
//            if (dialog == null || ActivityUtils.getTopActivity() !== (((dialog!!.context) as ContextWrapper).baseContext as ContextWrapper).baseContext) {
//                dialog = AlertDialog.Builder(ActivityUtils.getTopActivity())
//                        .setTitle("提示")
//                        .setMessage("登录超时，请重新登录")
//                        .setPositiveButton("确定", { dialog, which ->
//                            dialog?.dismiss()
//                            App.dialog = null
//                            toLoginAndClean()
//                        })
//                        .setCancelable(false)
//                        .create()
//                dialog?.setOnDismissListener {
//                    dialog = null
//                }
//            }
//            if (!(dialog?.isShowing ?: true || ActivityUtils.getTopActivity() is LoginActivity)) {
//
//                dialog?.show()
//            }
//        }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)

    }


    private fun initImagePicker() {
        val imagePicker = ImagePicker.getInstance()
        imagePicker.imageLoader = GlideImageLoader()   //设置图片加载器
        imagePicker.isShowCamera = true                      //显示拍照按钮
        imagePicker.isCrop = true                           //允许裁剪（单选才有效）
        imagePicker.isSaveRectangle = true                   //是否按矩形区域保存
        imagePicker.selectLimit = 6              //选中数量限制
        imagePicker.style = CropImageView.Style.RECTANGLE  //裁剪框的形状
        imagePicker.focusWidth = 800                       //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.focusHeight = 800                      //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.outPutX = 1000                         //保存文件的宽度。单位像素
        imagePicker.outPutY = 1000                         //保存文件的高度。单位像素
    }

    companion object {
        fun toLogin() {
//        "登录过期".showToast()
            SPUtils.getInstance().remove(SPConstant.USER_ID)
            SPUtils.getInstance().remove(SPConstant.TOKEN)
            SPUtils.getInstance().remove(SPConstant.USER_NAME)
            SPUtils.getInstance().remove(SPConstant.USER_PHONE)
            SPUtils.getInstance().remove(SPConstant.USER_INVITATION_NUM)
            SPUtils.getInstance().remove(SPConstant.USER_INVITATION_CODE)
            ActivityUtils.getTopActivity().startActivity(Intent(ActivityUtils.getTopActivity(), LoginActivity::class.java))
        }

        fun toLoginAndClean() {
            toLogin()
        }

        private var dialog: AlertDialog? = null

    }


}
