package com.example.itheima.heimagirl;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.io.IOException;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        OkHttpClient okHttpClient = new OkHttpClient();
        //创建网络请求
        String url = "http://gank.io/api/data/%E7%A6%8F%E5%88%A9/10/1";
        Request request = new Request.Builder().get().url(url).build();

        //同步
        /*try {
            Response response = okHttpClient.newCall(request).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        //异步
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(okhttp3.Call call, IOException e) {
                    
                }

                @Override
                public void onResponse(okhttp3.Call call, Response response) throws IOException {

                }
            });
    }


}
