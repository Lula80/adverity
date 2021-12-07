package org.challenge.dao;

import java.io.*;
import java.sql.*;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.sql.SQLException;

@Service
@Log4j2
public class AdConnection{
    private static String url = "jdbc:h2:mem:todo";
    private Connection connection = null;

    static
    {
        try
        {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e)
        {
            throw new ExceptionInInitializerError(e);
        }
    }

    public AdConnection() {
        try {
            openConnection();
            createTable();
            insertFromCSV();
        }catch (SQLException ex){
            log.error(ex);
        }

    }
    public void openConnection() throws SQLException
    {
        if (connection == null || connection.isClosed())
        {
            connection = DriverManager.getConnection(url,"sa", "");
        }
    }

    // Closes connection to database
    public void closeConnection() throws SQLException
    {
        if (connection != null || !connection.isClosed())
            connection.close();
    }

    private void createTable()
    {
        try
        {
            final Statement statement = connection.createStatement();

            statement.executeUpdate("SET MODE MySQL;CREATE TABLE IF NOT EXISTS ADVERT"
                    // + " id integer PRIMARY KEY,\n"
                    + "(Datasource     TEXT,"
                    + " Campaign      TEXT,"
                    + " Daily       DATE,"
                    + " Clicks       INTEGER,"
                    + " Impressions    INTEGER);");

        } catch (SQLException e)
        {
            e.getMessage();
        }
    }

    private void insertFromCSV()
    {
        try
        {
            InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("PIxSyyrIKFORrCXfMYqZBI.csv");
            BufferedReader reader = new BufferedReader(new InputStreamReader(resourceAsStream));
            CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build();

            PreparedStatement statement = connection.prepareStatement("INSERT INTO ADVERT(Datasource,Campaign,Daily,Clicks,Impressions) "
                    + "VALUES(?,?,?,?,?);");

            String[] nextRecord;
            while ((nextRecord = csvReader.readNext()) != null)
            {

                if (!Arrays.asList(nextRecord).contains(""))
                {
                    statement.setString(1, nextRecord[0]);
                    statement.setString(2, nextRecord[1]);
                    statement.setDate(3, Date.valueOf(LocalDate.parse(nextRecord[2], DateTimeFormatter.ofPattern("MM/dd/yy"))));
                    statement.setInt(4, Integer.parseInt(nextRecord[3]));
                    statement.setInt(5, Integer.parseInt(nextRecord[4]));;
                    statement.executeUpdate();
                }
            }

        } catch (SQLException | IOException | CsvValidationException e)
        {
           log.error(e.getMessage());
        }
    }

    public PreparedStatement prepareStatement(String query) throws SQLException {
        return connection.prepareStatement(query);
    }

  public Statement createStatement() throws SQLException {
      return connection.createStatement();
  }
}
