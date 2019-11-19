package com.hackday.subtysis;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.hackday.subtysis.model.Keyword;
import com.hackday.subtysis.model.response.ResponseData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MetadataCreatorImpl implements MetadataCreator {
    private ArrayList<Keyword> mKeywords;
    private HashMap<String, ResponseData> mResponseMap = new HashMap<>();
    private SetResponseListener mListener;
    private int requestCnt = 0;
    private int responseCnt = 0;

    @Override
    public void fillMetadata(ArrayList<Keyword> keywords, SetResponseListener listener) {
        mKeywords = keywords;
        mListener = listener;

        for (Keyword keyword : mKeywords) {
            String word = keyword.getWord();

            if (mResponseMap.containsKey(word)) {
                ResponseData data = keyword.getResponseData();
                data = mResponseMap.get(word);
            }
            else {
                sendRequest(word);
                ++requestCnt;
            }
        }
    }

    private void sendRequest(final String word) {
        StringRequest request = new StringRequest(
                Request.Method.GET,
                "https://openapi.naver.com/v1/search/shop.json?query=" + word,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        processResponse(word, response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Log", error.toString());
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("X-Naver-Client-ID", "cUAhSVDT7pqIUvvw6QEP");
                headers.put("X-Naver-Client-Secret", "0e8_sZCH1w");
                return headers;
            }
        };

        request.setShouldCache(false);
        RequestManager.requestQueue.add(request);
    }


    private void processResponse(String word, String response) {
        Gson gson = new Gson();
        ResponseData responseData = gson.fromJson(response, ResponseData.class);

        mResponseMap.put(word, responseData);

        for (Keyword keyword : mKeywords) {
            if (keyword.getWord().equals(word)) {
                keyword.setResponseData(mResponseMap.get(word));
                break;
            }
        }

        ++responseCnt;

        if (requestCnt == responseCnt) {
            mListener.onResponse(mKeywords);
        }
    }
}
