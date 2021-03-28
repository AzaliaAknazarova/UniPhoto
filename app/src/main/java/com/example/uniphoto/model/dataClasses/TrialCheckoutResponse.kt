package com.example.uniphoto.model.dataClasses

import com.google.gson.annotations.SerializedName

data class TrialCheckoutResponse(
    @SerializedName("days_to_license_end")
    val daysToEnd: Int
)
