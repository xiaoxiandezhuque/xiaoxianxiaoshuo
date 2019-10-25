package com.xh.common.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.xh.common.R;


public class LoadingDialog extends Dialog {
    public static final int NUM = 4;
    private Point mScreenPoint = new Point();


    private Activity mRootActivity = null;

    private TextView mMessageView;

    private ProgressBar mIcon;

    String mMessage = "加载中...";

    public LoadingDialog(Activity arg) {
        super(arg, R.style.LoginDialog);
        mRootActivity = arg;
    }

    public void setMessage(String message) {
        mMessage = message;
        if (mMessageView != null) {
            mMessageView.setText(message);
        }
    }

    public boolean isShowing() {
        return super.isShowing();
    }

    @Override
    public void show() {
        if (mIcon != null) {
            mIcon.setVisibility(View.VISIBLE);
        }
        super.show();
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new BitmapDrawable());
        this.setContentView(R.layout.dialog_loading);
        initDialogWindow();
        mMessageView = (TextView) findViewById(R.id.text);
        mMessageView.setText(mMessage);
        mIcon = (ProgressBar) findViewById(R.id.icon);
        Animation operatingAnim = AnimationUtils.loadAnimation(mRootActivity, R.anim.common_loading);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        //mIcon.startAnimation(operatingAnim);
    }

    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    private void initDialogWindow() {
        Window dialogWindow = getWindow();

        /*
         * lp.x与lp.y表示相对于原始位置的偏移.
         * 当参数值包含Gravity.LEFT时,对话框出现在左边,所以lp.x就表示相对左边的偏移,负值忽略.
         * 当参数值包含Gravity.RIGHT时,对话框出现在右边,所以lp.x就表示相对右边的偏移,负值忽略.
         * 当参数值包含Gravity.TOP时,对话框出现在上边,所以lp.y就表示相对上边的偏移,负值忽略.
         * 当参数值包含Gravity.BOTTOM时,对话框出现在下边,所以lp.y就表示相对下边的偏移,负值忽略.
         * 当参数值包含Gravity.CENTER_HORIZONTAL时
         * ,对话框水平居中,所以lp.x就表示在水平居中的位置移动lp.x像素,正值向右移动,负值向左移动.
         * 当参数值包含Gravity.CENTER_VERTICAL时
         * ,对话框垂直居中,所以lp.y就表示在垂直居中的位置移动lp.y像素,正值向右移动,负值向左移动.
         * gravity的默认值为Gravity.CENTER,即Gravity.CENTER_HORIZONTAL |
         * Gravity.CENTER_VERTICAL.
         *
         * 本来setGravity的参数值为Gravity.LEFT | Gravity.TOP时对话框应出现在程序的左上角,但在
         * 我手机上测试时发现距左边与上边都有一小段距离,而且垂直坐标把程序标题栏也计算在内了,
         * Gravity.LEFT, Gravity.TOP, Gravity.BOTTOM与Gravity.RIGHT都是如此,据边界有一小段距离
         */
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        lp.y = 0;
        lp.dimAmount = 0.0f;
        WindowManager m = mRootActivity.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用

        try {
            d.getSize(mScreenPoint);
        } catch (NoSuchMethodError ignore) { // Older device
            mScreenPoint.x = d.getWidth();
            mScreenPoint.y = d.getHeight();
        }


        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.width = SizeUtils.dp2px(160);    // 宽度设置为屏幕的0.9
        p.width = (int) (mScreenPoint.x * 1.0);
        p.height = (int) (mScreenPoint.y);  // 高度设置为屏幕的0.35
        dialogWindow.setAttributes(p);
    }
}
