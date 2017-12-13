package com.lens.ylink.regist;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.lens.ylink.BaseFragment;
import com.lens.ylink.R;
import com.yealink.base.util.ToastUtil;
import com.yealink.common.SettingsManager;
import com.yealink.common.data.AccountConstant;
import com.yealink.common.data.H323Profile;
import com.yealink.sdk.RegistListener;
import com.yealink.sdk.YealinkApi;

/**
 *
 * Created by chenhn on 2017/10/19.
 */

public class H323RegistFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener,Handler.Callback {

    private TextView mTvH323Status;
    private EditText mEtH323Name;
    private EditText mEtH323Number;
    private EditText mEtH323Gka;
    private EditText mEtGkUserName;
    private EditText mEtGkPassword;

    private View mH323Layout;
    private View mGkUserNameLayout;
    private View mGkPasswordLayout;

    private SwitchCompat mH323Switch;
    private SwitchCompat mGkSwitch;
    private SwitchCompat mH235Switch;
    private SwitchCompat mH460Switch;

    private static int STATUS_CHANGE = 1;

    private Handler mHandler;
    private H323Profile mH323Profile;
    private Context mContext;
    private Resources mResource;

    private RegistListener mRegistListener = new RegistListener() {
        @Override
        public void onH323Regist(int status) {
            mHandler.sendEmptyMessage(STATUS_CHANGE);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        mResource = mContext.getResources();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.regist_h323,container,false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        mH323Layout = findViewById(R.id.llyt_323_account_content);

        mTvH323Status =  findViewById(R.id.h323_status);

        mEtH323Name =  findViewById(R.id.h323_name);
        mEtH323Number =  findViewById(R.id.h323_extension);
        mEtH323Gka =  findViewById(R.id.h323_gka);
        mEtGkUserName =  findViewById(R.id.gkuser_name);
        mEtGkPassword =  findViewById(R.id.gk_password);

        mH323Switch =  findViewById(R.id.h323_switch);
        mGkSwitch =  findViewById(R.id.gk_switch);
        mH460Switch =  findViewById(R.id.h460_switch);
        mH235Switch = findViewById(R.id.h235_switch);

        mGkUserNameLayout = findViewById(R.id.llyt_gk_username);
        mGkPasswordLayout = findViewById(R.id.llyt_gk_password);

        //恢复最后一次设置
        mH323Profile = YealinkApi.instance().getH323Profile();
        setDefaultConfig(mH323Profile);

        mH323Switch.setOnCheckedChangeListener(this);
        mGkSwitch.setOnCheckedChangeListener(this);
        mH460Switch.setOnCheckedChangeListener(this);
        mH235Switch.setOnCheckedChangeListener(this);

        mHandler = new Handler(this);
        YealinkApi.instance().addRegistListener(mRegistListener);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_sp,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save:
                saveConfig();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setDefaultConfig(H323Profile hp) {
        if (hp != null) {
            mH323Switch.setChecked(hp.isEnabled);
            if (hp.isEnabled) {
                mH323Layout.setVisibility(View.VISIBLE);
            } else {
                mH323Layout.setVisibility(View.GONE);
            }
            mGkSwitch.setChecked(hp.gatekeeperVerify);
            if (hp.gatekeeperVerify) {
                mGkUserNameLayout.setVisibility(View.VISIBLE);
                mGkPasswordLayout.setVisibility(View.VISIBLE);
            } else {
                mGkUserNameLayout.setVisibility(View.GONE);
                mGkPasswordLayout.setVisibility(View.GONE);
            }
            mH235Switch.setChecked(hp.isH235EncryptEnabled);
            mH460Switch.setChecked(hp.isH460Active);
            getStatus();
            getRegisterStatus(hp);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.h323_switch:
                if (isChecked) {
                    mH323Layout.setVisibility(View.VISIBLE);
                } else {
                    mH323Layout.setVisibility(View.GONE);
                }
                break;
            case R.id.gk_switch:
                if (isChecked) {
                    mGkUserNameLayout.setVisibility(View.VISIBLE);
                    mGkPasswordLayout.setVisibility(View.VISIBLE);
                } else {
                    mGkUserNameLayout.setVisibility(View.GONE);
                    mGkPasswordLayout.setVisibility(View.GONE);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    /**
     * 更新H323的其他配置信息
     */
    private void getStatus() {
        mEtGkUserName.setText(mH323Profile.gatekeeperUsername);
        mEtGkPassword.setText(mH323Profile.gatekeeperPassword);
        mEtH323Name.setText(mH323Profile.h323Name);
        mEtH323Number.setText(mH323Profile.extension);
        mEtH323Gka.setText(mH323Profile.gatekeeperServer1);
    }


    /**
     * 更新H323账号的状态
     * @param hp
     */
    private void getRegisterStatus(H323Profile hp) {
        if(!hp.isEnabled){
            mTvH323Status.setText(R.string.state_disabled);
        }else{
            if (hp.state == AccountConstant.STATE_UNKNOWN) {
                mTvH323Status.setText(mResource.getString(R.string.state, mResource.getString(R.string.state_unknown)));
            } else if (hp.state == AccountConstant.STATE_DISABLED) {
                mTvH323Status.setText(mResource.getString(R.string.state, mResource.getString(R.string.state_disabled)));
            } else if (hp.state == AccountConstant.STATE_REGING) {
                mTvH323Status.setText(mResource.getString(R.string.state, mResource.getString(R.string.state_reging)));
            } else if (hp.state == AccountConstant.STATE_REGED) {
                mTvH323Status.setText(mResource.getString(R.string.state, mResource.getString(R.string.state_reged)));
            } else if (hp.state == AccountConstant.STATE_REG_FAILED) {
                if(TextUtils.isEmpty(hp.gatekeeperServer1)){
                    mTvH323Status.setText(mResource.getString(R.string.state, mResource.getString(R.string.state_unreged)));
                }else {
                    mTvH323Status.setText(mResource.getString(R.string.state, mResource.getString(R.string.state_reg_failed)));
                }
            } else if (hp.state == AccountConstant.STATE_UNREGING) {
                mTvH323Status.setText(mResource.getString(R.string.state, mResource.getString(R.string.state_unreging)));
            } else if (hp.state == AccountConstant.STATE_UNREGED) {
                mTvH323Status.setText(mResource.getString(R.string.state, mResource.getString(R.string.state_unreged)));
            } else if (hp.state == AccountConstant.STATE_REG_ON_BOOT) {
                mTvH323Status.setText(mResource.getString(R.string.state, mResource.getString(R.string.state_reg_on_boot)));
            }
        }
    }

    public boolean saveConfig() {
        H323Profile mH323Profile = new H323Profile();

        String name = mEtH323Name.getText().toString();
        mH323Profile.h323Name = name;

        String number = mEtH323Number.getText().toString();
        mH323Profile.extension = number;

        mH323Profile.gatekeeperServer1 = mEtH323Gka.getText().toString();
        mH323Profile.isEnabled = mH323Switch.isChecked();
        mH323Profile.gatekeeperVerify = mGkSwitch.isChecked();
        mH323Profile.gatekeeperUsername = mEtGkUserName.getText().toString();
        mH323Profile.gatekeeperPassword = mEtGkPassword.getText().toString();

        mH323Profile.isH460Active = mH460Switch.isChecked();
        mH323Profile.isH235EncryptEnabled = mH235Switch.isChecked();

        if (SettingsManager.getInstance().setH323Profile(mH323Profile)) {
            this.mH323Profile = mH323Profile;
            ToastUtil.toast(mContext, R.string.sip_illegal_hint);
            ToastUtil.toast(mContext, R.string.save_success);
        } else {
            ToastUtil.toast(mContext, R.string.save_fail);
            return false;
        }

        return true;
    }

    @Override
    public boolean handleMessage(Message msg) {
        if (msg.what == STATUS_CHANGE) {
            H323Profile hp = SettingsManager.getInstance().getH323Profile();
            if (hp != null) {
                getRegisterStatus(hp);
            }
        }
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        YealinkApi.instance().removeRegistListener(mRegistListener);
    }
}
