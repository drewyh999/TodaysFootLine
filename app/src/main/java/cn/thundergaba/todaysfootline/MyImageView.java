package cn.thundergaba.todaysfootline;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@SuppressLint("AppCompatCustomView")
public class MyImageView extends ImageView {
    public static final int GET_DATA_SUCCESS = 1;
    public static final int NETWORK_ERROR = 2;
    public static final int SERVER_ERROR = 3;
    private static final String TAG = "MYIMGVIEW";
    //子线程不能操作UI，通过Handler设置图片
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case GET_DATA_SUCCESS:
                    RoundedBitmapDrawable bitmap = (RoundedBitmapDrawable) msg.obj;
                    setImageDrawable(bitmap);
                    break;
                case NETWORK_ERROR:
                    Toast.makeText(getContext(),"网络连接失败",Toast.LENGTH_SHORT).show();
                    break;
                case SERVER_ERROR:
                    Toast.makeText(getContext(),"服务器发生错误",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    public MyImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyImageView(Context context) {
        super(context);
    }

    public MyImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //设置网络图片
    public void setImageURL(final String path) {
        //开启一个线程用于联网
        new Thread() {
            @Override
            public void run() {
                try {
                    //把传过来的路径转成URL
//                    URL url = new URL(path);
//                    Log.d(TAG,"IMG_URL:" + url.getPath());
//                    Log.d(TAG,"IMG_PATH:" + path);
//                    //获取连接
//                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                    //使用GET方法访问网络
//                    connection.setRequestMethod("GET");
//                    //超时时间为10秒
//                    connection.setConnectTimeout(10000);
                    OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象
                    Request request = new Request.Builder()
                            .url(path)//请求接口。如果需要传参拼接到接口后面。
                            .build();
                    Response response = null;
                    response = client.newCall(request).execute();//得到Response 对象

                    if (response.isSuccessful()) {
                        InputStream inputStream = response.body().byteStream();
                        //使用工厂把网络的输入流生产Bitmap
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources()
                                                                                                        ,bitmap);
                        roundedBitmapDrawable.setCircular(true);
                        //利用Message把图片发给Handler
                        Message msg = Message.obtain();
                        msg.obj = roundedBitmapDrawable;
                        msg.what = GET_DATA_SUCCESS;
                        handler.sendMessage(msg);
                        inputStream.close();
                    }else {
                        //服务启发生错误
                        handler.sendEmptyMessage(SERVER_ERROR);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    //网络连接错误
                    handler.sendEmptyMessage(NETWORK_ERROR);
                }
            }
        }.start();
    }

}

