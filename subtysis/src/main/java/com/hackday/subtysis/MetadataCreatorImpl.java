package com.hackday.subtysis;

import android.util.Log;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hackday.subtysis.model.Keyword;
import com.hackday.subtysis.model.SearchType;
import com.hackday.subtysis.model.items.BaseItem;
import com.hackday.subtysis.model.items.BlogItem;
import com.hackday.subtysis.model.items.EncyclopediaItem;
import com.hackday.subtysis.model.items.ShoppingItem;
import com.hackday.subtysis.model.response.ResponseData;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.json.JSONException;
import org.json.JSONObject;

public class MetadataCreatorImpl implements MetadataCreator {
    private final String TAG = "MetadataCreatorImpl";
    private final int MAX_REQUEST_CNT = 10;

    private ArrayList<Keyword> mKeywords;
    private ArrayList<SearchType> mTypes;
    private HashMap<String, HashMap<SearchType, ResponseData>> mResponsesMap = new HashMap<>();
    private SetResponseListener mListener;

    private Gson mGson = new Gson();

    private static AtomicInteger requestCnt = new AtomicInteger(0);
    private static AtomicInteger responseCnt = new AtomicInteger(0);

    @Override
    public void fillMetadata(ArrayList<Keyword> keywords, ArrayList<SearchType> types, SetResponseListener listener) {
        mKeywords = keywords;
        mTypes = types;
        mListener = listener;

        for (Keyword keyword : mKeywords) {
            String word = keyword.getWord();

            if (mResponsesMap.containsKey(word)) {
                keyword.setResponses(mResponsesMap.get(word));
            }
            else {
                for (SearchType type: mTypes) {
                    if (requestCnt.get() < MAX_REQUEST_CNT) {
                        requestCnt.incrementAndGet();
                        String url = type.getUrl() + "?query=" + word;
                        sendRequest(url, word);
                    }
                }
            }
        }
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
                        mListener.onFailure(error.toString());
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
        HashMap<SearchType, ResponseData> responses = getResponses(response);
        mResponsesMap.put(word, responses);

        for (Keyword keyword : mKeywords) {
            if (keyword.getWord().equals(word)) {
                keyword.setResponses(mResponsesMap.get(word));
                break;
            }
        }

        responseCnt.incrementAndGet();

        Log.d("Log", "requestCnt = " + requestCnt.get() + ", responseCnt = " + responseCnt.get());
        if (requestCnt.get() == responseCnt.get()) {
            Log.d("Log", ".aljkdf;aljelifjlskdafaskldfjalkfakfjaljflk");
            mListener.onResponse(mKeywords);
        }
    }

    private HashMap<SearchType, ResponseData> getResponses(String response) {
        HashMap<SearchType, ResponseData> results = new HashMap<>();

        for (SearchType type: mTypes) {
            ResponseData responseData = mGson.fromJson(response, ResponseData.class);

            try {
                Type listType;

                if (type == SearchType.BLOG) listType = new TypeToken<ArrayList<BlogItem>>() {}.getType();
                else if (type == SearchType.ENCYCLOPEDIA)
                    listType = new TypeToken<ArrayList<EncyclopediaItem>>() {}.getType();
                else if (type == SearchType.SHOPPING)
                    listType = new TypeToken<ArrayList<ShoppingItem>>() {}.getType();
                else {
                    Log.e(TAG, "type error!");
                    return null;
                }

                ArrayList<BaseItem> baseItems = mGson
                    .fromJson(new JSONObject(response).getJSONArray("items").toString(), listType);
                responseData.setItems(baseItems);
            } catch (JSONException e) {
                Log.e(TAG, "JSONException error message: " + e.getMessage());
            }

            results.put(type, responseData);
        }

        return results;
    }
}
