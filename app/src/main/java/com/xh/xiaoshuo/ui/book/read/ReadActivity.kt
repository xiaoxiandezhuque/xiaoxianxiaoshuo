package com.xh.xiaoshuo.ui.book.read


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.TimeUtils
import com.qy.reader.common.entity.book.SearchBook
import com.qy.reader.common.entity.chapter.Chapter
import com.qy.reader.common.utils.SPUtils
import com.qy.reader.common.widgets.reader.BookManager
import com.qy.reader.common.widgets.reader.OnPageStateChangedListener
import com.qy.reader.common.widgets.reader.annotation.SlideMode
import com.qy.reader.crawler.Crawler
import com.qy.reader.crawler.source.callback.ContentCallback
import com.xh.common.base.BaseActivity
import com.xh.common.ktextended.hide
import com.xh.common.ktextended.show
import com.xh.common.ktextended.showToast
import com.xh.xiaoshuo.R
import com.xh.xiaoshuo.bean.BookBean
import com.xh.xiaoshuo.bean.TextBean
import com.xh.xiaoshuo.constant.SPConstant
import com.xh.xiaoshuo.mode.BookDB
import com.xh.xiaoshuo.ui.book.BookInfoAdapter
import com.xh.xiaoshuo.util.ScreenUtil
import kotlinx.android.synthetic.main.activity_read.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import java.text.SimpleDateFormat

/**
 * Created by yuyuhang on 2018/1/13.
 */
class ReadActivity : BaseActivity() {


    private val mChapterList = ArrayList<Chapter>()
    private var currentChapter = 1  //从1开始
    private var currentPage = 1

    private lateinit var mSource: SearchBook.SL
    private lateinit var mBook: SearchBook
    private lateinit var mBookNum: String

    private lateinit var mHintDialog: AlertDialog

    private lateinit var mAdapter: BookInfoAdapter

    private lateinit var mTextSettingDialog: TextSettingDialog

    private val mSingThread = newSingleThreadContext("MyOwnThread")

    private lateinit var mTextBean: TextBean

    private var mBookBean: BookBean? = null

    private val mHandler = Handler()

    private val mBookDB = BookDB()

    private val mBgColorAndTextColor = arrayOf(arrayOf("#f4f6f9", "#4e4e4e", "#999999"),
            arrayOf("#ebdbbf", "#4b3b2f", "#9b8d7c"),
            arrayOf("#f7e2e1", "#8b535d", "#b49a9c"),
            arrayOf("#e2edd6", "#363e2c", "#868c81"),
            arrayOf("#000000", "#666666", "#333333"))

    override fun getLayoutId(): Int {
        return R.layout.activity_read
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        ScreenUtils.setFullScreen(this)

        super.onCreate(savedInstanceState)
    }

