package com.xh.xiaoshuo.ui

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.webkit.JsResult
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AlertDialog
import com.xh.common.base.BaseActivity
import com.xh.xiaoshuo.R
import kotlinx.android.synthetic.main.activity_web.*


/**
 * Created by BHKJ on 2016/8/31.
 */

class WebActivity : BaseActivity() {


    override fun getLayoutId(): Int {
        return R.layout.activity_web
    }

    override fun initView() {

        if (intent == null || intent.extras == null) {
            finish()
            return
        }
        val title = intent.extras!!.getString("title")
        val url = intent.extras!!.getString("url")
        title_view!!.setTitleText(title)

        web_view!!.settings.javaScriptEnabled = true

        web_view!!.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
            }

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {

                view?.loadUrl(url)

                return true
            }
        }

//        NetworkDialog.INSTANCE.show("加载中")

        web_view!!.webChromeClient =
                object : WebChromeClient() {
                    override fun onProgressChanged(view: WebView?, progress: Int) {// 载入进度改变而触发
//                        if (progress == 100) {
//                            NetworkDialog.INSTANCE.dismiss()
//                        }
                        super.onProgressChanged(view, progress)
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


        web_view!!.loadUrl(url)

    }

    override fun onRestart() {
        super.onRestart()

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
                         title: String,
                         url: String) {
            val intent = Intent(context, WebActivity::class.java)
            intent.putExtra("title", title)
            intent.putExtra("url", url)
            context.startActivity(intent)
        }
    }

}