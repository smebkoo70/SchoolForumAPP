package com.schoolforum.www.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.schoolforum.www.R;
import com.schoolforum.www.entity.Notice;
import com.schoolforum.www.entity.User;
import com.squareup.picasso.Picasso;
import butterknife.BindView;
import butterknife.ButterKnife;

public class NoticeInfoActivity extends AppCompatActivity {

    @BindView(R.id.left)
    ImageView left_iv;
    @BindView(R.id.title)
    TextView title_tv;
    @BindView(R.id.createman)
    TextView createman_tv;
    @BindView(R.id.createtime)
    TextView createtime_tv;
    @BindView(R.id.content)
    TextView content_tv;
    @BindView(R.id.picture)
    ImageView picture_iv;
    Context context;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_noticeinfo);
        ButterKnife.bind(this);
        context = this;
        user = (User) getIntent().getSerializableExtra("user");
        left_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                if(user==null){
                    intent = new Intent(context,NoticeManageActivity.class);
                }else{
                    intent = new Intent(context,UserMainActivity.class);
                    intent.putExtra("user",user);
                }
                startActivity(intent);
                finish();
            }
        });
        Notice notice = (Notice)getIntent().getSerializableExtra("notice");
        title_tv.setText(notice.getTitle());
        createman_tv.setText(notice.getCreateman());
        createtime_tv.setText(notice.getCreatetime());
        content_tv.setText(notice.getContent());
        String url = getResources().getString(R.string.notice_url)+notice.getPicture();
        Picasso.with(context).load(url).into(picture_iv);
    }






}
