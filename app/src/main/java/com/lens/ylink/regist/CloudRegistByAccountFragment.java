package com.lens.ylink.regist;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.lens.ylink.BaseFragment;
import com.lens.ylink.R;
import com.yealink.common.data.AccountConstant;
import com.yealink.common.data.CloudProfile;
import com.yealink.sdk.RegistListener;
import com.yealink.sdk.YealinkApi;

/**
 * 注册云账号
 * Created by chenhn on 2017/10/19.
 */

public class CloudRegistByAccountFragment extends BaseFragment {

    private EditText nameText;
    private EditText pwdText;
    private EditText serverText;
    private TextView statusText;
    private Handler mHandler;

    RegistListener mRegistListener = new RegistListener() {
        @Override
        public void onCloudRegist(final int status) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    updateStatus(status);
                }
            });

        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.regist_cloud_by_account,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nameText = findViewById(R.id.cloud_email);
        pwdText = findViewById(R.id.cloud_password);
        serverText = findViewById(R.id.cloud_server);
        statusText = findViewById(R.id.cloud_status);
        setHasOptionsMenu(true);
        CloudProfile cp = YealinkApi.instance().getCloudProfile();
        updateStatus(cp.state);
        mHandler = new Handler();
        YealinkApi.instance().addRegistListener(mRegistListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        YealinkApi.instance().removeRegistListener(mRegistListener);
    }

    private void login(){
        String name = nameText.getText().toString();
        String pwd = pwdText.getText().toString();
        String server = serverText.getText().toString();
        YealinkApi.instance().registerCloudByAccount(name,pwd,server);
    }

    private void logout(){
        YealinkApi.instance().unregistCloud();
    }

    private void updateStatus(int status){
        if (status == AccountConstant.STATE_UNKNOWN) {
            statusText.setText("未知");
        } else if (status == AccountConstant.STATE_DISABLED) {
            statusText.setText("禁用");
        } else if (status == AccountConstant.STATE_REGING) {
            statusText.setText("正在注册...");
        } else if (status == AccountConstant.STATE_REGED) {
            statusText.setText("已注册");
        } else if (status == AccountConstant.STATE_REG_FAILED) {
            statusText.setText("注册失败");
        } else if (status == AccountConstant.STATE_UNREGING) {
            statusText.setText("正在注销...");
        } else if (status == AccountConstant.STATE_UNREGED) {
            statusText.setText("已注销");
        } else if (status == AccountConstant.STATE_REG_ON_BOOT) {
            statusText.setText("启动时注册");
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.login:
                login();
                break;
            case R.id.loginOut:
                logout();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
