package org.challenge.rest.resp.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Error object to paste in json response
 *
 * Important: Lookup for error messages expects resource bundle "ErrorMessages"
 * in project.
 */
@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@FieldNameConstants
//@JsonPropertyrder({ status, message, details, invalidParams })
public class ServiceErrorResponse {

    public static final String MESSAGES = "ErrorMessages";
    @ApiModelProperty(required = true, example = "500", value = "HTTP Status code for the error.")
    HttpStatus status;

    @ApiModelProperty(required = true, value = "Human readable description of the error.")
    @Setter(AccessLevel.NONE)
    String message;

    @ApiModelProperty(value = "Additional information to the error.")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String details;

    @ApiModelProperty(value = "List of messages in case of validation errors")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    List<InvalidParameterEntry> invalidParams;

    public ServiceErrorResponse(ServiceError serviceError) {
        this.status = serviceError.getStatus();
        this.message = getLocalizedMessage(serviceError);
    }

    public ServiceErrorResponse(ServiceError serviceError, Locale locale) {
        this.status = serviceError.getStatus();
        this.message = getLocalizedMessage(serviceError, locale);
    }

    public ServiceErrorResponse(ServiceError serviceError, Exception exception, Locale locale) {
        this.status = serviceError.getStatus();
        this.message = getLocalizedMessage(serviceError, locale);
        this.details = exceptionToInfoString(exception);
    }

    public ServiceErrorResponse(HttpStatus status, String messageKey) {
        this.status = status;
        this.message = getLocalizedMessage(messageKey);
    }

    public ServiceErrorResponse(HttpStatus status, String messageKey, Locale locale) {
        this.status = status;
        this.message = getLocalizedMessage(messageKey, locale);
    }

    public ServiceErrorResponse details(String details) {
        this.details = details;
        return this;
    }

    public ServiceErrorResponse invalidParams(List<InvalidParameterEntry> invalidParams) {
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
}