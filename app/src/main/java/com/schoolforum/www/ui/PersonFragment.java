package com.schoolforum.www.ui;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.schoolforum.www.R;
import com.schoolforum.www.entity.User;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PersonFragment extends Fragment {

    private Unbinder unbinder;
    @BindView(R.id.loginname)
    TextView loginname_tv;
    @BindView(R.id.username)
    TextView username_tv;
    @BindView(R.id.studentno)
    TextView studentno_tv;
    @BindView(R.id.major)
    TextView major_tv;
    @BindView(R.id.telephone)
    TextView telephone_tv;
    @BindView(R.id.sex)
    TextView sex_tv;
    @BindView(R.id.modify)
    Button modifyBtn;
    @BindView(R.id.quit)
    Button quitBtn;
    Context context;//上下文对象
    User user;//用户

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this.getActivity();
        user = (User)getArguments().get("user");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person, container, false);
        //返回一个Unbinder值（进行解绑），注意这里的this不能使用getActivity()
        unbinder = ButterKnife.bind(this, view);
        loginname_tv.setText(user.getLoginnname());
        username_tv.setText(user.getUsername());
        studentno_tv.setText(user.getStudentno());
        major_tv.setText(user.getMajor());
        telephone_tv.setText(user.getTelephone());
        sex_tv.setText(user.getSex());
        modifyBtn.setOnClickListener(clickListener);
        quitBtn.setOnClickListener(clickListener);
        return view;
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch(v.getId()) {
                case R.id.modify:
                    intent = new Intent(context,ModifyUserActivity.class);
                    intent.putExtra("user",user);
                    context.startActivity(intent);
                    getActivity().finish();
                    break;
                case R.id.quit:
                    new AlertDialog.Builder(context)
                            .setTitle("确定要退出系统吗？")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(context,LoginActivity.class);
                                    context.startActivity(intent);
                                    getActivity().finish();
                                }
                            })
                            .setNegativeButton("取消",null)
                            .show();
                    break;
                default:
                    break;
            }
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
