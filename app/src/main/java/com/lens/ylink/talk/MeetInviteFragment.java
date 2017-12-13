package com.lens.ylink.talk;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.lens.ylink.BaseFragment;
import com.lens.ylink.R;
import com.yealink.sdk.YealinkApi;

/**
 * Created by chenhn on 2017/10/26.
 */

public class MeetInviteFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = "MeetInviteFragment";
    private EditText mNumber;
    private Button mInvit;
    private Button mCancel;
    private View mContent;

    public void show(FragmentManager manager) {
        if(isAdded()){
            return;
        }
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, TAG);
        ft.addToBackStack(null);
        ft.commitAllowingStateLoss();
    }

    public void dismiss() {
        if(isAdded()){
            getFragmentManager().popBackStack();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.remove(this);
            ft.commitAllowingStateLoss();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.talk_meet_invite,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNumber = findViewById(R.id.number);
        mNumber.setOnClickListener(this);
        mInvit = findViewById(R.id.invite);
        mInvit.setOnClickListener(this);
        mCancel = findViewById(R.id.cancel);
        mCancel.setOnClickListener(this);
        mContent = findViewById(R.id.content);
        mContent.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.invite:
                invite();
                break;
            case R.id.cancel:
                dismiss();
                break;
            case R.id.content:
                dismiss();
                break;
        }
    }

    private void invite(){
        String number = mNumber.getText().toString();
        YealinkApi.instance().meetInvite(new String[]{number});
        dismiss();
    }
}
