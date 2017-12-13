package com.lens.ylink;

import android.app.Application;

import com.yealink.sdk.YealinkApi;

/**
 * Created by chenhn on 2017/10/18.
 */

public class SampleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        YealinkApi.instance().init(this);
    }
}
