package cn.thundergaba.todaysfootline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.like.LikeButton;

import org.sufficientlysecure.htmltextview.HtmlAssetsImageGetter;
import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlResImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class NewsDetail extends AppCompatActivity {

    private static final String TAG = "NEWS_DETAIL";
    private VideoDetail.CommentAdapter commentAdapter;
    private String item_id;
    private String type;

    @SuppressLint("ShowToast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        HtmlTextView news_detail = findViewById(R.id.n_news_detail);
        TextView news_title = findViewById(R.id.n_news_title);
        TextView news_source = findViewById(R.id.n_news_source);
        TextView news_time = findViewById(R.id.n_news_time);
        MyImageView news_image = findViewById(R.id.n_news_image);
        ScrollView news_scrollView = findViewById(R.id.n_news_scrollView);

        Intent intent = getIntent();
        type = "news";
        item_id = intent.getStringExtra("item_id");
        String detail = intent.getStringExtra("content");
        String title = intent.getStringExtra("title");
        String source = intent.getStringExtra("source");
        String time = intent.getStringExtra("publish_time");
        String avatar_url = intent.getStringExtra("avatar_url");
        String praiserPhoneNumber = intent.getStringExtra("praiserPhoneNumber");
        news_source.setText(source);
//        news_time.setText(time);
        news_title.setText(title);
        news_image.setImageURL(avatar_url);
        news_detail.setHtml(detail, new HtmlHttpImageGetter(news_detail));

        LikeButton btn_n_like = findViewById(R.id.btn_n_like);
        Button btn_submit_comment = findViewById(R.id.n_comment_submit);
        EditText comment_content = findViewById(R.id.n_comment_edit);

        if(BmobUser.isLogin()) {
            BmobQuery<Praise> praiseBmobQuery = new BmobQuery<>();
            praiseBmobQuery.addWhereEqualTo("item_id", item_id);
            praiseBmobQuery.addWhereEqualTo("praiserPhoneNumber",praiserPhoneNumber);
//            praiseBmobQuery.addWhereEqualTo("type",type);
            praiseBmobQuery.findObjects(new FindListener<Praise>() {
                @Override
                public void done(List<Praise> object, BmobException e) {
                    if (e == null && object.size() != 0) {
                        btn_n_like.setLiked(true);
                        Log.d(TAG, "PRAISE INFO FETCHED FOR" + object.get(0).getItem_id());
                    } else {
                        Log.e(TAG, e.getMessage());
                    }
                }
            });
        }
        btn_n_like.setOnClickListener((v) ->{
            if (BmobUser.isLogin()) {
                Praise praise = new Praise();
                praise.setItem_id(item_id);
                praise.setPraiserPhoneNumber(praiserPhoneNumber);
                praise.setType(type);
                if(btn_n_like.isLiked()){
                    praise.delete(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e == null){
                                btn_n_like.setLiked(false);
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
                                btn_n_like.setLiked(true);
                            }
                            else{
                                Log.e(TAG,e.toString());
                            }
                        }
                    });
                }
            } else {
                Toast.makeText(this,"未登陆不可点赞",Toast.LENGTH_SHORT);
            }
        });
        btn_submit_comment.setOnClickListener((v) ->{
            String content = comment_content.getText().toString();
            //TODO submit comment
            if (BmobUser.isLogin()) {
                User user = BmobUser.getCurrentUser(User.class);
                Comment comment_item = new Comment();
                comment_item.setUser_avatar(user.getAvatar());
                comment_item.setContent(comment_content.getText().toString());
                comment_item.setUser_name(user.getUsername());
                comment_item.setCommentitemid(item_id);
                comment_item.setType(type);
//                commentAdapter.AddComment(comment_item);
                BmobInsertNewComment(comment_item);
                comment_content.setText("");
                Toast.makeText(NewsDetail.this,"评论成功",Toast.LENGTH_SHORT);
            } else {
                Toast.makeText(NewsDetail.this,"未登陆不可评论",Toast.LENGTH_SHORT);
            }
        });
//        ConstraintLayout play_btn_layout = findViewById(R.id.v_video_cover_detail);
////            video.setMediaController(new MediaController(getContext()));
//        video.setOnClickListener((v) ->{
//            if(video.isPlaying()){
//                video.pause();
//                play_btn_layout.setVisibility(View.VISIBLE);
//            }
//        });
//        play_btn.setOnClickListener((v) ->{
//            video.start();
//            play_btn_layout.setVisibility(View.GONE);
//        });
//        video.setVideoPath(getIntent().getExtras().getString("play_url"));
//        play_btn_layout.setVisibility(View.GONE);
//        video.start();
//        String item_id = getIntent().getExtras().getString("item_id");
//        Log.d(TAG,"CLICKED VIDEO ITEM ID:" + item_id);
        //TODO Get Comment Data and set the adapter
    }

    private void BmobInsertNewComment(Comment newcomment){
        newcomment.save(new SaveListener<String>() {
            @SuppressLint("ShowToast")
            @Override
            public void done(String s, BmobException e) {
                if(e == null){
                    Log.d(TAG,"COMMENT INSERTED" + newcomment.getUser_name() + newcomment.getContent());
                    Toast.makeText(NewsDetail.this,"评论成功",Toast.LENGTH_SHORT);
                }
                else{
                    Toast.makeText(NewsDetail.this,"评论失败",Toast.LENGTH_SHORT);
                    Log.e(TAG,e.toString());
                }
            }
        });
    }

//    public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder>{
//        List<Comment> list;
//
//        public CommentAdapter(List<Comment> list) {
//            this.list = list;
//        }
//
//        @NonNull
//        @Override
//        public CommentAdapter.CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            View view = LayoutInflater.from(parent.getContext())
//                    .inflate(R.layout.commentitem,parent,false);
//            return new CommentAdapter.CommentViewHolder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull NewsDetail.CommentAdapter.CommentViewHolder holder, int position) {
////            holder.avatar.setImageURI();
//            //TODO Set the avatar of commentors
//            holder.username.setText(list.get(position).getUsername());
//            holder.content.setText(list.get(position).getContent());
//
//        }
//
//        @Override
//        public int getItemCount() {
//            return list.size();
//        }
//
//        public void Refresh(){
//            GetCommentData();
//        }
//
//        public void GetCommentData(){
//            //TODO Implement Get Comment Data
//            notifyDataSetChanged();
//        }
//
//        public class CommentViewHolder extends RecyclerView.ViewHolder{
//
//            ImageView avatar;
//
//            TextView username;
//
//            TextView content;
//
//            public CommentViewHolder(@NonNull View itemView) {
//                super(itemView);
//                avatar = itemView.findViewById(R.id.v_commentor_avatar);
//                username = itemView.findViewById(R.id.v_commentor_id);
//                content = itemView.findViewById(R.id.v_commentor_content);
//            }
//        }
//    }

}