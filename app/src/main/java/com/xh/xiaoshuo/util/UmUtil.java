package com.xh.xiaoshuo.util;

import android.content.Context;

import com.umeng.commonsdk.statistics.common.DeviceConfig;

public class UmUtil {

    public static String[] getTestDeviceInfo(Context context){
        String[] deviceInfo = new String[2];
        try {
            if(context != null){
                deviceInfo[0] = DeviceConfig.getDeviceIdForGeneral(context);
                deviceInfo[1] = DeviceConfig.getMac(context);
            }
        } catch (Exception e){
        }
        return deviceInfo;
    }
}
