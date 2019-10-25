package com.xh.xiaoshuo.ui.book.read

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.webkit.JsResult
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AlertDialog
import com.qy.reader.common.entity.book.SearchBook
import com.qy.reader.common.entity.chapter.Chapter
import com.xh.common.base.BaseActivity
import com.xh.xiaoshuo.R
import com.xh.xiaoshuo.bean.BookBean
import com.xh.xiaoshuo.mode.BookDB
import kotlinx.android.synthetic.main.activity_web.*
import kotlinx.coroutines.launch
import java.util.*


/**
 * Created by BHKJ on 2016/8/31.
 */

class ReadWebActivity : BaseActivity() {

    private lateinit var searchBook: SearchBook
    private lateinit var chapterList: ArrayList<Chapter>
    private lateinit var source: SearchBook.SL
    private var position: Int = 0
    private var mBookBean: BookBean? = null

    private var scrollY = 0

    private val mBookDB = BookDB()

    override fun getLayoutId(): Int {
        return R.layout.activity_web
    }

    override fun initView() {

        if (intent == null || intent.extras == null) {
            finish()
            return
        }
        searchBook = intent.extras!!.getSerializable("book") as SearchBook
        chapterList = intent.extras!!.getParcelableArrayList("chapter_list")
        position = intent.extras!!.getInt("position")
        source = intent.extras.getSerializable("source") as SearchBook.SL
//        if (!StringUtils.isTrimEmpty(searchBook!!.title)) {
        title_view!!.setTitleText(chapterList.get(position).title + "")
//        }


        web_view!!.settings.javaScriptEnabled = true
        //        mWebView.getSettings().setDomStorageEnabled(true);
        //        //开启 database storage API 功能
        //        mWebView.getSettings().setDatabaseEnabled(true);
        //        mWebView.getSettings().setAllowFileAccess(true);
        //        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        //        覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        web_view!!.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
            }

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                var isChange = false
                if (chapterList!!.size > position) {
                    if ((url + "") == chapterList!![position + 1].link) {
                        position = position + 1
                        isChange = true
                    } else {
                        for (i in chapterList.indices) {
                            if ((url + "") == chapterList!![i].link) {
                                position = i
                                isChange = true
                            }
                        }
                    }
                }
                if (isChange) {
                    view?.loadUrl(url)
                    title_view.setTitleText(chapterList.get(position).title + "")
                }
                return true
            }
        }



        web_view!!.webChromeClient =
                object : WebChromeClient() {
                    override fun onProgressChanged(view: WebView?, progress: Int) {// 载入进度改变而触发
                        super.onProgressChanged(view, progress)
                        if (progress < 20) {
                            mLoadingDialog.show()
                        }
                        if (progress == 100) {
                            mLoadingDialog.dismiss()
                        }

                    }

                    //扩展支持alert事件
                    override fun onJsAlert(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean {
                        val builder = AlertDialog.Builder(view!!.context)
                        builder.setTitle("提示").setMessage(message).setPositiveButton("确定", null)
                        builder.setCancelable(false)
                        val dialog = builder.create()
                        dialog.show()
                        result?.confirm()
                        return true
                    }
                }
//        web_view.setOnScrollChangedCallback { dx, dy ->
//            scrollY += dy
//        }

        web_view!!.loadUrl(chapterList!![position].link)
        getBookBeanFromDB()
    }

    override fun onRestart() {
        super.onRestart()

    }

    override fun onPause() {
        super.onPause()
        mMainScope.launch {
            mBookBean?.readNum = position + 1
            mBookBean?.readPage = scrollY
            mBookBean?.maxNum = chapterList.size
            mBookDB.update(mBookBean!!)
        }

    }


    private fun getBookBeanFromDB() {
        mMainScope.launch {
            mBookBean = mBookDB.query(searchBook, source)
            if ((position + 1) == mBookBean!!.readNum) {
                web_view.scrollTo(0, mBookBean!!.readPage)
            }
        }

    }

    override fun onBackPressed() {
//        if (web_view!!.canGoBack()) {
//            web_view!!.goBack()
//        } else {
        finish()
//        }
    }

    companion object {


        fun intentToThis(context: Context,
                         searchBook: SearchBook,
                         chapterList: ArrayList<Chapter>,
                         position: Int,
                         source: SearchBook.SL) {
            val intent = Intent(context, ReadWebActivity::class.java)

            intent.putExtra("book", searchBook)
            intent.putParcelableArrayListExtra("chapter_list", chapterList)
            intent.putExtra("position", position)
            intent.putExtra("source", source)
            context.startActivity(intent)
        }
    }

}