package cn.thundergaba.todaysfootline;

import android.util.Base64;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class JsonConversion {
    public static ToutiaoVideo GetVideoFromJson(String TAG,JSONObject object){

        try {
            if(object.has("user_info") && object.has("video_play_info") && object.has("first_frame_image")){
                JSONObject user_object = object.getJSONObject("user_info");
                TouTiaoUserInfo userInfo = new TouTiaoUserInfo(user_object.getString("name")
                                                            ,user_object.getString("avatar_url"));
                JSONObject video_info_object = new JSONObject(object.getString("video_play_info"));
                if(!video_info_object.getJSONObject("video_list").has("video_1")){
                    return null;
                }
                String main_url_encoded = video_info_object.getJSONObject("video_list")
                                            .getJSONObject("video_1")
                                            .getString("main_url");
                String main_url_decoded = new String(Base64.decode(main_url_encoded, Base64.DEFAULT));
                Log.d(TAG,"DECODED URL:" + main_url_decoded);
                String item_id = new JSONObject(object.getString("pread_params")).getString("item_id");
                Date publish_date = new Date(object.getInt("publish_time"));
                String first_frame_pic_url = object.getJSONObject("first_frame_image").getString("url");
                String title = object.getString("title");
                ToutiaoVideo result = new ToutiaoVideo();
                result.setTitle(title);
                result.setIs_liked(false);
                result.setUserInfo(userInfo);
                result.setPlay_url(main_url_decoded);
                result.setItem_id(item_id);
                result.setPublishTime(publish_date);
                result.setCover_url(first_frame_pic_url);
                return result;
            }
            else{
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, e.toString());
        }
        return null;
    }
}
