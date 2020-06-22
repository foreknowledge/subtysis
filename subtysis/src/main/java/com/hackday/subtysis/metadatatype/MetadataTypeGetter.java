package com.hackday.subtysis.metadatatype;

import com.hackday.subtysis.model.SearchType;

/**
 * Create by Yeji on 22,June,2020.
 */
public interface MetadataTypeGetter {
    BaseMetadataType getInstance(SearchType searchType);
}
