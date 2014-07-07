/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.umu.uikm.argbuilder.control.entities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import se.umu.uikm.argbuilder.control.DerbyConnect;

/**
 *
 * @author Esteban
 */
public class ProgramEntity {

    private int idProgram;
    private String URLProgram;
    
    private DerbyConnect connec = new DerbyConnect();

    public ProgramEntity(int idProgram) {
        this.idProgram = idProgram;
    }

    public ProgramEntity(String URLProgram) {
        this.URLProgram = URLProgram;
    }

    

    
    
    public int findIDProgramByURL(Connection conn){
                ResultSet rs = null;
        String URLFile = this.URLProgram;
        Statement s = null;
        ArrayList statements = new ArrayList(); // list of Statements, PreparedStatements
        int Id_Program=0;
        //String sql1 = "INSERT INTO PROGRAMS VALUES (DEFAULT)";
        
        String sql2="SELECT ID_PROGRAMS FROM USER1.PROGRAMS WHERE CAST(URL AS VARCHAR(128)) = '"+URLFile+"'";

        try {
            
            
            s = conn.createStatement();
            statements.add(s);
            //Check or veryfing  SELECTION
            /*
             We select the rows and verify the results.
             */
            rs = s.executeQuery(sql2);
            
            
            

            /* we expect the first returned column to be an integer (num),
             * and second to be a String (addr). Rows are sorted by street
             * number (num).
             *
             * Normally, it is best to use a pattern of
             *  while(rs.next()) {
             *    // do something with the result set
             *  }
             * to process all returned rows, but we are only expecting two rows
             * this time, and want the verification code to be easy to
             * comprehend, so we use a different pattern.
             */
            
            boolean failure = false;
            if (!rs.next() || !failure) {
                failure = true;
                reportFailure("No rows in ResultSet");
            }else{
                Id_Program = rs.getInt("ID_PROGRAMS");
                System.out.println("(OK) \t The ID of the program with the URL:"+this.URLProgram+ "  is:" + idProgram);
            }

            // COMMIT CHANGES
            conn.commit();
            System.out.println("(OK)\t Committed the transaction:"+sql2);

        } catch (SQLException sqle) {
            System.err.println("SQL Exception:" + sqle.getLocalizedMessage() + ", " + sqle.getSQLState());
        } finally {
            // release all open resources to avoid unnecessary memory usage

            // Statements and PreparedStatements
            int i = 0;
            while (!statements.isEmpty()) {
                // PreparedStatement extend Statement
                Statement st = (Statement) statements.remove(i);
                try {
                    if (st != null) {
                        st.close();
                        st = null;
                    }
                } catch (SQLException sqle) {
                    System.err.println("SQL Exception:" + sqle.getLocalizedMessage() + ", " + sqle.getSQLState());
                }
            }
        }

        //Close  connection
        this.connec.CloseConnection(conn);
        
        return Id_Program;
    }
    
    
    /**
     * Insert a new Program
     * URL es la URL del archivo o del file to read
     * @param conn
     * @param URL 
     */
    public void InsertRow(Connection conn) {
        ArrayList statements = new ArrayList(); // list of Statements, PreparedStatements
        Statement s = null;
        PreparedStatement psInsert = null;
        String URLFile = this.URLProgram;

        //String sql1 = "INSERT INTO PROGRAMS VALUES (DEFAULT)";
        
        System.out.println("(OK) \t this is the URL FILE>"+URLFile);
        
        String sql2="INSERT INTO USER1.PROGRAMS	(ID_PROGRAMS, TYPE, URL) VALUES (DEFAULT, 'LOGIC', '"+URLFile+"')";

        try {
            psInsert = conn.prepareStatement(sql2);

            statements.add(psInsert);
            psInsert.executeUpdate();

            // COMMIT CHANGES
            conn.commit();
            System.out.println("(OK)\t Committed the transaction:"+sql2);

        } catch (SQLException sqle) {
            System.err.println("SQL Exception:" + sqle.getLocalizedMessage() + ", " + sqle.getSQLState());
        } finally {
            // release all open resources to avoid unnecessary memory usage

            // Statements and PreparedStatements
            int i = 0;
            while (!statements.isEmpty()) {
                // PreparedStatement extend Statement
                Statement st = (Statement) statements.remove(i);
                try {
                    if (st != null) {
                        st.close();
                        st = null;
                    }
                } catch (SQLException sqle) {
                    System.err.println("SQL Exception:" + sqle.getLocalizedMessage() + ", " + sqle.getSQLState());
                }
            }
        }

        //Close  connection
        this.connec.CloseConnection(conn);


    }
    
    
    
        /**
     * Reports a data verification failure to System.err with the given message.
     *
     * @param message A message describing what failed.
     */
    private void reportFailure(String message) {
        System.err.println("\nData verification failed:");
        System.err.println('\t' + message);
    }
    
    
    
    /***  GETS SETS
     * 
     */ 
    
    
    
    public int getIdProgram() {
        return idProgram;
    }

    public void setIdProgram(int idProgram) {
        this.idProgram = idProgram;
    }

    public String getURLProgram() {
        return URLProgram;
    }

    public void setURLProgram(String URLProgram) {
        this.URLProgram = URLProgram;
    }

    
    
}
