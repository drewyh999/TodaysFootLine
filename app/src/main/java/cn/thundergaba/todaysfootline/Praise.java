package cn.thundergaba.todaysfootline;

import cn.bmob.v3.BmobObject;

public class Praise extends BmobObject {
    private String praiserPhoneNumber;

    private String item_id;

    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPraiserPhoneNumber() {
        return praiserPhoneNumber;
    }

    public void setPraiserPhoneNumber(String praiserPhoneNumber) {
        this.praiserPhoneNumber = praiserPhoneNumber;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }
}
