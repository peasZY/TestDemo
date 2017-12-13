package com.lens.ylink.talk;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lens.ylink.BaseFragment;
import com.lens.ylink.R;
import com.yealink.callscreen.ExternalInterface;
import com.yealink.callscreen.function.OnInviteListener;
import com.yealink.common.data.Contact;
import com.yealink.sdk.YealinkApi;

import java.util.ArrayList;

/**
 * Created by chenhn on 2017/10/19.
 */

public class MeetNowFragment extends BaseFragment implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.talk_meetnow,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        View makeCallBtn = findViewById(R.id.btn_makecall);
        makeCallBtn.setOnClickListener(this);
        YealinkApi.instance().setExtInterface(new ExternalInterface() {

            @Override
            public void inviteMember(FragmentManager manager, OnInviteListener listener, String[] members) {

                MeetInviteFragment fragment = new MeetInviteFragment();
                fragment.show(manager);

            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_makecall:
                YealinkApi.instance().meetNow(getActivity(),new ArrayList<Contact>());
                break;
        }
    }

}
