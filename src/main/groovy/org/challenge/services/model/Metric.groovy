package org.challenge.services.model

import io.swagger.annotations.ApiModel

/**
 * Describes used in request metrics o evaluate with their representation names and database names
 */
@ApiModel
 enum Metric {

      CLICKS("Clicks", "Clicks"),
      IMPRESSIONS("Impressions", "Impressions"),
      CTR("CTR", Constants.SELECT_CTR);
   // ALL("All", Constants.ALL_SQL);

      final private String name
      final private String sql

      Metric(String name, String sql){
        this.name = name
        this.sql = sql
      }

     String getName() {
         return name
     }

     String getSql() {
         return sql
     }

}

interface Constants{
    String SELECT_CTR = "CAST(${Metric.CLICKS.getSql()} AS DECIMAL)/ NULLIF(${Metric.IMPRESSIONS.getSql()}, 0)"
 //  String ALL_SQL= Metric.values().collect {m->m.sql}.join(",")
}
