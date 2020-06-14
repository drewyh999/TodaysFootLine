package cn.thundergaba.todaysfootline;

import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UploadFileListener;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    public static final String APP_ID = "099b2f81af9c83ba937e77ac8aa9b865";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bmob.initialize(this, APP_ID); //初始化Bmob应用
    }


}
