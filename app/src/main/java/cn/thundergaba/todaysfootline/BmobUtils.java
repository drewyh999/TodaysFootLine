package cn.thundergaba.todaysfootline;

import android.app.Application;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;

public class BmobUtils extends Application {
    public static final String APP_ID = "099b2f81af9c83ba937e77ac8aa9b865";

    @Override
    public void onCreate() {
        super.onCreate();



        //Bmob SDK初始化
        Bmob.initialize(this, APP_ID);



        /**
         * TODO 2.2、SDK初始化方式二
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

}
