package com.lens.ylink;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by chenhn on 2017/10/19.
 */

public class BaseFragment extends Fragment {

    private ViewGroup mRootView;
    private View mView;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView = view;
        mRootView = (ViewGroup) getActivity().getWindow().getDecorView().findViewById(android.R.id.content);
        mRootView.addView(mView);
    }

    @Override
    public void onDestroyView() {
        if(mRootView != null && mView !=null) {
            mRootView.removeView(mView);
        }
        super.onDestroyView();
    }

    /**
     * 通过viewId获取控件
     *
     * @param viewId
     * @return
     */
    public <T extends View> T findViewById(int viewId) {
        View view = mView.findViewById(viewId);
        return (T) view;
    }
}
