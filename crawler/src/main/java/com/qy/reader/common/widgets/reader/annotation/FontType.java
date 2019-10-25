package com.qy.reader.common.widgets.reader.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;

@IntDef({
        FontType.SIMPLIFIED,
        FontType.TRADITIONAL,

})
@Retention(RetentionPolicy.SOURCE)
public @interface FontType {

    /**
     * 简体
     */
    int TRADITIONAL = 0;

    /**
     * 繁体
     */
    int SIMPLIFIED = 1;
}