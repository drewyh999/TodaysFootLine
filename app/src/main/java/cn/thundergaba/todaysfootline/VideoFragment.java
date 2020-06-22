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
import android.util.Log;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import com.google.android.material.tabs.TabLayout;
import com.like.LikeButton;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static android.view.View.GONE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link VideoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link VideoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VideoFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private final String TAG = "VIDEO_FRAGMENT";

    private ProgressDialog progressDialog;

    private String mParam1;
    private String mParam2;

    private HashMap<String,String> categories;

    private VideoItemAdapter videoItemAdapter;

    private OnFragmentInteractionListener mListener;

    private boolean isSearching = false;//Indicate if search mode is applied

    private User user;

    public VideoFragment() {
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

    public static VideoFragment newInstance(String param1, String param2) {
        VideoFragment fragment = new VideoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private void InitCategories(){
        categories = new HashMap<>();
        categories.put("全部","video_new");
        categories.put("小视频","xg_hotsoon_video");
        categories.put("游戏","subv_xg_game");
        categories.put("vlog","subv_xg_vlog");
        categories.put("影视","subv_xg_movie");
        categories.put("音乐","subv_xg_music");
        categories.put("综艺","subv_variety");
        categories.put("农人","subv_video_agriculture");
        categories.put("美食","subv_video_food");
        categories.put("宠物","subv_video_pet");
        categories.put("儿童","subv_video_child");
        categories.put("搞笑","subv_xg_funny");
        categories.put("体育","subv_car");
        categories.put("娱乐","subv_xg_entertainment");
        categories.put("文化","subv_video_culture");
        categories.put("手工","subv_xg_hand_made");
        categories.put("金秒奖","subv_jmj");
        categories.put("科技","subv_video_tech");
        categories.put("广场舞","subv_video_squaredance");
        categories.put("旅游","subv_xg_travel");
        categories.put("NBA","subv_xg_nba");
        categories.put("军事","subv_xg_military");
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

        View view = inflater.inflate(R.layout.fragment_video_view, container, false);
        InitCategories();

        RecyclerView videolist = view.findViewById(R.id.v_video_list);
        LinearLayoutManager video_list_manager = new LinearLayoutManager(getActivity());
        SearchView searchView = view.findViewById(R.id.v_search_view);
        videolist.setLayoutManager(video_list_manager);
        if(BmobUser.isLogin()) {
            user = BmobUser.getCurrentUser(User.class);
        }
        TabLayout tabLayout = view.findViewById(R.id.v_tablayout);
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
                UpdateVideoListByCategory(categories.get(tab.getText().toString()),videolist,"0");
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
        UpdateVideoListByCategory(categories.get(tabLayout.getTabAt(tabLayout.getSelectedTabPosition()).getText().toString()),videolist,"0");
        SmartRefreshLayout smartRefreshLayout = view.findViewById(R.id.refreshLayout);
        smartRefreshLayout.setOnRefreshListener((v) -> {
            if(!isSearching) {
                smartRefreshLayout.finishRefresh(2000);
                String category = tabLayout.getTabAt(tabLayout.getSelectedTabPosition()).getText().toString();
                Log.d(TAG, "THE SELECTED TAB TEXT IS " + category);
                UpdateVideoListByCategory(categories.get(category), videolist, "0");
            }
            else{
                smartRefreshLayout.finishLoadMore(2000);
                String keyword = searchView.getQuery().toString();
                UpdateVideoListBySearchKeyword(keyword,videolist,"0");
            }
        });
        smartRefreshLayout.setOnLoadMoreListener((v) -> {
            if(!isSearching) {
                smartRefreshLayout.finishLoadMore(2000);
                String category = tabLayout.getTabAt(tabLayout.getSelectedTabPosition()).getText().toString();
                UpdateVideoListByCategory(categories.get(category), videolist, JsonConversion.next_page);
            }
            else{
                smartRefreshLayout.finishLoadMore(2000);
                String keyword = searchView.getQuery().toString();
                UpdateVideoListBySearchKeyword(keyword,videolist,JsonConversion.search_next_page);
            }
        });

        Button quit_search_btn = view.findViewById(R.id.v_btn_quitsearch);
        quit_search_btn.setOnClickListener((v) -> {
            isSearching = false;
            tabLayout.setVisibility(View.VISIBLE);
            buildProgressDialog(0);
            UpdateVideoListByCategory(categories.get(tabLayout.getTabAt(tabLayout.getSelectedTabPosition()).getText().toString()),videolist,"0");
            quit_search_btn.setVisibility(View.GONE);
        });
        quit_search_btn.setVisibility(View.GONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(!query.equals("")){
                    isSearching = true;
                    tabLayout.setVisibility(View.GONE);
                    buildProgressDialog(0);
                    UpdateVideoListBySearchKeyword(query,videolist,"0");
                    quit_search_btn.setVisibility(View.VISIBLE);
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
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
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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

    /**
     * Update the video list by selected category
     * @param category the String indicate the category
     * @param rview the recycler view to apply change
     * @param page the page to update
     */
    private void UpdateVideoListByCategory(String category,RecyclerView rview,String page) {

        final String reqString = "https://api03.6bqb.com/xigua/app/categoryVideo?apikey=B10A922C01D27BB7EEDB02717A72BDA1&category=" + category + "&page=" + page;
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
                    JSONObject root_object = new JSONObject(responsestring);
                    JsonConversion.next_page = root_object.getString("page");
                    Log.d(TAG,"NEXT RESULT PAGE IS " + JsonConversion.next_page);
                    JSONArray video_object_array = root_object.getJSONArray("data");
                    List<ToutiaoVideo> newlist = new ArrayList<>();
                    for (int i = 0; i < video_object_array.length(); i++) {
                        JSONObject videoobject = video_object_array.getJSONObject(i);
                        ToutiaoVideo video = JsonConversion.GetVideoFromJson(TAG,videoobject);
                        if(video != null) {
                            newlist.add(video);
                        }
                    }
                    rview.post(() -> {
                        if(page.equals("0")) {
                            videoItemAdapter = new VideoItemAdapter(newlist, getActivity());
                            rview.setAdapter(videoItemAdapter);
                        }
                        else{
                            videoItemAdapter.ConcatenateVideoList(newlist);
                        }
                        cancelProgressDialog();
                    });
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                Log.d(TAG, e.toString());
            }
        }).start();
    }

    /**
     * Update the video list with the search results
     * @param keyword the keyword for searching
     * @param rview the recycler view to apply change
     * @param page the page to update
     */
    private void UpdateVideoListBySearchKeyword(String keyword,RecyclerView rview,String page){
        final String reqString = "https://api03.6bqb.com/xigua/app/generalSearch?apikey=B10A922C01D27BB7EEDB02717A72BDA1&keyword=" + keyword + "&page=" + page;
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
                    JSONObject root_object = new JSONObject(responsestring);
                    JsonConversion.SearchPageConversion(root_object.getString("page"));
                    Log.d(TAG,"NEXT SEARCH RESULT PAGE IS " + JsonConversion.search_next_page);
                    JSONArray video_object_array = root_object.getJSONArray("data");
                    //TODO Empty results?
                    List<ToutiaoVideo> newlist = new ArrayList<>();
                    for (int i = 0; i < video_object_array.length(); i++) {
                        JSONObject videoobject = video_object_array.getJSONObject(i);
                        ToutiaoVideo video = JsonConversion.GetVideoSearchResultFromJson(TAG,videoobject);
                        if(video != null) {
                                newlist.add(video);
                        }
                    }
                    rview.post(() -> {
                        if(page.equals("0")) {
                            videoItemAdapter = new VideoItemAdapter(newlist, getActivity());
                            rview.setAdapter(videoItemAdapter);
                        }
                        else{
                            videoItemAdapter.ConcatenateVideoList(newlist);
                        }
                        cancelProgressDialog();
                    });
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                Log.d(TAG, e.toString());
            }
        }).start();
    }

    public class VideoItemAdapter extends RecyclerView.Adapter<VideoItemAdapter.VideoViewholder>{
        List<ToutiaoVideo> list;

        Activity activity;

        public VideoItemAdapter(List<ToutiaoVideo> list,Activity activity){
            this.list = list;
            this.activity = activity;
        }
        @SuppressLint("ShowToast")
        @NonNull
        @Override
        public VideoViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.videoitem,parent,false);
            final VideoViewholder holder = new VideoViewholder(view);
            VideoView video = view.findViewById(R.id.vid_v_itemvideoview);
            Button play_btn = view.findViewById(R.id.v_play_btn);
            ConstraintLayout play_btn_layout = view.findViewById(R.id.v_video_cover);
            ConstraintLayout title_layout = view.findViewById(R.id.v_title_layout);
