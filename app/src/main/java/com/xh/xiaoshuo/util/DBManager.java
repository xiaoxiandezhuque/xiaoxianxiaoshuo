package com.xh.xiaoshuo.util;

import android.content.Context;

import com.xh.common.base.BaseLibApp;
import com.xh.xiaoshuo.database.DaoMaster;
import com.xh.xiaoshuo.database.DaoSession;

public class DBManager {
    private final static String dbName = "xiaoshuo_db";
    private static DBManager mInstance;
    private DaoMaster.DevOpenHelper openHelper;


    public DBManager(Context context) {
        openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
    }


    public DaoSession getSession() {
        DaoMaster daoMaster = new DaoMaster(openHelper.getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        return daoSession;
    }

    public static DBManager getInstance() {
        if (mInstance == null) {
            synchronized (DBManager.class) {
                if (mInstance == null) {
                    mInstance = new DBManager(BaseLibApp.Companion.getInstance());
                }
            }
        }
        return mInstance;
    }


}