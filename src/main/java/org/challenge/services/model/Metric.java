package org.challenge.services.model;

import org.challenge.dao.AdRepository;

/**
 * Describes used in request metrics o evaluate with their representation names and database names
 */
public enum Metric {
  CLICKS("Clicks", "Clicks"),
  IMPRESSIONS("Impressions", "Impressions"),
  CTR("CTR", AdRepository.SELECT_CTR);

  String name;

  String sqlName;
  Metric(String v, String sql){
    name = v;
    sqlName = sql;
  }

  public boolean isInteger() {

    return name.equalsIgnoreCase("Clicks") || name.equalsIgnoreCase("Impressions");
  }

  public String getName() {
    return name;
  }

  public String getSqlName() {
    return sqlName;
  }
}
