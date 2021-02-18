package com.schoolforum.www.ui;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import com.schoolforum.www.R;
import com.schoolforum.www.entity.Notice;
import com.schoolforum.www.entity.User;
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
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class NoticeFragment extends Fragment {

    private Unbinder unbinder;
    @BindView(R.id.lv)
    ListView lv;
    SimpleAdapter adapter;
    List<Map<String,Object>> list;
    Map<String,Object> map;
    Context context;//上下文对象
    User user;//用户
    String noticeid,title,content,createtime,createman,picture;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this.getActivity();
        user = (User)getArguments().get("user");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notice, container, false);
        //返回一个Unbinder值（进行解绑），注意这里的this不能使用getActivity()
        unbinder = ButterKnife.bind(this, view);
        initData();
        return view;
    }


    private void initData(){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(getResources().getString(R.string.api_url)+"getnoticelist").build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(context, "获取公告信息失败！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    final String responseStr = response.body().string();
                    if (getActivity() == null)
                        return;
                    getActivity().runOnUiThread(new Runnable() {
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
                                        String createtime = jsonObject.getString("createtime");
                                        String createman = jsonObject.getString("createman");
                                        String picture = jsonObject.getString("picture");
                                        map = new HashMap<>();
                                        map.put("noticeid",id);
                                        map.put("title",title);
                                        map.put("content",content);
                                        map.put("createtime",createtime);
                                        map.put("createman",createman);
                                        map.put("picture",picture);
                                        list.add(map);
                                    }
                                    adapter = new SimpleAdapter(context, list, R.layout.fragment_noticelistitem, new String[]{"title","createtime"},
                                            new int[]{R.id.list_title,R.id.list_createtime});
                                    lv.setAdapter(adapter);
                                    lv.setOnItemClickListener(itemClickListener);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Toast.makeText(context, "获取公告信息失败！", Toast.LENGTH_SHORT).show();
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
            noticeid = map.get("noticeid").toString();
            title = (String)map.get("title");
            content = (String)map.get("content");
            createtime = (String)map.get("createtime");
            createman = (String)map.get("createman");
            picture = (String)map.get("picture");
            Notice notice = new Notice(noticeid,title,content,createman,createtime,picture);
            Intent intent = new Intent(context,NoticeInfoActivity.class);
            intent.putExtra("notice",notice);
            intent.putExtra("user",user);
            startActivity(intent);
            getActivity().finish();
        }
    };

    /**
     * onDestroyView中进行解绑操作
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
