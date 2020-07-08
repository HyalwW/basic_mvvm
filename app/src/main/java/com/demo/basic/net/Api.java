package com.demo.basic.net;

import com.google.gson.JsonObject;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Url;

/**
 * Created by Wang.Wenhui
 * Date: 2020/7/8
 * Description: blablabla
 */
public interface Api {

    @GET()
    Observable<JsonObject> getJson(@Url String url);

    @POST("{path}")
    Observable<JsonObject> postJson(@Path(value = "path", encoded = true) String path,
                                    @HeaderMap Map<String, String> headers, @Body RequestBody requestBody);
}
