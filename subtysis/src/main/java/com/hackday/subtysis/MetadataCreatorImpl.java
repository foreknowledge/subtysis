package com.hackday.subtysis;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hackday.subtysis.model.Keyword;
import com.hackday.subtysis.model.SearchType;
import com.hackday.subtysis.model.response.ResponseData;
import com.mungziapp.testlib.model.items.BaseItem;
import com.mungziapp.testlib.model.items.EncyclopediaItem;
import com.mungziapp.testlib.model.items.ShoppingItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MetadataCreatorImpl implements MetadataCreator {
    private final String TAG = "MetadataCreatorImpl";

    private ArrayList<Keyword> mKeywords;
    private SearchType mType;
    private HashMap<String, ResponseData> mResponseMap = new HashMap<>();
    private SetResponseListener mListener;

    private int requestCnt = 0;
    private int responseCnt = 0;

    @Override
    public void fillMetadata(ArrayList<Keyword> keywords, SearchType type, SetResponseListener listener) {
        mKeywords = keywords;
        mType = type;
        mListener = listener;

        String requestUrl = getRequestURL();

        for (Keyword keyword : mKeywords) {
            String word = keyword.getWord();

            if (mResponseMap.containsKey(word)) {
                keyword.setResponseData(mResponseMap.get(word));
            }
            else {
                String url = requestUrl + "?query=" + word;
                sendRequest(url, word);
                ++requestCnt;
            }
        }
    }

    private String getRequestURL() {
        String url = "https://openapi.naver.com/v1/search";

        switch (mType) {
            case BLOG: url += "/blog.json"; break;
            case NEWS: url += "/news.json"; break;
            case BOOK: url += "/book.json"; break;
            case ADULT: url += "/adult.json"; break;
            case ENCYCLOPEDIA: url += "/encyc.json"; break;
            case MOVIE: url += "/movie.json"; break;
            case CAFEARTICLE: url += "/cafearticle.json"; break;
            case KIN: url += "/kin.json"; break;
            case LOCAL: url += "/local.json"; break;
            case ERRATA: url += "/errata.json"; break;
            case WEB: url += "/webkr.json"; break;
            case IMAGE: url += "/image.json"; break;
            case SHOPPING: url += "/shop.json"; break;
            case DOC: url += "/doc.json"; break;
            default: Log.e(TAG, "type error!"); return null;
        }

        return url;
    }

    private void sendRequest(String url, final String word) {
        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
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
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("X-Naver-Client-ID", "cUAhSVDT7pqIUvvw6QEP");
                headers.put("X-Naver-Client-Secret", "0e8_sZCH1w");
                return headers;
            }
        };

        request.setShouldCache(false);
        RequestManager.getInstance().add(request);
    }


    private void processResponse(String word, String response) {
        ResponseData responseData = getResponseData(response);
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

    private ResponseData getResponseData(String response) {
        Gson gson = new Gson();
        ResponseData responseData = gson.fromJson(response, ResponseData.class);

        try {
            Type listType;

            if (mType == SearchType.ENCYCLOPEDIA) listType = new TypeToken<ArrayList<EncyclopediaItem>>(){}.getType();
            else if (mType == SearchType.SHOPPING) listType = new TypeToken<ArrayList<ShoppingItem>>(){}.getType();
            else {
                Log.e(TAG, "type error!");
                return null;
            }

            ArrayList<BaseItem> baseItems = gson.fromJson(new JSONObject(response).getJSONArray("items").toString(), listType);
            responseData.setItems(baseItems);
        }
        catch (JSONException e) {
            Log.e(TAG, "JSONException error message: " + e.getMessage());
        }

        return responseData;
    }
}
