package com.example.uniphoto.model.dataClasses

import com.google.gson.annotations.SerializedName

data class TrialCheckoutResponse(
    @SerializedName("is_license_end")
    val timeIsOut: Boolean,
    @SerializedName("days_of_app_use")
    val daysOut: Int
)
