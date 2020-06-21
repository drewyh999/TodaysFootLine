package cn.thundergaba.todaysfootline;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class NavigationActivity extends AppCompatActivity implements VideoFragment.OnFragmentInteractionListener, NewsFragment.OnFragmentInteractionListener, PersonFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.newsfragment, R.id.videofragment, R.id.personalinfofragment)
                .build();
//        BmobUser.loginByAccount("测试员10086", "123456", new LogInListener<User>() {
//            @Override
//            public void done(User user, BmobException e) {
//                if (e == null) {
//
//                } else {
//                    Log.d("BMOB" , e.toString());
//                }
//            }
//        });
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        return;
    }
}
