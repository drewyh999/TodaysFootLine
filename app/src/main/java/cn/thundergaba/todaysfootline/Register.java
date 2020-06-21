package cn.thundergaba.todaysfootline;

import androidx.appcompat.app.AppCompatActivity;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Register extends AppCompatActivity {
    private EditText mAccount;                        //用户名编辑
    private EditText mPwd;                            //密码编辑
    private EditText mPwdCheck;                       //密码编辑
    private Button mSureButton,mSendButton;                       //确定按钮
    private TextView mTvInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAccount=findViewById(R.id.phone_number);
        mPwd=findViewById(R.id.text);
        mSendButton=findViewById(R.id.send);
        mSureButton=findViewById(R.id.confirm);
        mSendButton.setOnClickListener(m_register_Listener);
        mSureButton.setOnClickListener(m_register_Listener);
        mTvInfo=findViewById(R.id.mTvInfo);
    }
    View.OnClickListener m_register_Listener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.send:
                    register_send();
                    break;
                case R.id.confirm:
                    register_check();
                    break;
            }

        }
    };
    public void register_send(){
        String phone = mAccount.getText().toString().trim();
        BmobSMS.requestSMSCode(phone, "今日脚条", new QueryListener<Integer>() {
            @Override
            public void done(Integer smsId, BmobException e) {
                if (e == null) {
                    mTvInfo.append("发送验证码成功，短信ID：" + smsId + "\n");
                } else {
                    mTvInfo.append("发送验证码失败：" + e.getErrorCode() + "-" + e.getMessage() + "\n");
                }
            }
        });
    }
    public void register_check(){
        String phone = mAccount.getText().toString().trim();
        String code = mPwd.getText().toString().trim();
        BmobSMS.verifySmsCode(phone, code, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    mTvInfo.append("验证码验证成功，您可以在此时进行绑定操作！\n");
                    Intent intent = new Intent(Register.this,InputInfo.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("phonenumber",phone);
                    bundle.putString("code",code);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else {
                    mTvInfo.append("验证码验证失败：" + e.getErrorCode() + "-" + e.getMessage() + "\n");
                }
            }
        });
    }
}