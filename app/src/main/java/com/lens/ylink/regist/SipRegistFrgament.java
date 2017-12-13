package com.lens.ylink.regist;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.lens.ylink.BaseFragment;
import com.lens.ylink.R;
import com.yealink.base.util.ToastUtil;
import com.yealink.common.data.AccountConstant;
import com.yealink.common.data.SipProfile;
import com.yealink.sdk.RegistListener;
import com.yealink.sdk.YealinkApi;

/**
 * Sip账号注册
 * Created by chenhn on 2017/10/19.
 */

public class SipRegistFrgament extends BaseFragment implements Handler.Callback{

    private static final int MSG_ACCOUNT_CHANGE = 200;

    private static final String TAG = "SettingActivity";

    private Handler mHandler;

    private TextView sipStatus;
    private EditText mUserNameET;
    private EditText mRegisterNameET;
    private EditText mPasswordET;
    private EditText mSipServerET;
    private EditText mPortET;
    private EditText mOutboundServer;
    private EditText mOutboundServerPort;
    private Spinner mProtocolSP;
    private Spinner mBfcpSP;
    private SwitchCompat mOutboundSwitch;

    private RegistListener mRegistListener = new RegistListener() {
        @Override
        public void onSipRegist(int status) {
            mHandler.sendEmptyMessage(MSG_ACCOUNT_CHANGE);
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.regist_sip,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        sipStatus =  findViewById(R.id.status);
        mUserNameET =  findViewById(R.id.user_name);
        mRegisterNameET =  findViewById(R.id.register_name);
        mPasswordET = findViewById(R.id.password);
        mSipServerET = findViewById(R.id.sip_server);
        mPortET = findViewById(R.id.port);
        mOutboundServer = findViewById(R.id.outboundServer);
        mOutboundServerPort = findViewById(R.id.outboundServerPort);
        mProtocolSP = findViewById(R.id.protocol);
        mBfcpSP = findViewById(R.id.bfcp);
        mOutboundSwitch = findViewById(R.id.outboundServer_switch);

        mHandler = new Handler(this);
        YealinkApi.instance().addRegistListener(mRegistListener);
//        updateUI(YealinkApi.instance().getSipProfile());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        YealinkApi.instance().removeRegistListener(mRegistListener);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.login) {
            login();
            return true;
        } else if(id == R.id.loginOut){
            loginOut();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateUI(SipProfile sp) {
        if(!TextUtils.isEmpty(sp.userName)){
            mUserNameET.setText(sp.userName);
        }

        if(!TextUtils.isEmpty(sp.registerName)){
            mRegisterNameET.setText(sp.registerName);
        }

        if(!TextUtils.isEmpty(sp.password)){
            mPasswordET.setText(sp.password);
        }

        if(!TextUtils.isEmpty(sp.server)){
            mSipServerET.setText(sp.server);
        }

        if(!TextUtils.isEmpty(sp.outboundServer)){
            mOutboundServer.setText(sp.outboundServer);
        }

        if(sp.outboundPort != 0){
            mOutboundServerPort.setText(String.valueOf(sp.outboundPort));
        }


        if(sp.port>0){
            mPortET.setText(String.valueOf(sp.port));
        }

        mOutboundSwitch.setChecked(sp.isEnableOutbound);

        setBfcp(sp.isBFCPEnabled);
        setProtocol(sp.transPort);

        setStatus(sp);
    }

    private void login(){
        SipProfile sp = YealinkApi.instance().getSipProfile();
        try{
            sp.isEnabled = true;
            sp.userName = mUserNameET.getText().toString();
            sp.registerName = mRegisterNameET.getText().toString();
            sp.password = mPasswordET.getText().toString();
            sp.server = mSipServerET.getText().toString();
            sp.port = Integer.valueOf(mPortET.getText().toString());
            sp.isEnableOutbound = mOutboundSwitch.isChecked();
            sp.outboundServer = mOutboundServer.getText().toString();
            sp.outboundPort = Integer.valueOf(mOutboundServerPort.getText().toString());

            sp.isBFCPEnabled = getBfcp();
            sp.transPort = getProtocol();
        }catch (Exception e){
            ToastUtil.toast(getActivity(),"端口必须为数字!");
        }

        YealinkApi.instance().registerSip(sp);
    }

    private void loginOut(){
        SipProfile sp = YealinkApi.instance().getSipProfile();
        sp.registerName = "";
        sp.userName = "";
        sp.password = "";
        YealinkApi.instance().registerSip(sp);
    }

    private void setStatus(SipProfile sp){
        Log.i(TAG, "state:" + sp.state);
        if (sp.state == AccountConstant.STATE_UNKNOWN) {
            sipStatus.setText("未知");
        } else if (sp.state == AccountConstant.STATE_DISABLED) {
            sipStatus.setText("禁用");
        } else if (sp.state == AccountConstant.STATE_REGING) {
            sipStatus.setText("正在注册...");
        } else if (sp.state == AccountConstant.STATE_REGED) {
            sipStatus.setText("已注册");
        } else if (sp.state == AccountConstant.STATE_REG_FAILED) {
            sipStatus.setText("注册失败");
        } else if (sp.state == AccountConstant.STATE_UNREGING) {
            sipStatus.setText("正在注销...");
        } else if (sp.state == AccountConstant.STATE_UNREGED) {
            sipStatus.setText("已注销");
        } else if (sp.state == AccountConstant.STATE_REG_ON_BOOT) {
            sipStatus.setText("启动时注册");
        }
    }

    private void setBfcp(boolean enable) {
        if (enable) {
            mBfcpSP.setSelection(0);
        } else {
            mBfcpSP.setSelection(1);
        }
    }

    private boolean getBfcp() {
        String[] data = getResources().getStringArray(R.array.sip_bfcp);
        String selectedStr = data[mBfcpSP.getSelectedItemPosition()];
        if(selectedStr.equals("启用")){
        	return true;
        } else if(selectedStr.equals("禁用")){
        	return false;
        }
        return false;
    }

    private void setProtocol(int protocol) {
        switch (protocol) {
            case SipProfile.TRANSPORT_UDP:
                mProtocolSP.setSelection(0);
                break;
            case SipProfile.TRANSPORT_TCP:
                mProtocolSP.setSelection(1);
                break;
            case SipProfile.TRANSPORT_TLS:
                mProtocolSP.setSelection(2);
                break;
            case SipProfile.TRANSPORT_DNS_NAPTR:
                mProtocolSP.setSelection(3);
                break;
            default:
                break;
        }
    }

    private int getProtocol() {
        String[] data = getResources().getStringArray(R.array.sip_protocol);
        String selectedStr = data[mProtocolSP.getSelectedItemPosition()];
        if(selectedStr.equals("UDP")){
        	 return SipProfile.TRANSPORT_UDP;
        } else if(selectedStr.equals("TCP")){
        	return SipProfile.TRANSPORT_TCP;
        } else if(selectedStr.equals("TLS")){
        	return SipProfile.TRANSPORT_TLS;
        } else if(selectedStr.equals("DNS-NAPTR")){
        	return SipProfile.TRANSPORT_DNS_NAPTR;
        } 
        return SipProfile.TRANSPORT_UDP;
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch(msg.what){
            case MSG_ACCOUNT_CHANGE:
                setStatus(YealinkApi.instance().getSipProfile());
                break;
        }
        return false;
    }
}
