package com.example.uniphoto.model.dataClasses

data class TrialCheckoutResponse(
    val timeIsOut: Boolean,
    val daysOut: Int
)
