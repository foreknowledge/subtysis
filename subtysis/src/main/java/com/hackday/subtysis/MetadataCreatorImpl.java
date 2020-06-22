package com.hackday.subtysis;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.hackday.subtysis.model.Keyword;
import com.hackday.subtysis.model.SearchType;
import com.hackday.subtysis.model.response.ResponseData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MetadataCreatorImpl implements MetadataCreator {
    private ArrayList<Keyword> keywords;
    private ArrayList<SearchType> types;
    private HashMap<String, HashMap<SearchType, ResponseData>> metadataCache = new HashMap<>();
    private ResponseListener listener;

    private MetadataExtractor metadataExtractor = new MetadataExtractor();

    @Override
    public void fillMetadata(ArrayList<Keyword> keywords, ArrayList<SearchType> types, ResponseListener listener) {
        this.keywords = keywords;
        this.types = types;
        this.listener = listener;

        for (Keyword keyword : this.keywords) {
            String word = keyword.getWord();

            if (metadataCache.containsKey(word)) {
                keyword.setMetadata(metadataCache.get(word));
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
                        setMetadata(word, response);
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

    private void setMetadata(String word, String response) {
        HashMap<SearchType, ResponseData> metadata = metadataExtractor.extractMetadata(types, response);
        metadataCache.put(word, metadata);

        for (Keyword keyword : keywords) {
            if (keyword.getWord().equals(word)) {
                keyword.setMetadata(metadataCache.get(word));
                break;
            }
        }
    }

    private void callListenerIfFinished() {
        if (RequestState.isFinished()) {
            listener.onResponse(keywords);
        }
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
