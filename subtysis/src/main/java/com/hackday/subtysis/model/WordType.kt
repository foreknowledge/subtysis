package com.hackday.subtysis.model

enum class WordType {
    NN,       // 명사
    NP,       // 대명사
    VV,       // 동사
    VA,       // 형용사
    VX,       // 보조용언
    VC,       // 지정사
    MM,       // 수식언
    IC,       // 감탄사
    ASS,      // 관계언
    DEP,      // 의존형태
    SYMBOL,   // 기호
    FOREIGN,  // 외국어
    NF,       // 명사추정범주
    NV,       // 용언추정범주
    NUM,      // 숫자
    NONE      // 분석 불가
}