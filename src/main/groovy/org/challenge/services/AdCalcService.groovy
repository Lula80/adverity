package org.challenge.services

import com.fasterxml.jackson.annotation.JsonAnyGetter
import groovy.transform.TupleConstructor 
import org.challenge.rest.req.MetricRange 
import org.challenge.rest.resp.AdGroup 
import org.challenge.services.model.DimensionsDto
import org.springframework.beans.factory.annotation.Autowired 
import org.springframework.stereotype.Service 

@Service
@TupleConstructor(includeFields = true)
class AdCalcService {

    @Autowired
    private final AdPersistenceService connection 

    /**
     *
     * @param dto contains required aggregate function for SQL GROUP BY operator
     *            and optional list of dimensions for WHERE operator
     * @return result of applying aggregation function over metric CTR/clicks/impressions
     */
    String getMetricAggregated(DimensionsDto dto){
        return connection.findAggregatedMetricBy(dto) 
    }
    /**
     * This function can be used for further processing with streams API
     * @param dto optional list of dimensions for WHERE operator
     * @return result of filtering
     */
    List<? extends Serializable> findMetricsBy(DimensionsDto dto){
        if(dto && !dto.getDimensions() )
            return connection.findAll()
        return connection.findMetricsBy(dto.getDimensions(), dto.getMetric()) 
    }

    /**
     *
     * @param dto contains required metric to aggregate result groups on,
     *            required aggregate function for SQL GROUP BY operator
     *            and optional list of dimensions for WHERE operator
     *  @param minMax metrics range to be used in SQL HAVING clause
     * @return result of applying aggregation function over passed through dto metric
     */
    @JsonAnyGetter
    Map<String, Object> getMetricGroupedBy(DimensionsDto dto, Optional<MetricRange> minMax) {
        List<AdGroup> groups = connection.findMetricGroupedBy(dto, minMax)
        Map jsonObject = new HashMap(2)
        jsonObject.put("groups", groups)
        jsonObject.put ("count" , groups.size())
        return jsonObject 
    }
}
