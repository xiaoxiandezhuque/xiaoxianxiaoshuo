package com.xh.xiaoshuo.bean;

import com.qy.reader.common.entity.book.SearchBook;
import com.qy.reader.common.entity.source.Source;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class BookBean {

    @Id(autoincrement = true)
    private Long id;
    private String imageUrl;
    private String name;
    private String auther;
    private String desc;

    private int readNum;
    private int readPage;
    private int maxNum;

    private String link;
    private int sourceId;
    private String sourceName;
    private String sourceUrl;

    private int state; //  0历史   1 加入书架

    private long openTime;

    private boolean hasNofity;


    public SearchBook tfSearchBook() {
        SearchBook searchBook = new SearchBook();
        searchBook.title = name;
        searchBook.author = auther;
        searchBook.desc = desc;
        searchBook.cover = imageUrl;
        searchBook.sources.add(new SearchBook.SL(link,
                new Source(sourceId, sourceName, sourceUrl)));
        return searchBook;
    }


    @Generated(hash = 1935442077)
    public BookBean(Long id, String imageUrl, String name, String auther,
                    String desc, int readNum, int readPage, int maxNum, String link,
                    int sourceId, String sourceName, String sourceUrl, int state,
                    long openTime, boolean hasNofity) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.name = name;
        this.auther = auther;
        this.desc = desc;
        this.readNum = readNum;
        this.readPage = readPage;
        this.maxNum = maxNum;
        this.link = link;
        this.sourceId = sourceId;
        this.sourceName = sourceName;
        this.sourceUrl = sourceUrl;
        this.state = state;
        this.openTime = openTime;
        this.hasNofity = hasNofity;
    }

    @Generated(hash = 269018259)
    public BookBean() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuther() {
        return this.auther;
    }

    public void setAuther(String auther) {
        this.auther = auther;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getReadNum() {
        return this.readNum;
    }

    public void setReadNum(int readNum) {
        this.readNum = readNum;
    }

    public int getReadPage() {
        return this.readPage;
    }

    public void setReadPage(int readPage) {
        this.readPage = readPage;
    }

    public int getMaxNum() {
        return this.maxNum;
    }

    public void setMaxNum(int maxNum) {
        this.maxNum = maxNum;
    }

    public String getLink() {
        return this.link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getSourceId() {
        return this.sourceId;
    }

    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
    }

    public String getSourceName() {
        return this.sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getSourceUrl() {
        return this.sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public int getState() {
        return this.state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public long getOpenTime() {
        return this.openTime;
    }

    public void setOpenTime(long openTime) {
        this.openTime = openTime;
    }

    public boolean getHasNofity() {
        return this.hasNofity;
    }

    public void setHasNofity(boolean hasNofity) {
        this.hasNofity = hasNofity;
    }


}
