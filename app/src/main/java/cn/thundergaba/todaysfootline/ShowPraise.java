package cn.thundergaba.todaysfootline;

import androidx.appcompat.app.AppCompatActivity;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;

import android.os.Bundle;

public class ShowPraise extends AppCompatActivity {
    private String typename,item_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_praise);
        if (BmobUser.isLogin()){
            User user=BmobUser.getCurrentUser(User.class);
            BmobQuery<Praise> praiseBmobQuery = new BmobQuery<>();
            praiseBmobQuery.addWhereEqualTo("praiserNumber",user);


        }

    }
}