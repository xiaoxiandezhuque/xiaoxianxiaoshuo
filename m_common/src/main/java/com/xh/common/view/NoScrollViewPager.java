package com.xh.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

/**
 * Created by 49039 on 2016/5/3.  不能滑动的viewpager
 */
public class NoScrollViewPager extends ViewPager {
    public NoScrollViewPager(Context context) {
        super(context);
    }

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private boolean scroll = true;

    public void setScroll(boolean scroll) {
        this.scroll = scroll;
    }


    @Override
    public boolean onTouchEvent(MotionEvent arg0) {

        if (scroll)
            return false;
        else
            return super.onTouchEvent(arg0);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (scroll)
            return false;
        else
            return super.onInterceptTouchEvent(arg0);
    }




}
