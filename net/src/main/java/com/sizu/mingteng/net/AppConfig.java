package com.sizu.mingteng.net;

import android.content.Context;

import com.sizu.mingteng.net.utils.SPUtils;

/**
 * Created by lenovo on 2017/6/12.
 */

public class AppConfig {
    private static String KEY_IS_SEE = "key_is_see";
    private static String KEY_IS_Cache = "key_is_cache";
    /**
     * 设置是否缓存数据
     *
     * @param context
     * @param isSee
     */
    public static void setIsCache(Context context, boolean isSee) {
        SPUtils.put(context, KEY_IS_Cache, isSee);
    }

    /**
     * 获取是否缓存数据
     *
     * @param context
     * @return
     */
    public static boolean getIsCache(Context context) {
        return (boolean) SPUtils.get(context, KEY_IS_Cache, true);
    }

}
