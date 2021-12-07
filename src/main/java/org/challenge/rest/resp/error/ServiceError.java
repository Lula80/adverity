package org.challenge.rest.resp.error;
import org.springframework.http.HttpStatus;

/**
 * Common definition for error representation to Client
 *
 */
public interface ServiceError {
    HttpStatus getStatus();

    String getMessageKey();

}
