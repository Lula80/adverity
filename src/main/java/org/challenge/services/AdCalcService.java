package org.challenge.services;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.challenge.rest.req.MetricRange;
import org.challenge.rest.resp.AdGroup;
import org.challenge.services.model.DimensionsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AdCalcService {

    AdPersistenceService connection;

    /**
     *
     * @param dto contains required aggregate function for SQL GROUP BY operator
     *            and optional list of dimensions for WHERE operator
     * @return result of applying aggrgation function over metric Impressions
     */
    public String getImpression(DimensionsDto dto){
        return connection.findImpressionBy(dto.getDimensions(), dto.getAggregate());
    }

    /**
     *
     * @param dto contains required aggregate function for SQL GROUP BY operator
     *            and optional list of dimensions for WHERE operator
     * @return result of applying aggrgation function over metric Clicks
     */
    public String getClick(DimensionsDto dto){
        return connection.findClickBy(dto.getDimensions(), dto.getAggregate());
    }
    /**
     *
     * @param dto contains required aggregate function for SQL GROUP BY operator
     *            and optional list of dimensions for WHERE operator
     * @return result of applying aggrgation function over metric CTR
     */
    public String getCtr(DimensionsDto dto){
        return connection.findCtrBy(dto.getDimensions(), dto.getAggregate());
    }
    /**
     * This function can be used for further processing with streams API
     * @param dto optional list of dimensions for WHERE operator
     * @return result of filtering
     */
    public List<? extends Serializable> findMetricsBy(DimensionsDto dto){
        if(null == dto || null == dto.getDimensions() && null == dto.getMetric())
            return connection.findAll();
        return connection.findMetricsBy(dto.getDimensions(), dto.getMetric());
    }

    /**
     *
     * @param dto contains required metric to aggrgate result groups on,
     *            required aggregate function for SQL GROUP BY operator
     *            and optional list of dimensions for WHERE operator
     *  @param minMax metrics range to be used in SQL HAVING clause
     * @return result of applying aggregation function over passed through dto metric
     */
    public Object getMetricGroupedBy(DimensionsDto dto, Optional<MetricRange> minMax) {
        List<AdGroup> groups = connection.findMetricGroupedBy(dto, minMax);
        Map jsonObject = new HashMap(2);
        jsonObject.put("groups", groups);
        jsonObject.put ("count" , groups.size() );
        return jsonObject;
    }
}
