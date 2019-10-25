package com.xh.xiaoshuo.util

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.listener.OnOptionsSelectListener
import com.xh.xiaoshuo.R


object SelectorUtil {
    fun showSelector(context: Context,
                        title:String,
                        list: MutableList<String>,
                        selectStr: String,
                        listener: (position: Int) -> Unit) {
        var option = 0
        if (list.contains(selectStr)) {
            for (i in 0 until list.size) {
                if (selectStr == list[i]) {
                    option = i
                    break
                }
            }
        }
        val pvOptions = OptionsPickerBuilder(context, object : OnOptionsSelectListener {
            override fun onOptionsSelect(options1: Int, options2: Int, options3: Int, v: View?) {
                listener.invoke(options1)
            }
        })
                .setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .setTitleText(title)//标题
                .setSubCalSize(18)//确定和取消文字大小
                .setTitleSize(20)//标题文字大小
                .setTitleColor(ContextCompat.getColor(context, R.color.colorPrimaryText))//标题文字颜色
                .setSubmitColor(ContextCompat.getColor(context, R.color.colorAccent))//确定按钮文字颜色
                .setCancelColor(ContextCompat.getColor(context, R.color.colorPrimaryText))//取消按钮文字颜色
                .setContentTextSize(18)//滚轮文字大小
                .setCyclic(false, false, false)//循环与否
                .setSelectOptions(option, 1, 1)  //设置默认选中项
                .setOutSideCancelable(false)//点击外部dismiss default true
//                .isDialog(true)//是否显示为对话框样式
                .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
                .build<String>()

        pvOptions.setPicker(list)//添加数据源
        pvOptions.show()
    }


}
