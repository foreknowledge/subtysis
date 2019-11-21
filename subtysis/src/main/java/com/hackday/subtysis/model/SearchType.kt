package com.hackday.subtysis.model

enum class SearchType constructor( val url: String) {
    BLOG("https://openapi.naver.com/v1/search/blog.json"),     // 블로그
    NEWS("https://openapi.naver.com/v1/search/news.json"),     // 뉴스
    BOOK("https://openapi.naver.com/v1/search/book.json"),      // 책
    ADULT("https://openapi.naver.com/v1/search/adult.json"),    // 성인 검색어 판별
    ENCYCLOPEDIA("https://openapi.naver.com/v1/search/encyc.json"),         // 백과사전
    MOVIE("https://openapi.naver.com/v1/search/movie.json"),    // 영화
    CAFEARTICLE("https://openapi.naver.com/v1/search/cafearticle.json"),    // 카페글
    KIN("https://openapi.naver.com/v1/search/kin.json"),        // 지식인
    LOCAL("https://openapi.naver.com/v1/search/local.json"),    // 지역
    ERRATA("https://openapi.naver.com/v1/search/errata.json"),  // 오타변환
    WEB("https://openapi.naver.com/v1/search/webkr.json"),      // 웹문서
    IMAGE("https://openapi.naver.com/v1/search/image.json"),    // 이미지
    SHOPPING("https://openapi.naver.com/v1/search/shop.json"),  // 쇼핑
    DOC("https://openapi.naver.com/v1/search/doc.json")         // 전문자료
}