package cn.thundergaba.todaysfootline;

import androidx.appcompat.app.AppCompatActivity;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class InputInfo extends AppCompatActivity {
    private EditText musername,muserpwd,mconfirmpwd;
    private Button mconfirmregi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_info);
        musername=findViewById(R.id.user_name);
        muserpwd=findViewById(R.id.user_password);
        mconfirmpwd=findViewById(R.id.confirm_password);
        mconfirmregi=findViewById(R.id.regi_confirm);
        mconfirmregi.setOnClickListener(mListener);
    }

    View.OnClickListener mListener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            InputInfo(view);
        }
    };

    public void InputInfo(View view) {
        if (isUserNameAndPwdValid()) {
            String username = musername.getText().toString().trim();
            String userpwd = muserpwd.getText().toString().trim();
            String confirmpwd = mconfirmpwd.getText().toString().trim();

            Intent intent =getIntent();
            Bundle bundle = intent.getExtras();
            String phone=String.format(bundle.getString("phonenumber"));
            String code=String.format(bundle.getString("code"));
            User user = new User();
            user.setMobilePhoneNumber(phone);
            user.setPassword(userpwd);
            user.setUsername(username);
            user.signUp(new SaveListener<User>() {
                @Override
                public void done(User user,BmobException e) {
                    if (e == null) {
                        Snackbar.make(view,"短信注册成功：",Snackbar.LENGTH_SHORT);
                        Intent intent = new Intent(InputInfo.this,FirstChoose.class);
//                        Bundle bundle = new Bundle();
//                        bundle.putString("phonenumber",phone);
//                        bundle.putString("code",code);
//                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else {
                        Snackbar.make(view,"账号已存在或网络故障",Snackbar.LENGTH_SHORT);
                        Intent intent = new Intent(InputInfo.this,FirstChoose.class);
                        startActivity(intent);
                    }
                }
            });

        }
    }


    public boolean isUserNameAndPwdValid() {
        if (musername.getText().toString().trim().equals("")) {
            Toast.makeText(this, getString(R.string.account_empty),
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (muserpwd.getText().toString().trim().equals("")) {
            Toast.makeText(this, getString(R.string.pwd_empty),
                    Toast.LENGTH_SHORT).show();
            return false;
        }else if(mconfirmpwd.getText().toString().trim().equals("")) {
            Toast.makeText(this, getString(R.string.pwd_check_empty),
                    Toast.LENGTH_SHORT).show();
            return false;
        }else if(!muserpwd.getText().toString().trim().equals(mconfirmpwd.getText().toString().trim())){
            Toast.makeText(this, "两次密码不一致",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


}