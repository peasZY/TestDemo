package com.lens.ylink.util;

import android.content.Context;
import android.widget.Toast;


/**
 * Created by LY305512 on 2017/12/11.
 */

public class ToastUtlis {
    public static void show(Context context,String string){
        Toast.makeText(context, string,Toast.LENGTH_SHORT).show();
    }
}
