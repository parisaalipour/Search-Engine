package com.uu.searchengine.dto

data class WebPageDto (
        var url: String,
        var title: String?,
        var body: String,
        var images: List<ImageDto>
)