package com.xh.xiaoshuo.ui.my

import android.content.Intent
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.StringUtils
import com.lzy.imagepicker.ImagePicker
import com.lzy.imagepicker.bean.ImageItem
import com.xh.common.base.BaseActivity
import com.xh.common.network.NetworkApi
import com.xh.common.util.displayImageAvatar
import com.xh.xiaoshuo.R
import com.xh.xiaoshuo.bean.UpdateUserReqBean
import com.xh.xiaoshuo.constant.SPConstant
import com.xh.xiaoshuo.network.api.UploadFileApi
import com.xh.xiaoshuo.network.api.UserApi
import com.xh.xiaoshuo.ui.InputActivity
import kotlinx.android.synthetic.main.activity_personal_info.*
import kotlinx.coroutines.launch
import java.util.*


class PersonalInfoActivity : BaseActivity() {
    companion object {
        private const val IMAGE_ITEM_ADD = -1
        private const val REQUEST_CODE_SELECT = 100
        private const val REQUEST_CODE_PREVIEW = 101
    }

    private var mAvatarPath = ""
    private var mAvatar: String = ""
    private var mName: String = ""


    override fun getLayoutId(): Int {
        return R.layout.activity_personal_info
    }

    override fun initView() {
        getValueFromSP()
        setValue()


        ll_avatar.setOnClickListener {
            //            SelectPhotoDialog(this, {
//                ImagePicker.getInstance().selectLimit = 1
//                val intent = Intent(this, ImageGridActivity::class.java)
//                intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true) // 是否是直接打开相机
//                startActivityForResult(intent, REQUEST_CODE_SELECT)
//            }, {
//                ImagePicker.getInstance().selectLimit = 1
//                val intent1 = Intent(this, ImageGridActivity::class.java)
//                startActivityForResult(intent1, REQUEST_CODE_SELECT)
//            }).show()
        }

        ll_name.setOnClickListener {
            InputActivity.intentToThis(this, "昵称", tv_name.text.toString(), 2113)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            //添加图片返回
            if (data != null && requestCode == REQUEST_CODE_SELECT) {
                val images = data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS) as ArrayList<ImageItem>
                if (images != null && images.size != 0) {
                    mAvatarPath = images[0].path
                    uploadImg(mAvatarPath)
                }
            }
        } else if (resultCode == ImagePicker.RESULT_CODE_BACK) {
            //预览图片返回
            if (data != null && requestCode == REQUEST_CODE_PREVIEW) {
                val images = data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS) as ArrayList<ImageItem>
                if (images != null && images.size != 0) {
                    mAvatarPath = images[0].path
                    uploadImg(mAvatarPath)
                }
            }
        } else if (resultCode == InputActivity.INTENT_RESULT) {
            val name = data?.getStringExtra(InputActivity.EXTRE_ET_STRING) ?: ""
            mName = name
            updateInfo()
        }
    }


    private fun getValueFromSP() {
        mAvatar = SPUtils.getInstance().getString(SPConstant.USER_AVATAR)
        mName = SPUtils.getInstance().getString(SPConstant.USER_NAME)
//        mSex = SPUtils.getInstance().getString(SPConstant.USER_SEX)
//        mBirthday = SPUtils.getInstance().getString(SPConstant.USER_BIRTHDAY)

    }

    private fun setValue() {
        iv_avatar.displayImageAvatar(NetworkApi.BASE_URL + mAvatar, R.drawable.ic_avatar2)
        tv_name.setText(mName)

    }

    private fun uploadImg(path: String) = mMainScope.launch {
        mLoadingDialog.show()
        val data = UploadFileApi().uploadAvatar(path)
        if (data != null) {
            mAvatar = data.url
            SPUtils.getInstance().put(SPConstant.USER_AVATAR, data.url)
            setValue()
        }
        mLoadingDialog.dismiss()
    }


    private fun updateInfo() = mMainScope.launch {
        mLoadingDialog.show()
        val data = UserApi().updateUesr(UpdateUserReqBean(mName))
        if (!StringUtils.isTrimEmpty(data)) {
            SPUtils.getInstance().put(SPConstant.USER_NAME, mName)
            setValue()
        }
        mLoadingDialog.dismiss()
    }


}
