package com.hackday.subtysis;

import android.util.Log;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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
import java.util.concurrent.atomic.AtomicInteger;
import org.json.JSONException;
import org.json.JSONObject;

public class MetadataCreatorImpl implements MetadataCreator {
    private final static String TAG = "MetadataCreatorImpl";

    private ArrayList<Keyword> keywords;
    private ArrayList<SearchType> types;
    private HashMap<String, HashMap<SearchType, ResponseData>> metadataMap = new HashMap<>();
    private ResponseListener listener;

    private Gson gson = new Gson();

    @Override
    public void fillMetadata(ArrayList<Keyword> keywords, ArrayList<SearchType> types, ResponseListener listener) {
        this.keywords = keywords;
        this.types = types;
        this.listener = listener;

        for (Keyword keyword : this.keywords) {
            String word = keyword.getWord();

            if (metadataMap.containsKey(word)) {
                keyword.setMetadata(metadataMap.get(word));
            }
            else {
                for (SearchType type: this.types) {
                    if (RequestState.isRequestable()) {
                        RequestState.requestState();
                        String url = NaverRequest.MAIN_URL + type.getUrl() + "?query=" + word;
                        sendRequest(url, word);
                    }
                }
            }
        }
    }

    private void sendRequest(String url, final String word) {
        NaverRequest request = new NaverRequest(
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        RequestState.responseState();
                        fillMetadata(word, response);
                        callListenerIfFinished();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onFailure(error.toString());
                    }
                }
        );

        request.setShouldCache(false);
        RequestManager.getInstance().add(request);
    }


    private void fillMetadata(String word, String response) {
        HashMap<SearchType, ResponseData> metadata = getMetadata(response);
        metadataMap.put(word, metadata);

        for (Keyword keyword : keywords) {
            if (keyword.getWord().equals(word)) {
                keyword.setMetadata(metadataMap.get(word));
                break;
            }
        }
    }

    private void callListenerIfFinished() {
        if (RequestState.isFinished()) {
            listener.onResponse(keywords);
        }
    }

    private HashMap<SearchType, ResponseData> getMetadata(String response) {
        HashMap<SearchType, ResponseData> results = new HashMap<>();

        for (SearchType type: types) {
            ResponseData responseData = gson.fromJson(response, ResponseData.class);

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

                ArrayList<BaseItem> baseItems = gson
                        .fromJson(new JSONObject(response).getJSONArray("items").toString(), listType);
                responseData.setItems(baseItems);
            } catch (JSONException e) {
                Log.e(TAG, "JSONException error message: " + e.getMessage());
            }

            results.put(type, responseData);
        }

        return results;
    }
    
    static class RequestState {
        private final static int MAX_REQUEST_CNT = 10;

        private static AtomicInteger requestCnt = new AtomicInteger(0);
        private static AtomicInteger responseCnt = new AtomicInteger(0);

        static boolean isRequestable() {
            return requestCnt.get() < MAX_REQUEST_CNT;
        }

        static void requestState() {
            requestCnt.incrementAndGet();
        }

        static void responseState() {
            responseCnt.incrementAndGet();
        }

        static boolean isFinished() {
            return requestCnt.get() == responseCnt.get();
        }
    }
}
