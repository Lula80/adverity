package org.challenge.rest.resp.error;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

/**
 * Concrete definition of errors that could happen while running this application
 */
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum AdverityServiceError implements ServiceError {

    UNKNOWN_INTERNAL_SERVICE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "unknown.service.error"),
    UNKNOWN_CLIENT_REQUEST_ERROR(HttpStatus.BAD_REQUEST, "unknown.client.request.error"),
    METHOD_ARGUMENT_NOT_VALID(HttpStatus.BAD_REQUEST, "method.argument.not.valid.error"),
    METHOD_ARGUMENT_TYPE_MISMATCH(HttpStatus.BAD_REQUEST, "method.argument.type.mismatch.error"),
    HTTP_MEDIA_TYPE_NOT_SUPPORTED(HttpStatus.BAD_REQUEST, "http.media.type.not.supported.error"),
    HTTP_MESSAGE_NOT_READABLE(HttpStatus.BAD_REQUEST, "http.message.not.readable"),
    HTTP_REQUEST_METHOD_NOT_SUPPORTED(HttpStatus.BAD_REQUEST, "http.request.method.not.supported.error"),
    REQUIRED_PARAMETER_MISSING(HttpStatus.BAD_REQUEST, "http.empty.request.body"),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "validation.error"),
    NOT_FOUND_ON_DATABASE(HttpStatus.NOT_FOUND, "no.entity.found.on.database.error"),
    SQL_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "sql.error");

    HttpStatus status;

    String messageKey;
}
