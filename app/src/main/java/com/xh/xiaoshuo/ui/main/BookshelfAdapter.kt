package com.xh.xiaoshuo.ui.main

import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import com.xh.common.base.BaseAdapter
import com.xh.common.base.ViewHolder
import com.xh.common.util.displayImage
import com.xh.xiaoshuo.R
import com.xh.xiaoshuo.bean.BookBean


class BookshelfAdapter(mContext: Context) : BaseAdapter<BookBean>(R.layout.item_bookshelf, mContext, false) {


    override fun onMyBindViewHolder(holder: ViewHolder, position: Int, data: BookBean) {
        val iv_book = holder.getView<ImageView>(R.id.iv_book)
        val tv_name = holder.getView<TextView>(R.id.tv_name)
        val tv_progress = holder.getView<TextView>(R.id.tv_progress)
        if (mData.size - 1 == position) {
            iv_book.setImageResource(R.drawable.ic_add)
        } else {
            iv_book.displayImage(data.imageUrl + "")
        }

        tv_name.setText(data.name + "")
        if (data.readNum != 0 && data.maxNum != 0) {
            tv_progress.setText("已读 ${data.readNum * 100 / data.maxNum}%")
        }else{
            tv_progress.setText("")
        }


    }


}
