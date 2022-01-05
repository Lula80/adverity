package org.challenge.rest.resp.error;
import org.springframework.http.HttpStatus;

/**
 * Common definition for error representation to Client
 *
 */
interface ServiceError {
    HttpStatus getStatus();

    String getMessageKey();

}
