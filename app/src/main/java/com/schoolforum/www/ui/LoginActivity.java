package com.schoolforum.www.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import com.android.tu.loadingdialog.LoadingDailog;
import com.schoolforum.www.R;
import com.schoolforum.www.entity.User;
import com.schoolforum.www.util.PermissionsUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.loginname)
    EditText loginname_et;
    @BindView(R.id.password)
    EditText password_et;
    @BindView(R.id.savename)
    CheckBox savename_cb;
    @BindView(R.id.loginBtn)
    Button loginBtn;
    @BindView(R.id.registerBtn)
    Button registerBtn;
    String loginname,password;
    Context context;
    LoadingDailog dialog;
    SharedPreferences sp;
    public static String fileDir = Environment.getExternalStorageDirectory() + File.separator + "SchoolForumAPP";//文件目录
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        context = this;
        registerBtn.setOnClickListener(clickListener);
        loginBtn.setOnClickListener(clickListener);

        sp = getSharedPreferences("savename", MODE_PRIVATE);
        savename_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    loginname = loginname_et.getText().toString().trim();
                    password = password_et.getText().toString().trim();
                    SharedPreferences.Editor ed = sp.edit();
                    ed.putString("loginname", loginname);
                    ed.putString("password", password);
                    ed.commit();
                }
            }
        });

        String loginname1 = sp.getString("loginname", "");
        String password1 = sp.getString("password", "");
        if (!loginname1.equals("") || !password1.equals("")) {
            savename_cb.setChecked(true);
        }
        loginname_et.setText(loginname1);
        password_et.setText(password1);

        permission();
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.loginBtn://登录按钮
                    loginname = loginname_et.getText().toString().trim();
                    password = password_et.getText().toString().trim();
                    if(loginname.equals("")){
                        Toast.makeText(context,"请输入用户名！",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(password.equals("")){
                        Toast.makeText(context,"请输入密码！",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    SharedPreferences.Editor ed = sp.edit();
                    ed.putString("loginname", loginname);
                    ed.putString("password", password);
                    ed.commit();

                    LoadingDailog.Builder loadBuilder = new LoadingDailog.Builder(context)
                            .setMessage("登录中...")
                            .setCancelable(true)
                            .setCancelOutside(true);
                    dialog = loadBuilder.create();
                    dialog.show();

                    OkHttpClient client = new OkHttpClient();
                    FormBody formBody = new FormBody
                            .Builder()
                            .add("loginname", loginname)
                            .add("password", password)
                            .build();
                    Request request = new Request.Builder().url(getResources().getString(R.string.api_url)+"login").post(formBody).build();
                    Call call = client.newCall(request);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dialog.dismiss();
                                    Toast.makeText(context, "登录失败！", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onResponse(Call call, final Response response) throws IOException {
                            final String responseStr = response.body().string();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dialog.dismiss();
                                    if (responseStr!=null && !responseStr.equals("")) {
                                        Toast.makeText(context, "登录成功！", Toast.LENGTH_SHORT).show();
                                        if("admin".equals(loginname)){
                                            Intent intent = new Intent(context,AdminMainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }else{
                                            try {
                                                JSONArray jsonArray = new JSONArray(responseStr);
                                                JSONObject jsonObject = (JSONObject) jsonArray.getJSONObject(0);
                                                String id = jsonObject.getString("id");
                                                String loginname = jsonObject.getString("loginname");
                                                String password = jsonObject.getString("password");
                                                String username = jsonObject.getString("username");
                                                String studentno = jsonObject.getString("studentno");
                                                String major = jsonObject.getString("major");
                                                String telephone = jsonObject.getString("telephone");
                                                String sex = jsonObject.getString("sex");

                                                user = new User(id,loginname,password,username,studentno,major,telephone,sex);
                                                Intent intent = new Intent(context, UserMainActivity.class);
                                                intent.putExtra("user", user);
                                                startActivity(intent);
                                                finish();
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    } else {
                                        Toast.makeText(context, "登录失败！", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });
                    break;
                case R.id.registerBtn://注册按钮
                    Intent intent = new Intent(context,RegisterActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                default:
                    break;
            }
        }
    };


    /**
     * 动态授权
     */
    public void permission(){
        //一个拍照权限和一个数据读写权限
        String[] permissions = new String[]{
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.READ_PHONE_STATE
        };
        //PermissionsUtils.showSystemSetting = false;//是否支持显示系统设置权限设置窗口跳转
        //这里的this不是上下文，是Activity对象！
        PermissionsUtils.getInstance().chekPermissions(this, permissions, permissionsResult);
    }

    //创建监听权限的接口对象
    PermissionsUtils.IPermissionsResult permissionsResult = new PermissionsUtils.IPermissionsResult() {
        @Override
        public void passPermissons() {
            //Toast.makeText(MainActivity.this, "权限通过，可以做其他事情!", Toast.LENGTH_SHORT).show();
            File dir = new File(fileDir);
            if(!dir.exists()){
                dir.mkdirs();
            }
        }

        @Override
        public void forbitPermissons() {
            //Toast.makeText(MainActivity.this, "权限不通过!", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //就多一个参数this
        PermissionsUtils.getInstance().onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }



}
