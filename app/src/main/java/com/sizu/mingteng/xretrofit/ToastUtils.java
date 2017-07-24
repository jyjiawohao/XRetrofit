package com.sizu.mingteng.xretrofit;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by lenovo on 2017/7/24.
 */

public class ToastUtils {
    private static Toast sToast;

    /**
     * 强大的吐司，能够连续弹的吐司
     *
     * @param text
     */
    public static void showToast(String text) {
        if (sToast == null) {
            sToast = Toast.makeText(App.context, text, Toast.LENGTH_SHORT);
        } else {
            sToast.setText(text);//如果不为空，则直接改变当前toast的文本
        }
        sToast.show();
    }
    public static void showToast(Context context, String msg){
        if (sToast==null){
            sToast = Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_SHORT);
        }
        //如果这个Toast已经在显示了，那么这里会立即修改文本
        sToast.setText(msg);
        sToast.show();
    }
}
