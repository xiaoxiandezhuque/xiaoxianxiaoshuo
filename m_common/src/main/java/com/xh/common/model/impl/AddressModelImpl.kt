package com.xh.common.model.impl

import android.content.Context
import com.google.gson.Gson
import com.xh.common.bean.AddressBean
import com.xh.common.model.AddressModel


import org.json.JSONArray
import org.json.JSONException
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.*

/**
 * Created by Administrator on 2017/5/12.
 */

class AddressModelImpl @Throws(JSONException::class)
constructor(context: Context) : AddressModel {


    private val mProvinceData = ArrayList<AddressBean>()
    private val mCityData = ArrayList<ArrayList<String>>()
    private val mAreaData = ArrayList<ArrayList<ArrayList<String>>>()

    init {
        val JsonData = getJson(context, "province.json")//获取assets目录下的json文件数据
        val data = JSONArray(JsonData)
        val gson = Gson()
        for (i in 0..data.length() - 1) {
            val entity = gson.fromJson(data.optJSONObject(i).toString(), AddressBean::class.java)
            mProvinceData.add(entity)
        }
        /**
         * 添加省份数据

         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */

        for (i in mProvinceData.indices) {//遍历省份
            val CityList = ArrayList<String>()//该省的城市列表（第二级）
            val Province_AreaList = ArrayList<ArrayList<String>>()//该省的所有地区列表（第三极）

            for (c in 0..mProvinceData[i].city!!.size - 1) {//遍历该省份的所有城市
                val CityName = mProvinceData[i].city!![c].name
                CityList.add(CityName!!)//添加城市

                val City_AreaList = ArrayList<String>()//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (mProvinceData[i].city!![c].area == null || mProvinceData[i].city!![c].area!!.size == 0) {
                    City_AreaList.add("")
                } else {

                    for (d in 0..mProvinceData[i].city!![c].area!!.size - 1) {//该城市对应地区所有数据
                        val AreaName = mProvinceData[i].city!![c].area!![d]

                        City_AreaList.add(AreaName)//添加该城市所有地区数据
                    }
                }
                Province_AreaList.add(City_AreaList)//添加该省所有地区数据
            }

            mCityData.add(CityList)
            /**
             * 添加地区数据
             */
            mAreaData.add(Province_AreaList)

        }


    }


    private fun getJson(context: Context, fileName: String): String {

        val stringBuilder = StringBuilder()
        try {
            val assetManager = context.assets
            val bf = BufferedReader(InputStreamReader(
                    assetManager.open(fileName)))
            var line: String = ""

            while (bf.readLine().apply { line = this } != null) {
                stringBuilder.append(line)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return stringBuilder.toString()
    }

    override fun getProvince(): ArrayList<AddressBean> {
        return mProvinceData
    }

    override fun getCity(): ArrayList<ArrayList<String>> {
        return mCityData
    }

    override fun getArea(): ArrayList<ArrayList<ArrayList<String>>> {
        return mAreaData
    }
}
