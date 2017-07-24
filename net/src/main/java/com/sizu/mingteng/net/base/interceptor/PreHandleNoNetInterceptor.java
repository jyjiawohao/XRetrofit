package com.sizu.mingteng.net.base.interceptor;

import com.sizu.mingteng.net.base.NetUtils;
import com.sizu.mingteng.net.base.NetworkConfig;
import com.sizu.mingteng.net.base.ResultException;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * 介绍：无网络 异常 处理拦截器
 * 时间： 2017/2/22.
 */
public class PreHandleNoNetInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        if (NetUtils.isNetwork()) {
            return chain.proceed(chain.request());
        } else {
            throw new ResultException(NetworkConfig.ERROR_CODE_NO_NET, "当前网络不通，请确认网络后重试");
        }
    }
}
