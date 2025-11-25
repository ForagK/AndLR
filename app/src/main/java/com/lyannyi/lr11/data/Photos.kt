package com.lyannyi.lr11.data

data class Photo(
    var albumId: Int,
    var id: Int? = null,
    var title: String,
    var url: String,
    var thumbnailUrl: String
)
