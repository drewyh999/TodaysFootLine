package cn.thundergaba.todaysfootline;

import cn.bmob.v3.BmobObject;

public class Collection extends BmobObject {
    private String collectoritemid;

    private String collecturl;

    public String getCollectoritemid() {
        return collectoritemid;
    }

    public void setCollectoritemid(String collectoritemid) {
        this.collectoritemid = collectoritemid;
    }

    public String getCollecturl() {
        return collecturl;
    }

    public void setCollecturl(String collecturl) {
        this.collecturl = collecturl;
    }
}
