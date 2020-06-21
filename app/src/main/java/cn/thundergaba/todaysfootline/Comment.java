package cn.thundergaba.todaysfootline;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;

public class Comment extends BmobObject {
    private String commentitemid;

    private BmobDate ctime;

    private String content;

    private String user_avatar;

    private String user_name;

    private String type;

    private String phoneNumber;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getType(){return type;}

    public void setType(String type){this.type = type;}

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_avatar() {
        return user_avatar;
    }

    public void setUser_avatar(String user_avatar) {
        this.user_avatar = user_avatar;
    }

    public String getCommentitemid() {
        return commentitemid;
    }

    public void setCommentitemid(String commentitemid) {
        this.commentitemid = commentitemid;
    }

    public BmobDate getCtime() {
        return ctime;
    }

    public void setCtime(BmobDate ctime) {
        this.ctime = ctime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
