# XRetrofit
Retrofit+rxjava 的封装网络请求类


**使用**
```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

```
dependencies {
	        compile 'com.github.jyjiawohao:XRetrofit:-SNAPSHOT'
	}
    
    
```

```
//观察者
public interface ApiService {
    @GET("data/{all}/{number}/{pageNumber}")
    Observable<GanIoTypeBean> getAllType(@Path("all") String all, @Path("number") int number, @Path("pageNumber") int pageNumber);
}
```

    
    
    public class RetrofitHelper {
    //region @description API 定义相关
    public static final String BASEURL = "http://api.douban.com/";
    public static final String GanK_IO = "http://gank.io/api/";
    private static final ApiService API_SERVICE = RetrofitManager
            .getInstance(App.context)
            .newRetrofit(BASEURL)
            .create(ApiService.class);
    
    public static ApiService getApiService() {
        return API_SERVICE;
    }
    
    private static final ApiService API_GanK_IO = RetrofitManager
            .getInstance(App.context)
            .newRetrofit(GanK_IO)
            .create(ApiService.class);
    //获取
    public static ApiService getGanKApiService() {
        return API_GanK_IO;
    }
    }



    //网络请求
      Observable<GanIoTypeBean> movieDetail = RetrofitHelper.getGanKApiService().getAllType("Android", 20, 1);
                    movieDetail.subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<GanIoTypeBean>() {
                                @Override
                                public void onSubscribe(@NonNull Disposable d) {
                                }
    
                                @Override
                                public void onNext(@NonNull GanIoTypeBean response) {
                                    mTextView.setText(response.getResults().toString());
                                }
    
                                @Override
                                public void onError(@NonNull Throwable e) {
                                    //失败的时候回调-----一下可以忽略 直接 callBack.onFaild("请求失败");
                                    if (e instanceof HttpException) {
                                        HttpException httpException = (HttpException) e;
                                        //httpException.response().errorBody().string()
                                        int code = httpException.code();
                                        if (code == 500 || code == 404) {
                                            ToastUtils.showToast("服务器出错");
                                        }
                                    } else if (e instanceof ConnectException) {
                                        ToastUtils.showToast("网络中断，请检查您的网络状态");
                                    } else if (e instanceof SocketTimeoutException) {
                                        ToastUtils.showToast("网络连接超时");
                                    } else {
                                        // callBack.onFaild("发生未知错误" + e.getMessage());
                                        ToastUtils.showToast("error:" + e.getMessage());
                                    }
                                }
    
                                @Override
                                public void onComplete() {
                                }
                            });
