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
import android.widget.TextView;
import android.widget.Toast;
import com.schoolforum.www.R;
import com.schoolforum.www.entity.Post;
import com.squareup.picasso.Picasso;
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

public class PostInfoActivity extends AppCompatActivity {

    @BindView(R.id.left)
    ImageView left_iv;
    @BindView(R.id.title)
    TextView title_tv;
    @BindView(R.id.postman)
    TextView postman_tv;
    @BindView(R.id.posttime)
    TextView posttime_tv;
    @BindView(R.id.content)
    TextView content_tv;
    @BindView(R.id.picture)
    ImageView picture_iv;
    @BindView(R.id.lv)
    ListView lv;
    SimpleAdapter adapter;
    List<Map<String,Object>> list;
    Map<String,Object> map;
    Context context;
    String postid,replyid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_postinfo);
        ButterKnife.bind(this);
        context = this;
        left_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,PostManageActivity.class);
                startActivity(intent);
                finish();
            }
        });
        Post post = (Post)getIntent().getSerializableExtra("post");
        postid = post.getId();
        title_tv.setText(post.getTitle());
        postman_tv.setText(post.getPostman());
        posttime_tv.setText(post.getPosttime());
        content_tv.setText(post.getContent());
        String url = getResources().getString(R.string.post_url)+post.getPicture();
        Picasso.with(context).load(url).into(picture_iv);
        initData();
    }

    private void initData(){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(getResources().getString(R.string.api_url)+"getreplylist?postid="+postid).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(context, "获取回复信息失败！", Toast.LENGTH_SHORT).show();
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
                                        String comment = jsonObject.getString("comment");
                                        String replytime = jsonObject.getString("replytime");
                                        String replyman = jsonObject.getString("replyman");
                                        map = new HashMap<>();
                                        map.put("replyid",id);
                                        map.put("comment",comment);
                                        map.put("replytime",replytime);
                                        map.put("replyman",replyman);
                                        list.add(map);
                                    }
                                    adapter = new SimpleAdapter(context, list, R.layout.activity_replylistitem, new String[]{"comment","replyman","replytime"},
                                            new int[]{R.id.list_comment,R.id.list_replyman,R.id.list_replytime});
                                    lv.setAdapter(adapter);
                                    lv.setOnItemLongClickListener(itemLongClickListener);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Toast.makeText(context, "获取回复信息失败！", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }

    private AdapterView.OnItemLongClickListener itemLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
            new AlertDialog.Builder(context)
                    .setTitle("确定要删除吗？")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            replyid = list.get(position).get("replyid").toString();
                            OkHttpClient client = new OkHttpClient();
                            Request request = new Request.Builder().url(getResources().getString(R.string.api_url)+"deletereply?replyid="+replyid).build();
                            Call call = client.newCall(request);
                            call.enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    Toast.makeText(context, "删除回复失败！", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    if(response.isSuccessful()){
                                        final String responseStr = response.body().string();
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if ("success".equals(responseStr)) {
                                                    Toast.makeText(context, "删除回复成功！", Toast.LENGTH_SHORT).show();
                                                    initData();
                                                } else {
                                                    Toast.makeText(context, "删除回复失败！", Toast.LENGTH_SHORT).show();
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
