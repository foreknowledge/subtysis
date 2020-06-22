package com.hackday.subtysis;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.hackday.subtysis.model.Keyword;
import com.hackday.subtysis.model.SearchType;
import com.hackday.subtysis.model.response.ResponseData;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MetadataCreatorImpl implements MetadataCreator {
    private List<Keyword> keywords;
    private List<SearchType> types;
    private HashMap<String, HashMap<SearchType, ResponseData>> metadataCache = new HashMap<>();
    private ResponseListener listener;
    private RequestState requestState = new RequestState();

    private MetadataExtractor metadataExtractor = new MetadataExtractor();

    @Override
    public void fillMetadata(List<Keyword> keywords, List<SearchType> types, ResponseListener listener) {
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
                    if (requestState.isRequestable()) {
                        requestState.requestState();
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
                        requestState.responseState();
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
        RequestQueueProvider.getInstance().add(request);
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
        if (requestState.isFinished()) {
            listener.onResponse(keywords);
        }
    }
    
    static class RequestState {
        private final static int MAX_REQUEST_CNT = 10;

        private AtomicInteger requestCnt = new AtomicInteger(0);
        private AtomicInteger responseCnt = new AtomicInteger(0);

        boolean isRequestable() {
            return requestCnt.get() < MAX_REQUEST_CNT;
        }

        void requestState() {
            requestCnt.incrementAndGet();
        }

        void responseState() {
            responseCnt.incrementAndGet();
        }

        boolean isFinished() {
            return requestCnt.get() == responseCnt.get();
        }
    }
}