//            video.setMediaController(new MediaController(getContext()));
            video.setOnClickListener((v) ->{
                if(video.isPlaying()){
                    video.pause();
                    play_btn_layout.setVisibility(View.VISIBLE);
                    title_layout.setVisibility(View.VISIBLE);
                }
            });
            play_btn.setOnClickListener((v) ->{
                video.start();
                video.getBackground().setAlpha(0);
                play_btn_layout.setVisibility(GONE);
                title_layout.setVisibility(GONE);
            });


            LikeButton btn_like = view.findViewById(R.id.btn_v_like);



            return holder;
        }

        @SuppressLint("ShowToast")
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onBindViewHolder(@NonNull VideoViewholder holder, int position) {
            ToutiaoVideo videoitem = list.get(position);
            holder.avatar.setImageURL(videoitem.getUserInfo().getAvatar_url());
            holder.user.setText(videoitem.getUserInfo().getName());
            holder.video.setVideoPath(videoitem.getPlay_url());
            holder.title.setText(videoitem.getTitle());
            if(BmobUser.isLogin()) {
                BmobQuery<Praise> praiseBmobQuery = new BmobQuery<>();
                praiseBmobQuery.addWhereEqualTo("item_id", videoitem.getItem_id());
                praiseBmobQuery.addWhereEqualTo("praiserPhoneNumber",user.getMobilePhoneNumber());
                praiseBmobQuery.findObjects(new FindListener<Praise>() {
                    @Override
                    public void done(List<Praise> object, BmobException e) {
                        if (e == null && object.size() != 0) {
                            holder.btn_like.setLiked(true);
                            Log.d(TAG, "PRAISE INFO FETCHED FOR" + object.get(0).getItem_id());
                        } else {
                            Log.e(TAG, e.getMessage());
                        }
                    }
                });
            }
            holder.btn_like.setOnClickListener((v) ->{
                if (BmobUser.isLogin()) {
                    Praise praise = new Praise();
                    praise.setItem_id(videoitem.getItem_id());
                    praise.setPraiserPhoneNumber(user.getMobilePhoneNumber());
                    praise.setType("video");
                    if(holder.btn_like.isLiked()){
                        praise.delete(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if(e == null){
                                    holder.btn_like.setLiked(false);
                                }
                                else{
                                    Log.e(TAG,e.toString());
                                }
                            }
                        });
                    }
                    else{
                        praise.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if(e == null){
                                    holder.btn_like.setLiked(true);
                                }
                                else{
                                    Log.e(TAG,e.toString());
                                }
                            }
                        });
                    }
                } else {
                    Toast.makeText(getActivity(),"未登陆不可点赞",Toast.LENGTH_SHORT);
                }
            });
            holder.btn_comment.setOnClickListener((v) ->{
                String item_id = videoitem.getItem_id();
                String play_url = videoitem.getPlay_url();
                Intent intent = new Intent(getActivity(),VideoDetail.class);
                intent.putExtra("item_id",item_id);
                intent.putExtra("play_url",play_url);
                startActivity(intent);
            });
            holder.layout.setVisibility(View.VISIBLE);
            new Thread(() ->{
                try {
                    OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象
                    Request request = new Request.Builder()
                            .url(list.get(position).getCover_url())//请求接口。如果需要传参拼接到接口后面。
                            .build();
                    Response response = null;
                    response = client.newCall(request).execute();//得到Response 对象
                    Log.d(TAG,"COVER HTTP CONNECTION ESTABLISHED");
                    if (response.isSuccessful()) {
                        InputStream inputStream = response.body().byteStream();
                        //使用工厂把网络的输入流生产Bitmap
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//                        holder.video.setBackground(new BitmapDrawable(bitmap));
                        holder.video.post(()->{
                            holder.video.setBackground(new BitmapDrawable(bitmap));
                            holder.video.getBackground().setAlpha(255);
                        });
                        inputStream.close();
                    }else {
                        Log.e(TAG,"VIDEO COVER IMAGE FAILED TO GET");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, Objects.requireNonNull(e.getMessage()));

                }
            }).start();
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        /**
         * For loading more video, add the videos of next page to the tail of video list
         * @param next_page_video the list of added video
         */
        public void ConcatenateVideoList(List<ToutiaoVideo> next_page_video){
            list.addAll(next_page_video);
            notifyDataSetChanged();
        }

        public class VideoViewholder extends RecyclerView.ViewHolder{
            MyImageView avatar;
            TextView user;
            VideoView video;
            ConstraintLayout layout;
            Button btn_comment;
            LikeButton btn_like;
            TextView title;
            public VideoViewholder(@NonNull View itemView) {
                super(itemView);
                avatar = itemView.findViewById(R.id.img_v_avatar);
                user = itemView.findViewById(R.id.txt_v_username);
                video = itemView.findViewById(R.id.vid_v_itemvideoview);
                layout = itemView.findViewById(R.id.v_video_cover);
                btn_comment = itemView.findViewById(R.id.btn_v_comment);
                title = itemView.findViewById(R.id.v_video_title);
                btn_like = itemView.findViewById(R.id.btn_v_like);
            }
        }

    }

}
