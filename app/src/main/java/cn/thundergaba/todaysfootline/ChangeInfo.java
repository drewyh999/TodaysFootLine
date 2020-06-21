package cn.thundergaba.todaysfootline;

import androidx.appcompat.app.AppCompatActivity;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;

public class ChangeInfo extends AppCompatActivity {
    EditText editname,editgender,editage;
    Button changeconfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_info);
        editname=findViewById(R.id.editname);
        editgender=findViewById(R.id.editgender);
        //editage=findViewById(R.id.editage);
        changeconfirm=findViewById(R.id.changeconfirm);
        changeconfirm.setOnClickListener(mListener);
    }

    View.OnClickListener mListener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (BmobUser.isLogin()) {
                String name = editname.getText().toString().trim();
                String gender = editgender.getText().toString().trim();
                //tring age = editage.getText().toString().trim();
                User user = BmobUser.getCurrentUser(User.class);
                user.setUsername(name);
                user.setGender(gender);
                //user.set;
                user.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Snackbar.make(view, "更新用户信息成功：" , Snackbar.LENGTH_LONG).show();
                            startActivity(new Intent(ChangeInfo.this,personmoduledev.class));
                        } else {
                            Snackbar.make(view, "更新用户信息失败：" + e.getMessage(), Snackbar.LENGTH_LONG).show();
                            Log.e("error", e.getMessage());
                        }
                    }
                });
            }

        }
    };
}