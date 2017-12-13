package com.lens.ylink;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.yealink.base.util.ToastUtil;
import com.yealink.common.NativeInit;
import com.yealink.common.data.SipProfile;
import com.yealink.sdk.YealinkApi;

public class SampleActivity extends AppCompatActivity implements NativeInit.NativeInitListener, View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        findViewById(R.id.start).setOnClickListener(this);
        NativeInit.registerListener(this);
        if(NativeInit.isInited()){
            //初始化完成展示UI
            FunctionFragment.show(getSupportFragmentManager(),"Root",DataSource.instance().getDatas());
        }
    }

    @Override
    public void onInitFinish() {
        //初始化完成展示UI
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView textView = (TextView) findViewById(R.id.notice);
                textView.setText("主页");
                FunctionFragment.show(getSupportFragmentManager(),"Root",DataSource.instance().getDatas());
            }
        });
    }

    @Override
    public void onNativeError(int errorCode) {
        //初始化失败，提示信息
        ToastUtil.toast(this,"端口被占用!");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.start:
                if(NativeInit.isInited()){
                    FunctionFragment.show(getSupportFragmentManager(),"Root",DataSource.instance().getDatas());
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SipProfile sp = YealinkApi.instance().getSipProfile();
        sp.registerName = "";
        sp.userName = "";
        sp.password = "";
        YealinkApi.instance().registerSip(sp);
        YealinkApi.instance().unregistCloud();
    }
}
