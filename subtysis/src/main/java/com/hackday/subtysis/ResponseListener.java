package com.hackday.subtysis;

import com.hackday.subtysis.model.Keyword;
import java.util.ArrayList;

public interface ResponseListener {
    void onResponse(ArrayList<Keyword> keywords);

  void onFailure(String errorMsg);
}
