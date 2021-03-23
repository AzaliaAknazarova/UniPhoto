package com.example.uniphoto.model.dataClasses

import java.util.*

data class TrialCheckoutResponse(
    val timeIsOut: Boolean,
    val registrationData: Date
)
