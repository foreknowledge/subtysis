package com.hackday.subtysis.model

enum class SearchType constructor(val url: String) {
    BLOG("/blog.json"),     // 블로그
    NEWS("/news.json"),     // 뉴스
    BOOK("/book.json"),      // 책
    ADULT("/adult.json"),    // 성인 검색어 판별
    ENCYCLOPEDIA("/encyc.json"),         // 백과사전
    MOVIE("/movie.json"),    // 영화
    CAFEARTICLE("/cafearticle.json"),    // 카페글
    KIN("/kin.json"),        // 지식인
    LOCAL("/local.json"),    // 지역
    ERRATA("/errata.json"),  // 오타변환
    WEB("/webkr.json"),      // 웹문서
    IMAGE("/image.json"),    // 이미지
    SHOPPING("/shop.json"),  // 쇼핑
    DOC("/doc.json")         // 전문자료
}