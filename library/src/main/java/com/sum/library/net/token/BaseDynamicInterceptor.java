package com.sum.library.net.token;

import android.util.Log;

import com.sum.library.BuildConfig;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * Created by sdl on 2017/12/28.
 * 统一参数添加
 */

public abstract class BaseDynamicInterceptor implements Interceptor {

    public BaseDynamicInterceptor() {

    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (request.method().equals("GET")) {
            request = this.addGetParamsSign(request);
        } else if (request.method().equals("POST")) {
            request = this.addPostParamsSign(request);
        }
        if (BuildConfig.DEBUG) {
            Log.d("net",
                    "req->method:" + request.method() +
                            " head:" + request.headers().toString() +
                            " url:" + request.url().toString());
        }
        Response response = chain.proceed(request);
        if (BuildConfig.DEBUG) {
            Log.d("net", "rsp->" + getBodyString(response));
        }
        return response;
    }


    private String getBodyString(Response response) throws IOException {
        ResponseBody responseBody = response.body();
        BufferedSource source = responseBody.source();
        if (source == null) {
            return null;
        }
        source.request(Long.MAX_VALUE);
        Buffer buffer = source.buffer();
        Charset charset = Charset.forName("UTF-8");
        MediaType contentType = responseBody.contentType();
        if (contentType != null) {
            contentType.charset(charset);
        }
        return buffer.clone().readString(charset);
    }

    private Request addGetParamsSign(Request request) throws UnsupportedEncodingException {
        HttpUrl httpUrl = request.url();
        HttpUrl.Builder urlBuilder = httpUrl.newBuilder();
        Set<String> nameSet = httpUrl.queryParameterNames();
        //GET查询参数
        ArrayList<String> nameList = new ArrayList<>();
        nameList.addAll(nameSet);

        TreeMap<String, String> oldParams = new TreeMap<>();
        for (int i = 0; i < nameList.size(); ++i) {
            String name = nameList.get(i);
            //同个key对应多个value
            String value = httpUrl.queryParameterValues(name) != null
                    &&
                    httpUrl.queryParameterValues(name).size() > 0 ?
                    httpUrl.queryParameterValues(name).get(0) : "";
            oldParams.put(name, value);
        }
        String nameKeys = Arrays.asList(new ArrayList[]{nameList}).toString();
        addPubParams(oldParams);
        //过滤重复数据
        for (Map.Entry<String, String> entry : oldParams.entrySet()) {
            if (entry.getKey() == null || entry.getValue() == null) {
                continue;
            }
            //String urlValue = URLEncoder.encode((String) entry.getValue(), "UTF-8");
            if (!nameKeys.contains(entry.getKey())) {
                urlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
            }
        }
        httpUrl = urlBuilder.build();

        Request.Builder builder = request.newBuilder();
        HashMap<String, String> headers = addPubHeaders();
        if (headers != null && !headers.isEmpty()) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        request = builder.url(httpUrl).build();
        return request;
    }

    private Request addPostParamsSign(Request request) throws UnsupportedEncodingException {
        if (request.body() instanceof FormBody) {
            //完全新创建一个查询参数体
            okhttp3.FormBody.Builder bodyBuilder = new okhttp3.FormBody.Builder();
            FormBody formBody = (FormBody) request.body();
            TreeMap<String, String> oldParams = new TreeMap<>();
            if (formBody != null) {
                for (int i = 0; i < formBody.size(); ++i) {
                    oldParams.put(formBody.encodedName(i), formBody.encodedValue(i));
                }
            }
            this.addPubParams(oldParams);

            for (Map.Entry<String, String> entry : oldParams.entrySet()) {
                if (entry.getValue() == null) {
                    continue;
                }
                bodyBuilder.addEncoded(entry.getKey(), URLDecoder.decode(entry.getValue(), "UTF-8"));
            }
            formBody = bodyBuilder.build();

            Request.Builder builder = request.newBuilder();
            HashMap<String, String> headers = addPubHeaders();
            if (headers != null && !headers.isEmpty()) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    builder.addHeader(entry.getKey(), entry.getValue());
                }
            }
            request = builder.post(formBody).build();
        } else if (request.body() instanceof MultipartBody) {
            MultipartBody multipartBody = (MultipartBody) request.body();
            okhttp3.MultipartBody.Builder bodyBuilder = new okhttp3.MultipartBody.Builder();
            List<MultipartBody.Part> oldparts = multipartBody.parts();
            List<MultipartBody.Part> newparts = new ArrayList<>();
            newparts.addAll(oldparts);

            TreeMap<String, String> newParams = new TreeMap<>();
            this.addPubParams(newParams);

            for (Map.Entry<String, String> entry : newParams.entrySet()) {
                MultipartBody.Part part = MultipartBody.Part.createFormData(entry.getKey(), entry.getValue());
                newparts.add(part);
            }

            for (MultipartBody.Part part : newparts) {
                bodyBuilder.addPart(part);
            }

            multipartBody = bodyBuilder.build();
            request = request.newBuilder().post(multipartBody).build();
        }
        return request;
    }

    /**
     * 添加统一请求参数
     */
    public abstract void addPubParams(TreeMap<String, String> params);

    /**
     * 添加统一head参数
     */
    public abstract HashMap<String, String> addPubHeaders();
}