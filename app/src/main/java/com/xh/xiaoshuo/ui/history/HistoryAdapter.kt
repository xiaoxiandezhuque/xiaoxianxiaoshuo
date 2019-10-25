package com.xh.xiaoshuo.ui.history

import android.content.Context

import android.widget.ImageView
import android.widget.TextView
import com.xh.common.base.BaseAdapter
import com.xh.common.base.ViewHolder
import com.xh.common.util.displayImage
import com.xh.xiaoshuo.R
import com.xh.xiaoshuo.bean.BookBean


class HistoryAdapter(mContext: Context) : BaseAdapter<BookBean>(R.layout.item_history, mContext, false) {


    override fun onMyBindViewHolder(holder: ViewHolder, position: Int, data: BookBean) {
        val iv_book = holder.getView<ImageView>(R.id.iv_book)
        val tv_title = holder.getView<TextView>(R.id.tv_title)
        val tv_author = holder.getView<TextView>(R.id.tv_author)
        val tv_open_time = holder.getView<TextView>(R.id.tv_open_time)
        val tv_desc = holder.getView<TextView>(R.id.tv_desc)
        iv_book.displayImage(data.imageUrl)
        tv_title.setText("书名:" + data.name)
        tv_author.setText("作者:" + data.auther)
        tv_open_time.setText("上次打开时间:" + com.blankj.utilcode.util.TimeUtils.millis2String(data.openTime))
        tv_desc.setText("简介:" + data.desc)
    }


}
