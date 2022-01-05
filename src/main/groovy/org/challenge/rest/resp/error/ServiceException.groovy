package org.challenge.rest.resp.error

import org.springframework.context.i18n.LocaleContextHolder

class ServiceException extends RuntimeException {

    private ServiceErrorResponse errorResponse

     ServiceErrorResponse getErrorResponse() {
        return errorResponse;
    }

    ServiceException(ServiceError apiErrorEnumValue) {
        super(apiErrorEnumValue.getMessageKey());
        errorResponse = new ServiceErrorResponse(apiErrorEnumValue, LocaleContextHolder.getLocale());
    }

    ServiceException(Exception ex, String exceptionText) {
        super(exceptionText);
        errorResponse = new ServiceErrorResponse(AdverityServiceError.valueOf(exceptionText),
                ex, LocaleContextHolder.getLocale());
    }

    ServiceException(ServiceError apiErrorEnumValue, String details) {
        super(details);
        errorResponse = new ServiceErrorResponse(apiErrorEnumValue, LocaleContextHolder.getLocale()).details(details);
    }

    ServiceException(ServiceError apiErrorEnumValue, String details,
                     List<InvalidParameterEntry> invalidParams) {
        super(details);
        errorResponse = new ServiceErrorResponse(apiErrorEnumValue, LocaleContextHolder.getLocale()).details(details)
                .invalidParams(invalidParams);
    }

    ServiceException(ServiceError serviceError, Exception exception) {
        super(exception);
        errorResponse = new ServiceErrorResponse(serviceError, exception, LocaleContextHolder.getLocale());
    }
}
