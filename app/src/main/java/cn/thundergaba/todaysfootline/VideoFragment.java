package cn.thundergaba.todaysfootline;

import android.app.Activity;
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


    private String mParam1;
    private String mParam2;

    private HashMap<String,String> categories;

    private OnFragmentInteractionListener mListener;

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
        videolist.setLayoutManager(video_list_manager);

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
        UpdateVideoListByCategory(categories.get(tabLayout.getTabAt(tabLayout.getSelectedTabPosition()).getText().toString()),videolist,"0");
        SmartRefreshLayout smartRefreshLayout = view.findViewById(R.id.refreshLayout);
        smartRefreshLayout.setOnRefreshListener((v) ->{
            smartRefreshLayout.finishRefresh(2000);
            String category = tabLayout.getTabAt(tabLayout.getSelectedTabPosition()).getText().toString();
            Log.d(TAG,"THE SELECTED TAB TEXT IS " + category);
            UpdateVideoListByCategory(categories.get(category),videolist,"0");

        });
        //TODO implement load more
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
                    Log.d(TAG, "response.code()==" + response.code());
                    Log.d(TAG, "response.message()==" + response.message());
                    String responsestring = response.body().string();
                    Log.d(TAG, "res==" + responsestring);
                    //此时的代码执行在子线程，修改UI的操作请使用handler跳转到UI线程。
                    JSONObject root_object = new JSONObject(responsestring);
                    JSONArray video_object_array = root_object.getJSONArray("data");
                    rview.post(() -> {
                            try {
                                List<ToutiaoVideo> newlist = new ArrayList<>();
                                for (int i = 0; i < video_object_array.length(); i++) {
                                    JSONObject videoobject = video_object_array.getJSONObject(i);
                                    ToutiaoVideo video = JsonConversion.GetVideoFromJson(TAG,videoobject);
                                    if(video != null) {
                                        newlist.add(video);
                                    }
                                }
                                VideoItemAdapter adapter;
                                adapter = new VideoItemAdapter(newlist,getActivity());
                                rview.setAdapter(adapter);

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

    public class VideoItemAdapter extends RecyclerView.Adapter<VideoItemAdapter.VideoViewholder>{
        List<ToutiaoVideo> list;

        Activity activity;

        public VideoItemAdapter(List<ToutiaoVideo> list,Activity activity){
            this.list = list;
            this.activity = activity;
        }
        @NonNull
        @Override
        public VideoViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.videoitem,parent,false);
            final VideoViewholder holder = new VideoViewholder(view);
            VideoView video = view.findViewById(R.id.vid_v_itemvideoview);
            Button play_btn = view.findViewById(R.id.v_play_btn);
            ConstraintLayout play_btn_layout = view.findViewById(R.id.v_video_cover);
//            video.setMediaController(new MediaController(getContext()));
            video.setOnClickListener((v) ->{
                if(video.isPlaying()){
                    video.pause();
                    play_btn_layout.setVisibility(View.VISIBLE);
                }
            });
            play_btn.setOnClickListener((v) ->{
                video.start();
                video.getBackground().setAlpha(0);
                play_btn_layout.setVisibility(View.GONE);
            });


            LikeButton btn_like = view.findViewById(R.id.btn_v_like);
            btn_like.setOnClickListener((v) ->{

            });
            //TODO Get the user information and do the like


            return holder;
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onBindViewHolder(@NonNull VideoViewholder holder, int position) {
            ToutiaoVideo videoitem = list.get(position);
            holder.avatar.setImageURL(videoitem.getUserInfo().getAvatar_url());
            holder.user.setText(videoitem.getUserInfo().getName());
            holder.video.setVideoPath(videoitem.getPlay_url());
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

        public class VideoViewholder extends RecyclerView.ViewHolder{
            MyImageView avatar;
            TextView user;
            VideoView video;
            ConstraintLayout layout;
            Button btn_comment;
            public VideoViewholder(@NonNull View itemView) {
                super(itemView);
                avatar = itemView.findViewById(R.id.img_v_avatar);
                user = itemView.findViewById(R.id.txt_v_username);
                video = itemView.findViewById(R.id.vid_v_itemvideoview);
                layout = itemView.findViewById(R.id.v_video_cover);
                btn_comment = itemView.findViewById(R.id.btn_v_comment);
            }
        }


    }

}
