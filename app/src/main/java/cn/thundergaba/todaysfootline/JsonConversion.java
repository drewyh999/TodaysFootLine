package cn.thundergaba.todaysfootline;

import android.util.Base64;
import android.util.Log;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;

public class JsonConversion {

    public static String next_page;

    public static String search_next_page;

    public static ToutiaoVideo GetVideoFromJson(String TAG,JSONObject object) throws JSONException {
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
                Log.d(TAG,"DECODED PLAY URL:" + main_url_decoded);
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
    }

    public static ToutiaoVideo GetVideoSearchResultFromJson(String TAG,JSONObject object) throws JSONException, IOException {
        if(object.has("item_id") && object.has("title")) {
            String item_id = object.getString("item_id");
            Log.d(TAG,"FETCHING DETAIL FOR SEARCH RESULT " + item_id);
            String title = object.getString("title");
            Date publish_date = new Date(object.getInt("publish_time"));
            String reqString = "https://api03.6bqb.com/xigua/app/videoDetail?apikey=B10A922C01D27BB7EEDB02717A72BDA1&itemId=" + item_id;
            OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象
            Request request = new Request.Builder()
                    .url(reqString)//请求接口。如果需要传参拼接到接口后面。
                    .build();
            Response response = null;
            response = client.newCall(request).execute();//得到Response 对象
            if(response.isSuccessful()){
                String responsestring = response.body().string();
                Log.d(TAG,"SEARCH RESULT DETAIL FETCHED:" + responsestring);
                JSONObject root_object = new JSONObject(responsestring);
                JSONObject data_object = root_object.getJSONObject("data");
                if(data_object.has("user_info") && data_object.has("video_info")) {
                    JSONObject user_object = data_object.getJSONObject("user_info");
                    TouTiaoUserInfo userInfo = new TouTiaoUserInfo(user_object.getString("name")
                            , user_object.getString("avatar_url"));
                    if(!data_object.getJSONObject("video_info").has("video_url")){
                        return null;
                    }
                    String play_url = data_object.getJSONObject("video_info").getString("video_url");
                    String first_frame_pic_url = data_object.getJSONObject("video_info").getString("video_poster");
                    ToutiaoVideo result = new ToutiaoVideo();
                    result.setTitle(title);
                    result.setIs_liked(false);
                    result.setUserInfo(userInfo);
                    result.setPlay_url(play_url);
                    result.setItem_id(item_id);
                    result.setPublishTime(publish_date);
                    result.setCover_url(first_frame_pic_url);
                    return result;
                }
            }
        }
        return null;
    }

    public static void SearchPageConversion(String encoded_page_json){
        String decoded_page_json = new String(Base64.decode(encoded_page_json, Base64.DEFAULT));
        try {
            JSONObject page_object = new JSONObject(decoded_page_json);
            search_next_page = page_object.getString("page");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
