package cn.thundergaba.todaysfootline;

import androidx.appcompat.app.AppCompatActivity;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;

public class PictureAll extends AppCompatActivity {
    private ImageView picture_choose,picture_choose1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_all);
        picture_choose = findViewById(R.id.imageView3);
        picture_choose1 = findViewById(R.id.imageView2);
        picture_choose.setOnClickListener(mlistener);
        picture_choose1.setOnClickListener(mlistener);
    }

    View.OnClickListener mlistener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {


            ImageView imageView=findViewById(view.getId());
            Resources resource=imageView.getResources();






            if (BmobUser.isLogin()) {
                String id=String.valueOf(view.getId());
                User user = BmobUser.getCurrentUser(User.class);
                Snackbar.make(view, "当前用户：" + user.getUsername() + "-" , Snackbar.LENGTH_LONG).show();
                String username = (String) BmobUser.getObjectByKey("username");
                Snackbar.make(view, "当前用户属性：" + username + "-" , Snackbar.LENGTH_LONG).show();
                user.setAvatar(id);
                user.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Snackbar.make(view, "更新用户信息成功：" , Snackbar.LENGTH_LONG).show();
                            startActivity(new Intent(PictureAll.this,personmoduledev.class));
                        } else {
                            Snackbar.make(view, "更新用户信息失败：" + e.getMessage(), Snackbar.LENGTH_LONG).show();
                            Log.e("error", e.getMessage());
                        }
                    }
                });
            } else {
                Snackbar.make(view, "尚未登录，请先登录", Snackbar.LENGTH_LONG).show();
            }
        }
    };



}
