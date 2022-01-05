package org.challenge.dao

import groovy.transform.builder.Builder;
import java.sql.Date;

import javax.persistence.*;

@Entity
@Builder
class AdItem implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id

    String datasource
    String campaign
    Date daily
    Integer clicks
    Integer impressions

}
