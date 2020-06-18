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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

import java.util.ArrayList;
import java.util.List;

public class VideoDetail extends AppCompatActivity {
    private static final String TAG = "VIDEO_DETAIL";

    private CommentAdapter commentAdapter;

    private String item_id;

    private RecyclerView recyclerView;

    private ConstraintLayout no_comment_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_detail);
        VideoView video = findViewById(R.id.v_video_detail);
        Button play_btn = findViewById(R.id.v_play_btn_detail);
        Button btn_submit_comment = findViewById(R.id.v_comment_submit);
        EditText comment_content = findViewById(R.id.v_comment_edit);
        recyclerView = findViewById(R.id.v_comment_rview);

        item_id = getIntent().getExtras().getString("item_id");
        no_comment_layout = findViewById(R.id.v_no_comment_layout);

        BmobGetCommentByItemId(item_id);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(linearLayoutManager);


        btn_submit_comment.setOnClickListener((v) -> {
            String content = comment_content.getText().toString();
            if (BmobUser.isLogin()) {
                User user = BmobUser.getCurrentUser(User.class);
                Comment comment_item = new Comment();
                comment_item.setUser_avatar(user.getAvatar());
                comment_item.setContent(comment_content.getText().toString());
                comment_item.setUser_name(user.getUsername());
                comment_item.setCommentitemid(item_id);
                commentAdapter.AddComment(comment_item);
                BmobInsertNewComment(comment_item);
                comment_content.setText("");
                Toast.makeText(VideoDetail.this,"评论成功",Toast.LENGTH_SHORT);
            } else {
               Toast.makeText(VideoDetail.this,"未登陆不可评论",Toast.LENGTH_SHORT);
            }
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


        Log.d(TAG,"CLICKED VIDEO ITEM ID:" + item_id);
    }

    private void BmobGetCommentByItemId(String item_id) {
        BmobQuery<Comment> categoryBmobQuery = new BmobQuery<>();
        categoryBmobQuery.addWhereEqualTo("commentitemid", item_id);
        categoryBmobQuery.findObjects(new FindListener<Comment>() {
            @Override
            public void done(List<Comment> object, BmobException e) {
                if (e == null) {
                    commentAdapter = new CommentAdapter(object);
                    recyclerView.setAdapter(commentAdapter);
                    if(object.size() != 0){
                        no_comment_layout.setVisibility(View.GONE);
                    }
                    Log.d(TAG,"COMMENTS FETCHED" + " size:" + object.size());
                } else {
                    Log.e(TAG, e.toString());
                }
            }
        });
    }

    private void BmobInsertNewComment(Comment newcomment){
        newcomment.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e == null){
                    Log.d(TAG,"COMMENT INSERTED" + newcomment.getUser_name() + newcomment.getContent());
                    Toast.makeText(VideoDetail.this,"评论成功",Toast.LENGTH_SHORT);
                }
                else{
                    Toast.makeText(VideoDetail.this,"评论失败",Toast.LENGTH_SHORT);
                    Log.e(TAG,e.toString());
                }
            }
        });
    }

    public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder>{
        List<Comment> list;

        public CommentAdapter(List<Comment> list) {
            this.list = list;
        }

        @NonNull
        @Override

        public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.commentitem,parent,false);
            return new CommentViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
            //TODO Commenter's avatar not correctly displayed
            holder.avatar.setImageURI(Uri.parse(list.get(position).getUser_avatar()));
            holder.username.setText(list.get(position).getUser_name());
            holder.content.setText(list.get(position).getContent());

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public void AddComment(Comment newcomment){
            list.add(newcomment);
            no_comment_layout.setVisibility(View.GONE);
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
