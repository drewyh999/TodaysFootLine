package cn.thundergaba.todaysfootline;

import androidx.appcompat.app.AppCompatActivity;
import cn.bmob.v3.Bmob;

import android.net.Uri;
import android.os.Bundle;

public class personmoduledev extends AppCompatActivity implements PersonFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this,"099b2f81af9c83ba937e77ac8aa9b865");
        setContentView(R.layout.activity_personmodeledev);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}