    override fun initView() {

        val list = intent.getParcelableArrayListExtra<Chapter>("chapter_list")
        if (list == null) {
            "章节加载失败".showToast()
            return
        }
        mBook = intent.getSerializableExtra("book") as SearchBook
        mSource = intent.getSerializableExtra("source") as SearchBook.SL
        mBookNum = BookManager.getInstance().getBookNum(mBook.title, mBook.author)

        title_view.setTitleText(mBook.title + "")
        mChapterList.addAll(list)

        val chapter = intent.getParcelableExtra("chapter") as Chapter

        currentChapter = getCurrentChapter(chapter)

        getCharpterContent(chapter)
        getLastCharpterContent()
        getNextCharpterContent()

        mHintDialog = AlertDialog.Builder(this@ReadActivity)
                .setTitle("提示")
                .setMessage("没有上一章或者下一章了")
                .create()

        read_view.setOnPageStateChangedListener(object : OnPageStateChangedListener {

            override fun loadChapter() {
                mLoadingDialog.show()
            }

            override fun noMore() {
                if (!mHintDialog.isShowing) {
                    mHintDialog.show()
                }
            }

            override fun onCenterClick() {
                if (ll_bottom.visibility == View.VISIBLE) {
                    ll_bottom.hide()
                    title_view.hide()
                } else {
                    ll_bottom.show()
                    title_view.show()
                }
            }

            override fun onChapterChanged(currentChapter: Int, fromChapter: Int, fromUser: Boolean) {
                this@ReadActivity.currentChapter = currentChapter;
                mAdapter.currectChapter = currentChapter - 1
                getLastCharpterContent()
                getNextCharpterContent()
                title_view.hide()
                ll_bottom.hide()
            }

            override fun onPageChanged(currentPage: Int, currentChapter: Int) {
                this@ReadActivity.currentPage = currentPage
                title_view.hide()
                ll_bottom.hide()
            }

            override fun onChapterLoadFailure(currentChapter: Int) {
                getLastCharpterContent()
                getNextCharpterContent()
            }
        })

        ll_drawer.setOnClickListener(null)

        tv_title.setText(mBook.title + "")
        recyclerview.setLayoutManager(LinearLayoutManager(this))
//        recyclerview.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        mAdapter = BookInfoAdapter(this)
        recyclerview.setAdapter(mAdapter)
        mAdapter.addAllData(mChapterList)
        mAdapter.currectChapter = currentChapter - 1
        mAdapter.setOnItemClickListener { view, postion, data ->
            //            getCharpterContent(mChapterList.get(postion))

            mLoadingDialog.show()
            getCharpterContent(mChapterList.get(postion), {
                read_view.jumpChapter(postion + 1)
                drawerlayout.closeDrawers()
                ScreenUtil.hideSystemNavigationBar(this)
            })

        }

        drawerlayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerStateChanged(p0: Int) {

            }

            override fun onDrawerSlide(p0: View, p1: Float) {

            }

            override fun onDrawerClosed(p0: View) {
            }

            override fun onDrawerOpened(p0: View) {
                recyclerview.scrollToPosition(currentChapter - 1)
                title_view.hide()
                ll_bottom.hide()
            }

        })

        for (i in 0..3) {
            val linearLayout = ll_bottom.getChildAt(i)
            linearLayout.setTag(i)
            linearLayout.setOnClickListener {
                val i = it.getTag() as Int
                when (i) {
                    0 -> {
                        drawerlayout.openDrawer(ll_drawer)
                    }
                    1 -> {
                        mTextSettingDialog.show()
                    }
                    2 -> {
                        if (mBookBean != null) {
                            if (mBookBean?.state == 0) {
                                mBookBean?.state = 1
                                tv_bookshelf.setText("已在书架")
                                "已加入书架".showToast()
                            }
                        } else {
                            "加入书架失败".showToast()
                        }

                    }
                    3 -> {
                        "分享".showToast()
                    }
                }
                title_view.hide()
                ll_bottom.hide()

            }
        }



        initText()

        getBookBeanFromDB()
    }


    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)

        ScreenUtil.hideSystemNavigationBar(this)
    }


    private fun getCurrentChapter(chapter: Chapter): Int {
        var chapterIndex = 0
        if (chapter != null) {
            for (i in mChapterList.indices) {
                val c = mChapterList[i]
                if (TextUtils.equals(c.link, chapter.link)) {
                    chapterIndex = i
                    break
                }
            }
        }
        return chapterIndex + 1
    }


    private fun getCharpterContent(chapter: Chapter, listener: (() -> Unit)? = null) {
        if (BookManager.getInstance().isExists(mSource.source.id, mBookNum, chapter.title)) {
            if (!read_view.isInit()) {
                read_view.initChapterList(mSource.source.id, mBookNum, mChapterList, currentChapter, 1)
            }
            listener?.invoke()
            read_view.postInvalidate()
            mLoadingDialog.dismiss()
            return
        }

        mMainScope.launch(mSingThread) {
            Crawler.content(mSource, chapter.link, object : ContentCallback {
                override fun onResponse(content: String?) {
                    BookManager.getInstance().saveContentFile(mSource.source.id, mBookNum, chapter.title,
                            "${chapter.title}\n$content")
                    launch(Dispatchers.Main) {
                        if (!read_view.isInit()) {
                            read_view.initChapterList(mSource.source.id, mBookNum, mChapterList, currentChapter, 1)
                        }
                        read_view.postInvalidate()
                        listener?.invoke()
                        mLoadingDialog.dismiss()
                    }
                }

                override fun onError(msg: String?) {
                    msg?.showToast()
                }

            })
        }

        read_view.setSlideMode(SlideMode.FOLLOW)
    }


    private fun getLastCharpterContent() {
        if (currentChapter - 2 > 0 &&
                !BookManager.getInstance().isExists(mSource.source.id, mBookNum, mChapterList.get(currentChapter - 2).title)) {
            getCharpterContent(mChapterList.get(currentChapter - 2))
        }
    }

    private fun getNextCharpterContent() {
        if (currentChapter < mChapterList.size &&
                !BookManager.getInstance().isExists(mSource.source.id, mBookNum, mChapterList.get(currentChapter).title)) {
            getCharpterContent(mChapterList.get(currentChapter))
        }
    }

    private fun initText() {
        mTextBean = TextBean(SPUtils.getInstance().getInt(SPConstant.TEXT_SIZE, 16),
                SPUtils.getInstance().getInt(SPConstant.TEXT_SPACING, 8),
                SPUtils.getInstance().getInt(SPConstant.TEXT_SLIDE, SlideMode.FOLLOW),
                SPUtils.getInstance().getInt(SPConstant.TEXT_BG_AND_TEXT_COLOR, 3))

        read_view.setSlideMode(mTextBean.mode)
        read_view.pageFactory.setTextSize(mTextBean.size)
        read_view.pageFactory.setTextSpacint(mTextBean.spacing)
        read_view.pageFactory.setBgAndTextColor(mBgColorAndTextColor[mTextBean.color][0],
                mBgColorAndTextColor[mTextBean.color][1],
                mBgColorAndTextColor[mTextBean.color][2])
        read_view.postInvalidate()

        mTextSettingDialog = TextSettingDialog(this, mTextBean)
        mTextSettingDialog.addSizeChangeListener { bool, left, right ->
            if (bool) {
                if (mTextBean.size <= 30) {
                    mTextBean.size += 2
                    if (mTextBean.size == 32) {
                        right.isEnabled = false
                    }
                } else {
                    right.isEnabled = false
                }
                left.isEnabled = true
            } else {
                if (mTextBean.size >= 12) {
                    mTextBean.size -= 2

                    if (mTextBean.size == 10) {
                        left.isEnabled = false
                    }
                } else {
                    left.isEnabled = false
                }
                right.isEnabled = true
            }
            SPUtils.getInstance().put(SPConstant.TEXT_SIZE, mTextBean.size)
            read_view.pageFactory.setTextSize(mTextBean.size)
            read_view.postInvalidate()
        }
        mTextSettingDialog.addSpacingChangeListener { bool, left, right ->
            if (bool) {
                if (mTextBean.spacing <= 16) {
                    mTextBean.spacing += 2
                    if (mTextBean.spacing == 18) {
                        right.isEnabled = false
                    }
                } else {
                    right.isEnabled = false
                }
                left.isEnabled = true
            } else {
                if (mTextBean.spacing >= 6) {
                    mTextBean.spacing -= 2

                    if (mTextBean.spacing == 4) {
                        left.isEnabled = false
                    }
                } else {
                    left.isEnabled = false
                }
                right.isEnabled = true
            }
            SPUtils.getInstance().put(SPConstant.TEXT_SIZE, mTextBean.spacing)
            read_view.pageFactory.setTextSpacint(mTextBean.spacing)
            read_view.postInvalidate()
        }
        mTextSettingDialog.addSlideChangeListener { mode, left, right ->
            when (mode) {
                SlideMode.FOLLOW -> {
                    left.isEnabled = false
                    right.isEnabled = true

                }
                SlideMode.OVERLAP -> {
                    left.isEnabled = true
                    right.isEnabled = false

                }
            }
            SPUtils.getInstance().put(SPConstant.TEXT_SLIDE, mode)
            read_view.setSlideMode(mode)
            read_view.postInvalidate()

        }
        mTextSettingDialog.addBgChangeListener {
            //            read_view.setBackgroundColor(Color.parseColor(mBgColorAndTextColor[it][0]))
//            read_view.pageFactory.set

            SPUtils.getInstance().put(SPConstant.TEXT_BG_AND_TEXT_COLOR, it)
            read_view.pageFactory.setBgAndTextColor(mBgColorAndTextColor[it][0],
                    mBgColorAndTextColor[it][1],
                    mBgColorAndTextColor[it][2])
            read_view.postInvalidate()
        }

    }

    private fun getBookBeanFromDB() {
        mMainScope.launch {
            mBookBean = mBookDB.query(mBook, mSource)
            if (mBookBean?.state == 1) {
                tv_bookshelf.setText("已在书架")
            } else {
                tv_bookshelf.setText("加入书架")
            }

            if (read_view.isInit) {
                if (currentChapter == mBookBean!!.readNum) {
                    currentPage = mBookBean!!.readPage
                    read_view.jumpPage(currentPage)
                } else {
                    currentChapter = mBookBean!!.readNum
                    currentPage = mBookBean!!.readPage
                    read_view.jumpChapter(currentChapter)
                    read_view.jumpPage(currentPage)
                }

            }
        }

    }

    override fun onResume() {
        super.onResume()
        read_view.pageFactory.setTime(TimeUtils.getNowString(SimpleDateFormat("HH:mm")))
        read_view.postInvalidate()
        setTime()
    }

    override fun onPause() {
        super.onPause()
        mMainScope.launch {
            mBookBean?.readNum = currentChapter
            mBookBean?.readPage = currentPage
            mBookBean?.maxNum = mChapterList.size
            mBookDB.update(mBookBean!!)
            mHandler.removeCallbacksAndMessages(null)
        }

    }

    private fun setTime() {
        mHandler.postDelayed({
            read_view.pageFactory.setTime(TimeUtils.getNowString(SimpleDateFormat("HH:mm")))
            read_view.postInvalidate()
            setTime()
        }, 30000)

    }


    companion object {
        fun intentToThis(context: Context, searchBook: SearchBook
                         , chapterList: ArrayList<Chapter>
                         , chapter: Chapter, source: SearchBook.SL) {
            context.startActivity(Intent(context, ReadActivity::class.java)
                    .putExtra("book", searchBook)
                    .putParcelableArrayListExtra("chapter_list", chapterList)
                    .putExtra("chapter", chapter)
                    .putExtra("source", source))
        }
    }

}
