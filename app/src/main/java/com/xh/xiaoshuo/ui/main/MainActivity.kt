package com.xh.xiaoshuo.ui.main

import android.content.Intent
import android.net.Uri
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.SPUtils
import com.qy.reader.common.utils.FileUtils
import com.xh.common.base.BaseActivity
import com.xh.common.ktextended.showToast
import com.xh.common.view.MyFragmentPagerAdapter
import com.xh.xiaoshuo.R
import com.xh.xiaoshuo.constant.SPConstant
import com.xh.xiaoshuo.network.api.VersionApi
import com.yx.jjs.ui.home.main.MyFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch


class MainActivity : BaseActivity() {

    private var current = 0

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }


    override fun initView() {
        getVersion()

        FileUtils.createBookRootPath()
        val imageHome = arrayOf(R.drawable.ic_bookshelf, R.drawable.ic_bookshelf_select)
        val imageMy = arrayOf(R.drawable.ic_my, R.drawable.ic_my_select)
        val image = arrayOf(imageHome, imageMy)

        val llBtn = arrayOfNulls<LinearLayout>(image.size)
        val iv = arrayOfNulls<ImageView>(image.size)

        val fragments: List<Fragment> = arrayListOf(BookshelfFragment(), MyFragment())

        view_pager.setAdapter(MyFragmentPagerAdapter(fragments, supportFragmentManager))
        view_pager.setCurrentItem(0)
        view_pager.setOffscreenPageLimit(image.size)

        for (i in 0 until ll_bottom.childCount) {

            llBtn[i] = ll_bottom.getChildAt(i) as LinearLayout
            iv[i] = llBtn[i]?.getChildAt(0) as ImageView
            llBtn[i]?.setTag(i)
            llBtn[i]?.setOnClickListener {
                val i = it.getTag() as Int
                view_pager.setCurrentItem(i);
            }


        }

        view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                iv[current]?.setImageResource(image[current][0])
                current = position
                iv[position]?.setImageResource(image[position][1])
//                LogUtils.e("xuanzhong  $current")
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })

    }

    private fun getVersion() =mMainScope.launch{
        val data =VersionApi().getVersion()
        if (data!=null){
            SPUtils.getInstance().put(SPConstant.LINK,data.link)

            if (AppUtils.getAppVersionCode() < data.version) {
                val dialog = AlertDialog.Builder(this@MainActivity)
                        .setTitle("有新版本需要更新")
                        .setMessage(data.content ?: "")
                        .setPositiveButton("确定", { dialog, which ->
                            dialog.dismiss()
                            val uri = Uri.parse(data.link ?: "")
                            val intent = Intent(Intent.ACTION_VIEW, uri)
                            startActivity(intent)
                        })
                if (data.isUpdate != 0) {
                    dialog.setPositiveButton("确定", { dialog, which ->
                        finish()
                        val uri = Uri.parse(data.link ?: "")
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        startActivity(intent)
                    })
                            .setCancelable(false)
                } else {
                    dialog.setNegativeButton("取消", { dialog, which ->
                        dialog.dismiss()
                    })
                            .setPositiveButton("确定", { dialog, which ->
                                dialog.dismiss()
                                val uri = Uri.parse(data.link ?: "")
                                val intent = Intent(Intent.ACTION_VIEW, uri)
                                startActivity(intent)
                            })
                            .setCancelable(true)
                }
                dialog.show()
            }

        }

    }

    private var firstClick: Long = 0
    override fun onBackPressed() {
        val secondClick = System.currentTimeMillis()
        if (secondClick - firstClick > 2000) {
            "再次点击退出".showToast()
            firstClick = secondClick
        } else {
            finish()
        }
    }

}
