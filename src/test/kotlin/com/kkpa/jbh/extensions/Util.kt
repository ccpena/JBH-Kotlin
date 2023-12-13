package com.kkpa.jbh.extensions

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.kkpa.jbh.dto.JbhDTO
import java.math.BigDecimal
import java.math.RoundingMode

fun JbhDTO.convertToByteArray(): ByteArray? {
    var mapper = ObjectMapper()
    try {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
        return mapper.writeValueAsBytes(this)
    } catch (e: JsonProcessingException) {
        e.printStackTrace()
    }
    return null
}

fun BigDecimal.generateRandom(): BigDecimal {
    val max = BigDecimal(this.intValueExact())
    val randFromDouble = BigDecimal(Math.random())
    return randFromDouble.divide(max.plus(), 2, RoundingMode.HALF_DOWN)
}
