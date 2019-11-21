package com.hackday.subtysis;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class RequestManager {
    public static final String mainURL = "https://openapi.naver.com/v1/search";
    private static RequestQueue requestQueue;

    private RequestManager() {}
    public static RequestQueue getInstance() { return requestQueue; }

    public static void createRequestQueue(Context context) {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        }
    }
}
