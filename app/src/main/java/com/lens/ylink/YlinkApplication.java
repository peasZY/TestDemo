package com.lens.ylink;

import android.app.Application;

import com.yealink.sdk.YealinkApi;

/**
 * Created by LY305512 on 2017/12/11.
 */

public class YlinkApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        YealinkApi.instance().init(this);
    }
}
