package com.hackday.subtysis;

import android.util.Log;

import com.google.gson.Gson;
import com.hackday.subtysis.metadatatype.MetadataTypeGetter;
import com.hackday.subtysis.model.SearchType;
import com.hackday.subtysis.model.items.BaseItem;
import com.hackday.subtysis.model.response.ResponseData;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Create by Yeji on 22,June,2020.
 */
public class MetadataExtractor {
    private final static String TAG = "MetadataExtractor";

    private MetadataTypeGetter metadataTypeGetter;
    private Gson gson;

    private ResponseData responseData;
    private SearchType searchType;

    public MetadataExtractor(MetadataTypeGetter metadataTypeGetter) {
        this.metadataTypeGetter = metadataTypeGetter;
        gson = new Gson();
    }

    public Map<SearchType, ResponseData> extractMetadata(List<SearchType> types, String response) {
        Map<SearchType, ResponseData> results = new HashMap<>();

        for (SearchType type : types) {
            responseData = gson.fromJson(response, ResponseData.class);
            searchType = type;

            extractAndSet(response);

            results.put(type, responseData);
        }

        return results;
    }

    private void extractAndSet(String response) {
        try {
            Type listType = metadataTypeGetter.getInstance(searchType).getListType();

            List<BaseItem> baseItems = gson
                    .fromJson(new JSONObject(response).getJSONArray("items").toString(), listType);

            responseData.setItems(baseItems);
        } catch (JSONException e) {
            Log.e(TAG, "JSONException error message: " + e.getMessage());
        }
    }
}
