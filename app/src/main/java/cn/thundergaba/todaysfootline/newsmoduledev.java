package cn.thundergaba.todaysfootline;

import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

public class newsmoduledev extends AppCompatActivity implements NewsFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsmoduledev);
        BmobUser.loginByAccount("10086", "123456", new LogInListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                } else {
                }
            }
        });
    }
    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}