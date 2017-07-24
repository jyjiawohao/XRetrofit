package com.sizu.mingteng.net;

import android.content.Context;
import android.util.Log;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.sizu.mingteng.net.utils.AppUtil;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 介绍：通过这个类 获取 Retrofit的实例。
 * 使用者 通过这个Retrofit实例，创建Api Service
 * 时间：
 */
public class RetrofitManager {
    private static Context mContext;

    private OkHttpClient mOkHttpClient;

    private static RetrofitManager INSTACE;

    public static RetrofitManager getInstance(Context context) {
        if (INSTACE == null) {
            synchronized (RetrofitManager.class) {
                if (INSTACE == null) {
                    //将传入的context转换成Application的context
                    INSTACE = new RetrofitManager(context.getApplicationContext());
                }
            }
        }
        return INSTACE;
    }

    public Retrofit newRetrofit(String url) {
        // 拿到Retrofit实例
        return new Retrofit.Builder()
                .baseUrl(url)
                //引入Gson解析库 ，就可以直接以实体的形式拿到返回值
                .addConverterFactory(GsonConverterFactory.create())
                //加入我们自定义的Gson解析库，就可以更友好的处理错误
                // .addConverterFactory(CstGsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                //将我们客制化的OkHttp实例传入
                .client(mOkHttpClient)
                .build();
    }


    private RetrofitManager(Context context) {
        mContext = context;

        //日志拦截器
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.e("loggin拦截日志: ", message);
            }
        });
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        /**
         * 获取缓存
         */
        Interceptor baseInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                if (!AppUtil.isNetWorkAvailable(mContext)) {
                    /**
                     * 离线缓存控制  总的缓存时间=在线缓存时间+设置离线缓存时间
                     */
                    int maxStale = 60 * 60 * 24 * 28; // 离线时缓存保存4周,单位:秒
                    CacheControl tempCacheControl = new CacheControl.Builder()
                            .onlyIfCached()
                            .maxStale(maxStale, TimeUnit.SECONDS)
                            .build();
                    request = request.newBuilder()
                            .cacheControl(tempCacheControl)
                            .build();
                    Log.i("RetrofitManager", "intercept:no network ");
                }
                return chain.proceed(request);
            }
        };
        //只有 网络拦截器环节 才会写入缓存写入缓存,在有网络的时候 设置缓存时间
        Interceptor rewriteCacheControlInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Response originalResponse = chain.proceed(request);
                int maxAge = 5 * 60; // 在线缓存在1分钟内可读取 单位:秒
                return originalResponse.newBuilder()
                        .removeHeader("Pragma")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                        .removeHeader("Cache-Control")
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .build();
            }
        };
        //设置缓存路径 内置存储
        //File httpCacheDirectory = new File(context.getCacheDir(), "responses");
        //外部存储
        File httpCacheDirectory = new File(mContext.getExternalCacheDir(), "responses");
        //设置缓存 10M
        int cacheSize = 10 * 1024 * 1024;
        Cache cache = new Cache(httpCacheDirectory, cacheSize);

        OkHttpClient client;
        //如果默认为 缓存数据
        if (AppConfig.getIsCache(mContext)) {
            client = new OkHttpClient.Builder()
                    .cache(cache)
                    .addInterceptor(logging)
                    .addInterceptor(baseInterceptor)
                    .addNetworkInterceptor(rewriteCacheControlInterceptor)
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .build();
        } else {
            client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .build();
        }

        mOkHttpClient = client;
    }
}
