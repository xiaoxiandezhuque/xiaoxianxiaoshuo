package com.xh.common.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.xh.common.R;


public class DescDialog extends Dialog {
    private Point mScreenPoint = new Point();
    private Activity mRootActivity = null;

    private TextView mTitleView;
    private TextView mDescriptionView;
    private TextView mDescView2;
    private Button mRightBtnView;
    private Button mLeftBtnView;

    private String mTitle;
    private String mDescription;
    private String mCancelText;
    private String mOkText;
    private boolean isForce = false;

    /**
     * 一个按钮Dialog
     *
     * @param arg
     * @param title
     * @param des
     */
    public DescDialog(Activity arg, String title, String des) {
        super(arg, R.style.LoginDialog);
        mRootActivity = arg;
        mTitle = title;
        mDescription = des;
    }

    /**
     * 一个按钮Dialog
     *
     * @param arg
     * @param title
     * @param des
     */
    public DescDialog(Activity arg, String title, String des, boolean isForce) {
        super(arg, R.style.LoginDialog);
        mRootActivity = arg;
        mTitle = title;
        mDescription = des;
        this.isForce = isForce;
    }

    /**
     * 两个按钮的Dialog
     *
     * @param arg
     * @param title
     * @param des
     */
    public DescDialog(Activity arg, String title, String des,
                      String cancelText, String okText) {
        super(arg, R.style.LoginDialog);
        mRootActivity = arg;
        mTitle = title;
        mCancelText = cancelText;
        mOkText = okText;
        mDescription = des;
    }

    public void setTitle(String text) {
        mTitle = text;
        if (mTitleView != null) {
            mTitleView.setText(mTitle);
        }
    }

    public void setDescription(String Text) {
        mDescription = Text;
        if (mDescriptionView != null) {
            mDescriptionView.setText(mDescription);
        }
    }

    public void setLeftBtnText(String cancelText) {
        mCancelText = cancelText;
        if (mLeftBtnView != null) {
            mLeftBtnView.setText(cancelText);
        }
    }

    public void setDesc2Text(String desc) {
        mDescView2.setVisibility(View.VISIBLE);
        if (mDescView2 != null) {
            mDescView2.setText(desc);
        }
    }

    public void setRightBtnText(String okText) {
        mOkText = okText;
        if (mRightBtnView != null) {
            mRightBtnView.setText(okText);
        }
    }

    public void setLeftClick(View.OnClickListener cancelClick) {
        if (mLeftBtnView != null) {
            mLeftBtnView.setOnClickListener(cancelClick);
        }
    }

    public void setRightClick(View.OnClickListener okClick) {
        if (mRightBtnView != null) {
            mRightBtnView.setOnClickListener(okClick);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new BitmapDrawable());
        this.setContentView(R.layout.dialog_desc);
        initDialogWindow();
        initView();
        setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return isForce;
            }
        });
    }

    void initView() {
        mRightBtnView = (Button) findViewById(R.id.btnOk);
        mLeftBtnView = (Button) findViewById(R.id.btnCancel);
        mTitleView = (TextView) findViewById(R.id.title);
        mDescriptionView = (TextView) findViewById(R.id.description);
        mDescView2 = (TextView) findViewById(R.id.tvDesc2);

        mTitleView.setText(mTitle);

        mDescriptionView.setText(mDescription);
        if (!TextUtils.isEmpty(mOkText)) {
            mRightBtnView.setText(mOkText);
        }
        if (!TextUtils.isEmpty(mCancelText)) {
            mLeftBtnView.setText(mCancelText);
            mLeftBtnView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        } else {
            mLeftBtnView.setVisibility(View.GONE);
            mRightBtnView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
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
        //WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        //lp.y = - Utils.dip2px(50, mRootActivity.getResources());

        WindowManager m = mRootActivity.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用

        try {
            d.getSize(mScreenPoint);
        } catch (NoSuchMethodError ignore) { // Older device
            mScreenPoint.x = d.getWidth();
            mScreenPoint.y = d.getHeight();
        }

        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.width = (int) (mScreenPoint.x * 0.80);    // 宽度设置为屏幕的0.9
        //p.height = (int) (mScreenPoint.y * 0.35);  // 高度设置为屏幕的0.35
        dialogWindow.setAttributes(p);
    }

    @Override
    public void dismiss() {
        Log.e("aaaa","dismiss11111");
        super.dismiss();
        Log.e("aaaa","dismiss22222");
    }
}
