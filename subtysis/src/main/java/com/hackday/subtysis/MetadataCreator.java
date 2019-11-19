package com.hackday.subtysis;

import com.hackday.subtysis.model.Keyword;

import java.util.ArrayList;

public interface MetadataCreator {
    void fillMetadata(ArrayList<Keyword> keywordKts, SetResponseListener listener);
}
