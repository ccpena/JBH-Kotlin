package com.kkpa.jbh.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider
import org.springframework.context.annotation.Bean
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.stereotype.Component

@Component
class CategoryFilterConfig {

    @Bean
    fun mappingJackson2HttpMessageConverter(objectMapper: ObjectMapper): MappingJackson2HttpMessageConverter {
        val jsonConverter = MappingJackson2HttpMessageConverter()

        val simpleFilterProvider = SimpleFilterProvider().setFailOnUnknownId(false)
        val filters = simpleFilterProvider.addFilter(
            "categoryFilter",
            SimpleBeanPropertyFilter.serializeAllExcept(ignorableFieldNames)
        )
        objectMapper.setFilterProvider(filters)

        jsonConverter.objectMapper = objectMapper
        return jsonConverter
    }

    companion object {
        private val ignorableFieldNames = setOf("subCategories", "byDefault")
    }
}