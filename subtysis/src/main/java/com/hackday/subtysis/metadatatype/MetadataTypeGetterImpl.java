package com.hackday.subtysis.metadatatype;

import com.hackday.subtysis.model.SearchType;

/**
 * Create by Yeji on 22,June,2020.
 */
public class MetadataTypeGetterImpl implements MetadataTypeGetter {

    @Override
    public BaseMetadataType getInstance(SearchType searchType) {
        switch (searchType) {
            case BLOG: return BlogMetadataType.getInstance();
            case SHOPPING: return ShoppingMetadataType.getInstance();
            case ENCYCLOPEDIA: return EncyclopediaMetadataType.getInstance();
            default: return null;
        }
    }
}
