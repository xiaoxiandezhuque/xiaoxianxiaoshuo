//package com.xh.common.location
//
//
//import android.Manifest
//import android.content.ComponentName
//import android.content.Context
//import android.content.Intent
//import android.location.Criteria
//import android.location.LocationManager
//import android.location.LocationProvider
//import android.os.Build
//import android.os.Handler
//import android.os.Looper
//import android.provider.Settings
//import android.view.View
//import com.amap.api.location.AMapLocationClient
//import com.amap.api.location.AMapLocationClientOption
//import com.blankj.utilcode.util.ActivityUtils
//import com.blankj.utilcode.util.LogUtils
//import com.xh.baselibrary.base.BaseLibApp
//import com.xh.baselibrary.bean.LocationBean
//import com.xh.baselibrary.ktextended.showToast
//import com.xh.baselibrary.model.impl.PermissionModelImpl
//import com.xh.baselibrary.network.NetworkDialog
//import com.xh.baselibrary.view.DescDialog
//
//
///**
// * Created by Administrator on 2017/11/29.
// */
//
//class LocationUtilGaode private constructor() {
//
//    private lateinit var mLocationClient: AMapLocationClient
//    private var mLintener: ((bean: LocationBean) -> Unit)? = null
//    val mLocationBean = LocationBean()
//    private var count = 0
//    private val mHandler = Handler(Looper.getMainLooper())
//
//    fun init(context: Context) {
//        //声明LocationClient类
//        mLocationClient = AMapLocationClient(context);
//        //注册监听函数
//        mLocationClient.setLocationListener {
//            LogUtils.e("定位++++$count+++$it")
//            if (it == null) {
//                showHintDialog(mLintener!!)
//                return@setLocationListener
//            }
//            if (it.errorCode == 0) {
//                mLocationBean.province = it.province ?: ""
//                mLocationBean.city = it.city ?: ""
//                mLocationBean.desc = it?.description ?: ""
//                mLocationBean.lat = it?.latitude
//                mLocationBean.lng = it?.longitude
//                if (count > MAX_AGAIN_COUNT && mLocationBean.isEmpty()) {
//                    showHintDialog(mLintener!!)
//                } else if (!mLocationBean.isEmpty()) {
//                    mHandler.post {
//                        mLintener?.invoke(mLocationBean)
//                        stopLocation()
//                    }
//                } else {
//                    count++
//                }
//            } else {
//                LogUtils.e("AmapError", "location Error, ErrCode:"
//                        + it.getErrorCode() + ", errInfo:"
//                        + it.getErrorInfo());
//                showHintDialog(mLintener!!)
//            }
//
//        }
//
//        val option = AMapLocationClientOption()
//
//        option.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.SignIn);
//        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//        //获取一次定位结果：
////该方法默认为false。
//        option.setOnceLocation(false);
////获取最近3s内精度最高的一次定位结果：
////设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
//        option.setOnceLocationLatest(true);
//        option.setNeedAddress(true);
//        mLocationClient.setLocationOption(option)
//
//    }
//
//    fun startLocatoin(listener: (bean: LocationBean) -> Unit) {
//        if (!mLocationBean.isEmpty()) {
//            listener.invoke(mLocationBean)
//            return
//        }
//
//        PermissionModelImpl().request(ActivityUtils.getTopActivity(), {
//            if (isMockPosition()) {
//                //模拟位置已经打开了
//                val dialog = DescDialog(ActivityUtils.getTopActivity(), "提示",
//                        "您的模拟位置已打开，无法进行后续操作，请先关闭，点击确定进行设置!",
//                        "取消", "确定")
//                dialog.show()
//                dialog.setCancelClick(object : View.OnClickListener {
//                    override fun onClick(v: View) {
//                        dialog.dismiss()
//                    }
//                })
//                dialog.setOkClick(object : View.OnClickListener {
//                    override fun onClick(v: View) {
//                        dialog.dismiss()
////                         跳转到关闭界面
//                        try {
//                            val componentName = ComponentName(
//                                    "com.android.settings",
//                                    "com.android.settings.DevelopmentSettings")
//                            val intent = Intent()
//                            intent.setComponent(componentName)
//                            intent.setAction("android.intent.action.View")
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                            ActivityUtils.getTopActivity()?.startActivity(intent)
//                        } catch (e: Exception) {
////                             找不到开发设置界面就跳系统设置
//                            val intent = Intent(
//                                    Settings.ACTION_SETTINGS)
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                            ActivityUtils.getTopActivity()?.startActivity(intent)
//                        }
//
//                    }
//                })
//            } else {
////                模拟位置没有打开
////                showLoadingDialog()
////                initLocation()
//
//                NetworkDialog.INSTANCE.show("加载中...")
//                mLintener = listener
//                count = 0
//                mLocationClient.startLocation()
//            }
//        }, {
//            "请打开定位权限".showToast()
////            listener.invoke(null)
//        }, Manifest.permission.READ_PHONE_STATE,
//                Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
//                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//    }
//
//
//    //这个代码没测试
//    private fun isMockPosition(): Boolean {
////        var hasAddTestProvider = false
//        //6.0一下判断
//        var canMockPosition = Settings.Secure.getInt(ActivityUtils.getTopActivity().getContentResolver(), Settings.Secure.ALLOW_MOCK_LOCATION, 0) != 0
//                || Build.VERSION.SDK_INT > 22
//        //6.0以上判断
//        if (canMockPosition) {
//            var locationManager = BaseLibApp.instance.getSystemService(Context.LOCATION_SERVICE) as LocationManager
//            try {
//                val providerStr = LocationManager.GPS_PROVIDER
//                val provider = locationManager.getProvider(providerStr)
//                if (provider != null) {
//                    locationManager.addTestProvider(
//                            provider.getName(), provider.requiresNetwork(), provider.requiresSatellite(), provider.requiresCell(), provider.hasMonetaryCost(), provider.supportsAltitude(), provider.supportsSpeed(), provider.supportsBearing(), provider.getPowerRequirement(), provider.getAccuracy())
//                } else {
//                    locationManager.addTestProvider(
//                            providerStr, true, true, false, false, true, true, true, Criteria.POWER_HIGH, Criteria.ACCURACY_FINE)
//                }
//                locationManager.setTestProviderEnabled(providerStr, true)
//                locationManager.setTestProviderStatus(providerStr, LocationProvider.AVAILABLE, null, System.currentTimeMillis())
//
//                // 模拟位置可用
////                hasAddTestProvider = true
//                canMockPosition = true
//            } catch (e: SecurityException) {
//                canMockPosition = false
//            }
//
//        }
//        return canMockPosition
//    }
//
//
//    private fun showHintDialog(listener: (bean: LocationBean) -> Unit) {
//        stopLocation()
//
//        mHandler.post {
//            val dialog = DescDialog(ActivityUtils.getTopActivity(), "提示", "获取地理位置信息失败，请重新定位！",
//                    "取消", "重新定位")
//            dialog.show()
//            dialog.setCancelClick({ dialog.dismiss() })
//            dialog.setOkClick({
//                //开了定位的进程，需要重新new一下dialog
//                NetworkDialog.INSTANCE.refreshDialog()
//                dialog.dismiss()
//                startLocatoin(listener)
//            })
//        }
//
//
//    }
//
//    private fun stopLocation() {
//        mLintener = null
//        NetworkDialog.INSTANCE.directDismiss()
//        mLocationClient.stopLocation()
//    }
//
//
//    companion object {
//        const val MAX_AGAIN_COUNT = 3
//        //单列
//        val instance: LocationUtilGaode
//            get() = SingeInstance.INSTANCE
//    }
//
//    private object SingeInstance {
//        internal val INSTANCE = LocationUtil()
//    }
//}
