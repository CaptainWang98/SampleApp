package com.weizhixiang.imagetest.data;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class image extends BmobObject {
    private BmobFile image;
    private works work;
    private String url;

    public BmobFile getImage() {
        return image;
    }

    public void setImage(BmobFile image) {
        this.image = image;
    }

    public works getWork() {
        return work;
    }

    public void setWork(works work) {
        this.work = work;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
