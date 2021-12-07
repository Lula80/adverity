package org.challenge.rest.controller;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.validation.Valid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.challenge.rest.req.GetGroupedMetricRq;
import org.challenge.rest.req.GetMetricsRq;
import org.challenge.rest.req.Views;
import org.challenge.rest.req.validaion.RequestValidationGroups;
import org.challenge.rest.resp.ResponseWrapper;
import org.challenge.services.AdCalcService;
import org.challenge.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping(Constants.REST.APP_REQ_URI)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AdController {

    AdCalcService adCalcService;
    Rest2DtoMappingService mappingService;

    @PostMapping(Constants.REST.METRIC_FILTERD)
    @ApiOperation(notes = "clicks, impressions, CTR = clicks /impressions", value = "getMetricsFilteredBy()")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad Request") })
    Object getMetricsFiltered(@RequestBody  @JsonView(Views.FilterView.class) @Validated(RequestValidationGroups.FilterGroup.class) GetMetricsRq req){
        return ResponseEntity.of(Optional.ofNullable(
                adCalcService.findMetricsBy(mappingService.mapToNonNullDimensions(req))));
    }
    @PostMapping(Constants.REST.CTR_AGGR)
    @ApiOperation(notes = "CTR = clicks /impressions", value = "getAggregatedCtr()")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad Request") })
    ResponseEntity<String> getCtr(@RequestBody @Validated(RequestValidationGroups.AggregatedGroup.class) GetMetricsRq req){
        return ResponseEntity.of(Optional.ofNullable(
                adCalcService.getCtr(mappingService.mapToNonNullDimensions(req))));
    }

    @PostMapping(Constants.REST.CLICKS_AGGR)
    @ApiOperation(notes = "Clicks aggregated/filtered", value = "getAggregatedClicks()")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad Request") })
    ResponseEntity<String> getClick(@RequestBody @Validated(RequestValidationGroups.AggregatedGroup.class) GetMetricsRq req){
        return ResponseEntity.of(Optional.ofNullable(
                adCalcService.getClick(mappingService.mapToNonNullDimensions(req))));
    }

    @PostMapping(Constants.REST.IMPRESSIONS_AGGR)
    @ApiOperation(notes = "Impressions aggregated/filtered", value = "getAggregatedImpressions()")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad Request") })
    ResponseEntity<String> getImpression(@RequestBody @Validated(RequestValidationGroups.AggregatedGroup.class) GetMetricsRq req){
        return ResponseEntity.of(Optional.ofNullable(
                adCalcService.getImpression(mappingService.mapToNonNullDimensions(req))));
    }

    @PostMapping(Constants.REST.METRIC_GROUPED)
    @ApiOperation(notes = "Clicks grouped", value = "getMetricGrouped()")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad Request") })
    Object getMetricGrouped(@RequestBody @Valid GetGroupedMetricRq req){
        return new ResponseWrapper<>().responseObject(
                adCalcService.getMetricGroupedBy(mappingService.mapToNonNullDimensions(req),
                        Optional.ofNullable(req.getHaving())))
                .wrapToResponseEntity();

    }
}
