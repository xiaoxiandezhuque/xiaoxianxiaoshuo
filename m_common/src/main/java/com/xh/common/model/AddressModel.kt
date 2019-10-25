package com.xh.common.model


import com.xh.common.bean.AddressBean
import java.util.*

interface AddressModel {
    fun getProvince(): ArrayList<AddressBean>
    fun getCity(): ArrayList<ArrayList<String>>
    fun getArea(): ArrayList<ArrayList<ArrayList<String>>>
}
