package com.xh.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xh.common.R;

import androidx.annotation.Nullable;


/**
 * Created by Administrator on 2017/5/8.
 */

public class InputImgLayout extends LinearLayout {
    private EditText editText;
    private ImageView imageView;
    private int img, imgFocus;

    public InputImgLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);
        View.inflate(context, R.layout.view_input_img, this);
        imageView = (ImageView) getChildAt(0);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.InputImgLayout);
        img = array.getResourceId(R.styleable.InputImgLayout_img, 0);
        imgFocus = array.getResourceId(R.styleable.InputImgLayout_imgFocus, 0);
        array.recycle();
        imageView.setImageResource(img);

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        editText = (EditText) getChildAt(1);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setFocusable(true);
            }
        });
        editText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    imageView.setImageResource(imgFocus);
                } else {
                    imageView.setImageResource(img);
                }
            }
        });

    }
}
