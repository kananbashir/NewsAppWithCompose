package com.example.composenewscatcher.util.constant

object NewsCatcherApiConstants {

    const val NEWS_API_KEY: String = ""
    const val NEWS_API_BASE_URL: String = "https://api.newscatcherapi.com/v2/"

    //Values
    const val PAGE_SIZE: Int = 20

    //Query parameters
    object QueryParameter {
        const val LANGUAGE: String = "lang"
        const val PAGE_SIZE: String = "page_size"
        const val PAGE: String = "page"
        const val QUERY_KEY: String = "q"
    }

}