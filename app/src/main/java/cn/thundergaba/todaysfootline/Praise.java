package cn.thundergaba.todaysfootline;

import cn.bmob.v3.BmobObject;

public class Praise extends BmobObject {
    private Integer pnum;

    private String purl;

    public Integer getPnum() {
        return pnum;
    }

    public void setPnum(Integer pnum) {
        this.pnum = pnum;
    }

    public String getPurl() {
        return purl;
    }

    public void setPurl(String purl) {
        this.purl = purl;
    }
}
