package com.sizu.mingteng.xretrofit;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import java.lang.ref.WeakReference;

/**
 * Created by lenovo on 2017/5/24.
 */

public class App extends Application {
    public static Context context;
    public static Handler mhandler;

    private static View sHandler;

    public static View getHandler() {
        return sHandler;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        mhandler = new Handler();
    }

    public static Context getContext() {
        return context;
    }

    private MyHandler mHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        private WeakReference<Context> reference;

        public MyHandler(Context context) {
            reference = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity activity = (MainActivity) reference.get();
            if (activity != null) {
                activity.finish();
            }
        }
    }
}
