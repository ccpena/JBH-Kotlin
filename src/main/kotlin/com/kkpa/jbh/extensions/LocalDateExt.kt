package com.kkpa.jbh.extensions

import com.kkpa.jbh.payload.ApiResponse
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date

fun LocalDate.convertToDateViaInstant(): Date {
    return Date.from(this.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())
}
