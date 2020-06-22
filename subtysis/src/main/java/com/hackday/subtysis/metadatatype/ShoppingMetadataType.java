package com.hackday.subtysis.metadatatype;

import com.google.gson.reflect.TypeToken;
import com.hackday.subtysis.model.items.ShoppingItem;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Create by Yeji on 22,June,2020.
 */
class ShoppingMetadataType extends BaseMetadataType {
    private static ShoppingMetadataType shoppingMetadataType;
    private Type listType = new TypeToken<List<ShoppingItem>>() {}.getType();

    private ShoppingMetadataType() {}

    public static BaseMetadataType getInstance() {
        if (shoppingMetadataType == null) {
            shoppingMetadataType = new ShoppingMetadataType();
        }

        return shoppingMetadataType;
    }

    @Override
    public Type getListType() {
        return listType;
    }
}
