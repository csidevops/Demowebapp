package com.example;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.sql.Timestamp;

@WebServlet("/InsertApplicationIdServlet")
public class InsertApplicationIdServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Retrieve data from the form
        String appid = request.getParameter("appId");
        String appname = request.getParameter("appname");
        String appdesc = request.getParameter("appdesc");
        String techstack = request.getParameter("techstack");
        String srclocation = request.getParameter("srclocation");        
        String cptools = request.getParameter("cptools");
        String scmtools = request.getParameter("scmtools");
        String citools = request.getParameter("citools");
        String buildtools = request.getParameter("buildtools");
        String unittesttools = request.getParameter("unittesttools");
        String cqtools = request.getParameter("cqtools");
        String artifactools = request.getParameter("artifactools");
        String fttools = request.getParameter("fttools");
        String pttools = request.getParameter("pttools");
        String relpipelinetools = request.getParameter("relpipelinetools");
        String coltools = request.getParameter("coltools");
        

        // Connection details for Azure SQL Database
        String jdbcUrl = "jdbc:sqlserver://selfservicedevops.database.windows.net:1433;databaseName=selfservicedevops";
        String username = "csidevops";
        String password = "xxxxxxxxx";

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
                String getMaxSsdIdQuery = "SELECT MAX(SSD_ID) AS MaxSsdId FROM dbo.SSD_REQUESTS";
                try (PreparedStatement getMaxSsdIdStatement = connection.prepareStatement(getMaxSsdIdQuery)) {
                    ResultSet resultSet = getMaxSsdIdStatement.executeQuery();
                    int nextSsdId = 1; // Default value if the table is empty
                    if (resultSet.next()) {
                        int maxSsdId = resultSet.getInt("MaxSsdId");
                        nextSsdId = maxSsdId + 1;
                    }

                    String insertSql = "INSERT INTO dbo.SSD_REQUESTS (SSD_ID, APP_ID, APP_NAME, APP_DESC, TECH_STACK, PLANNING_TOOL, SCM_TOOL, BUILD_TOOL, CI_TOOL, UNIT_TEST_TOOL, CODE_QUALITY_TOOL, ARTIFACT_TOOL, FUNCTIONAL_TEST_TOOL, LOAD_TEST_TOOL, CD_TOOL, RECORD_STATUS, RECORD_CREATED, RECORD_UPDATED) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement insertStatement = connection.prepareStatement(insertSql)) {
                        java.util.Date currentDate = new java.util.Date();
                        Timestamp currentDateTimeSql = new Timestamp(currentDate.getTime());
                        insertStatement.setInt(1, nextSsdId);
                        insertStatement.setString(2, appid);
                        insertStatement.setString(3, appname);
                        insertStatement.setString(4, appdesc);
                        insertStatement.setString(5, techstack);
                        insertStatement.setString(6, cptools);
                        insertStatement.setString(7, scmtools);
                        insertStatement.setString(8, buildtools);
                        insertStatement.setString(9, citools);
                        insertStatement.setString(10, unittesttools);
                        insertStatement.setString(11, cqtools);
                        insertStatement.setString(12, artifactools);
                        insertStatement.setString(13, fttools);
                        insertStatement.setString(14, pttools);
                        insertStatement.setString(15, relpipelinetools);  
                        insertStatement.setInt(16, 0);
                        insertStatement.setTimestamp(17, currentDateTimeSql);
                        insertStatement.setTimestamp(18, currentDateTimeSql);
                        insertStatement.executeUpdate();
                    }

                    System.out.println("Data inserted successfully into the ssdrequests table");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Database connection error");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("SQL Server Driver not found");
        }

        response.sendRedirect("thankyou.html");
    }
}
