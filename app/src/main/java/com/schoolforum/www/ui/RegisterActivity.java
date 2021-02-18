package com.schoolforum.www.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.tu.loadingdialog.LoadingDailog;
import com.schoolforum.www.R;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.loginname)
    EditText loginname_et;
    @BindView(R.id.password)
    EditText password_et;
    @BindView(R.id.password2)
    EditText password2_et;
    @BindView(R.id.username)
    EditText username_et;
    @BindView(R.id.studentno)
    EditText studentno_et;
    @BindView(R.id.major)
    EditText major_et;
    @BindView(R.id.telephone)
    EditText telephone_et;
    @BindView(R.id.nanRb)
    RadioButton nanRb;
    @BindView(R.id.nvRb)
    RadioButton nvRb;
    @BindView(R.id.registerBtn)
    Button registerBtn;
    @BindView(R.id.loginBtn)
    Button loginBtn;
    String loginname,password,password2,username,studentno,major,telephone,sex;
    Context context;
    LoadingDailog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        context = this;
        registerBtn.setOnClickListener(clickListener);
        loginBtn.setOnClickListener(clickListener);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.registerBtn://注册按钮
                    loginname = loginname_et.getText().toString().trim();
                    password = password_et.getText().toString().trim();
                    password2 = password2_et.getText().toString().trim();
                    username = username_et.getText().toString().trim();
                    studentno = studentno_et.getText().toString().trim();
                    major = major_et.getText().toString().trim();
                    telephone = telephone_et.getText().toString().trim();
                    sex = nanRb.isChecked()?"男":"女";
                    if(loginname.equals("")){
                        Toast.makeText(context,"请输入用户名！",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(password.equals("")){
                        Toast.makeText(context,"请输入密码！",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(password2.equals("")){
                        Toast.makeText(context,"请确认密码！",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(!password.equals(password2)){
                        Toast.makeText(context,"两次密码输入不一致！",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(username.equals("")){
                        Toast.makeText(context,"请输入姓名！",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(studentno.equals("")){
                        Toast.makeText(context,"请输入学号！",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    LoadingDailog.Builder loadBuilder = new LoadingDailog.Builder(context)
                            .setMessage("注册中...")
                            .setCancelable(true)
                            .setCancelOutside(true);
                    dialog = loadBuilder.create();
                    dialog.show();

                    OkHttpClient client = new OkHttpClient();
                    FormBody formBody = new FormBody
                            .Builder()
                            .add("loginname", loginname)
                            .add("password", password)
                            .add("username", username)
                            .add("studentno", studentno)
                            .add("major", major)
                            .add("telephone", telephone)
                            .add("sex", sex)
                            .build();
                    Request request = new Request.Builder().url(getResources().getString(R.string.api_url)+"register").post(formBody).build();
                    Call call = client.newCall(request);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dialog.dismiss();
                                    Toast.makeText(context, "注册失败！", Toast.LENGTH_SHORT).show();
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
                                    if ("success".equals(responseStr)) {
                                        Toast.makeText(context, "注册成功！", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(context,LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(context, "注册失败！", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });
                    break;
                case R.id.loginBtn://登录按钮
                    Intent intent = new Intent(context,LoginActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                default:
                    break;
            }

        }
    };











}
