package cn.thundergaba.todaysfootline;

import android.app.Application;
import cn.bmob.v3.Bmob;

public class FootLineApplication extends Application {
    public static final String APP_ID = "099b2f81af9c83ba937e77ac8aa9b865";

    @Override
    public void onCreate()
    {
        super.onCreate();
        Bmob.initialize(this, APP_ID); //初始化Bmob应用
    }
}
