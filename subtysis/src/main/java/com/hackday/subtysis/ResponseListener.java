package com.hackday.subtysis;

import com.hackday.subtysis.model.Keyword;

import java.util.List;

public interface ResponseListener {
    void onResponse(List<Keyword> keywords);

  void onFailure(String errorMsg);
}
