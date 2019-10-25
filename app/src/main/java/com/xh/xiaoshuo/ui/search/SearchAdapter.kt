package com.xh.xiaoshuo.ui.search

import android.content.Context
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.qy.reader.common.entity.book.SearchBook
import com.xh.common.base.BaseAdapter
import com.xh.common.base.ViewHolder
import com.xh.common.util.displayImage
import com.xh.xiaoshuo.R

class SearchAdapter(mContext: Context) : BaseAdapter<SearchBook>(R.layout.item_search, mContext, false) {


    var searchStr = ""

    override fun onMyBindViewHolder(holder: ViewHolder, position: Int, data: SearchBook) {
        val iv_book = holder.getView<ImageView>(R.id.iv_book)
        val tv_name = holder.getView<TextView>(R.id.tv_name)
        val tv_author = holder.getView<TextView>(R.id.tv_author)
        val tv_content = holder.getView<TextView>(R.id.tv_content)

        iv_book.displayImage(data.cover + "")
        tv_name.setText(changeTextColor(data.title + "", searchStr))
        tv_author.setText(changeTextColor(data.author + "", searchStr))
        tv_content.setText(data.desc + "")
    }


    private fun changeTextColor(content: String, splitText: String): SpannableString {
        var content = content
        var start = 0
        var end: Int
        val result = SpannableString(content)
        if (TextUtils.isEmpty(splitText)) {
            return result
        }
        if (!TextUtils.isEmpty(splitText) && content!!.length >= splitText.length) {


            while (start.run {
                        start = content.indexOf(splitText, start)
                        start
                    } >= 0) {
                end = start + splitText.length
                result.setSpan(ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.colorAccent)),
                        start, end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE)
                start = end
            }
        }
        return result
    }
}