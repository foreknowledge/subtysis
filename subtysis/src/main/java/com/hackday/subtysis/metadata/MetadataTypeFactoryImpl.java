package com.hackday.subtysis.metadata;

import com.hackday.subtysis.model.SearchType;

/**
 * Create by Yeji on 22,June,2020.
 */
public class MetadataTypeFactoryImpl implements MetadataTypeFactory {
    @Override
    public BaseMetadata createInstance(SearchType searchType) {
        switch (searchType) {
            case BLOG: return new BlogMetadata();
            case SHOPPING: return new ShoppingMetadata();
            case ENCYCLOPEDIA: return new EncyclopediaMetadata();
            default: return null;
        }
    }
}
