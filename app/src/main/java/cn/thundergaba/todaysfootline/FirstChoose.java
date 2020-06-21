package cn.thundergaba.todaysfootline;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.DragStartHelper;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Picture;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class FirstChoose extends Activity {
    private EditText mAccount;                        //用户名编辑
    private EditText mPwd;                            //密码编辑
    private Button mRegisterButton;                   //注册按钮
    private Button mLoginButton;                      //登录按钮
    private TextView Snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_choose);
        Bmob.initialize(this,"099b2f81af9c83ba937e77ac8aa9b865");
        mAccount=findViewById(R.id.sname);
        mPwd=findViewById(R.id.spassword);
        mRegisterButton=findViewById(R.id.regi);
        mLoginButton=findViewById(R.id.login);
        mRegisterButton.setOnClickListener(mListener);
        mLoginButton.setOnClickListener(mListener);
        Snackbar=findViewById(R.id.text1);
    }
    View.OnClickListener mListener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch(view.getId()){
                case R.id.regi:
                    Intent intent_Login_to_Register=new Intent(FirstChoose.this,Register.class);
                    startActivity(intent_Login_to_Register);
                    finish();

                    break;
                case R.id.login:
                    login(view);
            }
        }
    };




    //登录点击
    private void login(final View view){
        String phone = mAccount.getText().toString().trim();
        String code = mPwd.getText().toString().trim();
            final User user = new User();
            //此处替换为你的用户名
            user.setUsername(phone);
            //此处替换为你的密码
            user.setPassword(code);
            user.login(new SaveListener<User>() {
                @Override
                public void done(User bmobUser, BmobException e) {
                    if (e == null) {
                        User user = BmobUser.getCurrentUser(User.class);
                        Snackbar.append("登录成功：" + user.getUsername());
//                        Intent intent_FirstChoose_to_VideoFragment = new Intent(FirstChoose.this, PictureChoose.class);
//                        startActivity(intent_FirstChoose_to_VideoFragment);
//                        finish();
                        if (user.getAvatar()==null) {
                            Intent intent_FirstChoose_to_VideoFragment = new Intent(FirstChoose.this, PictureChoose.class);
                            startActivity(intent_FirstChoose_to_VideoFragment);
                            finish();
                        }else{
                            startActivity(new Intent(FirstChoose.this,NavigationActivity.class));
                        }
                    } else {
                        Snackbar.append("登录失败：" + e.getMessage()+ "\n");
                    }
                }
            });
        }




}