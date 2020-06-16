package cn.thundergaba.todaysfootline;

import android.util.Base64;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class JsonConversion {
    public static ToutiaoVideo GetVideoFromJson(String TAG,JSONObject object){

        try {
            if(object.has("user_info") && object.has("video_play_info")){
                JSONObject user_object = object.getJSONObject("user_info");
                TouTiaoUserInfo userInfo = new TouTiaoUserInfo(user_object.getString("name")
                                                            ,user_object.getString("avatar_url"));
                JSONObject video_info_object = new JSONObject(object.getString("video_play_info"));
                String main_url_encoded = video_info_object.getJSONObject("video_list")
                                            .getJSONObject("video_2")
                                            .getString("main_url");
                String main_url_decoded = new String(Base64.decode(main_url_encoded, Base64.DEFAULT));
                Log.d(TAG,"DECODED URL:" + main_url_decoded);
                String item_id = new JSONObject(object.getString("pread_params")).getString("item_id");
                Date publish_date = new Date(object.getInt("publish_time"));
                ToutiaoVideo result = new ToutiaoVideo();
                result.setIs_liked(false);
                result.setUserInfo(userInfo);
                result.setPlay_url(main_url_decoded);
                result.setItem_id(item_id);
                result.setPublishTime(publish_date);
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
