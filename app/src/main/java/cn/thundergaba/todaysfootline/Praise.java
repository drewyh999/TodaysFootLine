package cn.thundergaba.todaysfootline;

import cn.bmob.v3.BmobObject;

public class Praise extends BmobObject {
    private String praiserPhoneNumber;

    private String praiseitemid;

    public String getPraiserPhoneNumber() {
        return praiserPhoneNumber;
    }

    public void setPraiserPhoneNumber(String praiserPhoneNumber) {
        this.praiserPhoneNumber = praiserPhoneNumber;
    }

    public String getPraiseitemid() {
        return praiseitemid;
    }

    public void setPraiseitemid(String praiseitemid) {
        this.praiseitemid = praiseitemid;
    }
}
