package cn.thundergaba.todaysfootline;

import cn.bmob.v3.BmobObject;

public class Collection extends BmobObject {
    private String collectorPhoneNumber;

    private String collecturl;

    public String getCollectorPhoneNumber() {
        return collectorPhoneNumber;
    }

    public void setCollectorPhoneNumber(String collectorPhoneNumber) {
        this.collectorPhoneNumber = collectorPhoneNumber;
    }

    public String getCollecturl() {
        return collecturl;
    }

    public void setCollecturl(String collecturl) {
        this.collecturl = collecturl;
    }
}
