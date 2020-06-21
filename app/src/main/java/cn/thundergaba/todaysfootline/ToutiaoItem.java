package cn.thundergaba.todaysfootline;

import java.util.Date;

public class ToutiaoItem {
    private String item_id;

    private TouTiaoUserInfo userInfo;

    private Date PublishTime;

    private String title;

    public ToutiaoItem() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public TouTiaoUserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(TouTiaoUserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public Date getPublishTime() {
        return PublishTime;
    }

    public void setPublishTime(Date publishTime) {
        PublishTime = publishTime;
    }
}
