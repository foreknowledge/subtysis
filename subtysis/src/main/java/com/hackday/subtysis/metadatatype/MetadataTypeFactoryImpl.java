package com.hackday.subtysis.metadatatype;

import com.hackday.subtysis.model.SearchType;

/**
 * Create by Yeji on 22,June,2020.
 */
public class MetadataTypeFactoryImpl implements MetadataTypeFactory {
    @Override
    public BaseMetadataType createInstance(SearchType searchType) {
        switch (searchType) {
            case BLOG: return new BlogMetadataType();
            case SHOPPING: return new ShoppingMetadataType();
            case ENCYCLOPEDIA: return new EncyclopediaMetadataType();
            default: return null;
        }
    }
}
