package com.research.surveyor.exceptions

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler : ResponseEntityExceptionHandler() {
    @ExceptionHandler(value = [InvalidRequestException::class])
    protected fun handleInvalidRequestError(ex: InvalidRequestException): ResponseEntity<Any> {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse(ex.message!!))
    }

    @ExceptionHandler(value = [Exception::class])
    protected fun handleInternalServerError(ex: Exception): ResponseEntity<Any> {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ErrorResponse(ex.message!!))
    }
}

data class InvalidRequestException(override val message: String): RuntimeException(message)
