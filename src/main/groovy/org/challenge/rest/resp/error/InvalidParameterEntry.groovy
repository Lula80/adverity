package org.challenge.rest.resp.error;

import io.swagger.annotations.ApiModelProperty
import groovy.transform.builder.Builder

@Builder
class InvalidParameterEntry {

    @ApiModelProperty(required = true, example = "age", value = "Name of the field in request with validation error.")
    String name
    @ApiModelProperty(required = true, example = "must be a positive integer", value = "Validation message")
    String reason
}
