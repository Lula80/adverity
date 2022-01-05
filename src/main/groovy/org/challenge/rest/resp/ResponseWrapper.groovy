package org.challenge.rest.resp

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonView 
import org.challenge.rest.resp.error.ServiceErrorResponse 
import org.springframework.http.HttpStatus 
import org.springframework.http.ResponseEntity 

import java.util.function.Supplier 

@JsonView(ResponseWrapper.class)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE)
class ResponseWrapper<T> {

    private T responseObject 
    private ServiceErrorResponse error 

    ResponseWrapper<T> responseObject(@SuppressWarnings("OptionalUsedAsFieldOrParameterType") Optional<T> par) {
        resetMessageStatus() 
        return responseObject(par.orElse(null)) 
    }

    ResponseWrapper<T> responseObject(T responseObject) {
        resetMessageStatus() 
        this.responseObject = responseObject 
        return this 
    }

    T getResponseObject() {
        return responseObject
    }
    private void resetMessageStatus() {
        this.error = null 
    }

    ResponseWrapper<T> error(ServiceErrorResponse error) {
        this.error = error 
        return this 
    }

    ResponseEntity<ResponseWrapper<T>> wrapToResponseEntity() {
        return ResponseEntity.status(error? error.getStatus(): HttpStatus.OK ).body(this)
    }

}
