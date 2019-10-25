package com.xh.common.base

import android.os.Bundle
import androidx.annotation.LayoutRes

import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.KeyboardUtils
import com.xh.common.view.LoadingDialog
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancelChildren


abstract class BaseActivity : AppCompatActivity() {

    protected val mMainScope = MainScope()
    protected val   mLoadingDialog by lazy {
        LoadingDialog(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        initView()
    }

    override fun onDestroy() {
        super.onDestroy()
        mMainScope.coroutineContext.cancelChildren()
    }


    @LayoutRes
    protected abstract fun getLayoutId(): Int

    protected abstract fun initView()

    override fun onBackPressed() {
        super.onBackPressed()
        KeyboardUtils.hideSoftInput(this)
    }

}
