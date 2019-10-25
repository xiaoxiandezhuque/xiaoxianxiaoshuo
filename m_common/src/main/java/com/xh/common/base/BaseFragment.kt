package com.xh.common.base

import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.xh.common.view.LoadingDialog
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancelChildren


abstract class BaseFragment : Fragment() {

    protected val mMainScope = MainScope()
    /**
     * 标志位，标志已经初始化完成
     */
    protected val   mLoadingDialog by lazy {
        LoadingDialog(activity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutId(), container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        view?.setOnClickListener(null)
        initView()
    }


    override fun onDestroy() {
        super.onDestroy()
        mMainScope.coroutineContext.cancelChildren()
    }

    /**
     * 延迟加载
     * 子类必须重写此方法
     */
    protected abstract fun initView()

    @LayoutRes
    protected abstract fun getLayoutId(): Int


}
