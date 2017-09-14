/*
 * BaseDBTestCase.java
 *
 * Created on Sep 7, 2007, 6:19:14 PM
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.db.test;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import org.dbunit.DatabaseTestCase;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.operation.DatabaseOperation;

/**
 *
 * @author db2admin
 */
public abstract class BaseDBTestCase extends DatabaseTestCase {

    protected IDatabaseConnection getConnection() throws Exception {
        Class driverClass = Class.forName("oracle.jdbc.driver.OracleDriver");
        Connection jdbcConnection = DriverManager.getConnection("jdbc:oracle:thin:@192.168.7.3:1521:one2many", "tump", "tump");
        return new DatabaseConnection(jdbcConnection);
    }
    
    public abstract String getDataSetPath();
        
    public abstract DatabaseOperation getDatabaseOperation() throws Exception;
    
    public abstract IDataSet getDataSet() throws Exception;

    public void setUp() throws Exception {
        try {
            getDatabaseOperation().execute(getConnection(), getDataSet());
        } catch (Exception e) {
            System.err.println("Database Setup Operation Failed: " + e.getMessage());
            throw new Exception(e);
        }
    }
    
    public void tearDown() throws Exception {
        try {
            DatabaseOperation.NONE.execute(getConnection(), getDataSet());
        } catch(Exception e) {
            
        }
    }
}
