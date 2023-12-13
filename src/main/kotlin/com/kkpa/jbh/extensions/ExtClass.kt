package com.kkpa.jbh.extensions

import java.time.LocalDate
import java.time.ZoneId
import java.util.Date

class ExtClass {

    fun LocalDate.convertToDateViaInstant(): Date {
        return Date.from(this.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())
    }

    fun Int.justTest(): Int {
        return this.plus(1)
    }
}