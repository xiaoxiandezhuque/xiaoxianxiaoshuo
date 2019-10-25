package com.xh.common.base


import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.Utils
import com.xh.common.BuildConfig
import me.jessyan.autosize.AutoSizeConfig
import me.jessyan.autosize.unit.Subunits


abstract class BaseLibApp : Application() {


    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()

        instance = this

        initARouter()
        Utils.init(this)
        AutoSizeConfig.getInstance().unitsManager
                .setSupportDP(false).supportSubunits = Subunits.MM
        init()
    }

    /**
     * 初始化路由
     */
    private fun initARouter() {
        if (BuildConfig.DEBUG) {
            ARouter.openLog()  // 打印日志
            ARouter.openDebug() // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(this)// 尽可能早，推荐在Application中初始化
    }

    companion object {
        //唯一baseapp的引用
        lateinit var instance: BaseLibApp
            private set
    }

    abstract fun init()

}
