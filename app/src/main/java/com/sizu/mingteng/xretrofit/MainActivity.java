package com.sizu.mingteng.xretrofit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

public class MainActivity extends AppCompatActivity {

    private Button mBt;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBt = (Button) findViewById(R.id.bt01);
        mTextView = (TextView) findViewById(R.id.tv);
        mBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });
    }
}
