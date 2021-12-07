package org.challenge.rest.resp.error;

import java.util.List;

import org.springframework.context.i18n.LocaleContextHolder;


public class ServiceException extends RuntimeException {
    private ServiceErrorResponse errorResponse;

    public ServiceErrorResponse getErrorResponse() {
        return errorResponse;
    }

    public ServiceException(ServiceError serviceError) {
        super(serviceError.toString());
        errorResponse = new ServiceErrorResponse(serviceError, LocaleContextHolder.getLocale());
    }

    public ServiceException(ServiceError serviceError, String details) {
        super(serviceError.toString());
        errorResponse = new ServiceErrorResponse(serviceError, LocaleContextHolder.getLocale()).details(details);
    }

    public ServiceException(ServiceError serviceError, String details, List<InvalidParameterEntry> invalidParams) {
        errorResponse = new ServiceErrorResponse(serviceError, LocaleContextHolder.getLocale()).details(details)
                .invalidParams(invalidParams);
    }

    public ServiceException(Exception exception) {
        super(exception);
        errorResponse = new ServiceErrorResponse(AdverityServiceError.UNKNOWN_INTERNAL_SERVICE_ERROR,
                exception,
                LocaleContextHolder.getLocale());
    }

    public ServiceException(ServiceError serviceError, Exception exception) {
        super(exception);
        errorResponse = new ServiceErrorResponse(serviceError, exception, LocaleContextHolder.getLocale());
    }
}
