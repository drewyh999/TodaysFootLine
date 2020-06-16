package cn.thundergaba.todaysfootline;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.like.LikeButton;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


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
        RecyclerView videolist = view.findViewById(R.id.v_video_list);
        LinearLayoutManager video_list_manager = new LinearLayoutManager(getActivity());
        videolist.setLayoutManager(video_list_manager);
        UpdateVideoListByCategory("video_new",videolist);
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

    private void UpdateVideoListByCategory(String category,RecyclerView rview) {
        final String reqString = "https://api03.6bqb.com/xigua/app/categoryVideo?apikey=B10A922C01D27BB7EEDB02717A72BDA1&category=";
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
            video.setMediaController(new MediaController(getActivity()));
            Button btn_comment = view.findViewById(R.id.btn_v_comment);
            btn_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Jump to video detail activity
                }
            });
            LikeButton btn_like = view.findViewById(R.id.btn_v_like);
            //TODO Get the user information and do the like


            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull VideoViewholder holder, int position) {
            // TODO user avatar is not correctly loaded
            holder.avatar.setImageURL(list.get(position).getUserInfo().getAvatar_url());
            holder.user.setText(list.get(position).getUserInfo().getName());
            holder.video.setVideoPath(list.get(position).getPlay_url());
            //TODO video not played
            //TODO get the thumbnail of each video
            // Bitmap map = new Bitmap();
            //holder.video.setBackground();
        }


        @Override
        public int getItemCount() {
            return list.size();
        }

        public class VideoViewholder extends RecyclerView.ViewHolder{
            MyImageView avatar;
            TextView user;
            VideoView video;
            public VideoViewholder(@NonNull View itemView) {
                super(itemView);
                avatar = itemView.findViewById(R.id.img_v_avatar);
                user = itemView.findViewById(R.id.txt_v_username);
                video = itemView.findViewById(R.id.vid_v_itemvideoview);
            }
        }


    }


}
