package com.xh.xiaoshuo.ui.history

import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.xh.common.base.BaseActivity
import com.xh.xiaoshuo.R
import com.xh.xiaoshuo.mode.BookDB
import com.xh.xiaoshuo.ui.book.BookInfoActivity
import kotlinx.android.synthetic.main.activity_history.*
import kotlinx.coroutines.launch

class HistoryActivity : BaseActivity() {

    private lateinit var mAdapter: HistoryAdapter

    override fun getLayoutId(): Int {
        return R.layout.activity_history
    }

    override fun initView() {
        mAdapter = HistoryAdapter(this)
        recyclerview.setHasFixedSize(true)
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recyclerview.adapter = mAdapter
        mAdapter.setOnItemClickListener { view, postion, data ->
            BookInfoActivity.intentToThis(this, data)
        }
    }

    override fun onResume() {
        super.onResume()
        getData()
    }


    private fun getData() = mMainScope.launch {
        mAdapter.resetData(BookDB().queryHistoryList())
    }


}