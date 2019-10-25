package com.xh.common.location

import android.content.Context
import android.location.Criteria
import android.location.LocationManager
import android.location.LocationProvider
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.baidu.location.BDLocation
import com.baidu.location.BDLocationListener
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.LogUtils

import com.xh.common.base.BaseLibApp
import com.xh.common.bean.LocationBean


/**
 * Created by Administrator on 2017/11/29.
 */

class LocationUtil private constructor() {

    private lateinit var mLocationClient: LocationClient
    private var mLintener: ((bean: LocationBean) -> Unit)? = null
    val mLocationBean = LocationBean()
    private var count = 0
    private val mHandler = Handler(Looper.getMainLooper())

    fun init(context: Context) {
        //声明LocationClient类
        mLocationClient = LocationClient(context)
        //注册监听函数
        mLocationClient.registerLocationListener(object : BDLocationListener {
            override fun onConnectHotSpotMessage(p0: String?, p1: Int) {
            }

            override fun onReceiveLocation(location: BDLocation?) {

                if (location?.getLocType() == BDLocation.TypeCriteriaException) {
                    LogUtils.d("无法获取地理位置信息")
                    showHintDialog(mLintener!!)
                    return
                }

                mLocationBean.province = location?.province ?: ""
                mLocationBean.city = location?.city ?: ""
                mLocationBean.desc = location?.addrStr ?: ""
                mLocationBean.lat = location?.latitude ?: 0.0
                mLocationBean.lng = location?.longitude ?: 0.0
//                mLocationClient.stop()
                if (count > MAX_AGAIN_COUNT && mLocationBean.isEmpty()) {
                    Log.e("11", "11")
                    showHintDialog(mLintener!!)
                } else if (!mLocationBean.isEmpty()) {
                    Log.e("22", "22")
                    mHandler.post {
                        mLintener?.invoke(mLocationBean)
                        stopLocation()
                    }

                } else {
                    Log.e("33", "33")
                    count++
                }

                if (location?.getLocType() == BDLocation.TypeGpsLocation) {
                    // GPS定位结果
                    LogUtils.d("gps定位成功 ：" + location.getAddrStr())
                    //locationSuccess();
                    //  layoutLocationFail.setVisibility(View.GONE);
                } else if (location?.getLocType() == BDLocation.TypeNetWorkLocation) {
                    // 网络定位结果
                    LogUtils.d("网络定位成功")
                } else if (location?.getLocType() == BDLocation.TypeOffLineLocation) {
                    LogUtils.d("离线定位成功，离线定位结果也是有效的")
                } else if (location?.getLocType() == BDLocation.TypeServerError) {
                    LogUtils.d("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因")
                } else if (location?.getLocType() == BDLocation.TypeNetWorkException) {
                    LogUtils.d("网络不同导致定位失败，请检查网络是否通畅")
                } else if (location?.getLocType() == BDLocation.TypeCriteriaException) {
                    LogUtils.d("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机")
                } else {
                    LogUtils.d("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机")
                }

            }

        })
        val option = LocationClientOption()
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy)
        //可选，默认gcj02，设置返回的定位结果坐标系
        option.setCoorType("bd09ll")
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setScanSpan(1000)
        //可选，设置是否需要地址信息，默认不需要
        option.setIsNeedAddress(true)
        //可选，默认false,设置是否使用gps
        option.setOpenGps(true)
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setLocationNotify(true)
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationDescribe(false)
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIsNeedLocationPoiList(false)
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.setIgnoreKillProcess(false)
        //可选，默认false，设置是否收集CRASH信息，默认收集
        option.SetIgnoreCacheException(false)
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        option.setEnableSimulateGps(false)
        mLocationClient.locOption = option

    }

    fun startLocatoin(listener: (bean: LocationBean) -> Unit) {
        if (!mLocationBean.isEmpty()) {
            listener.invoke(mLocationBean)
            return
        }

//        PermissionModelImpl().request(ActivityUtils.getTopActivity(), {
//
//
//            if (isMockPosition()) {
//                //模拟位置已经打开了
//                val dialog = DescDialog(ActivityUtils.getTopActivity(), "提示",
//                        "您的模拟位置已打开，无法进行后续操作，请先关闭，点击确定进行设置!",
//                        "取消", "确定")
//                dialog.show()
//                dialog.setLeftClick(object : View.OnClickListener {
//                    override fun onClick(v: View) {
//                        dialog.dismiss()
//                    }
//                })
//                dialog.setRightClick(object : View.OnClickListener {
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
//                mLocationClient.start()
//            }
//        }, {
//            "请打开定位权限".showToast()
////            listener.invoke(null)
//        }, Manifest.permission.READ_PHONE_STATE,
//                Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
//                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }


    //这个代码没测试
    private fun isMockPosition(): Boolean {
//        var hasAddTestProvider = false
        //6.0一下判断
        var canMockPosition = Settings.Secure.getInt(ActivityUtils.getTopActivity().getContentResolver(), Settings.Secure.ALLOW_MOCK_LOCATION, 0) != 0
                || Build.VERSION.SDK_INT > 22
        //6.0以上判断
        if (canMockPosition) {
            var locationManager = BaseLibApp.instance.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            try {
                val providerStr = LocationManager.GPS_PROVIDER
                val provider = locationManager.getProvider(providerStr)
                if (provider != null) {
                    locationManager.addTestProvider(
                            provider.getName(), provider.requiresNetwork(), provider.requiresSatellite(), provider.requiresCell(), provider.hasMonetaryCost(), provider.supportsAltitude(), provider.supportsSpeed(), provider.supportsBearing(), provider.getPowerRequirement(), provider.getAccuracy())
                } else {
                    locationManager.addTestProvider(
                            providerStr, true, true, false, false, true, true, true, Criteria.POWER_HIGH, Criteria.ACCURACY_FINE)
                }
                locationManager.setTestProviderEnabled(providerStr, true)
                locationManager.setTestProviderStatus(providerStr, LocationProvider.AVAILABLE, null, System.currentTimeMillis())

                // 模拟位置可用
//                hasAddTestProvider = true
                canMockPosition = true
            } catch (e: SecurityException) {
                canMockPosition = false
            }

        }
        return canMockPosition
    }


    private fun showHintDialog(listener: (bean: LocationBean) -> Unit) {
        stopLocation()

        mHandler.post {
            val dialog = AlertDialog.Builder(ActivityUtils.getTopActivity())
                    .setTitle("提示")
                    .setMessage("获取地理位置信息失败，请重新定位！")
                    .setNegativeButton("取消", { dialog, which ->
                        dialog.dismiss()
                    })
                    .setPositiveButton("重新定位", { dialog, which ->
//                        NetworkDialog.INSTANCE.refreshDialog()
                        dialog.dismiss()
                        startLocatoin(listener)
                    })
                    .create()
            dialog.show()
        }

    }

    private fun stopLocation() {
        mLintener = null
//        NetworkDialog.INSTANCE.directDismiss()
        mLocationClient.stop()
    }


    companion object {
        const val MAX_AGAIN_COUNT = 3
        //单列
        val instance: LocationUtil
            get() = SingeInstance.INSTANCE
    }

    private object SingeInstance {
        internal val INSTANCE = LocationUtil()
    }
}
