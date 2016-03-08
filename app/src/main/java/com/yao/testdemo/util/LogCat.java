package com.yao.testdemo.util;

import android.util.Log;

/**
 * Created by Yao on 2016/1/25 0025.
 * @author Yao
 */
public final class LogCat {

    public static final boolean isDebug = true;

    public static final void i(String tag, String msg){
        if(isDebug) Log.i(tag, msg);
    }

    public static final void d(String tag, String msg){
        if(isDebug) Log.d(tag, msg);
    }

    public static final void e(String tag, String msg){
        if(isDebug) Log.e(tag, msg);
    }
}
