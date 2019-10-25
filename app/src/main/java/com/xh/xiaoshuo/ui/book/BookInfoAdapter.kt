package com.xh.xiaoshuo.ui.book

import android.content.Context
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.qy.reader.common.entity.chapter.Chapter
import com.xh.common.base.BaseAdapter
import com.xh.common.base.ViewHolder
import com.xh.xiaoshuo.R

/**
 * Created by quezhongsang on 2018/1/13.
 */
class BookInfoAdapter(mContext: Context) : BaseAdapter<Chapter>(R.layout.item_book_chapter_list, mContext, false) {

    private var mIsAsc = true
    var currectChapter = -1
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onMyBindViewHolder(holder: ViewHolder, position: Int, data: Chapter) {
        var textView = holder.getView<TextView>(R.id.tv_content);
        textView.setText(data.title + "")
        if (currectChapter == position) {
            textView.setTextColor(ContextCompat.getColor(mContext, R.color.colorAccent))
        } else {
            textView.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimaryText))
        }


    }

    //    倒叙
    fun orderByDesc() {
        if (mIsAsc) {
            transformData()
            mIsAsc = false
        }
    }

    //    正序
    fun orderByAsc() {
        if (!mIsAsc) {
            transformData()
            mIsAsc = true
        }

    }

    private fun transformData() {
        if (mData != null) {
            val chapterList = mutableListOf<Chapter>()
            for (i in mData.size - 1 downTo 0) {
                chapterList.add(mData.get(i))
            }
            mData.clear()
            mData.addAll(chapterList)
            notifyDataSetChanged()
        }
    }

    fun isAsc(): Boolean {
        return mIsAsc
    }

    fun clear() {
        mIsAsc = true
        mData.clear()
        notifyDataSetChanged()
    }
}