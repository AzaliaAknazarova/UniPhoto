package com.example.uniphoto.model.dataClasses

import com.google.gson.annotations.SerializedName

data class ContentData(
    val file: String,
    @SerializedName("post_date")
    val date: String,
    @SerializedName("username")
    val userName: String? = null
)
