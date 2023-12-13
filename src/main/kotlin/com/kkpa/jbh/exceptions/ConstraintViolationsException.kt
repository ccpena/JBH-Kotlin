package com.kkpa.jbh.exceptions

import com.kkpa.jbh.payload.ErrorsResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class ConstraintViolationsException(val errorsResponse: ErrorsResponse) : RuntimeException(errorsResponse.toString())