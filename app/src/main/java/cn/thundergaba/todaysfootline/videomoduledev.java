package cn.thundergaba.todaysfootline;

import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class videomoduledev extends AppCompatActivity implements VideoView.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videomoduledev);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
