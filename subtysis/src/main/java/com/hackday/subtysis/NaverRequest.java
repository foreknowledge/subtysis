package com.hackday.subtysis;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class NaverRequest extends StringRequest {

    public NaverRequest(String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(url, listener, errorListener);
    }

    @Override
    public Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("X-Naver-Client-ID", BuildConfig.CLIENT_ID);
        headers.put("X-Naver-Client-Secret", BuildConfig.CLIENT_SECRET);
        return headers;
    }
}
