package org.challenge.dao;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import groovy.util.logging.Log;
import org.springframework.stereotype.Service;

import groovy.sql.*

import java.sql.Date
import java.sql.SQLException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.logging.Level;

@Service
@Log
@GrabConfig(systemClassLoader=true)
//@Grab(group='com.h2database', module='h2', version='1.4.192')

class AdDbConnection {
  def sql = Sql.newInstance("jdbc:h2:./data/db", "sa", "sa", "org.h2.Driver")

  AdDbConnection() {
    try {
        createTable()
        insertFromCSV()
    }catch (SQLException ex){
      log.log(Level.SEVERE, ex.getMessage())
    }

  }

  private void createTable()
  {
    try
    {
      sql.execute("SET MODE MySQL")
      sql.execute("DROP TABLE ADVERT;")
      String createSql = "CREATE TABLE IF NOT EXISTS "+AdRepository.TABLE+
                    "( ID IDENTITY," +
                    "Datasource     VARCHAR(50),"+
                     "Campaign    VARCHAR(50),"+
                     "Daily       DATE,"+
                     "Clicks       INTEGER,"+
                     "Impressions    INTEGER)"
      sql.execute(createSql)

    } catch (SQLException ex)
    {
      log.log(Level.SEVERE, ex.getMessage())
    }
  }

  private void insertFromCSV()
  {
    try
    {
         new File("src/main/resources","PIxSyyrIKFORrCXfMYqZBI.csv").withReader {
           reader ->
         CSVReader csvReader = new CSVReaderBuilder(reader).build();
         insertValues csvReader, sql
    }

    } catch (SQLException | IOException | CsvValidationException ex)
    {
      log.log(Level.SEVERE, ex.message);
    }
  }

  private def insertValues = (csvReader, sql) ->{
    def columns = csvReader.readNext().join(", ")
    def insertSql = "INSERT INTO $AdRepository.TABLE ($columns) VALUES(?,?,?,?,?);"

    String[] nextRecord;

    while ((nextRecord = csvReader.readNext())) {
      sql.withBatch(100, insertSql) { stmt ->
        nextRecord[2] = Date.valueOf(LocalDate.parse(nextRecord[2], DateTimeFormatter.ofPattern("MM/dd/yy")))
        stmt.addBatch nextRecord
      }
    }
  }

  GroovyResultSet query(String sqlQuery) {
    sql.query(sqlQuery) { resultSet -> resultSet
      }
  }

  List<GroovyResultSet> paramQuery(String sqlQuery, List<Object> params) {
     sql.rows(sqlQuery, params)
  }

  List<GroovyResultSet> rows(String sqlQuery) {
    sql.rows(sqlQuery)
  }

}
