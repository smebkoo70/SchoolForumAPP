package com.schoolforum.www.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import com.schoolforum.www.R;
import com.schoolforum.www.entity.Post;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PostManageActivity extends AppCompatActivity {

    @BindView(R.id.left)
    ImageView left_iv;
    @BindView(R.id.lv)
    ListView lv;
    SimpleAdapter adapter;
    List<Map<String,Object>> list;
    Map<String,Object> map;
    Context context;
    String postid,title,content,posttime,postman,picture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_postmanage);
        ButterKnife.bind(this);
        context = this;
        left_iv.setOnClickListener(clickListener);
        initData();
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch(v.getId()) {
                case R.id.left:
                    intent = new Intent(context,AdminMainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                default:
                    break;
            }
        }
    };

    private void initData(){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(getResources().getString(R.string.api_url)+"getpostlist").build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(context, "获取发帖信息失败！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    final String responseStr = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (responseStr!=null && !responseStr.equals("")) {
                                list = new ArrayList<Map<String,Object>>();
                                try {
                                    JSONArray jsonArray = new JSONArray(responseStr);
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = (JSONObject) jsonArray.getJSONObject(i);
                                        int id = jsonObject.getInt("id");
                                        String title = jsonObject.getString("title");
                                        String content = jsonObject.getString("content");
                                        String posttime = jsonObject.getString("posttime");
                                        String postman = jsonObject.getString("postman");
                                        String picture = jsonObject.getString("picture");
                                        map = new HashMap<>();
                                        map.put("postid",id);
                                        map.put("title",title);
                                        map.put("content",content);
                                        map.put("posttime",posttime);
                                        map.put("postman",postman);
                                        map.put("picture",picture);
                                        list.add(map);
                                    }
                                    adapter = new SimpleAdapter(context, list, R.layout.activity_postlistitem, new String[]{"title","postman","posttime"},
                                            new int[]{R.id.list_title,R.id.list_postman,R.id.list_posttime});
                                    lv.setAdapter(adapter);
                                    lv.setOnItemClickListener(itemClickListener);
                                    lv.setOnItemLongClickListener(itemLongClickListener);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Toast.makeText(context, "获取发帖信息失败！", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }
    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            map = list.get(position);
            postid = map.get("postid").toString();
            title = (String)map.get("title");
            content = (String)map.get("content");
            posttime = (String)map.get("posttime");
            postman = (String)map.get("postman");
            picture = (String)map.get("picture");
            Post post = new Post(postid,title,content,postman,posttime,picture);
            Intent intent = new Intent(context,PostInfoActivity.class);
            intent.putExtra("post",post);
            startActivity(intent);
            finish();
        }
    };

    private AdapterView.OnItemLongClickListener itemLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
            new AlertDialog.Builder(context)
                    .setTitle("确定要删除吗？")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            postid = list.get(position).get("postid").toString();
                            OkHttpClient client = new OkHttpClient();
                            Request request = new Request.Builder().url(getResources().getString(R.string.api_url)+"deletepost?postid="+postid).build();
                            Call call = client.newCall(request);
                            call.enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    Toast.makeText(context, "删除帖子失败！", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    if(response.isSuccessful()){
                                        final String responseStr = response.body().string();
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if ("success".equals(responseStr)) {
                                                    Toast.makeText(context, "删除帖子成功！", Toast.LENGTH_SHORT).show();
                                                    initData();
                                                } else {
                                                    Toast.makeText(context, "删除帖子失败！", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                }
                            });

                        }
                    })
                    .setNegativeButton("取消", null)
                    .show();

            return true;
        }
    };


}
