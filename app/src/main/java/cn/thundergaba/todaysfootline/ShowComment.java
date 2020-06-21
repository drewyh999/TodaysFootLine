package cn.thundergaba.todaysfootline;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ShowComment extends AppCompatActivity {


    private String item_id;
    private TextView msg1;
    private final String TAG="woshishabi";
    private List<ToutiaoNews> newslist = new ArrayList<>();
    private RecyclerView mview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //msg1=findViewById(R.id.msg);
        setContentView(R.layout.activity_show_comment);
        mview = findViewById(R.id.show_comment);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mview.setLayoutManager(layoutManager);
        if (BmobUser.isLogin()) {
            User user = BmobUser.getCurrentUser(User.class);
            BmobQuery<Comment> commentBmobQuery=new BmobQuery<>();
            commentBmobQuery.addWhereEqualTo("phoneNumber",user.getMobilePhoneNumber());
            buildProgressDialog(0);
            commentBmobQuery.findObjects(new FindListener<Comment>() {
                @Override
                public void done(List<Comment> list, BmobException e) {
                    for (int j = 0; j < list.size(); j++) {
                        if (list.get(j).getType() == "video") {
                            //j++;
                            //showvideo(mview, list.get(j).getItem_id());

                        } else {
                            try {
                                shownews(mview, list.get(j).getCommentitemid());
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                    cancelProgressDialog();
                }
            });
        }


    }



    public void shownews(RecyclerView nview, String news_id) throws InterruptedException {

        new Thread(() -> {
            try {
                final String searchString = "https://api03.6bqb.com/toutiao/detail?apikey=B10A922C01D27BB7EEDB02717A72BDA1&itemId=" + news_id;
                OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象
                Request request = new Request.Builder()
                        .url(searchString)//请求接口。如果需要传参拼接到接口后面。
                        .build();
                Response response = null;
                response = client.newCall(request).execute();//得到Response 对象
                if (response.isSuccessful()) {
                    String responsestring = response.body().string();
                    Log.d(TAG, "res==" + responsestring);
                    //此时的代码执行在子线程，修改UI的操作请使用handler跳转到UI线程。
                    JSONObject root_object = new JSONObject(responsestring);
                    nview.post(() -> {
                        try {

                            JSONObject newsobject = root_object.getJSONObject("data");
                            String title = newsobject.getString("title");
                            JSONObject avatarURL = newsobject.getJSONObject("media_user");
                            TouTiaoUserInfo source = new TouTiaoUserInfo(newsobject.getString("source"), avatarURL.getString("avatar_url"));
                            //String avatar=avatarURL.getString("avatar_url");
                            //News news = NewsSearchJsonConversion.GetNewsFromSearchJson(TAG,newsobject);
                            ToutiaoNews news = new ToutiaoNews();
                            news.setTitle(title);
                            news.setUserInfo(source);
                            news.setItem_id(news_id);
                            if (news != null) {
                                newslist.add(news);
                            }
                            ShowComment.NewsItemAdapter adapter;
                            adapter = new ShowComment.NewsItemAdapter(newslist, ShowComment.this);
                            nview.setAdapter(adapter);
                        } catch (JSONException e) {
                            Log.d(TAG, e.getMessage());
                            e.printStackTrace();
                        }
                    });
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                Log.d(TAG, e.toString());
            }
        }).start();
        Thread.sleep(2000);
    }




    private ProgressDialog progressDialog;
    public void buildProgressDialog(int id) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(ShowComment.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        }
        progressDialog.setMessage("正在载入");
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    public void cancelProgressDialog() {
        if (progressDialog != null)
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
    }



    public class NewsItemAdapter extends RecyclerView.Adapter<NewsItemAdapter.NewsViewholder>{
        List<ToutiaoNews> list;

        Activity activity;

        public NewsItemAdapter(List<ToutiaoNews> list, Activity activity){
            this.list = list;
            this.activity = activity;
        }
        @SuppressLint("ShowToast")
        @NonNull
        @Override
        public NewsViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.newsitem,parent,false);
            final NewsViewholder holder = new NewsViewholder(view);

            return holder;
        }

        @SuppressLint("ShowToast")
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onBindViewHolder(@NonNull NewsViewholder holder, int position) {
            // TODO user avatar is not correctly loaded
            ToutiaoNews newsItem = list.get(position);
            holder.avatar.setImageURL(newsItem.getUserInfo().getAvatar_url());
            holder.user.setText(list.get(position).getUserInfo().getName());
            holder.news.setText(list.get(position).getTitle());
            holder.news.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    User user=BmobUser.getCurrentUser(User.class);
                    String title = newsItem.getTitle();
                    String item_id = newsItem.getItem_id();
                    String source = newsItem.getUserInfo().getName();
                    String praiserPhoneNumber = user.getMobilePhoneNumber();
                    new Thread(() -> {
                        try {

                            final String detailurl = "https://api03.6bqb.com/toutiao/detail?apikey=E449EF5F8EE5C6C6919C52AA98160F07&itemId=";
                            OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象
                            Request request = new Request.Builder()
                                    .url(detailurl+item_id)//请求接口。如果需要传参拼接到接口后面。
                                    .build();
                            Response response = null;
                            response = client.newCall(request).execute();//得到Response 对象
                            if (response.isSuccessful()) {
                                //Log.d(TAG, "response.code()==" + response.code());
                                //Log.d(TAG, "response.message()==" + response.message());
                                String responsestring = response.body().string();
                                //Log.d(TAG, "res==" + responsestring);
                                //此时的代码执行在子线程，修改UI的操作请使用handler跳转到UI线程。
                                JSONObject root_object = new JSONObject(responsestring);
                                JSONObject news_object_array = root_object.getJSONObject("data");
                                JSONObject media_user = news_object_array.getJSONObject("media_user");
                                String detail = new String(news_object_array.getString("content"));
                                String avatar_url = new String(media_user.getString("avatar_url"));
                                Date time = new Date(news_object_array.getInt("publish_time"));
                                //Log.d(TAG, "detail==" + detail);
                                //Log.d(TAG, "publish_time==" + time);
                                Intent intent = new Intent(ShowComment.this,NewsDetail.class);
                                intent.putExtra("item_id",item_id);
                                intent.putExtra("content",detail);
                                intent.putExtra("title",title);
                                intent.putExtra("source",source);
                                intent.putExtra("publish_time",time);
                                intent.putExtra("avatar_url",avatar_url);
                                intent.putExtra("praiserPhoneNumber",praiserPhoneNumber);
                                startActivity(intent);
                            }
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                            //Log.d(TAG, e.toString());
                        }
                    }).start();

                }
            });
        }


        @Override
        public int getItemCount() {
            return list.size();
        }

        public void ConcatenateNewsList(List<ToutiaoNews> next_page_news){
            list.addAll(next_page_news);
            notifyDataSetChanged();
        }

        public class NewsViewholder extends RecyclerView.ViewHolder{
            MyImageView avatar;
            TextView user;
            TextView news;
            //            TextView time;
            public NewsViewholder(@NonNull View itemView) {
                super(itemView);
                avatar = itemView.findViewById(R.id.news_image);
                user = itemView.findViewById(R.id.news_source);
                news = itemView.findViewById(R.id.news_title);
//                time = itemView.findViewById(R.id.news_time);
            }
        }


    }
}