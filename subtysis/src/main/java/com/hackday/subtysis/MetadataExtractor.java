package com.hackday.subtysis;

import android.util.Log;

import com.google.gson.Gson;
import com.hackday.subtysis.metadatatype.MetadataTypeFactory;
import com.hackday.subtysis.metadatatype.MetadataTypeFactoryImpl;
import com.hackday.subtysis.model.SearchType;
import com.hackday.subtysis.model.items.BaseItem;
import com.hackday.subtysis.model.response.ResponseData;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Create by Yeji on 22,June,2020.
 */
public class MetadataExtractor {
    private final static String TAG = "MetadataExtractor";
    private MetadataTypeFactory metadataTypeFactory = new MetadataTypeFactoryImpl();
    private Gson gson = new Gson();

    public HashMap<SearchType, ResponseData> extractMetadata(ArrayList<SearchType> types, String response) {
        HashMap<SearchType, ResponseData> results = new HashMap<>();

        for (SearchType type: types) {
            ResponseData responseData = gson.fromJson(response, ResponseData.class);

            extractAndPut(responseData, response, type);

            results.put(type, responseData);
        }

        return results;
    }

    private void extractAndPut(ResponseData responseData, String response, SearchType type) {
        try {
            Type listType = metadataTypeFactory.createInstance(type).getListType();

            ArrayList<BaseItem> baseItems = gson
                    .fromJson(new JSONObject(response).getJSONArray("items").toString(), listType);
            responseData.setItems(baseItems);
        } catch (JSONException e) {
            Log.e(TAG, "JSONException error message: " + e.getMessage());
        }
    }
}
