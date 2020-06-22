package com.hackday.subtysis.metadatatype;

import com.google.gson.reflect.TypeToken;
import com.hackday.subtysis.model.items.BlogItem;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Create by Yeji on 22,June,2020.
 */
class BlogMetadataType extends BaseMetadataType {
    private static BlogMetadataType blogMetadataType;

    private BlogMetadataType() {}

    public static BaseMetadataType getInstance() {
        if (blogMetadataType == null) {
            blogMetadataType = new BlogMetadataType();
        }

        return blogMetadataType;
    }

    @Override
    public Type getListType() {
        return new TypeToken<List<BlogItem>>() {}.getType();
    }
}
