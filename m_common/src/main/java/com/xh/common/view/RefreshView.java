package com.xh.common.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.xh.common.R;

import androidx.core.view.MotionEventCompat;
import androidx.core.view.VelocityTrackerCompat;
import androidx.core.view.ViewCompat;
import androidx.customview.widget.ViewDragHelper;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * Created by BHKJ on 2016/6/3.
 * 基于recyclerview的下拉刷新
 */

public class RefreshView extends FrameLayout {

    private Context mContext;

    private final ViewDragHelper mViewDragHelper;
    //判断是否拦截事件
    private boolean isOpenEvent = true;
    //    刷新的view
    private View mTopView;
    private TextView mTextView;
    private ImageView mImageView;

    private RecyclerView mView;
    //宽高
    private int mTopViewHeight;
    private int mViewWidth, mViewHeight;
    private final int mRefreshHeight;


    private int downY;

    //自己拦截recycleview的滑动， 自己来滑动
    private int scrollY;

    //是否在刷新中
    private boolean isRefresh;
    //recyclerview距离顶部的长度
    private int layoutToTop;
    //旋转动画
    private RotateAnimation animation;
    //刷新监听
    private OnRefreshListener mListener;

    //刷新完成  退回初始状态
    private boolean isRefreshToStart;

    private boolean isFirst = true;

    private VelocityTracker mVelocityTracker;


    private int mMaxFlingVelocity, mTouchSlop;
    private int mScrollPointerId;

    public void setOnRefreshListener(OnRefreshListener mListener) {
        this.mListener = mListener;
    }

    public interface OnRefreshListener {
        void onRefresh();

    }

