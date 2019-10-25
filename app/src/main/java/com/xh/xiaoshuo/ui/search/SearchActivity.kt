package com.xh.xiaoshuo.ui.search

import android.content.Intent
import android.text.TextUtils
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.qy.reader.common.entity.book.SearchBook
import com.qy.reader.crawler.Crawler
import com.qy.reader.crawler.source.callback.SearchCallback
import com.xh.common.base.BaseActivity
import com.xh.common.ktextended.isEmptyForMy
import com.xh.common.ktextended.showToast
import com.xh.xiaoshuo.BuildConfig
import com.xh.xiaoshuo.R
import com.xh.xiaoshuo.ui.book.BookInfoActivity
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchActivity : BaseActivity() {

    private lateinit var mAdapter: SearchAdapter
    override fun getLayoutId(): Int {
        return R.layout.activity_search
    }

    override fun initView() {

        recyclerview.setHasFixedSize(true)
        recyclerview.itemAnimator = DefaultItemAnimator()
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        mAdapter = SearchAdapter(this)
        recyclerview.adapter = mAdapter

        mAdapter.setOnItemClickListener { view, postion, data ->
            startActivity(Intent(this@SearchActivity, BookInfoActivity::class.java)
                    .putExtra("search_book", data))
        }




        btn_sourch.setOnClickListener {
            val content = et_content.text.toString()
            if (content?.isEmptyForMy() ?: true) {
                return@setOnClickListener
            }

            search(content)


        }
        btn_cancel.setOnClickListener {
            onBackPressed()
        }

        if (BuildConfig.DEBUG) {
            et_content.setText("活着")
        }

    }


    private fun search(content: String) = mMainScope.launch {
        mLoadingDialog.show()
        mAdapter.searchStr = content
        mAdapter.mData.clear()
        val list = mutableListOf<SearchBook>()
        withContext(Dispatchers.IO) {
            Crawler.search(content, object : SearchCallback {
                override fun onResponse(keyword: String, appendList: List<SearchBook>) {
                    list.addAll(appendList)
                }

                override fun onFinish() {
                }

                override fun onError(msg: String) {
                    "搜索失败".showToast()
                }
            })
        }

        if (!(list == null || list!!.isEmpty())) {
            for (newBook in list) {
                var exists = false
                for (book in mAdapter.mData) {
                    if (TextUtils.equals(book.title, newBook.title) && !newBook.sources.isEmpty()) {
                        if (TextUtils.isEmpty(book.cover) && !TextUtils.isEmpty(newBook.cover)) {
                            book.cover = newBook.cover
                        }
                        book.sources.add(newBook.sources[0])
                        exists = true
                        break
                    }
                }
                if (!exists) {
                    mAdapter.addData(newBook)
                }
            }
        }

        ("共搜索到" + mAdapter.mData.size + "本书").showToast()
        mAdapter.notifyDataSetChanged()
        mLoadingDialog.dismiss()
    }

}
