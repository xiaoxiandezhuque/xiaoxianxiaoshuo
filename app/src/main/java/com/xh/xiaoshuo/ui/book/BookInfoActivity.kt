package com.xh.xiaoshuo.ui.book

import android.Manifest
import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import com.qy.reader.common.entity.book.SearchBook
import com.qy.reader.common.entity.chapter.Chapter
import com.qy.reader.common.entity.source.SourceID
import com.qy.reader.common.utils.StringUtils
import com.qy.reader.crawler.Crawler
import com.qy.reader.crawler.source.callback.ChapterCallback
import com.xh.common.base.BaseActivity
import com.xh.common.ktextended.showToast
import com.xh.common.util.displayImage
import com.xh.xiaoshuo.R
import com.xh.xiaoshuo.bean.BookBean
import com.xh.xiaoshuo.mode.BookDB
import com.xh.xiaoshuo.ui.book.read.ReadActivity
import com.xh.xiaoshuo.ui.book.read.ReadWebActivity
import com.xh.xiaoshuo.util.UMStatisticsUtil
import kotlinx.android.synthetic.main.activity_book_info.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnPermissionDenied
import permissions.dispatcher.RuntimePermissions
import java.util.*


/**
 * Created by quezhongsang on 2018/1/13.
 */
@RuntimePermissions
class BookInfoActivity : BaseActivity() {


    private val mSourceList = mutableListOf<SearchBook.SL>()

    private lateinit var mAdapter: BookInfoAdapter
    private lateinit var mSearchBook: SearchBook
    private var mCurrentSourcePosition = 0

    private val mChapterList = ArrayList<Chapter>()

    private var mBookBean: BookBean? = null

    private var isToDetail = false

    private val mBookDB = BookDB()

    override fun getLayoutId(): Int {
        return R.layout.activity_book_info
    }

    override fun initView() {

        mSearchBook = intent.getSerializableExtra("search_book") as SearchBook
        isToDetail = intent.getBooleanExtra("is_to_detail", false)
        if (mSearchBook.sources != null) {
            mSourceList.addAll(mSearchBook.sources)
        }



        iv_book_cover.displayImage(mSearchBook.cover + "")
        tv_book_title.setText(mSearchBook.title + "")
        tv_book_author.setText(mSearchBook.author + "")
        tv_book_desc.setText(mSearchBook.desc + "")


        recyclerview.setLayoutManager(LinearLayoutManager(this))
        mAdapter = BookInfoAdapter(this)
        recyclerview.setAdapter(mAdapter)

        mAdapter.setOnItemClickListener { view, postion, data ->
            //            正序和倒叙的位子不一样，所有直接找对象的位子
            intentToReadWithPermissionCheck(mChapterList.indexOf(data))
            UMStatisticsUtil.book(this, mSearchBook.title + "", mSearchBook.author + "")
        }



        tv_order_by.setOnClickListener {
            val str = tv_order_by.getText().toString()
            if ("正序".equals(str, ignoreCase = true)) {//倒序
                mAdapter.orderByDesc()
                tv_order_by.setText("倒序")
            } else if ("倒序".equals(str, ignoreCase = true)) {//正序
                mAdapter.orderByAsc()
                tv_order_by.setText("正序")
            }
        }

        btn_add_bookshelf.setOnClickListener {
            if (mBookBean?.state == 1) {
                btn_add_bookshelf.setText("加入书架")
                mBookBean?.state = 0

            } else {
                mBookBean?.state = 1
                btn_add_bookshelf.setText("移除书架")
            }
        }
        btn_continue.setOnClickListener {
            if (mBookBean != null && mBookBean!!.readNum <= mChapterList.size) {
                intentToReadWithPermissionCheck(mBookBean!!.readNum - 1)
            }
        }

        requestNet()

        mMainScope.launch {
            val mBookBean = mBookDB.query(mSearchBook, mSourceList[mCurrentSourcePosition])
            if (mBookBean?.state == 1) {
                btn_add_bookshelf.setText("移除书架")
            } else {
                btn_add_bookshelf.setText("加入书架")
            }
        }

    }


