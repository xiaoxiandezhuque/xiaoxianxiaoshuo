package com.xh.xiaoshuo.ui.main

import android.content.Intent
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.bigkoo.convenientbanner.ConvenientBanner
import com.qy.reader.common.entity.chapter.Chapter
import com.qy.reader.crawler.Crawler
import com.qy.reader.crawler.source.callback.ChapterCallback
import com.xh.common.base.BaseFragment
import com.xh.xiaoshuo.R
import com.xh.xiaoshuo.bean.BannerBean
import com.xh.xiaoshuo.bean.BookBean
import com.xh.xiaoshuo.mode.BookDB
import com.xh.xiaoshuo.ui.book.BookInfoActivity
import com.xh.xiaoshuo.ui.search.SearchActivity
import kotlinx.android.synthetic.main.fragment_bookshelf.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BookshelfFragment : BaseFragment() {

    private var convenientBanner: ConvenientBanner<BannerBean>? = null

    private lateinit var mAdapter: BookshelfAdapter

    private val mBookDB = BookDB()

    override fun getLayoutId(): Int {
        return R.layout.fragment_bookshelf
    }


    override fun initView() {
        iv_search.setOnClickListener {
            startActivity(Intent(activity, SearchActivity::class.java))
        }

        initBanner()

        recyclerview.setHasFixedSize(true)
        recyclerview.itemAnimator = DefaultItemAnimator()
        recyclerview.layoutManager = GridLayoutManager(activity, 3)
        mAdapter = BookshelfAdapter(activity!!)
        recyclerview.adapter = mAdapter
        mAdapter.setOnItemClickListener { view, postion, data ->
            if (mAdapter.mData.size - 1 == postion) {
//                最后一点，跳转搜索
                startActivity(Intent(activity, SearchActivity::class.java))

            } else {
                BookInfoActivity.intentToThis(activity!!, data, true)
            }

        }


    }


    private fun initBanner() {
//        BannerApiImpl().getBanner(bindToLifecycle(), {
//            success {
//                //自定义你的Holder，实现更多复杂的界面，不一定是图片翻页，其他任何控件翻页亦可。
//                convenientBanner = view!!.findViewById<ConvenientBanner<BannerBean>>(R.id.convenient_banner)
//                convenientBanner?.setPages(
//                        object : CBViewHolderCreator {
//                            override fun createHolder(itemView: View?): Holder<BannerBean> {
//
//                                return LocalImageHolderView(itemView)
//                            }
//
//                            override fun getLayoutId(): Int {
//                                return R.layout.item_banner
//                            }
//
//                        }, it)
//                        //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
//                        ?.setPageIndicator(intArrayOf(R.drawable.spot_gray, R.drawable.spot_blue))
//                        //设置指示器的方向
//                        ?.setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
//
//                //设置翻页的效果，不需要翻页效果可用不设
//                //.setPageTransformer(Transformer.DefaultTransformer);    集成特效之后会有白屏现象，新版已经分离，如果要集成特效的例子可以看Demo的点击响应。
////        convenientBanner.setManualPageable(false);//设置不能手动影响
//            }
//            fail {
//                Handler().postDelayed({
//                    initBanner()
//                }, 3000)
//            }
//        })
    }


    private fun getData() = mMainScope.launch {
        val dataList = mBookDB.queryBookshelfList()
        //最后一个是+号
        val bookBean = BookBean()
        bookBean.name = "添加更多"
        dataList.add(bookBean)
        mAdapter.resetData(dataList)
        notifyProgress()
    }

    private fun notifyProgress() = mMainScope.launch {

        for (i in 0 until (mAdapter.mData.size - 1)) {
            val bookBean = mAdapter.mData[i]
            val searchBook = bookBean.tfSearchBook()
            var list = mutableListOf<Chapter>()
            withContext(Dispatchers.IO) {
                Crawler.catalog(searchBook.sources[0], object : ChapterCallback {
                    override fun onResponse(chapters: List<Chapter>) {
                        list.addAll(chapters)
                    }

                    override fun onError(msg: String) {
                    }
                })

            }

            if (!(list == null || list!!.isEmpty()) && bookBean.maxNum != list!!.size) {
                bookBean.maxNum = list!!.size
                mBookDB.update(bookBean, false)
                mAdapter.notifyDataSetChanged()
            }
        }


    }

    override fun onResume() {
        super.onResume()
        convenientBanner?.startTurning(5000)
        getData()
    }

    override fun onPause() {
        super.onPause()
        convenientBanner?.stopTurning()
    }

}