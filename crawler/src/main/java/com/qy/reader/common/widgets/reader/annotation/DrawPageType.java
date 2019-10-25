package com.qy.reader.common.widgets.reader.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;

@IntDef({
        DrawPageType.DRAW_CUR_PAGE,
        DrawPageType.DRAW_NEXT_PAGE,
        DrawPageType.DRAW_PRE_PAGE
})
@Retention(RetentionPolicy.SOURCE)
public @interface DrawPageType {

    /**
     * 绘制上一页
     */
    int DRAW_PRE_PAGE = 1;

    /**
     * 绘制当前页
     */
    int DRAW_CUR_PAGE = 2;

    /**
     * 绘制下一页
     */
    int DRAW_NEXT_PAGE = 3;
}