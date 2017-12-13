package com.lens.ylink.talk;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lens.ylink.BaseFragment;
import com.lens.ylink.R;
import com.yealink.callscreen.ExternalInterface;
import com.yealink.sdk.YealinkApi;

/**
 * Created by chenhn on 2017/10/19.
 */

public class IncomingFragment extends BaseFragment {

    private TextView mNotice;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //监听来电事件 ， 如果想在任意界面都可以收到来电，可以将事件放于Service中处理。
        YealinkApi.instance().setExtInterface(new ExternalInterface() {

            public void onTalkEvent(int type, Bundle data) {
                switch (type){
                    case ExternalInterface.EVENT_INCOMING:
                        YealinkApi.instance().incoming(getActivity());
                        break;
                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.talk_incoming,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNotice = findViewById(R.id.notice);
    }


}
