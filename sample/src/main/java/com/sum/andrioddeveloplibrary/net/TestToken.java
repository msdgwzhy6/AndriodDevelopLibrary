package com.sum.andrioddeveloplibrary.net;

import com.sum.library.net.token.BaseDynamicInterceptor;

import java.util.HashMap;
import java.util.TreeMap;

/**
 * Created by sdl on 2017/12/29.
 */

public class TestToken extends BaseDynamicInterceptor {

    @Override
    protected boolean needLog() {
        return true;
    }

    private HashMap<String, String> mHeaders;

    @Override
    public void addPubParams(TreeMap<String, String> old) {
        old.put("token", "000000007debdb10ffffffff8dcff671_jjr");
        old.put("v", "4.0.0");
        old.put("api_key", "android");
        old.put("ver", "v1");
        old.put("city", "nj");
    }

    @Override
    public HashMap<String, String> addPubHeaders() {
        if (mHeaders == null) {
            mHeaders = new HashMap<>();
            mHeaders.put("head_1", "111");
            mHeaders.put("head_2", "222");
        }
        return mHeaders;
    }
}
