package my_util;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.*;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import model.Course;

public class Database {

    // private Connection connect;
    private static final String DBURL = "jdbc:mysql:///school_management_db";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root123";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DBURL, USERNAME, PASSWORD);
    }

    public <T> T executeQuery(String sqlQuery, ParamSetter paramSetter, ResultProcessor<T> resultSetProcessor) {

        try (
                // help with automatic close of the database connection
                Connection con = getConnection();
                PreparedStatement stm = con.prepareStatement(sqlQuery);) {
            // apply parameter on the Prepared statement
            if (paramSetter != null) {
                paramSetter.setParams(stm);
            }
            if (resultSetProcessor != null) {
                ResultSet results = stm.executeQuery(); // Execute the query
                return resultSetProcessor.process(results);
            }
            // Otherwise it's in INSERT, UPDATE, or DELETE operation
            else {
                int rows = stm.executeUpdate();

                // return boolean if the number of rows affected
                return (T) Boolean.valueOf(rows > 0);
            }
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
            return (T) Boolean.valueOf(false);
        }
        
    }
    
    public void executeUpdate(String sql, SQLConsumer<PreparedStatement> paramSetter) {
        try (
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            if (paramSetter != null) {
                paramSetter.accept(stmt);
            }
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public interface ParamSetter{
        //Method to set parameters on the PreparedStatement
        // This will be implemented  in the model class
        void setParams(PreparedStatement stm) throws SQLException;
    }
     public interface ResultProcessor<T>{

        T process(ResultSet results)throws SQLException;
         
     }
     
     public interface SQLConsumer<T> {
    	    void accept(T t) throws Exception;
    	}
}









    




