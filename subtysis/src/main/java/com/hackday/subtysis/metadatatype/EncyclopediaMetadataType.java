package com.hackday.subtysis.metadatatype;

import com.google.gson.reflect.TypeToken;
import com.hackday.subtysis.model.items.EncyclopediaItem;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Create by Yeji on 22,June,2020.
 */
class EncyclopediaMetadataType extends BaseMetadataType {
    private static EncyclopediaMetadataType encyclopediaMetadataType;

    private EncyclopediaMetadataType() {}

    public static BaseMetadataType getInstance() {
        if (encyclopediaMetadataType == null) {
            encyclopediaMetadataType = new EncyclopediaMetadataType();
        }

        return encyclopediaMetadataType;
    }

    @Override
    public Type getListType() {
        return new TypeToken<List<EncyclopediaItem>>() {}.getType();
    }
}
