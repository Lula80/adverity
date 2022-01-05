package org.challenge.rest.controller 

import com.fasterxml.jackson.annotation.JsonView
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse 
import io.swagger.annotations.ApiResponses
import org.springframework.beans.factory.annotation.Autowired 

import javax.validation.Valid 
import org.challenge.rest.req.GetGroupedMetricRq 
import org.challenge.rest.req.GetMetricsRq 
import org.challenge.rest.req.Views 
import org.challenge.rest.req.validaion.RequestValidationGroups 
import org.challenge.rest.resp.ResponseWrapper 
import org.challenge.services.AdCalcService 
import org.challenge.utils.Constants 
import org.springframework.http.ResponseEntity 
import org.springframework.validation.annotation.Validated 
import org.springframework.web.bind.annotation.* 

@RestController
@RequestMapping(value = Constants.REST.APP_REQ_URI)
 class AdController {
    @Autowired
    private final AdCalcService adCalcService
    @Autowired
    private final Rest2DtoMappingService mappingService

    @PostMapping(Constants.REST.METRIC_FILTERED)
    @ApiOperation(notes = "clicks, impressions, CTR = clicks /impressions", value = "getMetricsFilteredBy()")
    @ApiResponses(value =
            @ApiResponse(code = 400, message = "Bad Request") )
    Object getMetricsFiltered(@RequestBody  @JsonView(Views.FilterView.class)
                              @Validated(RequestValidationGroups.FilterGroup.class) GetMetricsRq req){
        return ResponseEntity.of(Optional.ofNullable(
                adCalcService.findMetricsBy(mappingService.mapToNonNullDimensionsFiltered(req))))
    }

    @PostMapping(Constants.REST.METRIC_AGGREGATED)
    @ApiOperation(notes = "CTR/ clicks /impressions aggregated", value = "getAggregated()")
    @ApiResponses(value =
            @ApiResponse(code = 400, message = "Bad Request") )
    ResponseEntity<String> getMetricAggregated(@RequestBody @JsonView(Views.AggregatedView.class)
              @Validated(RequestValidationGroups.AggregatedGroup.class) GetMetricsRq req){
        return ResponseEntity.of(Optional.ofNullable(
                adCalcService.getMetricAggregated(mappingService.mapToNonNullDimensions(req)))) 
    }

    @PostMapping(Constants.REST.METRIC_GROUPED)
    @ApiOperation(notes = "Clicks grouped", value = "getMetricGrouped()")
    @ApiResponses(value =
            @ApiResponse(code = 400, message = "Bad Request") )
    ResponseEntity<Map<String, Object>> getMetricGrouped(@RequestBody @Valid GetGroupedMetricRq req){
        return new ResponseWrapper<>().responseObject(
                adCalcService.getMetricGroupedBy(mappingService.mapToNonNullDimensions(req),
                        Optional.ofNullable(req.getHaving())))
                .wrapToResponseEntity()
    }

}
