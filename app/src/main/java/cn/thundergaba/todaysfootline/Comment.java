package cn.thundergaba.todaysfootline;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;

public class Comment extends BmobObject {
    private String commentitemid;

    private String authorPhoneNumber;

    private BmobDate ctime;

    private String content;

    public String getCommentitemid() {
        return commentitemid;
    }

    public void setCommentitemid(String commentitemid) {
        this.commentitemid = commentitemid;
    }

    public String getAuthorPhoneNumber() {
        return authorPhoneNumber;
    }

    public void setAuthorPhoneNumber(String authorPhoneNumber) {
        this.authorPhoneNumber = authorPhoneNumber;
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
