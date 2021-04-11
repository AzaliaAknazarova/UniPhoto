package com.example.uniphoto.model.dataClasses

import com.google.gson.annotations.SerializedName

data class FilesPage(
    @SerializedName("count")
    val contentCount: Int,
    @SerializedName("next")
    val nextPage: String?,
    @SerializedName("previous")
    val previousPage: String?,
    @SerializedName("results")
    val content: List<ContentData>,
)
