package cn.thundergaba.todaysfootline;

import android.net.Uri;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import com.google.android.material.snackbar.Snackbar;

public class videomoduledev extends AppCompatActivity implements VideoFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videomoduledev);
        // TODO dev use only
        BmobUser.loginByAccount("测试员10086", "123456", new LogInListener<User>() {
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
