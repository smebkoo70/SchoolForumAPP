package com.schoolforum.www.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.schoolforum.www.R;
import com.schoolforum.www.entity.User;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ModifyUserActivity extends AppCompatActivity {

    @BindView(R.id.left)
    ImageView left_iv;
    @BindView(R.id.save)
    Button save_bt;
    @BindView(R.id.loginname)
    EditText loginname_et;
    @BindView(R.id.password)
    EditText password_et;
    @BindView(R.id.username)
    EditText username_et;
    @BindView(R.id.studentno)
    EditText studentno_et;
    @BindView(R.id.major)
    EditText major_et;
    @BindView(R.id.telephone)
    EditText telephone_et;
    @BindView(R.id.nan)
    RadioButton nan_rb;
    @BindView(R.id.nv)
    RadioButton nv_rb;
    Context context;
    User user;
    String userid,loginname,password,username,studentno,major,telephone,sex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_modifyuser);
        ButterKnife.bind(this);
        context = this;
        user = (User)getIntent().getSerializableExtra("user");
        initData();
    }

    private void initData(){
        userid = user.getId();
        loginname_et.setText(user.getLoginnname());
        password_et.setText(user.getPassword());
        username_et.setText(user.getUsername());
        studentno_et.setText(user.getStudentno());
        major_et.setText(user.getMajor());
        telephone_et.setText(user.getTelephone());
        if(user.getSex().equals("男")){
            nan_rb.setChecked(true);
        }else{
            nv_rb.setChecked(true);
        }
    }


    @OnClick({R.id.left,R.id.save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left:
                Intent intent = new Intent(context,UserMainActivity.class);
                intent.putExtra("user",user);
                startActivity(intent);
                finish();
                break;
            case R.id.save:
                loginname = loginname_et.getText().toString().trim();
                password = password_et.getText().toString().trim();
                username = username_et.getText().toString().trim();
                studentno = studentno_et.getText().toString().trim();
                major = major_et.getText().toString().trim();
                telephone = telephone_et.getText().toString().trim();
                sex = nan_rb.isChecked()?"男":"女";
                if(loginname.equals("")){
                    Toast.makeText(context,"请输入用户名！",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(password.equals("")){
                    Toast.makeText(context,"请输入密码！",Toast.LENGTH_SHORT).show();
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
                OkHttpClient client = new OkHttpClient();
                FormBody formBody = new FormBody
                        .Builder()
                        .add("userid", userid)
                        .add("loginname", loginname)
                        .add("password", password)
                        .add("username", username)
                        .add("studentno", studentno)
                        .add("major", major)
                        .add("telephone", telephone)
                        .add("sex", sex)
                        .build();
                Request request = new Request.Builder().url(getResources().getString(R.string.api_url)+"modifyuserinfo").post(formBody).build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "修改个人信息失败！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        final String responseStr = response.body().string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if ("success".equals(responseStr)) {
                                    Toast.makeText(context, "修改个人信息成功！", Toast.LENGTH_SHORT).show();
                                    user = new User(userid,loginname,password,username,studentno,major,telephone,sex);
                                } else {
                                    Toast.makeText(context, "修改个人信息失败！", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                break;
            default:
                break;
        }
    }


}
