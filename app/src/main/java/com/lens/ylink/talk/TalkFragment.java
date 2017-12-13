package com.lens.ylink.talk;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.lens.ylink.BaseFragment;
import com.lens.ylink.R;
import com.yealink.base.util.ToastUtil;
import com.yealink.sdk.YealinkApi;

/**
 * 通话功能
 * Created by chenhn on 2017/10/19.
 */

public class TalkFragment extends BaseFragment implements View.OnClickListener {

    private EditText mNumber;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.talk_makecall,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNumber = findViewById(R.id.number);
        View makeCallBtn = findViewById(R.id.btn_makecall);
        makeCallBtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_makecall:
                String number = mNumber.getText().toString();
                if(!TextUtils.isEmpty(number)){
                    YealinkApi.instance().call(getActivity(),number);
                } else {
                    ToastUtil.toast(getActivity(),"号码为空！");
                }
                break;
        }
    }
}
