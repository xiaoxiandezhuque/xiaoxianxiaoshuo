package com.xh.xiaoshuo.util;

import android.content.Context;

import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

public class UMStatisticsUtil {


    public static void book(Context context, String name, String author) {
        Map<String, String> map_value = new HashMap<String, String>();
        map_value.put("name", name);
        map_value.put("author", author);
        MobclickAgent.onEvent(context, "book", map_value);
    }
}
