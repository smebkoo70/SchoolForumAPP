package com.schoolforum.www.ui;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.schoolforum.www.R;
import com.schoolforum.www.entity.User;

public class UserMainActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener{

    private BottomNavigationBar bottomNavigationBar;//底部导航栏
    private NoticeFragment noticeFragment;//教务公告选项卡
    private PostFragment postFragment;//学校论坛选项卡
    private PersonFragment personFragment;//个人选项卡
    User user;//用户对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_usermain);
        user = (User)getIntent().getSerializableExtra("user");
        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.mipmap.notice_pressed, "公告").setInactiveIcon(ContextCompat.getDrawable(this,R.mipmap.notice_released)))
                .addItem(new BottomNavigationItem(R.mipmap.forum_pressed, "论坛").setInactiveIcon(ContextCompat.getDrawable(this,R.mipmap.forum_released)))
                .addItem(new BottomNavigationItem(R.mipmap.personal_pressed, "个人").setInactiveIcon(ContextCompat.getDrawable(this,R.mipmap.personal_released)))
                .setFirstSelectedPosition(0)
                .initialise();
        bottomNavigationBar.setTabSelectedListener(this);
        initFragment();
        setDefaultFragment();
    }


    /**
     * 初始化Fragment
     */
    private void initFragment(){
        noticeFragment = new NoticeFragment();
        postFragment = new PostFragment();
        personFragment = new PersonFragment();
    }

    /**
     * 设置默认的
     */
    private void setDefaultFragment() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putSerializable("user",user);
        noticeFragment.setArguments(bundle);
        transaction.replace(R.id.tb, noticeFragment);
        transaction.commit();
    }

    /**
     * 选项卡选择
     * @param position
     */
    @Override
    public void onTabSelected(int position) {
        FragmentManager fm = this.getFragmentManager();
        //开启事务
        FragmentTransaction transaction = fm.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putSerializable("user",user);
        switch (position) {
            case 0:
                noticeFragment = new NoticeFragment();
                noticeFragment.setArguments(bundle);
                transaction.replace(R.id.tb, noticeFragment).commit();
                break;
            case 1:
                postFragment = new PostFragment();
                postFragment.setArguments(bundle);
                transaction.replace(R.id.tb, postFragment).commit();
                break;
            case 2:
                personFragment = new PersonFragment();
                personFragment.setArguments(bundle);
                transaction.replace(R.id.tb, personFragment).commit();
                break;
            default:
                break;
        }
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }




}

