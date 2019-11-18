package com.hackday.subtysis;

import java.util.ArrayList;

class Subtitle {
    int frame=0;
    String langCode="";
    String sentence="";
}

public interface SubtitleParser{
    public ArrayList<Subtitle> createSubtitle(String filename);
}




