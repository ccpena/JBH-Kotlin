package com.kkpa.jbh.extensions

import com.kkpa.jbh.payload.ApiResponse
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date

fun Int.extensionWithoutClass(): Int {
    return this.plus(1)
}

fun LocalDate.convertToDateViaInstant(): Date {
    return Date.from(this.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())
}

fun ApiResponse.fakeResponse(): ApiResponse {
    return ApiResponse(false, "fake")
}
