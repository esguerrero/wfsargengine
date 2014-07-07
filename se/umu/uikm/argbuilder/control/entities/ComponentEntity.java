/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.umu.uikm.argbuilder.control.entities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import se.umu.uikm.argbuilder.control.DerbyConnect;

/**
 *
 * @author Esteban
 */
public class ComponentEntity {
    
    private int idComponent;
    private  DerbyConnect connec = new DerbyConnect();

    public ComponentEntity(int idComponent) {
        this.idComponent = idComponent;
    }
    
    
    

    public int getIdComponent() {
        return idComponent;
    }

    public void setIdComponent(int idComponent) {
        this.idComponent = idComponent;
    }
    
    
    public void addComponent(ComponentEntity component){
        
    // insert component y los             psInsert.setInt(1, 1956);            psInsert.setString(2, "Webster St.");
    // son generados a traves de los component.getDato

    
        
    }
    
    
    
    
      public void InsertRow(Connection conn, int id_component, int id_program) {
        ArrayList statements = new ArrayList(); // list of Statements, PreparedStatements
        Statement s = null;
        int idProgram = id_program;
        int idComponent = id_component;
                
        PreparedStatement psInsert = null;
        
        //String sql1 = "INSERT INTO COMPONENTS VALUES (DEFAULT)";
        String sql2 ="INSERT INTO USER1.COMPONENTS  (ID_COMPONENTS, ID_PROGRAMS) VALUES ("+idComponent+","+idProgram+" )";
        
        try {            
            psInsert = conn.prepareStatement(sql2);
            
            statements.add(psInsert);
            psInsert.executeUpdate();
            
            // COMMIT CHANGES
            conn.commit();
            System.out.println("(OK)\t Committed the transaction:"+sql2);
                       
        } catch (SQLException sqle) {
            System.err.println("SQL Exception:"+ sqle.getLocalizedMessage() + ", "+sqle.getSQLState() );
        }
        finally {
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
                    System.err.println("SQL Exception:"+ sqle.getLocalizedMessage() + ", "+sqle.getSQLState() );
                }
            }
        }

        //Close  connection
        this.connec.CloseConnection(conn);


    }
    
    
    
    
    
    
    
}
