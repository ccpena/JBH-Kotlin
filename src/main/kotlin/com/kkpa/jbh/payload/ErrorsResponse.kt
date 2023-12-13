package com.kkpa.jbh.payload

data class ErrorsResponse(val errors: Set<ErrorMessageResponse>) {

    override fun toString(): String {
        return errors.map {
            it.message
        }.joinToString(separator = ";")
    }
}