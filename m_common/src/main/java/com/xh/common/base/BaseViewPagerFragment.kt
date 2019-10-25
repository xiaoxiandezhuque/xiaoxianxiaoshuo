package com.xh.common.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup



abstract class BaseViewPagerFragment : BaseFragment() {
    /**
     * Fragment当前状态是否可见
     */
    protected var isVisibleF: Boolean = false
    //fragment的布局
    protected var mView: View? = null
    /**
     * 标志位，标志已经初始化完成
     */
    protected var isPrepared: Boolean = false
    /**
     * 是否已被加载过一次，第二次就不再去请求数据了
     */
    protected var mHasLoadedOnce: Boolean = false


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        if (mView == null) {
            mView = inflater.inflate(getLayoutId(), container, false)
            isPrepared = true
        }
        //因为共用一个Fragment视图，所以当前这个视图已被加载到Activity中，必须先清除后再加入Activity
//        val parent = mView!!.parent as ViewGroup
//        parent.removeView(mView)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isVisibleF) {

            lazyLoad()
            mHasLoadedOnce = true
        }
    }


    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (userVisibleHint) {
            isVisibleF = true
            onVisible()
        } else {
            isVisibleF = false
            onInvisible()
        }
    }

    /**
     * 可见
     */
    protected fun onVisible() {

        if (!isPrepared || !isVisibleF || mHasLoadedOnce) {
            return
        }

        lazyLoad()
        mHasLoadedOnce = true
    }

    /**
     * 不可见
     */
    protected fun onInvisible() {}


    /**
     * 延迟加载
     * 子类必须重写此方法
     */
    protected abstract fun lazyLoad()

    override fun initView() {

    }
}
