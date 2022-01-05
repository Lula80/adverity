package org.challenge.rest.resp.error;

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import io.swagger.annotations.ApiModelProperty
import org.springframework.http.HttpStatus

/**
 * Error object to paste in json response
 *
 * Important: Lookup for error messages expects resource bundle "ErrorMessages"
 * in project.
 */
@JsonPropertyOrder([ "status", "message", "details", "invalidParams" ])
class ServiceErrorResponse {

    public static final String MESSAGES = "ErrorMessages";
    @ApiModelProperty(required = true, example = "500", value = "HTTP Status code for the error.")
    private HttpStatus status;

    @ApiModelProperty(required = true, value = "Human readable description of the error.")
    private String message;

    @ApiModelProperty(value = "Additional information to the error.")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String details;

    @ApiModelProperty(value = "List of messages in case of validation errors")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<InvalidParameterEntry> invalidParams;

    ServiceErrorResponse(ServiceError serviceError) {
        this.status = serviceError.getStatus();
        this.message = getLocalizedMessage(serviceError);
    }

    ServiceErrorResponse(ServiceError serviceError, Locale locale) {
        this.status = serviceError.getStatus();
        this.message = getLocalizedMessage(serviceError, locale);
    }

    ServiceErrorResponse(ServiceError serviceError, Exception exception, Locale locale) {
        this.status = serviceError.getStatus();
        this.message = getLocalizedMessage(serviceError, locale);
        this.details = exceptionToInfoString(exception);
    }
    ServiceErrorResponse(ServiceError serviceError, Exception exception) {
        this.status = serviceError.getStatus();
        this.message = getLocalizedMessage(serviceError);
        this.details = exceptionToInfoString(exception);
    }
    ServiceErrorResponse(HttpStatus status, String messageKey) {
        this.status = status;
        this.message = getLocalizedMessage(messageKey);
    }

    ServiceErrorResponse(HttpStatus status, String messageKey, Locale locale) {
        this.status = status;
        this.message = getLocalizedMessage(messageKey, locale);
    }

    ServiceErrorResponse details(String details) {
        this.details = details;
        return this;
    }

    ServiceErrorResponse invalidParams(List<InvalidParameterEntry> invalidParams) {
        this.invalidParams = invalidParams;
        return this;
    }

    private String getLocalizedMessage(ServiceError serviceError) {
        return getLocalizedMessage(serviceError.getMessageKey());
    }

    private String getLocalizedMessage(ServiceError serviceError,
                                       Locale locale) {
        return getLocalizedMessage(serviceError.getMessageKey(), locale);
    }

    private String getLocalizedMessage(String messageKey) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle(MESSAGES);
        return resourceBundle.getString(messageKey);
    }

    private String getLocalizedMessage(String messageKey,
                                       Locale locale) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle(MESSAGES, locale);
        return resourceBundle.getString(messageKey);
    }

    private static String exceptionToInfoString(Exception exception) {
        return new StringBuilder().append(exception.getClass().getName()).append(": ")
                .append(exception.getLocalizedMessage()).toString();
    }

    HttpStatus getStatus() {
        return status
    }

    String getMessage() {
        return message
    }
    String getDetails() {
        return details
    }

    List<InvalidParameterEntry> getInvalidParams() {
        return invalidParams
    }
}