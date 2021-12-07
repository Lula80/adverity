package org.challenge.rest.resp;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import org.challenge.rest.resp.error.ServiceErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.function.Supplier;

@Data
@JsonView(ResponseWrapper.class)
public class ResponseWrapper<T> {

    private T responseObject;
    private ServiceErrorResponse error;

    public ResponseWrapper<T> responseObject(@SuppressWarnings("OptionalUsedAsFieldOrParameterType") Optional<T> par) {
        resetMessageStatus();
        return responseObject(par.orElse(null));
    }

    public ResponseWrapper<T> responseObject(T responseObject) {
        resetMessageStatus();
        this.responseObject = responseObject;
        return this;
    }

    private void resetMessageStatus() {
        this.error = null;
    }

    public ResponseWrapper<T> error(ServiceErrorResponse error) {
        this.error = error;
        return this;
    }

    public ResponseEntity<ResponseWrapper<T>> wrapToResponseEntity() {
        return ResponseEntity.status(error == null ? HttpStatus.OK : error.getStatus()).body(this);
    }

    public ResponseWrapper<T> statusErrorMessageIfNoResponse(Supplier<ServiceErrorResponse> supplier) {
        return statusErrorMessageIfNoResponse(HttpStatus.NOT_FOUND, supplier);
    }

    public ResponseWrapper<T> statusErrorMessageIfNoResponse(HttpStatus st, Supplier<ServiceErrorResponse> supplier) {
        if (responseObject == null ||
                isEmptyIterable() || isEmptyOptional()) {
            this.error = supplier.get();
        }
        return this;
    }

    private boolean isEmptyIterable() {
        return responseObject instanceof Iterable && !((Iterable) responseObject).iterator().hasNext();
    }

    private boolean isEmptyOptional() {
        return responseObject instanceof Optional && ((Optional) responseObject).isEmpty();
    }
}
