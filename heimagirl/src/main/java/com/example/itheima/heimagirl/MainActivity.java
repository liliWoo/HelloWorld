package com.example.itheima.heimagirl;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    @BindView(R.id.lv_girls)
    ListView lvGirls;

    private Gson mgson;
    private GirlAdapter mAdapter;
    private List<ResultBean.ResultsBean> mlist;
    //private ListView lvGirls;
    private boolean isLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //lvGirls = (ListView) findViewById(R.id.lv_girls);

        //sendSyncRequest();
        sendAsyncRequest();

        mAdapter = new GirlAdapter();
        mlist = new ArrayList<ResultBean.ResultsBean>();

        lvGirls.setAdapter(mAdapter);

        lvGirls.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                if (scrollState == SCROLL_STATE_IDLE) {
                    if (lvGirls.getLastVisiblePosition() == mlist.size() - 1 && !isLoading) {

                        loadmoreData();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });


    }

    private void loadmoreData() {
        isLoading = true;

        OkHttpClient okHttpClient = new OkHttpClient();
        //创建网络请求
        int page = mlist.size() / 10 + 1;
        String url = "http://gank.io/api/data/福利/10/" + page;
        Request request = new Request.Builder().get().url(url).build();
        //异步
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(MainActivity.this, "网络超时...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                mgson = new Gson();
                String result = response.body().string();
                ResultBean resultBean = mgson.fromJson(result, ResultBean.class);
                mlist.addAll(resultBean.getResults());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged();
                    }
                });
            }
        });


        isLoading = false;
    }

    private void sendAsyncRequest() {
        OkHttpClient okHttpClient = new OkHttpClient();
        //创建网络请求
        String url = "http://gank.io/api/data/福利/10/1";
        Request request = new Request.Builder().get().url(url).build();
        //异步
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(MainActivity.this, "网络超时...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                mgson = new Gson();
                String result = response.body().string();
                ResultBean resultBean = mgson.fromJson(result, ResultBean.class);
                mlist.addAll(resultBean.getResults());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    private void sendSyncRequest() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                OkHttpClient okHttpClient = new OkHttpClient();
                //创建网络请求
                String url = "http://gank.io/api/data/福利/10/1";
                Request request = new Request.Builder().get().url(url).build();

                //同步
                try {
                    Response response = okHttpClient.newCall(request).execute();
                    mgson = new Gson();
                    String result = response.body().string();
                    ResultBean resultBean = mgson.fromJson(result, ResultBean.class);


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }

    class GirlAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mlist.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(MainActivity.this, R.layout.item_girls_view, null);
                holder.ivGirls = (ImageView) convertView.findViewById(R.id.iv_girls);
                holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);

                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }
  
            ResultBean.ResultsBean resultsBean = mlist.get(position);
            holder.tvTime.setText(resultsBean.getPublishedAt());

            /*Glide.with(MainActivity.this)
                    .load(resultsBean.getUrl())
                    .centerCrop()
                    .into(holder.ivGirls);*/

            Glide.with(MainActivity.this).load(resultsBean.getUrl()).centerCrop()
                    .bitmapTransform(new CropCircleTransformation(MainActivity.this))
                    .into(holder.ivGirls);

            return convertView;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }
    }

    static class ViewHolder {
        ImageView ivGirls;
        TextView tvTime;
    }

}
