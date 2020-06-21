package cn.thundergaba.todaysfootline;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class NewsSearchJsonConversion {

    public static String next_page;

    public static ToutiaoNews GetNewsFromSearchJson(String TAG, JSONObject object){

        try {
            if(object.has("media_avatar_url") && object.has("title" ) && object.has("publish_time")){
                TouTiaoUserInfo newsUserInfo = new TouTiaoUserInfo(object.getString("source")
                        ,object.getString("media_avatar_url"));
//                String main_url_encoded = video_info_object.getJSONObject("video_list")
//                        .getJSONObject("video_2")
//                        .getString("main_url");
//                String main_url_decoded = new String(Base64.decode(main_url_encoded, Base64.DEFAULT));
//                Log.d(TAG,"DECODED URL:" + main_url_decoded);
                String title = new String(object.getString("title"));
                String item_id = new String(object.getString("item_id"));
                Date publish_time = new Date(object.getInt("publish_time"));
                ToutiaoNews result = new ToutiaoNews();
                result.setTitle(title);
                result.setIs_liked(false);
                result.setUserInfo(newsUserInfo);
                result.setItem_id(item_id);
                result.setPublishTime(publish_time);
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
