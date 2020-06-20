package cn.thundergaba.todaysfootline;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.*;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.like.LikeButton;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private final String TAG = "NEWS_FRAGMENT";

    private ProgressDialog progressDialog;

    private HashMap<String,String> categories;

    private NewsItemAdapter newsItemAdapter;

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private User user;

    public NewsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VideoView.
     */

    public static NewsFragment newInstance(String param1, String param2) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private void InitCategories(){
        categories = new HashMap<>();
        categories.put("全部","_all_");
        categories.put("社会","news_society");
        categories.put("娱乐","news_entertainment");
        categories.put("科技","news_tech");
        categories.put("汽车","news_car");
        categories.put("体育","news_sports");
        categories.put("财经","news_finance");
        categories.put("军事","news_military");
        categories.put("国际","news_world");
        categories.put("健康","news_health");
        categories.put("正能量","positive");
        categories.put("房产","news_house");
        categories.put("时尚","news_fashion");
        categories.put("育儿","news_baby");
        categories.put("历史","news_history");
        categories.put("搞笑","funny");
        categories.put("数码","digital");
        categories.put("美食","news_food");
        categories.put("养生","news_regimen");
        categories.put("电影","movie");
        categories.put("手机","cellphone");
        categories.put("旅游","news_travel");
    }

    public void buildProgressDialog(int id) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news_views, container, false);
        InitCategories();
        RecyclerView newslist = view.findViewById(R.id.n_news_list);
        LinearLayoutManager news_list_manager = new LinearLayoutManager(getActivity());
        newslist.setLayoutManager(news_list_manager);
        if(BmobUser.isLogin()) {
            user = BmobUser.getCurrentUser(User.class);
        }
        TabLayout tabLayout = view.findViewById(R.id.n_tablayout);
        for(String key:categories.keySet()){
            TabLayout.Tab tab =tabLayout.newTab();
            tab.setText(key);
            tabLayout.addTab(tab);
        }
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                buildProgressDialog(0);
                UpdateNewsListByCategory(categories.get(tab.getText().toString()),newslist,"0");
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tabLayout.setSelected(true);
        buildProgressDialog(0);
        UpdateNewsListByCategory(categories.get(tabLayout.getTabAt(tabLayout.getSelectedTabPosition()).getText().toString()),newslist,"0");
        SmartRefreshLayout smartRefreshLayout = view.findViewById(R.id.refreshLayout);
        smartRefreshLayout.setOnRefreshListener((v) ->{
            smartRefreshLayout.finishRefresh(2000);
            String category = tabLayout.getTabAt(tabLayout.getSelectedTabPosition()).getText().toString();
            Log.d(TAG,"THE SELECTED TAB TEXT IS " + category);
            UpdateNewsListByCategory(categories.get(category),newslist,"0");

        });
        smartRefreshLayout.setOnLoadMoreListener((v) ->{
            smartRefreshLayout.finishLoadMore(2000);
            String category = tabLayout.getTabAt(tabLayout.getSelectedTabPosition()).getText().toString();
            UpdateNewsListByCategory(categories.get(category),newslist,NewsJsonConversion.next_page);
        });
        //TODO implement load more
        SearchView searchView = view.findViewById(R.id.n_search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String keyword) {
                if(TextUtils.isEmpty(keyword)){
                    Toast.makeText(getActivity(),"请输入查找内容",Toast.LENGTH_SHORT).show();
                }
                else{
                    UpdateNewsListBySearch(newslist,keyword);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return view;
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof NewsFragment.OnFragmentInteractionListener) {
            mListener = (NewsFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private void UpdateNewsListBySearch(RecyclerView nview, String keyword) {
        final String searchString = "https://api03.6bqb.com/toutiao/keyword?apikey=E449EF5F8EE5C6C6919C52AA98160F07&keyword=" + keyword + "&pageType=information&page=0";
        new Thread(() -> {
            try {

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
                    JSONArray news_object_array = root_object.getJSONArray("data");
                    nview.post(() -> {
                        try {
                            List<News> newslist = new ArrayList<>();
                            for (int i = 0; i < news_object_array.length(); i++) {
                                JSONObject newsobject = news_object_array.getJSONObject(i);
                                News news = NewsSearchJsonConversion.GetNewsFromSearchJson(TAG,newsobject);
                                if(news != null) {
                                    newslist.add(news);
                                }
                            }
                            NewsFragment.NewsItemAdapter adapter;
                            adapter = new NewsFragment.NewsItemAdapter(newslist,getActivity());
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
    }

    private void UpdateNewsListByCategory(String category,RecyclerView nview, String page) {
        final String reqString = "https://api03.6bqb.com/toutiao/search?apikey=E449EF5F8EE5C6C6919C52AA98160F07&tag=" + category + "&page=" + page;
        new Thread(() -> {
            try {

                OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象
                Request request = new Request.Builder()
                        .url(reqString)//请求接口。如果需要传参拼接到接口后面。
                        .build();
                Response response = null;
                response = client.newCall(request).execute();//得到Response 对象
                if (response.isSuccessful()) {
                    String responsestring = response.body().string();
                    Log.d(TAG, "res==" + responsestring);
                    //此时的代码执行在子线程，修改UI的操作请使用handler跳转到UI线程。
                    JSONObject root_object = new JSONObject(responsestring);
                    NewsJsonConversion.next_page = root_object.getString("page");
                    Log.d(TAG,"NEXT RESULT PAGE IS " + NewsJsonConversion.next_page);
                    JSONArray news_object_array = root_object.getJSONArray("data");
                    nview.post(() -> {
                        try {
                            List<News> newslist = new ArrayList<>();
                            for (int i = 0; i < news_object_array.length(); i++) {
                                JSONObject newsobject = news_object_array.getJSONObject(i);
                                News news = NewsJsonConversion.GetNewsFromJson(TAG,newsobject);
                                if(news != null) {
                                    newslist.add(news);
                                }
                            }
                            if(page == "0") {
                                newsItemAdapter = new NewsFragment.NewsItemAdapter(newslist, getActivity());
                                nview.setAdapter(newsItemAdapter);
                            }
                            else{
                                newsItemAdapter.ConcatenateNewsList(newslist);
                            }
                            cancelProgressDialog();
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
    }



    public class NewsItemAdapter extends RecyclerView.Adapter<NewsItemAdapter.NewsViewholder>{
        List<News> list;

        Activity activity;

        public NewsItemAdapter(List<News> list, Activity activity){
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
            NewsItem newsItem = list.get(position);
            holder.avatar.setImageURL(newsItem.getNewsUserInfo().getAvatar_url());
            holder.user.setText(list.get(position).getNewsUserInfo().getName());
            holder.news.setText(list.get(position).getTitle());
            holder.news.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String title = newsItem.getTitle();
                    String item_id = newsItem.getNewsItem_id();
                    String source = newsItem.getNewsUserInfo().getName();
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
                                Intent intent = new Intent(getActivity(),NewsDetail.class);
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

        public void ConcatenateNewsList(List<News> next_page_news){
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