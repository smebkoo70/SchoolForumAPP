package com.schoolforum.www.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import com.schoolforum.www.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AdminMainActivity extends AppCompatActivity {

    @BindView(R.id.usermanage)
    Button usermanageBtn;
    @BindView(R.id.noticemanage)
    Button noticemanageBtn;
    @BindView(R.id.postmanage)
    Button postmanageBtn;
    @BindView(R.id.quit)
    Button quitBtn;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_adminmain);
        ButterKnife.bind(this);
        context = this;
        usermanageBtn.setOnClickListener(clickListener);
        noticemanageBtn.setOnClickListener(clickListener);
        postmanageBtn.setOnClickListener(clickListener);
        quitBtn.setOnClickListener(clickListener);
    }


    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch(v.getId()) {
                case R.id.usermanage:
                    intent = new Intent(context,UserManageActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.noticemanage:
                    intent = new Intent(context,NoticeManageActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.postmanage:
                    intent = new Intent(context,PostManageActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.quit:
                    intent = new Intent(context,LoginActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                default:
                    break;
            }


        }
    };


}
