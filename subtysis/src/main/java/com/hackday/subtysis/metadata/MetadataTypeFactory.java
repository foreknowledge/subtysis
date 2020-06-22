package com.hackday.subtysis.metadata;

import com.hackday.subtysis.model.SearchType;

/**
 * Create by Yeji on 22,June,2020.
 */
public interface MetadataTypeFactory {
    BaseMetadata createInstance(SearchType searchType);
}
