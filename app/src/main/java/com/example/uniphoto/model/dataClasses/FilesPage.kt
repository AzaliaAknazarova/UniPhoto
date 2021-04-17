package com.example.uniphoto.model.dataClasses

import com.google.gson.annotations.SerializedName

data class FilesPage(
    @SerializedName("count")
    val contentCount: Int,
    @SerializedName("next")
    val nextPage: String? = null,
    @SerializedName("previous")
    val previousPage: String? = null,
    @SerializedName("results")
    val content: List<ContentData>,
)
