package cn.thundergaba.todaysfootline;

import android.app.Application;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobFile;

public class BmobUtils extends Application {

    public static final String APP_ID = "099b2f81af9c83ba937e77ac8aa9b865";

    @Override
    public void onCreate() {
        super.onCreate();



        //Bmob SDK初始化
        Bmob.initialize(this, APP_ID);



        /**
         *  2.2、SDK初始化方式二
         * 设置BmobConfig，允许设置请求超时时间、文件分片上传时每片的大小、文件的过期时间
         */
//        BmobConfig config = new BmobConfig.Builder(this)
//                //设置APPID
//                .setApplicationId(APP_ID)
//                //请求超时时间（单位为秒）：默认15s
//                .setConnectTimeout(30)
//                //文件分片上传时每片的大小（单位字节），默认512*1024
//                .setUploadBlockSize(1024 * 1024)
//                //文件的过期时间(单位为秒)：默认1800s
//                .setFileExpiration(5500)
//                .build();
//        Bmob.initialize(config);




//		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
//		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());


    }

    /*Class cont*/
    public class User extends BmobUser {

        public String gender;

        public BmobFile avatar;

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public BmobFile getAvatar() {
            return avatar;
        }

        public void setAvatar(BmobFile avatar) {
            this.avatar = avatar;
        }
    }

    public class Praise extends BmobObject{
        public Integer pnum;

        public String purl;

        public Praise(Integer pnum, String purl) {
            this.pnum = pnum;
            this.purl = purl;
        }

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

    public class Comment extends BmobObject{
        public String commenturl;

        public String authorPhoneNumber;

        public BmobDate ctime;

        public String content;

        public Comment(String commenturl, String authorPhoneNumber, BmobDate ctime, String content) {
            this.commenturl = commenturl;
            this.authorPhoneNumber = authorPhoneNumber;
            this.ctime = ctime;
            this.content = content;
        }

        public String getCommenturl() {
            return commenturl;
        }

        public void setCommenturl(String commenturl) {
            this.commenturl = commenturl;
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

    public class Collection extends BmobObject{
        public String collectorPhoneNumber;

        public String collecturl;

        public Collection(String collectorPhoneNumber, String collecturl) {
            this.collectorPhoneNumber = collectorPhoneNumber;
            this.collecturl = collecturl;
        }

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

}
