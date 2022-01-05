package org.challenge.rest.controller

import javax.validation.ConstraintViolationException
import java.util.logging.Level;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;
import javax.xml.bind.ValidationException;

import org.challenge.rest.resp.ResponseWrapper;
import org.challenge.rest.resp.error.*;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import groovy.util.logging.Log;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@Log
class ControllerExceptionHandler {

    private <T> ResponseEntity<ResponseWrapper<T>> buildResponseEntity(ServiceErrorResponse serviceErrorResponse) {
        ResponseWrapper<T> body = new ResponseWrapper<T>().error(serviceErrorResponse);
        return ResponseEntity.status(serviceErrorResponse.getStatus()).body(body);
    }

    private <T> ResponseEntity<ResponseWrapper<T>> buildResponseEntity(ServiceError serviceError, Exception exception) {
        return buildResponseEntity(
                new ServiceErrorResponse(serviceError, exception, LocaleContextHolder.getLocale()));
    }

    private <T> ResponseEntity<ResponseWrapper<T>> buildResponseEntity(ServiceError serviceError, Exception exception,
            List<InvalidParameterEntry> invalidParams) {
        return buildResponseEntity(
                new ServiceErrorResponse(serviceError, exception, LocaleContextHolder.getLocale())
                        .invalidParams(invalidParams));
    }

    @ExceptionHandler
    @ResponseBody
    <T> ResponseEntity<ResponseWrapper<T>> handleException(Exception e) {
        log.log(Level.SEVERE, e.getMessage())
        return buildResponseEntity(AdverityServiceError.UNKNOWN_INTERNAL_SERVICE_ERROR, e)
    }

    @ExceptionHandler
    @ResponseBody
    <T> ResponseEntity<ResponseWrapper<T>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        log.log(Level.SEVERE, ex.getMessage())
        return buildResponseEntity(AdverityServiceError.METHOD_ARGUMENT_NOT_VALID, ex,
                ex.getBindingResult().getFieldErrors().collect{fe -> InvalidParameterEntry.builder()
                                .name(fe.getField())
                                .reason(fe.getDefaultMessage())
                                .build()})
    }

    @ExceptionHandler
    @ResponseBody
    <T> ResponseEntity<ResponseWrapper<T>> handle(HttpMessageNotReadableException ex) {
        log.log(Level.SEVERE, ex.getMessage());
        if (ex.contains(InvalidFormatException.class)) {
            InvalidFormatException invalidFormatException = (InvalidFormatException) ex.getCause()
            String fieldName = invalidFormatException.getPath().stream()
                    .map(JsonMappingException.Reference::getFieldName).collect(Collectors.joining("."));
            List<InvalidParameterEntry> invalidParameterEntryList = [InvalidParameterEntry.builder().name(fieldName)
                            .reason(invalidFormatException.getLocalizedMessage()).build()]
           invalidParameterEntryList.each {e-> println "invalid entry $e.name"}
            return buildResponseEntity(AdverityServiceError.UNKNOWN_CLIENT_REQUEST_ERROR,
                    invalidFormatException,
                    invalidParameterEntryList);
        }
        return buildResponseEntity(AdverityServiceError.HTTP_MESSAGE_NOT_READABLE, ex);
    }

    @ExceptionHandler
    @ResponseBody
    <T> ResponseEntity<ResponseWrapper<T>> handle(MissingServletRequestParameterException ex) {
        log.log(Level.SEVERE, ex.getMessage());
        return buildResponseEntity(AdverityServiceError.UNKNOWN_CLIENT_REQUEST_ERROR, ex,
                [InvalidParameterEntry.builder().name(ex.getParameterName())
                         .reason(ex.getLocalizedMessage()).build()])
    }


    @ExceptionHandler
    @ResponseBody
    <T> ResponseEntity<ResponseWrapper<T>> handle(MethodArgumentTypeMismatchException ex) {
        log.log(Level.WARNING, ex.getMessage());
        return buildResponseEntity(AdverityServiceError.METHOD_ARGUMENT_TYPE_MISMATCH, ex,
                [InvalidParameterEntry.builder().name(ex.getParameter().getParameterName())
                        .reason(ex.getLocalizedMessage()).build()])
    }

    @ExceptionHandler
    @ResponseBody
    <T> ResponseEntity<ResponseWrapper<T>> handle(HttpMediaTypeNotSupportedException ex) {
        log.log(Level.WARNING, ex.getMessage());
        return buildResponseEntity(AdverityServiceError.HTTP_MEDIA_TYPE_NOT_SUPPORTED, ex);
    }

    @ExceptionHandler
    @ResponseBody
    <T> ResponseEntity<ResponseWrapper<T>> handle(HttpRequestMethodNotSupportedException ex) {
        log.log(Level.WARNING, ex.getMessage());
        return buildResponseEntity(AdverityServiceError.HTTP_REQUEST_METHOD_NOT_SUPPORTED, ex);
    }

    @ExceptionHandler
    @ResponseBody
    <T> ResponseEntity<ResponseWrapper<T>> handle(EntityNotFoundException ex) {
        log.log(Level.WARNING, ex.getMessage());
        return buildResponseEntity(AdverityServiceError.NOT_FOUND_ON_DATABASE, ex);
    }

    @ExceptionHandler
    @ResponseBody
    <T> ResponseEntity<ResponseWrapper<T>> handle(BindException ex) {
        log.log(Level.WARNING, ex.getMessage());
        List<InvalidParameterEntry> invalidParams = ex.getAllErrors().collect {it->
          InvalidParameterEntry.builder().name(it.getObjectName()).reason(it.getDefaultMessage()).build()
        }
        return buildResponseEntity(AdverityServiceError.UNKNOWN_CLIENT_REQUEST_ERROR, ex, invalidParams);
    }
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public <T> ResponseEntity<ResponseWrapper<T>> handle(ConstraintViolationException ex) {
        log.warn(ex, ex);
        List<InvalidParameterEntry> invalidParameterEntries = ex
                .getConstraintViolations().stream().map(v -> InvalidParameterEntry.builder()
                .name(v.getPropertyPath().iterator().next().getName()).reason(v.getMessage()).build())
                .collect(Collectors.toList());

        return buildResponseEntity(AdverityServiceError.VALIDATION_ERROR, ex, invalidParameterEntries);
    }
    @ExceptionHandler
    @ResponseBody
    <T> ResponseEntity<ResponseWrapper<T>> handle(JsonParseException ex) {
        log.log(Level.SEVERE, ex.getMessage());
        return buildResponseEntity(AdverityServiceError.UNKNOWN_CLIENT_REQUEST_ERROR, ex);
    }

    @ExceptionHandler
    @ResponseBody
    <T> ResponseEntity<ResponseWrapper<T>> handle(ValidationException ex) {
        log.log(Level.WARNING, ex.getMessage());
        return buildResponseEntity(AdverityServiceError.VALIDATION_ERROR, ex);
    }

    @ExceptionHandler
    @ResponseBody
    <T> ResponseEntity<ResponseWrapper<T>> handle(ServiceException e) {
        return buildResponseEntity(e.getErrorResponse());
    }

}