    override fun onResume() {
        super.onResume()
        mMainScope.launch {
            mBookBean = mBookDB.query(mSearchBook, mSourceList[mCurrentSourcePosition])
            if (mBookBean?.state == 1) {
                btn_add_bookshelf.setText("移除书架")
            } else {
                btn_add_bookshelf.setText("加入书架")
            }
            if (mBookBean != null && mBookBean!!.readNum <= mChapterList.size) {
                tv_read.setText("读到章节：" + mChapterList[mBookBean!!.readNum - 1].title)
            }
        }
    }


    override fun onPause() {
        super.onPause()
        mMainScope.launch {
            mBookDB.update(mBookBean!!)
        }

    }


    private fun requestNet() = mMainScope.launch {
        val sl = getSL() ?: return@launch
////        tv_book_source.setText(getSourceStr())
        mAdapter.clear()
        mLoadingDialog.show()
        withContext(Dispatchers.IO) {
            Crawler.catalog(sl, object : ChapterCallback {
                override fun onResponse(chapters: List<Chapter>) {
                    launch(Dispatchers.Main) {
                        mLoadingDialog.dismiss()
                        if (chapters == null || chapters.isEmpty()) {
                            return@launch
                        }
                        mChapterList.addAll(chapters)
                        mAdapter.addAllData(chapters)
//                    mAdapter.orderByDesc()
                        //set  最新章节
                        val lastChapter = chapters[chapters.size - 1]
                        if (lastChapter != null) {
                            tv_book_newest_chapter.setText("最新章节：" + StringUtils.getStr(lastChapter.title))
                        }
                        if (mBookBean != null && mBookBean!!.readNum <= mChapterList.size) {
                            tv_read.setText("读到章节：" + mChapterList[mBookBean!!.readNum - 1].title)
                        }
                        //set 排序名字
//                    if (mAdapter.isAsc()) {
//                        tv_order_by.setText("正序")
//                    } else {
//                        tv_order_by.setText("倒序")
//                    }
                        if (isToDetail && mBookBean != null && mBookBean!!.readNum <= mChapterList.size) {
                            intentToReadWithPermissionCheck(mBookBean!!.readNum - 1)
                        }
                    }

                }

                override fun onError(msg: String) {
                    launch(Dispatchers.Main) {
                        mLoadingDialog.dismiss()
                        "没有搜索到".showToast()
                    }
                }
            })

        }

    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun intentToRead(position: Int) {
        val sourceId = mSourceList.get(mCurrentSourcePosition).source.id
        if (sourceId == SourceID.CANGSHU99) {
            ReadWebActivity.intentToThis(this,
                    mSearchBook,
                    mChapterList,
                    position,
                    mSourceList.get(mCurrentSourcePosition))

        } else {
            ReadActivity.intentToThis(this,
                    mSearchBook,
                    mChapterList,
                    mChapterList[position],
                    mSourceList.get(mCurrentSourcePosition))
        }
    }

    @OnPermissionDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun onWriteFileDenied() {
        "不开启文件权限，不能正常阅读".showToast()
    }


    private fun getSL(): SearchBook.SL? {
        return if (mCurrentSourcePosition < mSearchBook.sources.size) {
            mSearchBook.sources[mCurrentSourcePosition]
        } else null
    }

    private fun getSourceStr(): String {
        var sourceStr = "来源(" + mSearchBook.sources.size + ")："
        if (mCurrentSourcePosition < mSearchBook.sources.size) {
            val sl = mSearchBook.sources[mCurrentSourcePosition]
            if (sl != null && sl.source != null) {
                sourceStr += StringUtils.getStr(sl.source.name)
            }
        }
        return sourceStr
    }


    companion object {
        fun intentToThis(context: Context, bookBean: BookBean, isToDetail: Boolean = false) {
            context.startActivity(Intent(context, BookInfoActivity::class.java)
                    .putExtra("search_book", bookBean.tfSearchBook())
                    .putExtra("is_to_detail", isToDetail))

        }

    }

}
