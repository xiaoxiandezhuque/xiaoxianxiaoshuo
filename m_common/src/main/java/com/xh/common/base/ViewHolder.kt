package com.xh.common.base


import android.util.SparseArray
import android.view.View
import androidx.recyclerview.widget.RecyclerView


class ViewHolder(val convertView: View) : RecyclerView.ViewHolder(convertView) {


    private val mViews: SparseArray<View>

    init {
        mViews = SparseArray<View>()
    }

    /**
     * 通过viewId获取控件

     * @param viewId
     * *
     * @return
     */
    fun <T : View> getView(viewId: Int): T {
        var view: View = mViews.get(viewId, convertView.findViewById(viewId))
        mViews.put(viewId, view)

        return view as T
    }
}


