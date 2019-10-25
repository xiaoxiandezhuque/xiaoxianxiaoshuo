package com.xh.common.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.xh.common.R;


/**
 * Created by BHKJ on 2016/8/30.
 * <p>
 * 自己定义的actionbar  。。。
 */

public class TitleView extends RelativeLayout {

    private  final TextView tvTitle;
    private  final ImageView ivBack;
    private final  TextView tvRight;
    private final  ImageView ivRight;


    private OnClickRightListener mListener;


    public TitleView(final Context context, AttributeSet attrs) {
        super(context, attrs);
        View.inflate(context, R.layout.view_title, this);


        tvTitle = (TextView) getChildAt(0);
        ivBack = (ImageView) getChildAt(1);
        tvRight = (TextView) getChildAt(2);
        ivRight = (ImageView) getChildAt(3);

        final TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.TitleView);
        boolean isLeftDisplay = a.getBoolean(R.styleable.TitleView_isLeftDisplay, true);
        String titleText = a.getString(R.styleable.TitleView_titleText);
        String rightText = a.getString(R.styleable.TitleView_rightText);
        Drawable image = a.getDrawable(R.styleable.TitleView_rightImg);
        int imageLeft = a.getResourceId(R.styleable.TitleView_leftImg, R.drawable.nav_back);
        int color = a.getColor(R.styleable.TitleView_titleColor, Color.parseColor("#555555"));
        a.recycle();
        setTitleText(titleText);


        if (!isLeftDisplay) {
            ivBack.setVisibility(View.GONE);
        }
        ivBack.setImageResource(imageLeft);
        tvTitle.setTextColor(color);
        if (image != null) {
            ivRight.setVisibility(View.VISIBLE);
            ivRight.setImageDrawable(image);
            ivRight.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onClick(v);
                    }
                }
            });
        }
        setRightText(rightText);

        ivBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) context).finish();
            }
        });
    }

    public void setTitleText(String titleText) {
        if (!StringUtils.isEmpty(titleText)) {
            if (titleText.length() > 10) {
                titleText = titleText.substring(0, 10) + "...";
            }
            tvTitle.setText(titleText);
        }
    }
    public void showRrightImage() {
        if (ivRight.getVisibility() == View.GONE) {
            ivRight.setVisibility(View.VISIBLE);
        }
    }

    public void setRightText(String titleText) {
        if (!StringUtils.isEmpty(titleText)) {
            tvRight.setVisibility(View.VISIBLE);
            tvRight.setText(titleText);
            tvRight.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onClick(v);
                    }
                }
            });
        }
    }

    public void hideRight() {
        if (tvRight.getVisibility() == View.VISIBLE) {
            tvRight.setVisibility(View.GONE);
        }
        if (ivRight.getVisibility() == View.VISIBLE) {
            ivRight.setVisibility(View.GONE);
        }
    }

    public void hideBack() {
        ivBack.setVisibility(View.GONE);
    }


    public void setOnClickRightListener(OnClickRightListener mListener) {
        this.mListener = mListener;
    }

    public interface OnClickRightListener {
        void onClick(View v);
    }
}
