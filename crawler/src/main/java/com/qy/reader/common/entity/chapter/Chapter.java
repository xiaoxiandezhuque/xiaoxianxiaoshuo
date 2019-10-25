package com.qy.reader.common.entity.chapter;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 章节
 * <p>
 * Created by yuyuhang on 2018/1/7.
 */
public class Chapter implements  Parcelable {

    public String title;

    public String lastUpdateTime;

    public String link;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.lastUpdateTime);
        dest.writeString(this.link);
    }

    public Chapter() {
    }

    protected Chapter(Parcel in) {
        this.title = in.readString();
        this.lastUpdateTime = in.readString();
        this.link = in.readString();
    }

    public static final Parcelable.Creator<Chapter> CREATOR = new Parcelable.Creator<Chapter>() {
        @Override
        public Chapter createFromParcel(Parcel source) {
            return new Chapter(source);
        }

        @Override
        public Chapter[] newArray(int size) {
            return new Chapter[size];
        }
    };
}
