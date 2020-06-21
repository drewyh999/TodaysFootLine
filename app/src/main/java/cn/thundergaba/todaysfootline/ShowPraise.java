package cn.thundergaba.todaysfootline;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.widget.AdapterView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import cn.thundergaba.todaysfootline.NewsDetail;

public class ShowPraise extends AppCompatActivity {
    private String typename, item_id;
    private TextView showpraise;
    private String TAG = "woshidashabi";
    private RecyclerView mview;
    private NewsItemAdapter newsItemAdapter;
    private List<ToutiaoNews> newslist = new ArrayList<>();
    private List<String> praise_list=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_praise);
        mview = findViewById(R.id.show_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mview.setLayoutManager(layoutManager);

        //showpraise=findViewById(R.id.showpraise);
        if (BmobUser.isLogin()) {

            User user = BmobUser.getCurrentUser(User.class);
            BmobQuery<Praise> praiseBmobQuery = new BmobQuery<>();
            praiseBmobQuery.addWhereEqualTo("praiserPhoneNumber", user.getMobilePhoneNumber());
            buildProgressDialog(0);
            //praiseBmobQuery.addQueryKeys("item_id");
            praiseBmobQuery.findObjects(new FindListener<Praise>() {
                @Override
                public void done(List<Praise> list, BmobException e) {

                    for (int j = 0; j < list.size(); j++) {

                        if (list.get(j).getType() == "video") {
                            //j++;
                            //showvideo(mview, list.get(j).getItem_id());

                        } else {
                            //praise_list.add(list.get(j).getItem_id());
                            try {

                                shownews(mview,list.get(j).getItem_id());

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
        new Thread(()->{
            try{

                //for (int i=0;i<news_list.size();i++){
                    //String news_id=news_list.get(i);
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
                            ShowPraise.NewsItemAdapter adapter;
                            adapter = new ShowPraise.NewsItemAdapter(newslist, ShowPraise.this);
                            nview.setAdapter(adapter);
                        } catch (JSONException e) {
                            Log.d(TAG, e.getMessage());
                            e.printStackTrace();
                        }
                    });
                }
                //}
            }catch (IOException | JSONException e) {
                e.printStackTrace();
                Log.d(TAG, e.toString());
            }
        }).start();
        Thread.sleep(1000);
    }







    private ProgressDialog progressDialog;
    public void buildProgressDialog(int id) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(ShowPraise.this);
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
                    //User user=BmobUser.getCurrentUser(User.class);
                    String title = newsItem.getTitle();
                    String item_id = newsItem.getItem_id();
                    String source = newsItem.getUserInfo().getName();
                    //String praiserPhoneNumber = user.getMobilePhoneNumber();
                    new Thread(() -> {
                        try {

                            final String detailurl = "https://api03.6bqb.com/toutiao/detail?apikey=E449EF5F8EE5C6C6919C52AA98160F07&itemId=";
                            OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象
                            Request request = new Request.Builder()
                                    .url(detailurl+item_id)//请求接口。如果需要传参拼接到接口后面。
                                    .build();
                            //Log.d(TAG,"itemid="+item_id);
                            Response response = null;
                            response = client.newCall(request).execute();//得到Response 对象
                            if (response.isSuccessful()) {
                                Log.d(TAG, "response.code()==" + response.code());
                                Log.d(TAG, "response.message()==" + response.message());
                                String responsestring = response.body().string();
                                Log.d(TAG, "res==" + responsestring);
                                //此时的代码执行在子线程，修改UI的操作请使用handler跳转到UI线程。
                                JSONObject root_object = new JSONObject(responsestring);
                                JSONObject news_object_array = root_object.getJSONObject("data");
                                JSONObject media_user = news_object_array.getJSONObject("media_user");
                                String detail = new String(news_object_array.getString("content"));
                                String avatar_url = new String(media_user.getString("avatar_url"));
                                Date time = new Date(news_object_array.getInt("publish_time"));
                                Log.d(TAG, "detail==" + detail);
                                Log.d(TAG, "publish_time==" + time);
                                Intent intent = new Intent(ShowPraise.this,NewsDetail.class);
                                intent.putExtra("item_id",item_id);
                                intent.putExtra("content",detail);
                                intent.putExtra("title",title);
                                intent.putExtra("source",source);
                                intent.putExtra("publish_time",time);
                                intent.putExtra("avatar_url",avatar_url);
                                //intent.putExtra("praiserPhoneNumber",praiserPhoneNumber);
                                startActivity(intent);
                            }
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                            Log.d(TAG, e.toString());
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




















    //    public void showvideo(RecyclerView rview, String video_id){
//        final String reqString = "https://api03.6bqb.com/xigua/app/generalSearch?apikey=B10A922C01D27BB7EEDB02717A72BDA1&keyword=" + keyword + "&page=" + page;
//        new Thread(() -> {
//            try {
//
//                OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象
//                Request request = new Request.Builder()
//                        .url(reqString)//请求接口。如果需要传参拼接到接口后面。
//                        .build();
//                Response response = null;
//                response = client.newCall(request).execute();//得到Response 对象
//                if (response.isSuccessful()) {
//                    String responsestring = response.body().string();
//                    Log.d(TAG, "res==" + responsestring);
//                    JSONObject root_object = new JSONObject(responsestring);
//                    JsonConversion.SearchPageConversion(root_object.getString("page"));
//                    Log.d(TAG,"NEXT SEARCH RESULT PAGE IS " + JsonConversion.search_next_page);
//                    JSONArray video_object_array = root_object.getJSONArray("data");
//                    //TODO Empty results?
//                    List<ToutiaoVideo> newlist = new ArrayList<>();
//                    for (int i = 0; i < video_object_array.length(); i++) {
//                        JSONObject videoobject = video_object_array.getJSONObject(i);
//                        ToutiaoVideo video = JsonConversion.GetVideoSearchResultFromJson(TAG,videoobject);
//                        if(video != null) {
//                            newlist.add(video);
//                        }
//                    }
//                    rview.post(() -> {
//                        if(page.equals("0")) {
//                            videoItemAdapter = new VideoItemAdapter(newlist, getActivity());
//                            rview.setAdapter(videoItemAdapter);
//                        }
//                        else{
//                            videoItemAdapter.ConcatenateVideoList(newlist);
//                        }
//                        cancelProgressDialog();
//                    });
//                }
//            } catch (IOException | JSONException e) {
//                e.printStackTrace();
//                Log.d(TAG, e.toString());
//            }
//        }).start();
//
//    }
//
//
//
//
//
//
//
//    /**
//     * Update the video list with the search results
//     * @param keyword the keyword for searching
//     * @param rview the recycler view to apply change
//     * @param page the page to update
//     */
//    private void UpdateVideoListBySearchKeyword(String keyword,RecyclerView rview,String page){
//
//    }
//
//    public class VideoItemAdapter extends RecyclerView.Adapter<VideoItemAdapter.VideoViewholder>{
//        List<ToutiaoVideo> list;
//
//        Activity activity;
//
//        public VideoItemAdapter(List<ToutiaoVideo> list,Activity activity){
//            this.list = list;
//            this.activity = activity;
//        }
//        @SuppressLint("ShowToast")
//        @NonNull
//        @Override
//        public VideoViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            View view = LayoutInflater.from(parent.getContext())
//                    .inflate(R.layout.videoitem,parent,false);
//            final VideoViewholder holder = new VideoViewholder(view);
//            VideoView video = view.findViewById(R.id.vid_v_itemvideoview);
//            Button play_btn = view.findViewById(R.id.v_play_btn);
//            ConstraintLayout play_btn_layout = view.findViewById(R.id.v_video_cover);
//            ConstraintLayout title_layout = view.findViewById(R.id.v_title_layout);
////            video.setMediaController(new MediaController(getContext()));
//            video.setOnClickListener((v) ->{
//                if(video.isPlaying()){
//                    video.pause();
//                    play_btn_layout.setVisibility(View.VISIBLE);
//                    title_layout.setVisibility(View.VISIBLE);
//                }
//            });
//            play_btn.setOnClickListener((v) ->{
//                video.start();
//                video.getBackground().setAlpha(0);
//                play_btn_layout.setVisibility(GONE);
//                title_layout.setVisibility(GONE);
//            });
//
//
//            LikeButton btn_like = view.findViewById(R.id.btn_v_like);
//
//
//
//            return holder;
//        }
//
//        @SuppressLint("ShowToast")
//        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
//        @Override
//        public void onBindViewHolder(@NonNull VideoViewholder holder, int position) {
//            ToutiaoVideo videoitem = list.get(position);
//            holder.avatar.setImageURL(videoitem.getUserInfo().getAvatar_url());
//            holder.user.setText(videoitem.getUserInfo().getName());
//            holder.video.setVideoPath(videoitem.getPlay_url());
//            holder.title.setText(videoitem.getTitle());
//            if(BmobUser.isLogin()) {
//                BmobQuery<Praise> praiseBmobQuery = new BmobQuery<>();
//                praiseBmobQuery.addWhereEqualTo("item_id", videoitem.getItem_id());
//                praiseBmobQuery.addWhereEqualTo("praiserPhoneNumber",user.getMobilePhoneNumber());
//                praiseBmobQuery.findObjects(new FindListener<Praise>() {
//                    @Override
//                    public void done(List<Praise> object, BmobException e) {
//                        if (e == null && object.size() != 0) {
//                            holder.btn_like.setLiked(true);
//                            Log.d(TAG, "PRAISE INFO FETCHED FOR" + object.get(0).getItem_id());
//                        } else {
//                            Log.e(TAG, e.getMessage());
//                        }
//                    }
//                });
//            }
//            holder.btn_like.setOnClickListener((v) ->{
//                if (BmobUser.isLogin()) {
//                    Praise praise = new Praise();
//                    praise.setItem_id(videoitem.getItem_id());
//                    praise.setPraiserPhoneNumber(user.getMobilePhoneNumber());
//                    praise.setType("video");
//                    if(holder.btn_like.isLiked()){
//                        praise.delete(new UpdateListener() {
//                            @Override
//                            public void done(BmobException e) {
//                                if(e == null){
//                                    holder.btn_like.setLiked(false);
//                                }
//                                else{
//                                    Log.e(TAG,e.toString());
//                                }
//                            }
//                        });
//                    }
//                    else{
//                        praise.save(new SaveListener<String>() {
//                            @Override
//                            public void done(String s, BmobException e) {
//                                if(e == null){
//                                    holder.btn_like.setLiked(true);
//                                }
//                                else{
//                                    Log.e(TAG,e.toString());
//                                }
//                            }
//                        });
//                    }
//                } else {
//                    Toast.makeText(getActivity(),"未登陆不可点赞",Toast.LENGTH_SHORT);
//                }
//            });
//            holder.btn_comment.setOnClickListener((v) ->{
//                String item_id = videoitem.getItem_id();
//                String play_url = videoitem.getPlay_url();
//                Intent intent = new Intent(getActivity(),VideoDetail.class);
//                intent.putExtra("item_id",item_id);
//                intent.putExtra("play_url",play_url);
//                startActivity(intent);
//            });
//            holder.layout.setVisibility(View.VISIBLE);
//            new Thread(() ->{
//                try {
//                    OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象
//                    Request request = new Request.Builder()
//                            .url(list.get(position).getCover_url())//请求接口。如果需要传参拼接到接口后面。
//                            .build();
//                    Response response = null;
//                    response = client.newCall(request).execute();//得到Response 对象
//                    Log.d(TAG,"COVER HTTP CONNECTION ESTABLISHED");
//                    if (response.isSuccessful()) {
//                        InputStream inputStream = response.body().byteStream();
//                        //使用工厂把网络的输入流生产Bitmap
//                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
////                        holder.video.setBackground(new BitmapDrawable(bitmap));
//                        holder.video.post(()->{
//                            holder.video.setBackground(new BitmapDrawable(bitmap));
//                            holder.video.getBackground().setAlpha(255);
//                        });
//                        inputStream.close();
//                    }else {
//                        Log.e(TAG,"VIDEO COVER IMAGE FAILED TO GET");
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    Log.e(TAG, Objects.requireNonNull(e.getMessage()));
//
//                }
//            }).start();
//        }
//
//        @Override
//        public int getItemCount() {
//            return list.size();
//        }
//
//        /**
//         * For loading more video, add the videos of next page to the tail of video list
//         * @param next_page_video the list of added video
//         */
//        public void ConcatenateVideoList(List<ToutiaoVideo> next_page_video){
//            list.addAll(next_page_video);
//            notifyDataSetChanged();
//        }
//
//        public class VideoViewholder extends RecyclerView.ViewHolder{
//            MyImageView avatar;
//            TextView user;
//            VideoView video;
//            ConstraintLayout layout;
//            Button btn_comment;
//            LikeButton btn_like;
//            TextView title;
//            public VideoViewholder(@NonNull View itemView) {
//                super(itemView);
//                avatar = itemView.findViewById(R.id.img_v_avatar);
//                user = itemView.findViewById(R.id.txt_v_username);
//                video = itemView.findViewById(R.id.vid_v_itemvideoview);
//                layout = itemView.findViewById(R.id.v_video_cover);
//                btn_comment = itemView.findViewById(R.id.btn_v_comment);
//                title = itemView.findViewById(R.id.v_video_title);
//                btn_like = itemView.findViewById(R.id.btn_v_like);
//            }
//        }
//
//    }



}