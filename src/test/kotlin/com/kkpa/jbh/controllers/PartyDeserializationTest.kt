package com.kkpa.jbh.controllers

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
class PartyDeserializationTest {

    @Autowired lateinit var mockMvc: MockMvc

    val PARTY_URL = "/party-deserialization"

    @Test
    fun timeMapping() {
        this.mockMvc.perform(get("$PARTY_URL/dates"))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(content().json("{'localDate':'2019-02-06'}"))
            .andExpect(content().json("{'localDateTime':'2019-02-06T03:45:42.01'}"))
            .andExpect(content().json("{'localTime':'03:45:42.01'}"))
    }

    @Test
    fun formattedTimeMapping() {
        this.mockMvc.perform(get("$PARTY_URL/dates-formatted"))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(content().json("{'localDate':'06-02-2019'}"))
            .andExpect(content().json("{'localDateTime':'2019-02-06 03:45'}"))
            .andExpect(content().json("{'localDateTimeFormatted':'2019-02-06'}"))
            .andExpect(content().json("{'localTime':'03:45'}"))
    }
}