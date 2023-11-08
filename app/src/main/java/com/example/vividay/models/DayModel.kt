package com.example.vividay.models

import java.time.LocalDateTime

data class DayModel (
    var color: String? = null,
    var note: String? = null,
    var dateTime: LocalDateTime
)
