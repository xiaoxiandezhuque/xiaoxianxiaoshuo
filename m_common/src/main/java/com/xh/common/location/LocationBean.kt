package com.xh.common.bean

import com.xh.common.ktextended.isEmptyForMy


/**
 * Created by Administrator on 2017/11/29.
 */
data class LocationBean(var province: String = "",
                        var city: String = "",
                        var desc: String = "",
                        var lat: Double = 0.0,
                        var lng: Double = 0.0) {

    fun isEmpty(): Boolean {
        return province.isEmptyForMy() || city.isEmptyForMy() || desc.isEmptyForMy() ||
                (lat == 0.0) || (lng == 0.0)
    }


}