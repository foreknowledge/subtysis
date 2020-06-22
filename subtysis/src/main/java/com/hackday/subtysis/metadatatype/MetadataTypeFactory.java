package com.hackday.subtysis.metadatatype;

import com.hackday.subtysis.model.SearchType;

/**
 * Create by Yeji on 22,June,2020.
 */
public interface MetadataTypeFactory {
    BaseMetadataType createInstance(SearchType searchType);
}
