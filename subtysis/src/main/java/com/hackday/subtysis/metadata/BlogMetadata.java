package com.hackday.subtysis.metadata;

import com.google.gson.reflect.TypeToken;
import com.hackday.subtysis.model.items.BlogItem;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Create by Yeji on 22,June,2020.
 */
public class BlogMetadata extends BaseMetadata {
    @Override
    public Type getListType() {
        return new TypeToken<ArrayList<BlogItem>>() {}.getType();
    }
}
