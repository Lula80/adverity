package org.challenge.services;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.challenge.dao.AdConnection;
import org.challenge.dao.AdItem;
import org.challenge.dao.AdRepository;
import org.challenge.rest.req.MetricRange;
import org.challenge.rest.resp.AdGroup;
import org.challenge.services.model.*;
import org.challenge.utils.Constants;
import org.jetbrains.annotations.TestOnly;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Log4j2
public class AdPersistenceService implements AdRepository {

  private AdConnection connection;

  @Override
  public  String findImpressionBy( List<Dimension> dimensions, String aggregator){
    return findMetricBy(dimensions, Metric.IMPRESSIONS.getSqlName(), AdRepository.Aggregator.valueOf(aggregator));
  }

  @Override
  public  String findCtrBy( List<Dimension> dimensions, String aggregator){
    return findMetricBy(dimensions, Metric.CTR.getSqlName(), AdRepository.Aggregator.valueOf(aggregator));
  }

  @Override
  public  String findClickBy( List<Dimension> dimensions, String aggregator){
    return findMetricBy(dimensions, Metric.CLICKS.CLICKS.getSqlName(), AdRepository.Aggregator.valueOf(aggregator));
  }

  @Override
  public List<AdGroup> findMetricGroupedBy(DimensionsDto dto, Optional<MetricRange> havingMetric){
    List<AdGroup> selected = new ArrayList<>();
    selectGroupedFromDB(dto, selected, havingMetric);
    return selected;
  }
  @Override
  @TestOnly
  public List<AdItem> findAll(){
      String sql = "SELECT * "+FROM_CLAUSE;
      List<AdItem> selected = new ArrayList<>();
      try {
        connection.openConnection();
        Statement statement  = connection.createStatement();
        ResultSet rs    = statement.executeQuery(sql);
        while (rs.next()) {
          selected.add(sqlResultToDto(rs));
        }
        statement.close();
        connection.closeConnection();
      } catch (SQLException e) {
        log.error(e.getMessage());
      }
      return selected;
  }

  private AdItem sqlResultToDto(ResultSet rs) throws SQLException {
      return AdItem.builder()
           .clicks(rs.getInt(Metric.CLICKS.getSqlName()))
           .impressions(rs.getInt(Metric.IMPRESSIONS.getSqlName()))
           .datasource(rs.getString(Constants.Dimensions.StringDimensions.DATASOURCE))
           .campaign(rs.getString(Constants.Dimensions.StringDimensions.CAMPAIGN))
           .daily(rs.getDate(Constants.Dimensions.TimeDimensions.DAILY)).build();
  }

  private AdGroup sqlResultToGroupDto(ResultSet rs, String metric, List<Dimension> dimensions) throws SQLException {
    AdGroup ret = new AdGroup();
    ret.addGroup(metric, rs.getString(AGGREGATE));
    for (Dimension dim : dimensions) {
        ret.addGroup(dim.getName(), rs.getString(dim.getName()));
    }
    return ret;
  }

  private String findMetricBy(List<Dimension> dimensions, String metric, Aggregator aggregator){
      String query = getStatementFiltered(dimensions, metric, aggregator.getSqlValue());
      String selected = "0.0";
      try {
        connection.openConnection();
        PreparedStatement statement = connection.prepareStatement(query);

        setParameters(dimensions, statement);
        ResultSet rs = statement.executeQuery();
        if(rs.next())
          selected = rs.getString(AGGREGATE);
        statement.close();
        connection.closeConnection();
      } catch (SQLException e) {
        log.error(e.getMessage());
      }
      return selected;
  }

  private void selectGroupedFromDB(DimensionsDto dto, List<AdGroup> selected, Optional<MetricRange> minMax) {
      try {
        String query = getStatementGrouped(dto, minMax);
        connection.openConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
          selected.add(sqlResultToGroupDto(rs, dto.getMetric().getName(), dto.getDimensions()));
        }
        statement.close();
        connection.closeConnection();
      } catch (SQLException e) {
        log.error(e.getMessage());
      }
  }

  private void selectFromDB(List<Dimension> dimensions, Metric metric, List<String> selected) {
      try {
        String query = getStatement(dimensions, metric.getName());

        connection.openConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        setParameters(dimensions, statement);
        ResultSet rs = statement.executeQuery();
        while (rs.next())
          selected.add(rs.getString("METRIC"));

        statement.close();
        connection.closeConnection();
      } catch (SQLException e) {
        log.error(e.getMessage());
      }
  }

  @Override
  public List<String> findMetricsBy(List<Dimension> dimensions, Metric metric){
      List<String> selected = new ArrayList<>();
      selectFromDB(dimensions, metric, selected);
      return selected;
  }

  private void setParameters(List<Dimension> dimensions, PreparedStatement statement) throws SQLException {
      int j = 1;
      for (int i = 0; i < dimensions.size(); i++) {
        if(dimensions.get(i) instanceof TimeDimension) {
          statement.setDate(j++, Date.valueOf(((TimeDimension) dimensions.get(i)).getFrom()));
          statement.setDate(j++, Date.valueOf(((TimeDimension) dimensions.get(i)).getTo()));
        }
        if(dimensions.get(i) instanceof StringDimension){
          statement.setString(j++, ((StringDimension) dimensions.get(i)).getValue());
        }
      }
  }

  private String getStatement(List<Dimension> dimensions, String metric) {

    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("SELECT "+  metric + " AS METRIC");
    buildFilterWhereClause(dimensions, queryBuilder);
    return queryBuilder.toString();
  }

  private String getStatementFiltered(List<Dimension> dimensions, String metric, String aggregator ) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("SELECT " + aggregator +"("+ metric +") AS "+ AGGREGATE);
    buildFilterWhereClause(dimensions, queryBuilder);
    return queryBuilder.toString();
  }

  private void buildFilterWhereClause(List<Dimension> dimensions, StringBuilder queryBuilder) {
    List<String> whereCause = new ArrayList<String>();
    for (Dimension dim: dimensions){
      if(dim instanceof TimeDimension){
        whereCause.add(dim.getName()+ " BETWEEN ? AND ?");
      }
      if(dim instanceof StringDimension){
        whereCause.add(dim.getName() +" = ?");
      }
    }
    queryBuilder.append(FROM_CLAUSE);
    if (whereCause.size() > 0) {
      queryBuilder.append(" WHERE " + StringUtils.join(whereCause, " AND "));
    }
    queryBuilder.append(";");
  }
  private String getStatementGrouped(DimensionsDto dto, Optional<MetricRange> havingRange) {
    String column = AdRepository.Aggregator.valueOf(dto.getAggregate()).getSqlValue()+"("+
            dto.getMetric().getSqlName() +")";
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("SELECT " + column+ " AS "+ AGGREGATE+", "
            + StringUtils.join(dto.getDimensions(), " , "));
    queryBuilder.append(FROM_CLAUSE);
    queryBuilder.append(" GROUP BY "+StringUtils.join(dto.getDimensions(), " , "));
    havingRange.ifPresent(m->queryBuilder.append(" HAVING "+ column + m.getSqlCondition()));
    return queryBuilder.toString();
  }

}
