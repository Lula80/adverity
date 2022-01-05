package org.challenge.services

import groovy.sql.GroovyResultSet
import groovy.transform.TupleConstructor
import org.challenge.dao.AdDbConnection
import org.challenge.dao.AdItem
import org.challenge.dao.AdRepository
import org.challenge.rest.req.MetricRange
import org.challenge.rest.resp.AdGroup
import org.challenge.rest.resp.error.AdverityServiceError
import org.challenge.rest.resp.error.ServiceException
import org.challenge.services.model.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

import java.sql.*

@Repository
@TupleConstructor(includeFields = true)
class AdPersistenceService implements AdRepository {

    @Autowired
    private final AdDbConnection connection 

    @Override
    String findAggregatedMetricBy(DimensionsDto dto){
        return findMetricBy(dto.dimensions, dto.metric.getSql(),
                AdRepository.Aggregator.valueOf(dto.getAggregate()))
    }

    @Override
    List<AdGroup> findMetricGroupedBy(DimensionsDto dto, Optional<MetricRange> havingMetric){
        return selectGroupedFromDB(dto, havingMetric)
    }

  @Override
  List<AdItem> findAll(){
      String sql = "SELECT * FROM $TABLE"

      try {
         List<ResultSet> rs = connection.rows(sql)
          rs.collect {row -> sqlResultToDto(row)}
      } catch (SQLException e) {
          throw new ServiceException(AdverityServiceError.SQL_EXCEPTION, e)
      }
  }

  private def sqlResultToDto = { rs ->
      AdItem.builder()
      .clicks(rs.CLICKS)
      .impressions(rs.IMPRESSIONS)
      .datasource(rs.DATASOURCE)
      .campaign(rs.CAMPAIGN)
      .daily(rs.DAILY).build()
  }

  private def sqlResultToGroupDto = {resultSet, metric, dimensions ->
      AdGroup ret = new AdGroup()
      ret.addGroup(metric, resultSet[0]) 
      int i = 1 
      dimensions.each { dim ->
            ret.addGroup(dim.getName(), resultSet[i++]) 
      }
      return  ret 
  }

  private String findMetricBy(List<Dimension> dimensions, String metric, Aggregator aggregator){
      String query = getStatementAggregated(dimensions, metric, aggregator.getSqlValue())
      String selected = '0.0' 
      try {
         List<GroovyResultSet> rs = connection.paramQuery(query, getParameters(dimensions)) 
         selected = "$aggregator $metric = ${rs[0][0]}"

      } catch (SQLException e) {
          throw new ServiceException(AdverityServiceError.SQL_EXCEPTION)
      }
      return selected 
  }

  private  List<AdGroup> selectGroupedFromDB(DimensionsDto dto, Optional<MetricRange> minMax) {
      try {
          String query = getStatementGrouped(dto, minMax)
          List<ResultSet> rs = connection.rows(query)
          rs.collect {row ->
              sqlResultToGroupDto(row, "${dto.getAggregate()} ${dto.getMetric().getName()}", dto.getDimensions())
          }
      } catch (SQLException e) {
            println e.getMessage()
      }
  }

  private  List<String> selectFromDB ( List<Dimension> dimensions, Metric metric) {
      try {
            String query = getStatement(dimensions, metric.getSql())
            List<GroovyResultSet> rs  = connection.paramQuery(query, getParameters(dimensions))
            rs.collect{row -> row[0]}
      } catch (SQLException e) {
            println e.getMessage()
      }
  }

  @Override
   List<String> findMetricsBy(List<Dimension> dimensions, Metric metric){
      return selectFromDB(dimensions, metric)
  }

  private List<Object> getParameters(List<Dimension> dimensions) throws SQLException {

      List<Object> params = new ArrayList<>(dimensions.size())
      dimensions.collect( {dim->
          if(dim instanceof TimeDimension) {
              def date = ((TimeDimension) dim)
              params.add(Date.valueOf(date.getFrom())) 
              params.add(Date.valueOf(date.getTo())) 
          }
          if(dim instanceof StringDimension){
              params.add(((StringDimension) dim).getValue()) 
          }
      })
      return params
  }

  private String getStatement(List<Dimension> dimensions, String metric) {
      return "SELECT $metric AS METRIC FROM $TABLE ${dimensions? buildFilterWhereClause(dimensions):""}"
  }

  private String getStatementAggregated(List<Dimension> dimensions, String metric, String aggregator ) {
      return "SELECT $aggregator($metric) AS $AGGREGATE FROM $TABLE ${dimensions? buildFilterWhereClause(dimensions):""}"
  }

  private GString buildFilterWhereClause(List<Dimension> dimensions) {
    List<GString> whereParams = new ArrayList<GString>() 
      dimensions.each {dim->
          if(dim instanceof TimeDimension){
            whereParams.add("${dim.getName()} BETWEEN ? AND ?")
          }
          if(dim instanceof StringDimension){
            whereParams.add("${dim.getName()} = ?")
          }
    }
    return  " WHERE ${whereParams.join(" AND ")}"

  }

  private String getStatementGrouped(DimensionsDto dto, Optional<MetricRange> havingRange) {

      def column = "${AdRepository.Aggregator.valueOf(dto.getAggregate()).getSqlValue()} (${dto.getMetric().getSql()})"
      def having = havingRange.isPresent()?"HAVING ${havingRange.get().getSqlCondition(column)}":""
      def dims = dto.getDimensions().join(" , ")
      return  """SELECT $column AS $AGGREGATE, $dims
                FROM $TABLE GROUP BY $dims $having"""
  }

}
