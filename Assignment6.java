/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csc410;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import org.apache.derby.jdbc.EmbeddedDriver;

public class Assignment6 
{
   
    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException, IOException {
        
        FileWriter writer = new FileWriter("/Users/mercedessacca/Desktop/Assignment6File.txt");
        //text file to write data too
        PrintWriter FileWrite = new PrintWriter(writer);
        
        
        Class<?> c = Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        EmbeddedDriver d = (EmbeddedDriver) c.newInstance();
        //url, user, password
        try (Connection conn = DriverManager.getConnection(
                "jdbc:derby:/Users/mercedessacca/Documents/Database/BirtSample",
                "root", null)) {

            Scanner kb = new Scanner(System.in);
            System.out.print("Enter Table Name");
            String TableName = kb.next();
            //put classicmodels because was not sure if it was required
            String query = ("Select * from classicmodels." + TableName);
            PreparedStatement stmt = conn.prepareStatement(query);
            
            ResultSetMetaData md = stmt.getMetaData();
            int count = md.getColumnCount();
          
            //System.out.println(count);
            
            String[] ColumnNames = new String[count];
            //store all column names
            String[] ColumnType = new String[count];
            //store all column types
            for (int i = 1; i <= count; i++)
            {
                ColumnNames[i - 1] = md.getColumnName(i);
                ColumnType[i - 1] = md.getColumnTypeName(i);
            }
            
            for(int i = 0; i < ColumnNames.length - 1; i++)
            {
                FileWrite.write(ColumnNames[i].toString() + ", ");
                //write column names to file
            }
            FileWrite.write(ColumnNames[ColumnNames.length - 1].toString());
            //write last column name to file
            FileWrite.println();
            
            for(int i = 0; i < ColumnType.length - 1; i++)
            {
                FileWrite.write(ColumnType[i].toString() + ", ");
                //write column types to file
            }
            FileWrite.write(ColumnType[ColumnType.length - 1].toString());
            FileWrite.println();
            
            ResultSet rs = stmt.executeQuery();
            int RowsCounter = 0;
            HashMap<Integer, String[]> TableRows = new HashMap<Integer, String[]>();
            //the integer is the row number
            //string is the data info
            
            while(rs.next())
            { 
                String[] RowsColumns = new String[count];
                for(int i = 0; i < RowsColumns.length; i++)
                {    
                    
                    switch (ColumnType[i])
                    {
                            case "INTEGER":
                            case "SMALLINT":
                                    int x = rs.getInt(ColumnNames[i]);
                                    RowsColumns[i] = Integer.toString(x);
                                break;
                            case "VARCHAR":
                            case "LONGVARCHAR":
                                    String x1 = rs.getString(ColumnNames[i]);
                                    RowsColumns[i] = x1;
                                break;
                            case "DOUBLE":
                                    double x2 = rs.getDouble(ColumnNames[i]);
                                    RowsColumns[i] = Double.toString(x2);
                                break;
                            case "DATE":
                                    Date x3 = rs.getDate(ColumnNames[i]);
                                    RowsColumns[i] = convertStringToDate(x3);
                                break;
                            case "BLOB":
                            case "CLOB":
                                RowsColumns[i] = "BLOB OR CLOB";
                                break;              
                    }
                }
                
                TableRows.put(RowsCounter, RowsColumns);
                RowsCounter++;
            }
            
            for(int i = 0; i <= TableRows.size() - 1; i++)
            {
                String[] Rows = TableRows.get(i);
                for(int y = 0; y < Rows.length - 1; y++)
                {
                    System.err.print(Rows[y] + " ,");
                    //write data info 
                    FileWrite.write(Rows[y] + " ,");
                }
                System.err.println(Rows[Rows.length - 1]);
                //write last data info
                FileWrite.write(Rows[Rows.length - 1]);
                FileWrite.println();
            }
            FileWrite.close();
            
            
            
            
        }
    }
    
 public static String convertStringToDate(Date indate)
    {
        String dateString = null;
        SimpleDateFormat sdfr = new SimpleDateFormat("yyyy-mm-dd");
        try
        {
            dateString = sdfr.format(indate);
        }
        catch (Exception ex)
        {
            System.out.println(ex);
        }
        return dateString;
    }
}
