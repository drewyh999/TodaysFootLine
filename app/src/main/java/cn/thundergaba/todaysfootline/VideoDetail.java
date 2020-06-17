package cn.thundergaba.todaysfootline;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class VideoDetail extends AppCompatActivity {
    private static final String TAG = "VIDEO_DETAIL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_detail);
        VideoView video = findViewById(R.id.v_video_detail);
        Button play_btn = findViewById(R.id.v_play_btn_detail);
        Button btn_submit_comment = findViewById(R.id.v_comment_submit);
        EditText comment_content = findViewById(R.id.v_comment_edit);
        btn_submit_comment.setOnClickListener((v) ->{
            String content = comment_content.getText().toString();
            //TODO submit comment
        });
        ConstraintLayout play_btn_layout = findViewById(R.id.v_video_cover_detail);
//            video.setMediaController(new MediaController(getContext()));
        video.setOnClickListener((v) ->{
            if(video.isPlaying()){
                video.pause();
                play_btn_layout.setVisibility(View.VISIBLE);
            }
        });
        play_btn.setOnClickListener((v) ->{
            video.start();
            play_btn_layout.setVisibility(View.GONE);
        });
        video.setVideoPath(getIntent().getExtras().getString("play_url"));
        play_btn_layout.setVisibility(View.GONE);
        video.start();
        String item_id = getIntent().getExtras().getString("item_id");
        Log.d(TAG,"CLICKED VIDEO ITEM ID:" + item_id);
        //TODO Get Comment Data and set the adapter
    }

    public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder>{
        List<ToutiaoComment> list;
        @NonNull
        @Override
        public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.commentitem,parent,false);
            return new CommentViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
//            holder.avatar.setImageURI();
            //TODO Set the avatar of commentors
            holder.username.setText(list.get(position).getUsername());
            holder.content.setText(list.get(position).getContent());

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public void Refresh(){
            GetCommentData();
        }

        public void GetCommentData(){
            //TODO Implement Get Comment Data
            notifyDataSetChanged();
        }

        public class CommentViewHolder extends RecyclerView.ViewHolder{

            ImageView avatar;

            TextView username;

            TextView content;

            public CommentViewHolder(@NonNull View itemView) {
                super(itemView);
                avatar = itemView.findViewById(R.id.v_commentor_avatar);
                username = itemView.findViewById(R.id.v_commentor_id);
                content = itemView.findViewById(R.id.v_commentor_content);
            }
        }
    }

}