    //刷新完成
    public void onRefreshComplete() {
        if (isRefresh) {
            mTextView.setText("　刷新成功　");
            isRefreshToStart = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mImageView.clearAnimation();
                    mViewDragHelper.smoothSlideViewTo(mView, 0, 0);
                    ViewCompat.postInvalidateOnAnimation(RefreshView.this);
                }
            }, 1000);

        }
    }

    //    开始刷新
    public void onRefreshStart() {
        if (isFirst) {
            isFirst = false;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    refresh();
                }
            }, 500);
        } else {
            refresh();
        }

    }

    private void refresh() {
        mImageView.startAnimation(animation);
        mTextView.setText("　正在刷新　");
        isRefresh = true;

        mViewDragHelper.smoothSlideViewTo(mView, 0, mRefreshHeight);
        ViewCompat.postInvalidateOnAnimation(RefreshView.this);
        if (mListener != null) {
            mListener.onRefresh();
        }
    }


    public RecyclerView getRecycleView() {
        return mView;
    }


    public void setOpenEvent(boolean openEvent) {
        isOpenEvent = openEvent;
    }

    public RefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //设置一个默认是布局
        mContext = context;
        mRefreshHeight = SizeUtils.dp2px(70);
        View.inflate(context, R.layout.view_refresh, this);
        mViewDragHelper = ViewDragHelper.create(this, 1f, callback);
        if (getChildCount() >= 2) {
            mView = (RecyclerView) getChildAt(1);
            mView.setHasFixedSize(true);
            mView.setItemAnimator(new DefaultItemAnimator());
            mView.setLayoutManager(new LinearLayoutManager(mContext));
            mTopView = getChildAt(0);
            mTextView = (TextView) mTopView.findViewById(R.id.refresh_textview);
            mImageView = (ImageView) mTopView.findViewById(R.id.refresh_imageview);

            animation = new RotateAnimation(0, 359, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
            animation.setDuration(500);
            animation.setInterpolator(new LinearInterpolator());
            animation.setRepeatCount(-1);
        }

        ViewConfiguration vc = ViewConfiguration.get(context);
        mTouchSlop = vc.getScaledTouchSlop();
        mMaxFlingVelocity = vc.getScaledMaximumFlingVelocity();

    }

    private final ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == mView;
        }


        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {

            int topa = top - scrollY;

            if (topa < 0) {
                scrollY = getScollYDistance();
                mView.scrollBy(0, -dy);
                return 0;
            }
            if (scrollY != 0) {
                mView.scrollBy(0, -scrollY);
                scrollY = 0;
            }

            if (top < 0) {
                return 0;
            } else if (top >= 0 && top <= mTopViewHeight) {
                //滑动速率
                return mView.getTop() + (top - mView.getTop()) / 2;
            } else {
                return mTopViewHeight;
            }

        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            layoutToTop = top;
            if (top == 0) {
                mTopView.layout(0, -mTopViewHeight, mViewWidth, 0);
                if (isRefresh) {
                    isRefresh = false;
                    isRefreshToStart = false;
                }
            } else if (top > 0 && top <= mTopViewHeight) {
                if (!isRefresh) {
                    if (top > mRefreshHeight) {
                        mTextView.setText("释放立即刷新");
                    } else {
                        mTextView.setText("　下拉刷新　");
                    }
                }

                mTopView.layout(0, -mTopViewHeight + top, mViewWidth, top);
            }
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            if (isRefresh) {
                if (isRefreshToStart) {
                    mViewDragHelper.smoothSlideViewTo(mView, 0, 0);
                    ViewCompat.postInvalidateOnAnimation(RefreshView.this);
                    return;
                }
                if (mView.getTop() >= mRefreshHeight / 1.5) {
                    mViewDragHelper.smoothSlideViewTo(mView, 0, mRefreshHeight);
                    ViewCompat.postInvalidateOnAnimation(RefreshView.this);
                } else {
                    isRefresh = false;
                    mViewDragHelper.smoothSlideViewTo(mView, 0, 0);
                    ViewCompat.postInvalidateOnAnimation(RefreshView.this);
                }

            } else {
                if (mView.getTop() >= mRefreshHeight) {
                    onRefreshStart();
                } else {
                    mViewDragHelper.smoothSlideViewTo(mView, 0, 0);
                    ViewCompat.postInvalidateOnAnimation(RefreshView.this);
                }
            }

        }
    };


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mTopView == null) {
            return;
        }
        mTopViewHeight = mTopView.getMeasuredHeight();
        mViewWidth = mView.getMeasuredWidth();
        mViewHeight = mView.getMeasuredHeight();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mTopView == null) {
            return;
        }
        mTopView.layout(0, -mTopViewHeight + layoutToTop, mViewWidth, layoutToTop);
        mView.layout(0, layoutToTop, mViewWidth, mViewHeight + layoutToTop);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean helper = mViewDragHelper.shouldInterceptTouchEvent(ev);

        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(ev);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                mVelocityTracker.clear();
                break;
        }
        if (isOpenEvent) {
            if (isRefresh) {
                return true;
            } else {
                boolean result = false;
                switch (MotionEventCompat.getActionMasked(ev)) {
                    case MotionEvent.ACTION_DOWN:
                        downY = (int) ev.getY();
                        mScrollPointerId = ev.getPointerId(0);
                        break;
                    case MotionEvent.ACTION_MOVE:

                        int moveY = (int) ev.getY() - downY;
                        if (Math.abs(moveY) > mTouchSlop) {

                            result = true;
                            mImageView.clearAnimation();
                            mViewDragHelper.captureChildView(mView, ev.getPointerId(0));

                        }

                        break;
                }
                return helper || result;
            }
        }

        return false;

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        if (mVelocityTracker == null) {
            mVelocityTracker = mVelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_UP:
                mVelocityTracker.computeCurrentVelocity(1000, mMaxFlingVelocity);
                final float yvel =
                        -VelocityTrackerCompat.getYVelocity(mVelocityTracker, mScrollPointerId);
                if (layoutToTop == 0) {
                    mView.fling(0, (int) yvel);
                }

                if (mVelocityTracker != null) {
                    mVelocityTracker.clear();
                }
                break;

        }
        return true;
    }

    @Override
    public void computeScroll() {
        if (mViewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public int getScollYDistance() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) mView.getLayoutManager();
        int position = layoutManager.findFirstVisibleItemPosition();
        View firstVisiableChildView = layoutManager.findViewByPosition(position);
        if (firstVisiableChildView != null) {
            int itemHeight = firstVisiableChildView.getHeight();
            return (position) * itemHeight - firstVisiableChildView.getTop();
        }

        return 0;
    }
}
