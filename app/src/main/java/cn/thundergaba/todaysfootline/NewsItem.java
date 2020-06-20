package cn.thundergaba.todaysfootline;

import java.util.Date;

public class NewsItem {
    private String newsitem_id;

    private NewsUserInfo newsUserInfo;

    private Date NewsPublishTime;

    private String title;

    public String getNewsItem_id() {
        return newsitem_id;
    }

    public void setNewsItem_id(String item_id) {
        this.newsitem_id = item_id;
    }

    public NewsUserInfo getNewsUserInfo() {
        return newsUserInfo;
    }

    public void setNewsUserInfo(NewsUserInfo userInfo) {
        this.newsUserInfo = userInfo;
    }

    public Date getNewsPublishTime() {
        return NewsPublishTime;
    }

    public void setNewsPublishTime(Date publish_time) {
        NewsPublishTime = publish_time;
    }

    public String getTitle(){return title;}

    public void setTitle(String title){this.title = title;}
}
