package com.xh.common.view;//package com.rst.baselibrary.view;
//
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.app.Dialog;
//import android.content.Context;
//import android.graphics.Point;
//import android.graphics.drawable.BitmapDrawable;
//import android.os.Bundle;
//import android.view.Display;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.Window;
//import android.view.WindowManager;
//import android.widget.AdapterView;
//import android.widget.BaseAdapter;
//import android.widget.CheckBox;
//import android.widget.ListView;
//import android.widget.TextView;
//
//
//import com.rst.baselibrary.R;
//import com.rst.baselibrary.view.spinnerwheel.AbstractWheel;
//import com.rst.baselibrary.view.spinnerwheel.OnWheelChangedListener;
//import com.rst.baselibrary.view.spinnerwheel.adapters.AbstractWheelTextAdapter;
//
//import java.util.List;
//
//
//public class ListItemDialog extends Dialog {
//    private Point mScreenPoint = new Point();
//
//    private Activity mRootActivity = null;
//
//    private int mCheckNo = 0;
//
//    private List<String> mDatas;
//
//    private ListView mListView;
//
//    private AdapterView.OnItemClickListener itemClickListener;
//
//    private AbstractWheel mDayAbstractWheel;
//    private int mOriginDayIndex = 0;
//    private OnCheckedChangedListener mOnCheckedChangedListener;
//
//    public ListItemDialog(Activity arg, List<String> list) {
//        super(arg, R.style.LoginDialog);
//        mRootActivity = arg;
//        mDatas = list;
//    }
//
//    @SuppressWarnings("deprecation")
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        getWindow().setBackgroundDrawable(new BitmapDrawable());
//        this.setContentView(R.layout.dialog_list_item);
//        initDialogWindow();
//        initView();
//        setCanceledOnTouchOutside(true);
//    }
//
//    public void setOnCheckedChangedListener(OnCheckedChangedListener listener) {
//        mOnCheckedChangedListener = listener;
//    }
//
//    public void setCurrentItem(int pos) {
//        mDayAbstractWheel.setCurrentItem(pos);
//    }
//
//    Object mTag;
//
//    public void setTag(Object tag) {
//        mTag = tag;
//    }
//
//    public Object getTag() {
//        return mTag;
//    }
//
//    void initView() {
//        mDayAbstractWheel = (AbstractWheel) findViewById(R.id.day_selector);
//        mDayAbstractWheel.setVisibleItems(5);
//        mDayAbstractWheel.setCurrentItem(mOriginDayIndex);
//        mDayAbstractWheel.setViewAdapter(new DaySelectorAdapter(mRootActivity));
//        mDayAbstractWheel.addChangingListener(new OnWheelChangedListener() {
//            @Override
//            public void onChanged(AbstractWheel wheel, int oldValue, int newValue) {
//                if (mOnCheckedChangedListener != null) {
//                    mOnCheckedChangedListener.onChanged(oldValue, newValue);
//                }
//            }
//        });
////        mListView = (ListView) findViewById(R.id.listView);
////        ListAdapter adapter = new ListAdapter();
////        mListView.setAdapter(adapter);
////        if (itemClickListener != null) {
////            mListView.setOnItemClickListener(itemClickListener);
////        }
//    }
//
//    public void setOnItemOnClickListener(AdapterView.OnItemClickListener listener) {
//        if (mListView != null) {
//            mListView.setOnItemClickListener(listener);
//        }
//    }
//
//    class ListAdapter extends BaseAdapter {
//
//        @Override
//        public int getCount() {
//            return mDatas.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return mDatas.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            ViewHolder holder;
//            if (convertView == null) {
//                convertView = LayoutInflater.from(mRootActivity).inflate(R.layout.item_list_type, null);
//                holder = new ViewHolder(convertView);
//                convertView.setTag(holder);
//            } else {
//                holder = (ViewHolder) convertView.getTag();
//            }
//            holder.text.setText(mDatas.get(position));
//            if (position == mCheckNo) {
//                holder.select.setChecked(true);
//            } else {
//                holder.select.setChecked(false);
//            }
//            return convertView;
//        }
//
//        class ViewHolder {
//           private TextView text;
//           private CheckBox select;
//
//            public ViewHolder(View view) {
//               text = (TextView) view.findViewById(R.id.text);
//               select = (CheckBox) view.findViewById(R.id.select);
//
//            }
//        }
//    }
//
//    private class DaySelectorAdapter extends AbstractWheelTextAdapter {
//        /**
//         * Constructor
//         */
//        protected DaySelectorAdapter(Context context) {
//            super(context, R.layout.item_list_select, NO_RESOURCE);
//            setItemTextResource(R.id.name);
//        }
//
//        @Override
//        public View getItem(int index, View cachedView, ViewGroup parent) {
//            View view = super.getItem(index, cachedView, parent);
//            return view;
//        }
//
//        @Override
//        public int getItemsCount() {
//            return mDatas.size();
//        }
//
//        @Override
//        protected CharSequence getItemText(int index) {
//            return mDatas.get(index);
//        }
//    }
//
//    public static interface OnCheckedChangedListener {
//        public void onChanged(int oldNo, int newNo);
//    }
//
//    @SuppressLint("NewApi")
//    @SuppressWarnings("deprecation")
//    private void initDialogWindow() {
//        Window dialogWindow = getWindow();
//
//        /*
//         * lp.x与lp.y表示相对于原始位置的偏移.
//         * 当参数值包含Gravity.LEFT时,对话框出现在左边,所以lp.x就表示相对左边的偏移,负值忽略.
//         * 当参数值包含Gravity.RIGHT时,对话框出现在右边,所以lp.x就表示相对右边的偏移,负值忽略.
//         * 当参数值包含Gravity.TOP时,对话框出现在上边,所以lp.y就表示相对上边的偏移,负值忽略.
//         * 当参数值包含Gravity.BOTTOM时,对话框出现在下边,所以lp.y就表示相对下边的偏移,负值忽略.
//         * 当参数值包含Gravity.CENTER_HORIZONTAL时
//         * ,对话框水平居中,所以lp.x就表示在水平居中的位置移动lp.x像素,正值向右移动,负值向左移动.
//         * 当参数值包含Gravity.CENTER_VERTICAL时
//         * ,对话框垂直居中,所以lp.y就表示在垂直居中的位置移动lp.y像素,正值向右移动,负值向左移动.
//         * gravity的默认值为Gravity.CENTER,即Gravity.CENTER_HORIZONTAL |
//         * Gravity.CENTER_VERTICAL.
//         *
//         * 本来setGravity的参数值为Gravity.LEFT | Gravity.TOP时对话框应出现在程序的左上角,但在
//         * 我手机上测试时发现距左边与上边都有一小段距离,而且垂直坐标把程序标题栏也计算在内了,
//         * Gravity.LEFT, Gravity.TOP, Gravity.BOTTOM与Gravity.RIGHT都是如此,据边界有一小段距离
//         */
//        //WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//        dialogWindow.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
//        //lp.y = - Utils.dp2px(50, mRootActivity.getResources());
//        //dialogWindow.setWindowAnimations(R.style.dialog_anim);
//
//        WindowManager m = mRootActivity.getWindowManager();
//        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
//
//        try {
//            d.getSize(mScreenPoint);
//        } catch (NoSuchMethodError ignore) { // Older device
//            mScreenPoint.x = d.getWidth();
//            mScreenPoint.y = d.getHeight();
//        }
//
//        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
//        p.width = (int) (mScreenPoint.x);    // 宽度设置为屏幕的0.9
//        //p.height = (int) (mScreenPoint.y * 0.35);  // 高度设置为屏幕的0.35
//        dialogWindow.setAttributes(p);
//    }
//
//    public int getCheckNo() {
//        return mCheckNo;
//    }
//
//    public void setCheckNo(int NO) {
//        mCheckNo = NO;
//    }
//}
