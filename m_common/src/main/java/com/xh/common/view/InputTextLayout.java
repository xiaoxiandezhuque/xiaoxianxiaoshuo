package com.xh.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.xh.common.R;

import androidx.annotation.Nullable;


/**
 * Created by Administrator on 2017/5/10.
 */

public class InputTextLayout extends LinearLayout {
    private EditText editText;
    private TextView textView;
    private String text;

    public InputTextLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);
        View.inflate(context, R.layout.view_input_text, this);
        textView = (TextView) getChildAt(0);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.InputTextLayout);
        text = array.getString(R.styleable.InputTextLayout_text);
        array.recycle();

        if (!StringUtils.isEmpty(text)){
            textView.setText(text);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildAt(1) instanceof EditText){
            editText = (EditText) getChildAt(1);
            setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    editText.setFocusable(true);
                }
            });
        }


    }
}
