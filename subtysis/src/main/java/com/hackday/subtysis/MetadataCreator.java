package com.hackday.subtysis;

import com.hackday.subtysis.model.Keyword;
import com.hackday.subtysis.model.SearchType;

import java.util.ArrayList;

public interface MetadataCreator {
    void fillMetadata(ArrayList<Keyword> keywordKts, ArrayList<SearchType> types, SetResponseListener listener);
}
