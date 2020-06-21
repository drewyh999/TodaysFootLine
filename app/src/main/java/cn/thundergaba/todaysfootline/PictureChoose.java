package cn.thundergaba.todaysfootline;

import androidx.appcompat.app.AppCompatActivity;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.snackbar.Snackbar;

public class PictureChoose extends AppCompatActivity {
    private ImageView picture;
    private Button picture_choose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_choose);
        picture = findViewById(R.id.imageView);
        picture_choose = findViewById(R.id.change_pic);
        picture_choose.setOnClickListener(mlistener);
    }

    View.OnClickListener mlistener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(PictureChoose.this,PictureAll.class));
        }
    };
}