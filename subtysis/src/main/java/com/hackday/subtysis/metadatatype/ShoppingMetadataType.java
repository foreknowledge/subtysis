package com.hackday.subtysis.metadatatype;

import com.google.gson.reflect.TypeToken;
import com.hackday.subtysis.model.items.ShoppingItem;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Create by Yeji on 22,June,2020.
 */
class ShoppingMetadataType extends BaseMetadataType {
    @Override
    public Type getListType() {
        return new TypeToken<ArrayList<ShoppingItem>>() {}.getType();
    }
}
