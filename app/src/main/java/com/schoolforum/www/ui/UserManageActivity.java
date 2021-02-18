package com.schoolforum.www.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.schoolforum.www.R;
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

public class UserManageActivity extends AppCompatActivity {

    @BindView(R.id.left)
    ImageView left_iv;
    @BindView(R.id.lv)
    ListView lv;
    SimpleAdapter adapter;
    List<Map<String,Object>> list;
    Map<String,Object> map;
    Context context;
    String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_usermanage);
        ButterKnife.bind(this);
        context = this;
        left_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,AdminMainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        initData();
    }

    private void initData(){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(getResources().getString(R.string.api_url)+"getuserlist").build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(context, "获取用户信息失败！", Toast.LENGTH_SHORT).show();
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
                                        String loginname = jsonObject.getString("loginname");
                                        String password = jsonObject.getString("password");
                                        String username = jsonObject.getString("username");
                                        String studentno = jsonObject.getString("studentno");
                                        String major = jsonObject.getString("major");
                                        String telephone = jsonObject.getString("telephone");
                                        String sex = jsonObject.getString("sex");
                                        map = new HashMap<>();
                                        map.put("userid",id);
                                        map.put("loginname",loginname);
                                        map.put("password",password);
                                        map.put("username",username);
                                        map.put("studentno",studentno);
                                        map.put("major",major);
                                        map.put("telephone",telephone);
                                        map.put("sex",sex);
                                        list.add(map);
                                    }
                                    adapter = new SimpleAdapter(context, list, R.layout.activity_userlistitem, new String[]{"loginname","username","studentno","sex"},
                                            new int[]{R.id.list_loginname,R.id.list_username,R.id.list_studentno,R.id.list_sex});
                                    lv.setAdapter(adapter);
                                    lv.setOnItemClickListener(itemClickListener);
                                    lv.setOnItemLongClickListener(itemLongClickListener);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Toast.makeText(context, "获取用户信息失败！", Toast.LENGTH_SHORT).show();
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
            LayoutInflater inflator = LayoutInflater.from(context);
            View v = inflator.inflate(R.layout.activity_userinfo, null);
            TextView loginname_tv = v.findViewById(R.id.loginname);
            TextView username_tv = v.findViewById(R.id.username);
            TextView studentno_tv = v.findViewById(R.id.studentno);
            TextView major_tv = v.findViewById(R.id.major);
            TextView telephone_tv = v.findViewById(R.id.telephone);
            TextView sex_tv = v.findViewById(R.id.sex);
            loginname_tv.setText((String)map.get("loginname"));
            username_tv.setText((String)map.get("username"));
            studentno_tv.setText((String)map.get("studentno"));
            major_tv.setText((String)map.get("major"));
            telephone_tv.setText((String)map.get("telephone"));
            sex_tv.setText((String)map.get("sex"));

            new AlertDialog.Builder(context)
                    .setTitle("查看详情：")
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setView(v)
                    .setPositiveButton("关闭", null)
                    .show();
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
                            userid = list.get(position).get("userid").toString();
                            OkHttpClient client = new OkHttpClient();
                            Request request = new Request.Builder().url(getResources().getString(R.string.api_url)+"deleteuserinfo?userid="+userid).build();
                            Call call = client.newCall(request);
                            call.enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    Toast.makeText(context, "删除用户失败！", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    if(response.isSuccessful()){
                                        final String responseStr = response.body().string();
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if ("success".equals(responseStr)) {
                                                    Toast.makeText(context, "删除用户成功！", Toast.LENGTH_SHORT).show();
                                                    initData();
                                                } else {
                                                    Toast.makeText(context, "删除用户失败！", Toast.LENGTH_SHORT).show();
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
