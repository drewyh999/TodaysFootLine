package cn.thundergaba.todaysfootline;

import androidx.appcompat.app.AppCompatActivity;
import cn.bmob.v3.BmobUser;

import android.os.Bundle;

public class ShowComment extends AppCompatActivity {


    private String item_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_show_comment);
        User user = BmobUser.getCurrentUser(User.class);




    }
}