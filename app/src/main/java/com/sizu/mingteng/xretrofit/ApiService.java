package com.sizu.mingteng.xretrofit;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by lenovo on 2017/7/24.
 */

public interface ApiService {
    @GET("data/{all}/{number}/{pageNumber}")
    Observable<GanIoTypeBean> getAllType(@Path("all") String all, @Path("number") int number, @Path("pageNumber") int pageNumber);

}
