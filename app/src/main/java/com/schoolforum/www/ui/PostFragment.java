package com.schoolforum.www.ui;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.schoolforum.www.R;
import com.schoolforum.www.entity.Post;
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
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PostFragment extends Fragment {

    private Unbinder unbinder;
    @BindView(R.id.post)
    Button post_bt;
    @BindView(R.id.lv)
    ListView lv;
    SimpleAdapter adapter;
    List<Map<String,Object>> list;
    Map<String,Object> map;
    Context context;//上下文对象
    User user;//用户
    String postid,title,content,posttime,postman,picture;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this.getActivity();
        user = (User)getArguments().get("user");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post, container, false);
        //返回一个Unbinder值（进行解绑），注意这里的this不能使用getActivity()
        unbinder = ButterKnife.bind(this, view);
        initData();
        return view;
    }

    @OnClick(R.id.post)
    public void post(){
        Intent intent = new Intent(context,PostActivity.class);
        intent.putExtra("user",user);
        context.startActivity(intent);
        getActivity().finish();
    }

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
                                    adapter = new SimpleAdapter(context, list, R.layout.fragment_postlistitem, new String[]{"title","posttime"},
                                            new int[]{R.id.list_title,R.id.list_posttime});
                                    lv.setAdapter(adapter);
                                    lv.setOnItemClickListener(itemClickListener);
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
            Intent intent = new Intent(context,ReplyActivity.class);
            intent.putExtra("post",post);
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
