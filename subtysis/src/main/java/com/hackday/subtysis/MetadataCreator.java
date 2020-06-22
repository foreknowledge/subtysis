package com.hackday.subtysis;

import com.hackday.subtysis.model.Keyword;
import com.hackday.subtysis.model.SearchType;

import java.util.List;

public interface MetadataCreator {
    void fillMetadata(List<Keyword> keywordKts, List<SearchType> types, ResponseListener listener);
}
