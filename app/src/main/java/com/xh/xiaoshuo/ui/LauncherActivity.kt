package com.xh.xiaoshuo.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.BarUtils
import com.xh.xiaoshuo.BuildConfig
import com.xh.xiaoshuo.R
import com.xh.xiaoshuo.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_launcher.*

class LauncherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        ScreenUtils.setFullScreen(this)
        BarUtils.setStatusBarVisibility(this, false)
        setContentView(R.layout.activity_launcher)
        if (BuildConfig.DEBUG) {
            toMain()
        } else {
            tv_splash_skip.postDelayed({
                toMain()
            }, 2000)

        }

        tv_splash_skip.setOnClickListener {
            toMain()
        }


    }


    private fun toMain() {
//        if (BuildConfig.DEBUG) {
//            val searchBook = SearchBook()
//            searchBook.author = "月遇"
//            searchBook.cover = "https://www.liewen.cc/files/article/image/32/32412/32412s.jpg"
//            searchBook.desc = "当我睁眼的时候，你们的故事就已经注定。这是一个道教道君重生在漫威世界的故事。一切从毒液开始。"
//            searchBook.title = "重生在漫威里的道君"
//            searchBook.sources.add(SearchBook.SL("https://www.liewen.cc/b/32/32412/",
//                    Source(SourceID.LIEWEN, "猎文网", "https://www.liewen.cc/search.php?keyword=%s", 1)))
//
//            startActivity(Intent(this, BookInfoActivity::class.java)
//                    .putExtra("search_book", searchBook))
//        } else {
        startActivity(Intent(this, MainActivity::class.java))

//        }

        finish()
    }
}

