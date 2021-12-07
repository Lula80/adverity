package org.challenge.rest.controller;

import java.util.List;
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

import lombok.extern.log4j.Log4j2;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@Log4j2
class ControllerExceptionHandler {

    private <T> ResponseEntity<ResponseWrapper<T>> buildResponseEntity(ServiceErrorResponse serviceErrorResponse) {
        log.info("Locale: {}", LocaleContextHolder.getLocale());
        ResponseWrapper<T> body = new ResponseWrapper<T>().error(serviceErrorResponse);
        return ResponseEntity.status(serviceErrorResponse.getStatus()).body(body);
    }

    private <T> ResponseEntity<ResponseWrapper<T>> buildResponseEntity(ServiceError serviceError, Exception exception) {
        log.info("Locale: {}", LocaleContextHolder.getLocale());
        return buildResponseEntity(
                new ServiceErrorResponse(serviceError, exception, LocaleContextHolder.getLocale()));
    }

    private <T> ResponseEntity<ResponseWrapper<T>> buildResponseEntity(ServiceError serviceError, Exception exception,
            List<InvalidParameterEntry> invalidParams) {
        log.info("Locale: {}", LocaleContextHolder.getLocale());
        return buildResponseEntity(
                new ServiceErrorResponse(serviceError, exception, LocaleContextHolder.getLocale())
                        .invalidParams(invalidParams));
    }

    @ExceptionHandler
    @ResponseBody
    public <T> ResponseEntity<ResponseWrapper<T>> handleException(Exception e) {
        log.error(e, e);
        return buildResponseEntity(AdverityServiceError.UNKNOWN_INTERNAL_SERVICE_ERROR, e);
    }

    @ExceptionHandler
    @ResponseBody
    public <T> ResponseEntity<ResponseWrapper<T>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        log.error(ex);

        return buildResponseEntity(AdverityServiceError.METHOD_ARGUMENT_NOT_VALID, ex,
                ex.getBindingResult().getFieldErrors().stream()
                        .map(fe -> InvalidParameterEntry.builder()
                                .name(fe.getField())
                                .reason(fe.getDefaultMessage())
                                .build())
                        .collect(Collectors.toList()));
    }

    @ExceptionHandler
    @ResponseBody
    public <T> ResponseEntity<ResponseWrapper<T>> handle(HttpMessageNotReadableException ex) {
        log.error(ex);

        if (ex.contains(InvalidFormatException.class)) {
            InvalidFormatException invalidFormatException = (InvalidFormatException) ex.getCause();
            String fieldName = invalidFormatException.getPath().stream()
                    .map(JsonMappingException.Reference::getFieldName).collect(Collectors.joining("."));
            List<InvalidParameterEntry> invalidParameterEntryList = List
                    .of(InvalidParameterEntry.builder().name(fieldName)
                            .reason(invalidFormatException.getLocalizedMessage()).build());
            return buildResponseEntity(AdverityServiceError.UNKNOWN_CLIENT_REQUEST_ERROR,
                    invalidFormatException,
                    invalidParameterEntryList);
        }
        return buildResponseEntity(AdverityServiceError.HTTP_MESSAGE_NOT_READABLE, ex);
    }

    @ExceptionHandler
    @ResponseBody
    public <T> ResponseEntity<ResponseWrapper<T>> handle(MissingServletRequestParameterException ex) {
        log.error(ex);
        return buildResponseEntity(AdverityServiceError.UNKNOWN_CLIENT_REQUEST_ERROR, ex,
                List.of(InvalidParameterEntry.builder().name(ex.getParameterName())
                        .reason(ex.getMessage())
                        .build()));
    }


    @ExceptionHandler
    @ResponseBody
    public <T> ResponseEntity<ResponseWrapper<T>> handle(MethodArgumentTypeMismatchException ex) {
        log.warn(ex);
        return buildResponseEntity(AdverityServiceError.METHOD_ARGUMENT_TYPE_MISMATCH, ex,
                List.of(InvalidParameterEntry.builder().name(ex.getParameter().getParameterName())
                        .reason(ex.getLocalizedMessage()).build()));
    }

    @ExceptionHandler
    @ResponseBody
    public <T> ResponseEntity<ResponseWrapper<T>> handle(HttpMediaTypeNotSupportedException ex) {
        log.warn(ex);
        return buildResponseEntity(AdverityServiceError.HTTP_MEDIA_TYPE_NOT_SUPPORTED, ex);
    }

    @ExceptionHandler
    @ResponseBody
    public <T> ResponseEntity<ResponseWrapper<T>> handle(HttpRequestMethodNotSupportedException ex) {
        log.warn(ex);
        return buildResponseEntity(AdverityServiceError.HTTP_REQUEST_METHOD_NOT_SUPPORTED, ex);
    }

    @ExceptionHandler
    @ResponseBody
    public <T> ResponseEntity<ResponseWrapper<T>> handle(EntityNotFoundException ex) {
        log.warn(ex);
        return buildResponseEntity(AdverityServiceError.NOT_FOUND_ON_DATABASE, ex);
    }

    @ExceptionHandler
    @ResponseBody
    public <T> ResponseEntity<ResponseWrapper<T>> handle(BindException ex) {
        log.warn(ex);
        List<InvalidParameterEntry> invalidParams = ex.getAllErrors().stream()
                .map(e -> InvalidParameterEntry.builder().name(e.getObjectName()).reason(e.getDefaultMessage()).build())
                .collect(Collectors.toList());
        return buildResponseEntity(AdverityServiceError.UNKNOWN_CLIENT_REQUEST_ERROR, ex, invalidParams);
    }

    @ExceptionHandler
    @ResponseBody
    public <T> ResponseEntity<ResponseWrapper<T>> handle(JsonParseException ex) {
        log.error(ex);
        return buildResponseEntity(AdverityServiceError.UNKNOWN_CLIENT_REQUEST_ERROR, ex);
    }

    @ExceptionHandler
    @ResponseBody
    public <T> ResponseEntity<ResponseWrapper<T>> handle(ValidationException ex) {
        log.warn(ex);
        return buildResponseEntity(AdverityServiceError.VALIDATION_ERROR, ex);
    }

    @ExceptionHandler
    @ResponseBody
    public <T> ResponseEntity<ResponseWrapper<T>> handle(ServiceException e) {
        return buildResponseEntity(e.getErrorResponse());
    }

}
