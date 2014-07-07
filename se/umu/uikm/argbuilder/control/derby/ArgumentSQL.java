/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.umu.uikm.argbuilder.control.derby;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Esteban
 */
public class ArgumentSQL {

    private String framework = "embedded";
    private String driver = "org.apache.derby.jdbc.EmbeddedDriver";
    private String protocol = "jdbc:derby:../db\\data\\argument00";
    //private String protocol = "jdbc:derby:C:/home/arg-engine2/db/data/argument00/";
    private static String dbURL = "jdbc:derby:../db\\data\\argument00;create=true";//user=mex;password=mine";
    //private static String dbURL = "jdbc:derby:C:/home/arg-engine2/db/data/argument00;create=true";//user=mex;password=mine";
    private static String tableNameRule = "clauses";// "restaurants";
    private static String tableNameArguments = "arguments";// "restaurants";
    // jdbc Connection
    //private static Connection conn = null;
    private static Statement stmt = null;

    public ArgumentSQL() {
        /*
        System.out.println(">>>");
         try {
         System.out.println(new File(".").getCanonicalPath());
         System.out.println("DIR:" + new File("../").getCanonicalPath());

         } catch (java.io.IOException ex) {
         System.out.println("ex." + ex.getLocalizedMessage());
         }
         */
    }


    
    
    
    /*
    
     public static void main(String[] args) {


     createConnection();
        
     //CreateRuleTable();
     //CreateArgumentsTable();
     //insertRestaurants(5, "LaVals", "Berkeley");
        
     insertRule(1, "p(a)", "p(a):-p(b)", "true");
     insertArguments(1, "<{p(a):-p(b)},p(a)>");
     //selectRestaurants();
     selectArguments();
     selectRules();
         
     shutdown();
     }
     */
    public void deleteAllArguments(Connection conn) {
        {
            Statement st = null;
            try {
                st = conn.createStatement();
                st.execute("DELETE FROM APP.ARGUMENTS WHERE ID = 6");
                //System.out.println("QUERYARG:" + "DELETE FROM ARGUMENTS");
                //System.out.println("(OK) Deleted all arguments from table");
                st.close();
            } catch (SQLException sqlExcept) {
                sqlExcept.printStackTrace();
            }
        }
    }

    /**
     * Deletes all the records from Arguments, Clauses and Subprograms tables
     *
     * @param connect
     */
    public void deleteRecords(Connection connect) {
        Statement st = null;
        ResultSet rs = null;

        String url = "jdbc:derby:testdb;user=USER12";

        try {

            /*
             System.setProperty("derby.system.home",
             "/home/janbodnar/programming/derby/dbs");

             connect = DriverManager.getConnection(url);

             */
            connect.setAutoCommit(false);
            st = connect.createStatement();

            st.addBatch("DELETE FROM ARGUMENTS");
            st.addBatch("DELETE FROM CLAUSES");
            st.addBatch("DELETE FROM SUBPROGRAMS");
            st.addBatch("DELETE FROM ACTIVITIES");
            st.addBatch("DELETE FROM GOALS");
            st.addBatch("DELETE FROM OBSERVATIONS");
            st.addBatch("DELETE FROM ACTIONS");            
            st.addBatch("DELETE FROM INFERHEADS");


            int counts[] = st.executeBatch();

            connect.commit();

            //System.out.println("(OK) \t Committed " + counts.length + " updates");

            //           DriverManager.getConnection("jdbc:derby:;shutdown=true");

        } catch (SQLException ex) {

            Logger lgr = Logger.getLogger(ArgumentSQL.class.getName());

            if (((ex.getErrorCode() == 50000)
                    && ("XJ015".equals(ex.getSQLState())))) {

                lgr.log(Level.INFO, "Derby shut down normally", ex);

            } else {

                if (connect != null) {
                    try {
                        connect.rollback();
                    } catch (SQLException ex1) {
                        lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                    }
                }

                lgr.log(Level.SEVERE, ex.getMessage(), ex);
            }

        }
    }

    public void deleteAllClauses(Connection conn) {
        {
            try {
                stmt = conn.createStatement();
                stmt.execute("DELETE FROM APP.CLAUSES");
                //System.out.println("QUERYCLAS:" + "DELETE FROM APP.CLAUSES");

                //System.out.println("(OK) Deleted all clauses from table");
                stmt.close();
            } catch (SQLException sqlExcept) {
                sqlExcept.printStackTrace();
            }
        }
    }

    public void createConnection(Connection conn) {
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
            //Get a connection
            Properties props = new Properties(); // connection properties
            String dbName = "";
            //props.load(new FileInputStream(File.pathSeparator));

            //new FileSystems.getDefault().getPath("C:\\home\\arg-engine2\\derbyDB.properties");

            //System.out.println("(--->)"+(new File("C:/home/arg-engine2/derbyDB.properties").getCanonicalPath().toString()));
            props.load(new FileInputStream("C:/home/arg-engine2/derbyDB.properties"));
            dbName = props.getProperty("dbname");


            String user = props.getProperty("user");//= "argeng";
            String password = props.getProperty("password");//= "argeng";

            System.out.println("Connected to database #2");
            conn = DriverManager.getConnection(dbURL, user, password);

            if (conn != null) {
            }
        } catch (Exception except) {
            except.printStackTrace();
        }

    }

    public void insertRule(int id, String head, String rule, String truth, Connection conn)//(int id, String restName, String cityName)
    {
        try {
            stmt = conn.createStatement();
            stmt.execute("insert into " + tableNameRule + " values ("
                    + //id + ",'" + restName + "','" + cityName +"')");
                    id + ",'" + head + "','" + rule + "','" + truth + "')");
            stmt.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
        }
    }

    /**
     * Inserts a new clause with values: head, clause and truth
     *
     * @param head
     * @param clause
     * @param truth
     * @param connect
     */
    public void InsertClauses(String head, String clause, String truth, Connection connect) {
        Statement st = null;
        ResultSet rs = null;

        try {

            connect.setAutoCommit(false);
            st = connect.createStatement();

            st.addBatch("INSERT INTO CLAUSES (head,clause,truth) VALUES('" + head + "', '" + clause + "', '" + truth + "')");

            int counts[] = st.executeBatch();

            connect.commit();

            // System.out.println("(OK) \t Committed " + counts.length + " updates");

            //           DriverManager.getConnection("jdbc:derby:;shutdown=true");

        } catch (SQLException ex) {

            Logger lgr = Logger.getLogger(ArgumentSQL.class.getName());

            if (((ex.getErrorCode() == 50000)
                    && ("XJ015".equals(ex.getSQLState())))) {

                lgr.log(Level.INFO, "Derby shut down normally", ex);

            } else {

                if (connect != null) {
                    try {
                        connect.rollback();
                    } catch (SQLException ex1) {
                        lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                    }
                }

                lgr.log(Level.SEVERE, ex.getMessage(), ex);
            }

        }
    }

    
    

    /***
     * 
     * @param head
     * @param filename
     * @param connect 
     */
    public void InsertHeads(String head, String filename, Connection connect) {
        Statement st = null;
        ResultSet rs = null;

        try {

            connect.setAutoCommit(false);
            st = connect.createStatement();

            st.addBatch("INSERT INTO APP.INFERHEADS (head,id_subp) VALUES('" + head + "', (select id from app.SUBPROGRAMS where FILENAME = '"+ filename+"'))");

            int counts[] = st.executeBatch();

            connect.commit();
        } catch (SQLException ex) {

            Logger lgr = Logger.getLogger(ArgumentSQL.class.getName());

            if (((ex.getErrorCode() == 50000)
                    && ("XJ015".equals(ex.getSQLState())))) {

                lgr.log(Level.INFO, "Derby shut down normally", ex);

            } else {

                if (connect != null) {
                    try {
                        connect.rollback();
                    } catch (SQLException ex1) {
                        lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                    }
                }

                lgr.log(Level.SEVERE, ex.getMessage(), ex);
            }

        }
    }
    

    /**
     * Inserts a new clause with values: head, clause and truth
     *
     * @param head
     * @param clause
     * @param truth
     * @param connect
     */
    public void InsertGoals(String head, String clause, String truth, Connection connect) {
        Statement st = null;
        ResultSet rs = null;

        try {

            connect.setAutoCommit(false);
            st = connect.createStatement();

            st.addBatch("INSERT INTO CLAUSES (head,clause,truth) VALUES('" + head + "', '" + clause + "', '" + truth + "')");

            int counts[] = st.executeBatch();

            connect.commit();

            // System.out.println("(OK) \t Committed " + counts.length + " updates");

            //           DriverManager.getConnection("jdbc:derby:;shutdown=true");

        } catch (SQLException ex) {

            Logger lgr = Logger.getLogger(ArgumentSQL.class.getName());

            if (((ex.getErrorCode() == 50000)
                    && ("XJ015".equals(ex.getSQLState())))) {

                lgr.log(Level.INFO, "Derby shut down normally", ex);

            } else {

                if (connect != null) {
                    try {
                        connect.rollback();
                    } catch (SQLException ex1) {
                        lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                    }
                }

                lgr.log(Level.SEVERE, ex.getMessage(), ex);
            }

        }
    }

    
    
    /**
     * Inserts a new clause with values: head, clause and truth
     *
     * @param head
     * @param clause
     * @param truth
     * @param connect
     */
    public void InsertGoals2(String goal,  Connection connect) {
        Statement st = null;
        ResultSet rs = null;

        try {

            connect.setAutoCommit(false);
            st = connect.createStatement();

            st.addBatch("INSERT INTO APP.GOALS (goal) VALUES('" + goal + "')");

            int counts[] = st.executeBatch();

            connect.commit();

            // System.out.println("(OK) \t Committed " + counts.length + " updates");

            //           DriverManager.getConnection("jdbc:derby:;shutdown=true");

        } catch (SQLException ex) {

            Logger lgr = Logger.getLogger(ArgumentSQL.class.getName());

            if (((ex.getErrorCode() == 50000)
                    && ("XJ015".equals(ex.getSQLState())))) {

                lgr.log(Level.INFO, "Derby shut down normally", ex);

            } else {

                if (connect != null) {
                    try {
                        connect.rollback();
                    } catch (SQLException ex1) {
                        lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                    }
                }

                lgr.log(Level.SEVERE, ex.getMessage(), ex);
            }

        }
    }

    
    
    /**
     * Updates the table ClausesArgs
     *
     * @param clauseIdList
     * @param idArg
     * @param connect
     */
    public void InsertClausesArgs(List<Integer> clauseIdList, Integer idArg, Connection connect) {
        Statement st = null;
        ResultSet rs = null;

        try {

            connect.setAutoCommit(false);
            st = connect.createStatement();


            for (Iterator<Integer> it = clauseIdList.iterator(); it.hasNext();) {
                Integer integer = it.next();

                st.addBatch("INSERT INTO APP.CLAUSESARGS (refarg,refcla) VALUES("+idArg+","+integer+")");
                int counts[] = st.executeBatch();
                connect.commit();

            }



            // System.out.println("(OK) \t Committed " + counts.length + " updates");

            //           DriverManager.getConnection("jdbc:derby:;shutdown=true");

        } catch (SQLException ex) {

            Logger lgr = Logger.getLogger(ArgumentSQL.class.getName());

            if (((ex.getErrorCode() == 50000)
                    && ("XJ015".equals(ex.getSQLState())))) {

                lgr.log(Level.INFO, "Derby shut down normally", ex);

            } else {

                if (connect != null) {
                    try {
                        connect.rollback();
                    } catch (SQLException ex1) {
                        lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                    }
                }

                lgr.log(Level.SEVERE, ex.getMessage(), ex);
            }

        }
    }




    /***
     * Updates the table APP.ACTIVGOALS 
     * @param activity
     * @param goal
     * @param connect 
     */
    public void InsertActivGoals(String activity, String goal, Connection connect) {
        Statement st = null;
        ResultSet rs = null;

        try {

            connect.setAutoCommit(false);
            st = connect.createStatement();

                st.addBatch("INSERT INTO APP.ACTIVGOALS (REFACT,REFGOAL) VALUES((select id from app.activities where activity = '"+activity+"'),(select id from app.goals where goal = '"+ goal+"'))");
                int counts[] = st.executeBatch();
                connect.commit();

        } catch (SQLException ex) {

            Logger lgr = Logger.getLogger(ArgumentSQL.class.getName());

            if (((ex.getErrorCode() == 50000)
                    && ("XJ015".equals(ex.getSQLState())))) {

                lgr.log(Level.INFO, "Derby shut down normally", ex);

            } else {

                if (connect != null) {
                    try {
                        connect.rollback();
                    } catch (SQLException ex1) {
                        lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                    }
                }

                lgr.log(Level.SEVERE, ex.getMessage(), ex);
            }

        }
    }
    
    
    /***
     * Updates the table APP.ARGGOAL
     * @param activity
     * @param goal
     * @param connect 
     */
    public void InsertArgGoals(int idArgument, int idGoal, Connection connect) {
        Statement st = null;
        ResultSet rs = null;

        try {

            connect.setAutoCommit(false);
            st = connect.createStatement();

                st.addBatch("INSERT INTO APP.ARGGOAL (REFARG,REFGOAL) VALUES("+ idArgument+","+ idGoal+")");
                int counts[] = st.executeBatch();
                connect.commit();

        } catch (SQLException ex) {

            Logger lgr = Logger.getLogger(ArgumentSQL.class.getName());

            if (((ex.getErrorCode() == 50000)
                    && ("XJ015".equals(ex.getSQLState())))) {

                lgr.log(Level.INFO, "Derby shut down normally", ex);

            } else {

                if (connect != null) {
                    try {
                        connect.rollback();
                    } catch (SQLException ex1) {
                        lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                    }
                }

                lgr.log(Level.SEVERE, ex.getMessage(), ex);
            }

        }
    }

    /***
     * Updates the table  CLAUSESACTIONS
     * @param activity
     * @param goal
     * @param connect 
     */
    public void InsertClausesActions(String clause, String action, Connection connect) {
        Statement st = null;
        ResultSet rs = null;

        try {

            connect.setAutoCommit(false);
            st = connect.createStatement();

                st.addBatch("INSERT INTO APP.CLAUSESACTIONS (REFCLA,REFACT) VALUES((select id from app.CLAUSES where CLAUSE = '"+ clause+"'),(select id from app.actions where action = '"+ action+"'))");
                
                System.out.println("SQL!!!!!"+"INSERT INTO APP.CLAUSESACTIONS (REFCLA,REFACT) VALUES((select id from app.CLAUSES where CLAUSE = '"+ clause+"'),(select id from app.actions where action = '"+ action+"'))");
                int counts[] = st.executeBatch();
                connect.commit();

        } catch (SQLException ex) {

            Logger lgr = Logger.getLogger(ArgumentSQL.class.getName());

            if (((ex.getErrorCode() == 50000)
                    && ("XJ015".equals(ex.getSQLState())))) {

                lgr.log(Level.INFO, "Derby shut down normally", ex);

            } else {

                if (connect != null) {
                    try {
                        connect.rollback();
                    } catch (SQLException ex1) {
                        lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                    }
                }

                lgr.log(Level.SEVERE, ex.getMessage(), ex);
            }

        }
    }
    
    
    
    
    /***
     * Updates the table   InsertClausesObs
     * @param activity
     * @param goal
     * @param connect 
     */
    public void InsertClausesObs(String clause, String observation, Connection connect) {
        Statement st = null;
        ResultSet rs = null;

        try {

            connect.setAutoCommit(false);
            st = connect.createStatement();

                st.addBatch("INSERT INTO APP.CLAUSESOBS (REFCLA,REFOBS) VALUES((select id from app.CLAUSES where CLAUSE = '"+ clause+"'),(select id from app.observations where observation = '"+ observation+"'))");
                int counts[] = st.executeBatch();
                connect.commit();

        } catch (SQLException ex) {

            Logger lgr = Logger.getLogger(ArgumentSQL.class.getName());

            if (((ex.getErrorCode() == 50000)
                    && ("XJ015".equals(ex.getSQLState())))) {

                lgr.log(Level.INFO, "Derby shut down normally", ex);

            } else {

                if (connect != null) {
                    try {
                        connect.rollback();
                    } catch (SQLException ex1) {
                        lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                    }
                }

                lgr.log(Level.SEVERE, ex.getMessage(), ex);
            }

        }
    }
    
    
    
    
    /**
     * Updates the table ClausesArgs
     *
     * @param clauseIdList
     * @param idArg
     * @param connect
     */
    public void InsertActivGoals(List<Integer> clauseIdList, Integer idArg, Connection connect) {
        Statement st = null;
        ResultSet rs = null;

        try {

            connect.setAutoCommit(false);
            st = connect.createStatement();


            for (Iterator<Integer> it = clauseIdList.iterator(); it.hasNext();) {
                Integer integer = it.next();

                st.addBatch("INSERT INTO APP.CLAUSESARGS (refarg,refcla) VALUES("+idArg+","+integer+")");
                int counts[] = st.executeBatch();
                connect.commit();

            }



            // System.out.println("(OK) \t Committed " + counts.length + " updates");

            //           DriverManager.getConnection("jdbc:derby:;shutdown=true");

        } catch (SQLException ex) {

            Logger lgr = Logger.getLogger(ArgumentSQL.class.getName());

            if (((ex.getErrorCode() == 50000)
                    && ("XJ015".equals(ex.getSQLState())))) {

                lgr.log(Level.INFO, "Derby shut down normally", ex);

            } else {

                if (connect != null) {
                    try {
                        connect.rollback();
                    } catch (SQLException ex1) {
                        lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                    }
                }

                lgr.log(Level.SEVERE, ex.getMessage(), ex);
            }

        }
    }
    
    
    /**
     * Insert a new subprogram in table subrpgram
     *
     * @param filename
     * @param connect
     */
    public void InsertFilenameInSubprograms(String filename, Connection connect) {
        Statement st = null;
        ResultSet rs = null;

        try {
            connect.setAutoCommit(false);
            st = connect.createStatement();
            st.addBatch("INSERT INTO SUBPROGRAMS (filename) VALUES('" + filename + "')");

            int counts[] = st.executeBatch();
            connect.commit();

        } catch (SQLException ex) {

            Logger lgr = Logger.getLogger(ArgumentSQL.class.getName());
            if (((ex.getErrorCode() == 50000)
                    && ("XJ015".equals(ex.getSQLState())))) {
                lgr.log(Level.INFO, "Derby shut down normally", ex);

            } else {

                if (connect != null) {
                    try {
                        connect.rollback();
                    } catch (SQLException ex1) {
                        lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                    }
                }

                lgr.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
    }

    
   /**
     * Insert a new activity 
     *
     * @param filename
     * @param connect
     */
    public int InsertActivity( String activityName, Connection connect) {
        Statement st = null;
        ResultSet rs = null;
        int idAutoGen = 0;

        try {
            connect.setAutoCommit(false);
            st = connect.createStatement();
            st.addBatch("INSERT INTO APP.ACTIVITIES (activity) VALUES('" + activityName + "')");

            int counts[] = st.executeBatch();

            connect.commit();

            return idAutoGen;

        } catch (SQLException ex) {

            Logger lgr = Logger.getLogger(ArgumentSQL.class.getName());
            if (((ex.getErrorCode() == 50000)
                    && ("XJ015".equals(ex.getSQLState())))) {
                lgr.log(Level.INFO, "Derby shut down normally", ex);

            } else {

                if (connect != null) {
                    try {
                        connect.rollback();
                    } catch (SQLException ex1) {
                        lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                    }
                }

                lgr.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
        return idAutoGen;
    }
    
    

   /**
     * Insert a new action 
     *
     * @param filename
     * @param connect
     */
    public int InsertAction( String actionName, Connection connect) {
        Statement st = null;
        ResultSet rs = null;
        int idAutoGen = 0;

        try {
            connect.setAutoCommit(false);
            st = connect.createStatement();
            st.addBatch("INSERT INTO APP.ACTIONS (action) VALUES('" + actionName + "')");

            int counts[] = st.executeBatch();

            connect.commit();

            return idAutoGen;

        } catch (SQLException ex) {

            Logger lgr = Logger.getLogger(ArgumentSQL.class.getName());
            if (((ex.getErrorCode() == 50000)
                    && ("XJ015".equals(ex.getSQLState())))) {
                lgr.log(Level.INFO, "Derby shut down normally", ex);

            } else {

                if (connect != null) {
                    try {
                        connect.rollback();
                    } catch (SQLException ex1) {
                        lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                    }
                }

                lgr.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
        return idAutoGen;
    }
    
    
    /**
     * Insert a new argument given a support and a head
     *
     * @param filename
     * @param connect
     */
    public int InsertArgument(String supports, String head, Connection connect) {
        Statement st = null;
        ResultSet rs = null;
        int idAutoGen = 0;

        /**
         *
         * try { stmt = conn.createStatement(); ResultSet results =
         * stmt.executeQuery("select ID from APP.SUBPROGRAMS where INFERRED LIKE
         * '%" + head + "%'");
         *
         * //ResultSet results = stmt.executeQuery("select ID from
         * APP.SUBPROGRAMS where INFERRED = '" + head + "'"); ResultSetMetaData
         * rsmd = results.getMetaData();
         *
         * while (results.next()) { headIds.add(results.getInt(1)); }
         * results.close(); stmt.close(); } catch (SQLException sqlExcept) {
         * sqlExcept.printStackTrace(); }
         *
         */
        try {
            connect.setAutoCommit(false);
            st = connect.createStatement();
            String arg = "< {" + supports + "} ,   " + head + ">";
            st.addBatch("INSERT INTO APP.ARGUMENTS (argument) VALUES('" + arg + "')");

            int counts[] = st.executeBatch();





            connect.commit();


            /*
            
             int autoIncKeyFromApi = -1;
             rs = st.getGeneratedKeys();
             if (rs.next()) {
             autoIncKeyFromApi = rs.getInt(1);
             } else {
             // do what you have to do
             }
             System.out.println("autoIncKeyFromApi:"+autoIncKeyFromApi);
            
             if (rs.next()) {
             //autoIncKeyFromApi = rs.getInt(1);
             } else {
             // do what you have to do
             }
             System.out.println("autoIncKeyFromApi:"+autoIncKeyFromApi);
             //rs.close();
             */




            return idAutoGen;

        } catch (SQLException ex) {

            Logger lgr = Logger.getLogger(ArgumentSQL.class.getName());
            if (((ex.getErrorCode() == 50000)
                    && ("XJ015".equals(ex.getSQLState())))) {
                lgr.log(Level.INFO, "Derby shut down normally", ex);

            } else {

                if (connect != null) {
                    try {
                        connect.rollback();
                    } catch (SQLException ex1) {
                        lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                    }
                }

                lgr.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
        return idAutoGen;
    }

    

    
    /**
     * Insert a new fragment given a support and a head
     *
     * @param filename
     * @param connect
     */
    public int InsertFragments(String supports, String head, Connection connect) {
        Statement st = null;
        ResultSet rs = null;
        int idAutoGen = 0;

        try {
            connect.setAutoCommit(false);
            st = connect.createStatement();
            String arg = "<{ " + supports + "},  " + head + ">";
            st.addBatch("INSERT INTO APP.ARGUMENTS (argument) VALUES('" + arg + "')");

            int counts[] = st.executeBatch();
            connect.commit();

            return idAutoGen;

        } catch (SQLException ex) {

            Logger lgr = Logger.getLogger(ArgumentSQL.class.getName());
            if (((ex.getErrorCode() == 50000)
                    && ("XJ015".equals(ex.getSQLState())))) {
                lgr.log(Level.INFO, "Derby shut down normally", ex);
            } else {
                if (connect != null) {
                    try {
                        connect.rollback();
                    } catch (SQLException ex1) {
                        lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                    }
                }
                lgr.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
        return idAutoGen;
    }

    
    

    /**
     * Insert a new argument given a support and a head
     *
     * @param filename
     * @param connect
     */
    public int InsertArgumentSuppGoal(String supports, String head, Connection connect) {
        Statement st = null;
        ResultSet rs = null;
        int idAutoGen = 0;

        try {
            connect.setAutoCommit(false);
            st = connect.createStatement();
            String arg = "<{" + supports + "}," + head + ">";
            st.addBatch("INSERT INTO APP.ARGUMENTS (argument) VALUES('" + arg + "')");

            int counts[] = st.executeBatch();

            connect.commit();

            return idAutoGen;

        } catch (SQLException ex) {

            Logger lgr = Logger.getLogger(ArgumentSQL.class.getName());
            if (((ex.getErrorCode() == 50000)
                    && ("XJ015".equals(ex.getSQLState())))) {
                lgr.log(Level.INFO, "Derby shut down normally", ex);

            } else {

                if (connect != null) {
                    try {
                        connect.rollback();
                    } catch (SQLException ex1) {
                        lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                    }
                }

                lgr.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
        return idAutoGen;
    }
    
    
    
    /**
     * Insert a new subprogram in table subrpgram
     *
     * @param filename
     * @param connect
     */
    public void InsertSubpClauses(String filename, Connection connect) {
        Statement st = null;
        ResultSet rs = null;

        try {
            connect.setAutoCommit(false);
            st = connect.createStatement();
            st.addBatch("INSERT INTO SUBPROGRAMS (filename) VALUES('" + filename + "')");

            int counts[] = st.executeBatch();
            connect.commit();

        } catch (SQLException ex) {

            Logger lgr = Logger.getLogger(ArgumentSQL.class.getName());
            if (((ex.getErrorCode() == 50000)
                    && ("XJ015".equals(ex.getSQLState())))) {
                lgr.log(Level.INFO, "Derby shut down normally", ex);

            } else {

                if (connect != null) {
                    try {
                        connect.rollback();
                    } catch (SQLException ex1) {
                        lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                    }
                }

                lgr.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
    }

    /**
     * Insert a new subprogram in table subrpgram
     *
     * @param filename
     * @param connect
     */
    public void InsertRowSubpClauses(String filename, String head, Connection connect) {
        Statement st = null;
        ResultSet rs = null;

        try {
            connect.setAutoCommit(false);
            st = connect.createStatement();
            st.addBatch("INSERT INTO SUBPROGRAMS (filename, inferred) VALUES('" + filename + "','" + head + "')");

            int counts[] = st.executeBatch();
            connect.commit();

        } catch (SQLException ex) {

            Logger lgr = Logger.getLogger(ArgumentSQL.class.getName());
            if (((ex.getErrorCode() == 50000)
                    && ("XJ015".equals(ex.getSQLState())))) {
                lgr.log(Level.INFO, "Derby shut down normally", ex);

            } else {

                if (connect != null) {
                    try {
                        connect.rollback();
                    } catch (SQLException ex1) {
                        lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                    }
                }

                lgr.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
    }

    public void GetSupports(Connection connect) {
        Statement st = null;

        try {
            st = connect.createStatement();
            ResultSet results = st.executeQuery("SELECT clause FROM APP.CLAUSES WHERE truth = 'T'");
            ResultSetMetaData rsmd = results.getMetaData();
            int numberCols = rsmd.getColumnCount();
            for (int i = 1; i <= numberCols; i++) {
                //print Column Namesa
                System.out.print(rsmd.getColumnLabel(i) + "\t\t");
            }

            // System.out.println("\n--------SELECTRULE--------------------");

            while (results.next()) {
                String clause = results.getString(1);



                // System.out.println("\t\t" + clause);
            }
            results.close();
            st.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
        }

    }

    /**
     * Returns the set of clauses given the index of a subgraph
     *
     * @param connect
     * @param subGraph
     */
    public List<String> GetSubgraphs(Connection connect, int subGraph) {
        Statement st = null;
        String clause = "";
        List<String> subgraphList = new ArrayList<>();

        try {
            st = connect.createStatement();
            ResultSet results = st.executeQuery("SELECT clause FROM APP.CLAUSES WHERE subgraph =" + subGraph);
            ResultSetMetaData rsmd = results.getMetaData();
            int numberCols = rsmd.getColumnCount();
            for (int i = 1; i <= numberCols; i++) {
                //print Column Namesa
                System.out.print(rsmd.getColumnLabel(i) + "\t\t");
            }


            while (results.next()) {
                subgraphList.add(results.getString(1));
                //clause = clause + results.getString(1);
            }

            results.close();
            st.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
        }
        return subgraphList;
    }

    /**
     * Returns the maximum number of components stored in clauses table
     *
     * @param connect
     * @return
     */
    public int GetMaxSubgraph(Connection connect) {
        Statement st = null;

        int maxSubgraph = 0;
        try {
            st = connect.createStatement();
            ResultSet results = st.executeQuery("SELECT  MAX (subgraph) FROM APP.CLAUSES");
            ResultSetMetaData rsmd = results.getMetaData();
            int numberCols = rsmd.getColumnCount();
            for (int i = 1; i <= numberCols; i++) {
                //print Column Namesa
                // System.out.print(rsmd.getColumnLabel(i) + "\t\t");
            }

            // System.out.println("\n--------MAX SUBGRAPH--------------------");

            while (results.next()) {
                maxSubgraph = results.getInt(1);
                //String clause = results.getString(1);



                System.out.println("\t maxSubgraph \t" + maxSubgraph);
            }
            results.close();
            st.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
        }
        return maxSubgraph;

    }

    /**
     * Returns the last id from the argument table
     *
     * @param connect
     * @return
     */
    public int GetLastArgument(Connection connect) {
        Statement st = null;

        int maxSubgraph = 0;
        try {
            st = connect.createStatement();
            ResultSet results = st.executeQuery("select MAX(ID) from APP.ARGUMENTS");
            ResultSetMetaData rsmd = results.getMetaData();
            int numberCols = rsmd.getColumnCount();
            for (int i = 1; i <= numberCols; i++) {
                //print Column Namesa
                // System.out.print(rsmd.getColumnLabel(i) + "\t\t");
            }

            // System.out.println("\n--------MAX SUBGRAPH--------------------");

            while (results.next()) {
                maxSubgraph = results.getInt(1);
                //String clause = results.getString(1);



                System.out.println("\t maxSubgraph \t" + maxSubgraph);
            }
            results.close();
            st.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
        }
        return maxSubgraph;

    }

    /**
     * Updates the truth value of a given atom
     *
     * @param head
     * @param connect
     */
    public void UpdateTruthValues(String head, Connection connect) {
        Statement st = null;
        try {
            connect.setAutoCommit(false);
            st = connect.createStatement();

            st.addBatch("UPDATE APP.CLAUSES SET TRUTH = 'T' WHERE ID = (SELECT ID FROM APP.CLAUSES WHERE HEAD = '" + head + "')");
            int counts[] = st.executeBatch();
            connect.commit();
            //System.out.println("(OK)\t Atom " + head + " was successfuly updated to True value");

            //System.out.println("(OK) \t Committed " + counts.length + " updates");


        } catch (SQLException ex) {

            Logger lgr = Logger.getLogger(ArgumentSQL.class.getName());

            if (((ex.getErrorCode() == 50000)
                    && ("XJ015".equals(ex.getSQLState())))) {

                lgr.log(Level.INFO, "Derby shut down normally", ex);

            } else {

                if (connect != null) {
                    try {
                        connect.rollback();
                    } catch (SQLException ex1) {
                        lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                    }
                }

                lgr.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
    }

    /**
     * Updates the number of the subgraph component
     *
     * @param head
     * @param connect
     */
    public void UpdateSubgraphValues(String head, int component, Connection connect) {
        Statement st = null;
        try {
            connect.setAutoCommit(false);
            st = connect.createStatement();

            st.addBatch("UPDATE APP.CLAUSES SET SUBGRAPH = " + component + " WHERE ID = (SELECT ID FROM APP.CLAUSES WHERE HEAD = '" + head + "')");
            int counts[] = st.executeBatch();
            connect.commit();

            //System.out.println("(OK) \t Committed " + counts.length + " updates");


        } catch (SQLException ex) {

            Logger lgr = Logger.getLogger(ArgumentSQL.class.getName());

            if (((ex.getErrorCode() == 50000)
                    && ("XJ015".equals(ex.getSQLState())))) {

                lgr.log(Level.INFO, "Derby shut down normally", ex);

            } else {

                if (connect != null) {
                    try {
                        connect.rollback();
                    } catch (SQLException ex1) {
                        lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                    }
                }

                lgr.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
    }

    /**
     *
     * @param conn
     */
    public int SelectIDFromClauses(String clause, Connection conn) {
        int id = 0;

        try {
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery("SELECT ID FROM APP.CLAUSES WHERE CLAUSE = '" + clause + "'");
            ResultSetMetaData rsmd = results.getMetaData();
            /*
             int numberCols = rsmd.getColumnCount();
             for (int i = 1; i <= numberCols; i++) {
             //print Column Names
             System.out.print(rsmd.getColumnLabel(i) + "\t\t");
             }

             System.out.println("\n--------SELECTRULE--------------------");
             */
            while (results.next()) {
                id = results.getInt(1);
            }
            results.close();
            stmt.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
        }
        return id;
    }




    /**
     *
     * @param conn
     */
    public int SelectIDFromGoals(String goal, Connection conn) {
        int id = 0;

        try {
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery("SELECT ID FROM APP.GOALS WHERE GOAL= '" + goal + "'");
            ResultSetMetaData rsmd = results.getMetaData();

            while (results.next()) {
                id = results.getInt(1);
            }
            results.close();
            stmt.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
        }
        return id;
    }
    
    
    
    /**
     *
     * @param conn
     */
    public int SelectLastIDFromClauses(Connection conn) {
        int id = 0;

        try {
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery("SELECT ID FROM APP.CLAUSES WHERE CLAUSE = ''");
            ResultSetMetaData rsmd = results.getMetaData();
            /*
             int numberCols = rsmd.getColumnCount();
             for (int i = 1; i <= numberCols; i++) {
             //print Column Names
             System.out.print(rsmd.getColumnLabel(i) + "\t\t");
             }

             System.out.println("\n--------SELECTRULE--------------------");
             */
            while (results.next()) {
                id = results.getInt(1);
            }
            results.close();
            stmt.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
        }
        return id;
    }
    
    /**
     *
     * @param conn
     */
    public int SelectIDFromSubprog(String filename, Connection conn) {
        int id = 0;

        try {
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery("SELECT ID FROM APP.SUBPROGRAMS WHERE FILENAME = '" + filename + "'");
            ResultSetMetaData rsmd = results.getMetaData();
            /*
             int numberCols = rsmd.getColumnCount();
             for (int i = 1; i <= numberCols; i++) {
             //print Column Names
             System.out.print(rsmd.getColumnLabel(i) + "\t\t");
             }

             System.out.println("\n--------SELECTRULE--------------------");
             */
            while (results.next()) {
                id = results.getInt(1);
            }
            results.close();
            stmt.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
        }
        return id;
    }

    /**
     *
     * @param conn
     */
    public List<String> SelectArguments(Connection conn) {
        List<String> argList = new ArrayList<>();
        try {
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery("select ARGUMENT from APP.ARGUMENTS");
            ResultSetMetaData rsmd = results.getMetaData();

            while (results.next()) {
                argList.add(results.getString(1));
            }
            results.close();
            stmt.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
        }
        return argList;
    }

    
    

    /**
     *
     * @param conn
     */
    public int SelectLastArguments(Connection conn) {
        List<String> argList = new ArrayList<>();
        int id=0;
        try {
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery("select MAX(ID)from APP.ARGUMENTS");
            ResultSetMetaData rsmd = results.getMetaData();

            while (results.next()) {
                //argList.add(results.getString(1));
                id = results.getInt(1);
            }
            results.close();
            stmt.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
        }
        return id;
    }

    
    
    /**
     * Returns all the heads from CLauses table
     *
     * @param conn
     */
    public List<Integer> SelectIDFromSubprog2(String head, Connection conn) {
        int id = 0;
        List<Integer> headIds = new ArrayList<>();
        try {
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery("select ID from APP.SUBPROGRAMS where INFERRED LIKE  '%" + head + "%'");

            //ResultSet results = stmt.executeQuery("select ID from APP.SUBPROGRAMS where INFERRED = '" + head + "'");
            ResultSetMetaData rsmd = results.getMetaData();

            while (results.next()) {
                headIds.add(results.getInt(1));
            }
            results.close();
            stmt.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
        }
        return headIds;
    }

    
        /**
     * Returns all the heads from CLauses table
     *
     * @param conn
     */
    public List<Integer> SelectIDSubpFromInferHead(String head, Connection conn) {
        int id = 0;
        List<Integer> headIds = new ArrayList<>();
        try {
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery("select ID_SUBP from APP.INFERHEADS where HEAD LIKE  '%" + head + "%'");

            //ResultSet results = stmt.executeQuery("select ID from APP.SUBPROGRAMS where INFERRED = '" + head + "'");
            ResultSetMetaData rsmd = results.getMetaData();

            while (results.next()) {
                headIds.add(results.getInt(1));
            }
            results.close();
            stmt.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
        }
        return headIds;
    }

    
    
    
      /**
     * Returns all the heads from CLauses table
     *
     * @param conn
     */
    public List<Integer> SelectIDFromSubprog2(Connection conn) {
        int id = 0;
        List<Integer> headIds = new ArrayList<>();
        try {
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery("select ID from APP.SUBPROGRAMS where INFERRED<>''");

            //ResultSet results = stmt.executeQuery("select ID from APP.SUBPROGRAMS where INFERRED = '" + head + "'");
            ResultSetMetaData rsmd = results.getMetaData();

            while (results.next()) {
                headIds.add(results.getInt(1));
            }
            results.close();
            stmt.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
        }
        return headIds;
    }
          /**
     * Returns all the heads from CLauses table
     *
     * @param conn
     */
    public List<String> SelectHeadFromSubprog2(Connection conn) {
        int id = 0;
        List<String> headIds = new ArrayList<>();
        try {
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery("select INFERRED from APP.SUBPROGRAMS where INFERRED<>''");

            //ResultSet results = stmt.executeQuery("select ID from APP.SUBPROGRAMS where INFERRED = '" + head + "'");
            ResultSetMetaData rsmd = results.getMetaData();

            while (results.next()) {
                headIds.add(results.getString(1));
            }
            results.close();
            stmt.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
        }
        return headIds;
    }
    
    
          /**
     * Returns all the heads from CLauses table
     *
     * @param conn
     */
    public List<String> SelectHeadFromInferredHeads(Connection conn) {
        int id = 0;
        List<String> headIds = new ArrayList<>();
        try {
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery("select HEAD from APP.INFERHEADS where HEAD<>''");

            //ResultSet results = stmt.executeQuery("select ID from APP.SUBPROGRAMS where INFERRED = '" + head + "'");
            ResultSetMetaData rsmd = results.getMetaData();

            while (results.next()) {
                headIds.add(results.getString(1));
            }
            results.close();
            stmt.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
        }
        return headIds;
    }
    
    
    
    
    /**
     * Returns the list of references (a set of clauses) given a id of a
     * subprogram This method is used when an argument is being built
     *
     * @param conn
     */
    public List<Integer> SelectREFCLA(int idSubrpg, Connection conn) {
        int id = 0;
        List<Integer> REFCLAList = new ArrayList<>();
        try {
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery("select  REFCLA from APP.CLAUSESSUBPROGRAMS where refsub =" + idSubrpg);
            ResultSetMetaData rsmd = results.getMetaData();

            while (results.next()) {
                REFCLAList.add(results.getInt(1));
            }
            results.close();
            stmt.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
        }
        return REFCLAList;
    }

    /**
     * Returns
     *
     * @param conn
     */
    public List<String> SelectClause(int idClause, Connection conn) {
        int id = 0;
        List<String> ClauseList = new ArrayList<>();
        try {
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery("select DISTINCT CLAUSE from APP.CLAUSES where id =" + idClause);
            ResultSetMetaData rsmd = results.getMetaData();

            while (results.next()) {
                ClauseList.add(results.getString(1));
            }
            results.close();
            stmt.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
        }
        return ClauseList;
    }

    /**
     * Returns all the heads from CLauses table
     *
     * @param conn
     */
    public List<String> SelectHEADS(Connection conn) {
        int id = 0;
        List<String> headList = new ArrayList<>();
        try {
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery("select HEAD from APP.CLAUSES");
            ResultSetMetaData rsmd = results.getMetaData();

            while (results.next()) {
                headList.add(results.getString(1));
            }
            results.close();
            stmt.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
        }
        return headList;
    }


    /**
     * Returns all the IDs froma Goals where goal is = to goal
     *
     * @param conn
     */
    public List<String> SelectGoals(String goal, Connection conn) {
        int id = 0;
        List<String> headList = new ArrayList<>();
        try {
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery("select id from APP.GOALS where goal = '"+goal+"'");
            ResultSetMetaData rsmd = results.getMetaData();

            while (results.next()) {
                headList.add(results.getString(1));
            }
            results.close();
            stmt.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
        }
        return headList;
    }    
    

    /**
     * Returns  the last Goal inserted
     *
     * @param conn
     */
    public int  SelectLastGoal(Connection conn) {
        int id = 0;
        List<String> headList = new ArrayList<>();
        try {
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery("select MAX(id) from APP.GOALS");
            ResultSetMetaData rsmd = results.getMetaData();

            while (results.next()) {
                //headList.add(results.getString(1));
                id = results.getInt(1);
            }
            results.close();
            stmt.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
        }
        return id;
    }    
    

    /**
     * Returns the last activity inserted
     *
     * @param conn
     */
    public int SelectLastActivity(Connection conn) {
        int id = 0;
        List<String> headList = new ArrayList<>();
        try {
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery("select MAX(id) from APP.ACTIVITIES");
            ResultSetMetaData rsmd = results.getMetaData();

            while (results.next()) {
                id = results.getInt(1);
                //headList.add(results.getString(1));
            }
            results.close();
            stmt.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
        }
        return id;
    }    
    
      /**
     * Returns all the IDs froma Goals where goal is = to goal
     *
     * @param conn
     */
    public List<String> SelectActivities(String activity, Connection conn) {
        int id = 0;
        List<String> headList = new ArrayList<>();
        try {
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery("select id from APP.ACTIVITIES where activity = '"+activity+"'");
            ResultSetMetaData rsmd = results.getMetaData();

            while (results.next()) {
                headList.add(results.getString(1));
            }
            results.close();
            stmt.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
        }
        return headList;
    }    
    
    /**
     * Returns all the IDs froma ACtions where goal is = to goal
     *
     * @param conn
     */
    public List<String> SelectActions(String clause, Connection conn) {
        int id = 0;
        List<String> headList = new ArrayList<>();
        try {
            stmt = conn.createStatement();
            //ResultSet results = stmt.executeQuery("select id from APP.ACTIONS where action = '"+action+"'");
            ResultSet results = stmt.executeQuery("SELECT a.\"ACTION\" FROM app.ACTIONS as a INNER JOIN app.CLAUSESACTIONS as p ON a.ID = p.REFACT INNER JOIN app.CLAUSES as c  ON c.ID = p.REFCLA WHERE c.CLAUSE = '"+ clause +"'");
            
            
            ResultSetMetaData rsmd = results.getMetaData();

            while (results.next()) {
                headList.add(results.getString(1));
            }
            results.close();
            stmt.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
        }
        return headList;
    }    
    
    

    
    /**
     * Returns all the Subgraph column from CLauses table
     *
     * @param conn
     */
    public List<String> SelectSUBGRAPH(Connection conn) {
        int id = 0;
        List<String> headList = new ArrayList<>();
        try {
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery("select SUBGRAPH from APP.CLAUSES");
            ResultSetMetaData rsmd = results.getMetaData();

            while (results.next()) {
                headList.add(results.getString(1));
            }
            results.close();
            stmt.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
        }
        return headList;
    }

    
    
    /**
     * Updates the TABLE CLAUSE-SUBPROGRAMS
     *
     * @param head
     * @param connect
     */
    public void UpdateSubprValues(int idClauses, int idSuprog, Connection connect) {
        Statement st = null;
        try {
            connect.setAutoCommit(false);
            st = connect.createStatement();

            // System.out.println("===>SQL:"+ "INSERT INTO APP.CLAUSESSUBPROGRAMS (REFSUB,REFCLA) VALUES("+idSuprog+","+idClauses+")");
            st.addBatch("INSERT INTO APP.CLAUSESSUBPROGRAMS (REFSUB,REFCLA) VALUES(" + idSuprog + "," + idClauses + ")");
            int counts[] = st.executeBatch();
            connect.commit();

        } catch (SQLException ex) {

            Logger lgr = Logger.getLogger(ArgumentSQL.class.getName());

            if (((ex.getErrorCode() == 50000)
                    && ("XJ015".equals(ex.getSQLState())))) {
//                lgr.log(Level.INFO, "Derby shut down normally", ex);
            } else {

                if (connect != null) {
                    try {
                        connect.rollback();
                    } catch (SQLException ex1) {
                        lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                    }
                }

//                lgr.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
    }

    /**
     * Updates the truth value (True) for a given subprogram file
     *
     * @param head
     * @param connect
     */
    public void UpdateInferredValues(String subprogramFile, String inferredCla, Connection connect) {
        Statement st = null;
        try {
            connect.setAutoCommit(false);
            st = connect.createStatement();

            st.addBatch("UPDATE APP.SUBPROGRAMS SET INFERRED = '" + inferredCla + "' WHERE  FILENAME = '" + subprogramFile + "'");
            int counts[] = st.executeBatch();
            connect.commit();

            //System.out.println("(OK) \t Committed " + counts.length + " updates");


        } catch (SQLException ex) {

            Logger lgr = Logger.getLogger(ArgumentSQL.class.getName());

            if (((ex.getErrorCode() == 50000)
                    && ("XJ015".equals(ex.getSQLState())))) {

                lgr.log(Level.INFO, "Derby shut down normally", ex);

            } else {

                if (connect != null) {
                    try {
                        connect.rollback();
                    } catch (SQLException ex1) {
                        lgr.log(Level.WARNING, ex1.getMessage(), ex1);
                    }
                }

                lgr.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
    }

    public void insertArguments(int id, String argument, Connection conn)//(int id, String restName, String cityName)
    {
        try {
            stmt = conn.createStatement();
            stmt.execute("insert into  " + tableNameArguments + "  values ("
                    + //id + ",'" + restName + "','" + cityName +"')");
                    id + ",'" + argument + "')");
            stmt.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
        }
    }

    /**
     * *
     * Delete all files from previous public void insertArguments(int id, String
     * argument, Connection conn)//(int id, String restName, String cityName) {
     * try { stmt = conn.createStatement(); stmt.execute("insert into " +
     * tableNameArguments + " values (" + //id + ",'" + restName + "','" +
     * cityName +"')"); id + ",'" + argument + "')"); stmt.close(); } catch
     * (SQLException sqlExcept) { sqlExcept.printStackTrace(); } }
     */
    public void selectRules(Connection conn) {
        try {
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery("select * from " + tableNameRule);
            ResultSetMetaData rsmd = results.getMetaData();
            int numberCols = rsmd.getColumnCount();
            for (int i = 1; i <= numberCols; i++) {
                //print Column Names
                System.out.print(rsmd.getColumnLabel(i) + "\t\t");
            }

            System.out.println("\n--------SELECTRULE--------------------");

            while (results.next()) {
                int id = results.getInt(1);
                String ruleHead = results.getString(2);
                String ruleRule = results.getString(3);
                String ruleTruth = results.getString(3);
                System.out.println(id + "\t\t" + ruleHead + "\t\t" + ruleRule + "\t\t" + ruleTruth);
            }
            results.close();
            stmt.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
        }
    }

    public void selectArguments(Connection conn) {
        try {
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery("select * from " + tableNameArguments);
            ResultSetMetaData rsmd = results.getMetaData();
            int numberCols = rsmd.getColumnCount();
            for (int i = 1; i <= numberCols; i++) {
                //print Column Names
                System.out.print(rsmd.getColumnLabel(i) + "\t\t");
            }

            System.out.println("\n----------SELECT ARGUMENTS------------------------------------");

            while (results.next()) {
                int id = results.getInt(1);
                String argument = results.getString(2);
                System.out.println(id + "\t\t" + argument);
            }
            results.close();
            stmt.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
        }
    }

    public List<String> SelectFilenames(Connection conn) {
        List<String> filenames = new ArrayList<>();
        try {
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery("select filename from APP.SUBPROGRAMS");
            ResultSetMetaData rsmd = results.getMetaData();
            int numberCols = rsmd.getColumnCount();

            while (results.next()) {
                filenames.add(results.getString(1));
            }
            results.close();
            stmt.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
        }
        return filenames;
    }

    public void shutdown(Connection connect) {
        try {

            if (connect != null) {
                DriverManager.getConnection(dbURL + ";shutdown=true");
                connect.close();
                //System.out.println("(OK) Database connection closed");
            }
        } catch (SQLException sqlExcept) {
            System.out.println("Exception in SQL:" + sqlExcept.getMessage());
        }

    }

    public void InsertRow(Connection conn, String statement) {
        ArrayList statements = new ArrayList(); // list of Statements, PreparedStatements
        Statement s = null;
        PreparedStatement psInsert = null;



        try {



            psInsert = conn.prepareStatement(
                    "insert into location values (?, ?)");
            statements.add(psInsert);

            psInsert.setInt(1, 1956);
            psInsert.setString(2, "Webster St.");
            psInsert.executeUpdate();
            System.out.println("Inserted 1956 Webster");

            psInsert.setInt(1, 1910);
            psInsert.setString(2, "Union St.");
            psInsert.executeUpdate();
            System.out.println("Inserted 1910 Union");



            //CREATE TABLE
            /* Creating a statement object that we can use for running various
             * SQL statements commands against the database.*/
            s = conn.createStatement();
            statements.add(s);

            // We create a table...
            //s.execute("create table location(num int, addr varchar(40))");
            s.execute(statement);
            System.out.println("Created table location");

            // COMMIT CHANGES
            /*
             We commit the transaction. Any changes will be persisted to
             the database now.
             */
            conn.commit();
            System.out.println("Committed the transaction");



        } catch (SQLException sqle) {
            printSQLException(sqle);
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
                    printSQLException(sqle);
                }
            }
        }

        //Close  connection
        this.CloseConnection(conn);


    }

    /**
     * Creates a table with a definite statement
     *
     * @param conn
     * @param statement
     */
    public void CreateRuleTable(Connection connect) {
        ArrayList statements = new ArrayList(); // list of Statements, PreparedStatements
        Statement s = null;


        try {
            //CREATE TABLE
            /* Creating a statement object that we can use for running various
             * SQL statements commands against the database.*/
            s = connect.createStatement();
            statements.add(s);

            // We create a table...
            s.execute("CREATE TABLE clauses(id INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY, head VARCHAR(100),  clause VARCHAR(100),  truth VARCHAR(100))");
            //s.execute(statement);
            System.out.println("Created table rules");

            // COMMIT CHANGES
            /*
             We commit the transaction. Any changes will be persisted to
             the database now.
             */
            connect.commit();
            System.out.println("Committed the transaction");



        } catch (SQLException sqle) {
            printSQLException(sqle);
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
                    printSQLException(sqle);
                }
            }
        }

        //Close  connection
        // CloseConnection(conn);


    }

    /**
     * Creates a table with a definite statement
     *
     * @param conn
     * @param statement
     */
    public void CreateSubProgramTable(Connection connect) {
        ArrayList statements = new ArrayList(); // list of Statements, PreparedStatements
        Statement s = null;
        try {
            s = connect.createStatement();
            statements.add(s);

            // We create a table...
            s.execute("CREATE TABLE subprograms(id INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY, filename VARCHAR(100),  inferred VARCHAR(100))");
            //s.execute(statement);
            System.out.println("Created table rules");

            connect.commit();
            //System.out.println("Committed the transaction");

        } catch (SQLException sqle) {
            printSQLException(sqle);
        } finally {
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
                    printSQLException(sqle);
                }
            }
        }
    }

    /**
     * Creates a table of arguments
     *
     * @param conn
     * @param statement
     */
    public void CreateArgumentsTable(Connection connect) {
        ArrayList statements = new ArrayList(); // list of Statements, PreparedStatements
        Statement s = null;


        try {
            //CREATE TABLE
            /* Creating a statement object that we can use for running various
             * SQL statements commands against the database.*/
            s = connect.createStatement();
            statements.add(s);

            // We create a table...
            //s.execute("CREATE TABLE arguments(id int, argument varchar(80))");
            s.execute("CREATE TABLE arguments(  id INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,  argument VARCHAR(100))");
            //System.out.println("Created table arguments");

            // COMMIT CHANGES
            /*
             We commit the transaction. Any changes will be persisted to
             the database now.
             */
            connect.commit();
            //System.out.println("Committed the transaction");



        } catch (SQLException sqle) {
            printSQLException(sqle);
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
                    printSQLException(sqle);
                }
            }
        }

        //Close  connection
        //CloseConnection(conn);


    }

    /**
     * Creates a table of clause-arguments
     *
     * @param conn
     * @param statement
     */
    public void CreateClauseArgumentsTable(Connection conn) {
        ArrayList statements = new ArrayList(); // list of Statements, PreparedStatements
        Statement s = null;


        try {
            //CREATE TABLE
            /* Creating a statement object that we can use for running various
             * SQL statements commands against the database.*/
            s = conn.createStatement();
            statements.add(s);

            // We create a table...
            //s.execute("CREATE TABLE arguments(id int, argument varchar(80))");
            s.execute("CREATE TABLE clausesargs(  refarg INTEGER REFERENCES arguments(id) ON DELETE CASCADE,   refcla INTEGER REFERENCES clauses(id) ON DELETE CASCADE)");
            //System.out.println("Created table clause-arguments");

            // COMMIT CHANGES
            /*
             We commit the transaction. Any changes will be persisted to
             the database now.
             */
            conn.commit();
            //System.out.println("Committed the transaction");



        } catch (SQLException sqle) {
            printSQLException(sqle);
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
                    printSQLException(sqle);
                }
            }
        }
    }

    


    /**
     * Creates a table of activities-goals
     *
     * @param conn
     * @param statement
     */
    public void CreateActivitiesGoalsTable(Connection conn) {
        ArrayList statements = new ArrayList(); // list of Statements, PreparedStatements
        Statement s = null;


        try {
            //CREATE TABLE
            /* Creating a statement object that we can use for running various
             * SQL statements commands against the database.*/
            s = conn.createStatement();
            statements.add(s);

            // We create a table...
            //s.execute("CREATE TABLE arguments(id int, argument varchar(80))");
            s.execute("CREATE TABLE activgoals(  refact INTEGER REFERENCES activites(id) ON DELETE CASCADE,   refgoal INTEGER REFERENCES goals(id) ON DELETE CASCADE)");
            //System.out.println("Created table clause-arguments");

            // COMMIT CHANGES
            /*
             We commit the transaction. Any changes will be persisted to
             the database now.
             */
            conn.commit();
            //System.out.println("Committed the transaction");



        } catch (SQLException sqle) {
            printSQLException(sqle);
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
                    printSQLException(sqle);
                }
            }
        }
    }

    
    

    /**
     * Creates a table of goals
     *
     * @param conn
     * @param statement
     */
    public void CreateGoalsTable(Connection conn) {
        ArrayList statements = new ArrayList(); // list of Statements, PreparedStatements
        Statement s = null;
        try {
            //CREATE TABLE
            /* Creating a statement object that we can use for running various
             * SQL statements commands against the database.*/
            s = conn.createStatement();
            statements.add(s);
            // We create a table...
            //s.execute("CREATE TABLE arguments(id int, argument varchar(80))");
            s.execute("CREATE TABLE goals( id INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY, goal VARCHAR(100), id_cla INTEGER REFERENCES clauses(id) ON DELETE CASCADE)");
            //System.out.println("Created table clause-arguments");

            // COMMIT CHANGES
            /*
             We commit the transaction. Any changes will be persisted to
             the database now.
             */
            conn.commit();
            //System.out.println("Committed the transaction");



        } catch (SQLException sqle) {
            printSQLException(sqle);
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
                    printSQLException(sqle);
                }
            }
        }
    }

    
    

    /**
     * Creates a table of actions
     *
     * @param conn
     * @param statement
     */
    public void CreateActionsTable(Connection conn) {
        ArrayList statements = new ArrayList(); // list of Statements, PreparedStatements
        Statement s = null;
        try {
            //CREATE TABLE
            /* Creating a statement object that we can use for running various
             * SQL statements commands against the database.*/
            s = conn.createStatement();
            statements.add(s);
            // We create a table...
            //s.execute("CREATE TABLE arguments(id int, argument varchar(80))");
            s.execute("CREATE TABLE actions( id INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY, action VARCHAR(100), id_cla INTEGER REFERENCES clauses(id) ON DELETE CASCADE)");
            //System.out.println("Created table clause-arguments");

            // COMMIT CHANGES
            /*
             We commit the transaction. Any changes will be persisted to
             the database now.
             */
            conn.commit();
            //System.out.println("Committed the transaction");



        } catch (SQLException sqle) {
            printSQLException(sqle);
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
                    printSQLException(sqle);
                }
            }
        }
    }
    
    
    
/**
     * Creates a table of observations
     *
     * @param conn
     * @param statement
     */
    public void CreateObservationsTable(Connection conn) {
        ArrayList statements = new ArrayList(); // list of Statements, PreparedStatements
        Statement s = null;
        try {
            //CREATE TABLE
            /* Creating a statement object that we can use for running various
             * SQL statements commands against the database.*/
            s = conn.createStatement();
            statements.add(s);
            // We create a table...
            //s.execute("CREATE TABLE arguments(id int, argument varchar(80))");
            s.execute("CREATE TABLE observations( id INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY, observation VARCHAR(100), id_cla INTEGER REFERENCES clauses(id) ON DELETE CASCADE)");
            //System.out.println("Created table clause-arguments");

            // COMMIT CHANGES
            /*
             We commit the transaction. Any changes will be persisted to
             the database now.
             */
            conn.commit();
            //System.out.println("Committed the transaction");



        } catch (SQLException sqle) {
            printSQLException(sqle);
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
                    printSQLException(sqle);
                }
            }
        }
    }
    
    
        /**
     * Creates a table of Activities
     *
     * @param conn
     * @param statement
     */
    public void CreateActivityTable(Connection connect) {
        ArrayList statements = new ArrayList(); // list of Statements, PreparedStatements
        Statement s = null;


        try {
            //CREATE TABLE
            /* Creating a statement object that we can use for running various
             * SQL statements commands against the database.*/
            s = connect.createStatement();
            statements.add(s);

            // We create a table...
            //s.execute("CREATE TABLE arguments(id int, argument varchar(80))");
            s.execute("CREATE TABLE activities(  id INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,  argument VARCHAR(100))");
            //System.out.println("Created table arguments");

            // COMMIT CHANGES
            /*
             We commit the transaction. Any changes will be persisted to
             the database now.
             */
            connect.commit();
            //System.out.println("Committed the transaction");

        } catch (SQLException sqle) {
            printSQLException(sqle);
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
                    printSQLException(sqle);
                }
            }
        }
    }


    
    
    
    /**
     * connects to a Derby DB
     *
     * @return
     */
    public Connection Connect() {


        /* load the desired JDBC driver */
        loadDriver();

        Connection conn = null;

        ArrayList statements = new ArrayList(); // list of Statements, PreparedStatements
        PreparedStatement psInsert = null;
        PreparedStatement psUpdate = null;
        Statement s = null;
        ResultSet rs = null;
        try {
            Properties props = new Properties(); // connection properties
            String dbName = "";

            /*
             //try retrieve data from file
             try {

             //props.put("user", "user1");
             //props.put("password", "user1");
             props.load(new FileInputStream("C:/home/arg-engine - Copy/derbyDB.properties"));

             dbName = props.getProperty("dbname");
             } catch (IOException e) {
             e.printStackTrace();
             }
             */

            conn = DriverManager.getConnection(protocol + ";create=true", props);

            //System.out.println("(OK)\tConnected to and created(if) database " + dbName);

            conn.setAutoCommit(false);

        } catch (SQLException sqle) {
            printSQLException(sqle);
        }

        return conn;
    }

    /**
     * Close the current connection with Derby and closed the statements and
     * resultset
     *
     * @param conn
     */
    public void CloseConnection(Connection conn) {
        boolean isconnected = true;

        try {

            try {
                // the shutdown=true attribute shuts down Derby
                DriverManager.getConnection("jdbc:derby:;shutdown=true");

                // To shut down a specific database only, but keep the
                // engine running (for example for connecting to other
                // databases), specify a database in the connection URL:
                //DriverManager.getConnection("jdbc:derby:" + dbName + ";shutdown=true");
            } catch (SQLException se) {
                if (((se.getErrorCode() == 50000)
                        && ("XJ015".equals(se.getSQLState())))) {
                    // we got the expected exception
                    //System.out.println("(OK)\t Derby shut down normally");
                    // Note that for single database shutdown, the expected
                    // SQL state is "08006", and the error code is 45000.
                } else {
                    // if the error code or SQLState is different, we have
                    // an unexpected exception (shutdown failed)
                    //System.err.println("Derby did not shut down normally");
                    printSQLException(se);
                }
            }


            if (conn != null) {
                conn.close();

                // System.out.println("(OK) \t Is the database closed? ANSW:" + conn.isClosed());

                conn = null;
                // System.out.println("(OK)\t Derby DATABASE shut down normally");

            }



        } catch (SQLException sqle) {
            printSQLException(sqle);
        }


    }

    /**
     * Prints details of an SQLException chain to
     * <code>System.err</code>. Details included are SQL State, Error code,
     * Exception message.
     *
     * @param e the SQLException from which to print details.
     */
    public void printSQLException(SQLException e) {
        // Unwraps the entire exception chain to unveil the real cause of the
        // Exception.
        while (e != null) {
            System.err.println("\n----- SQLException -----");
            System.err.println("  SQL State:  " + e.getSQLState());
            System.err.println("  Error Code: " + e.getErrorCode());
            System.err.println("  Message:    " + e.getMessage());
            // for stack traces, refer to derby.log or uncomment this:
            //e.printStackTrace(System.err);
            e = e.getNextException();
        }
    }

    /**
     * Loads the appropriate JDBC driver for this environment/framework. For
     * example, if we are in an embedded environment, we load Derby's embedded
     * Driver,
     * <code>org.apache.derby.jdbc.EmbeddedDriver</code>.
     */
    void loadDriver() {
        /*
         *  The JDBC driver is loaded by loading its class.
         *  If you are using JDBC 4.0 (Java SE 6) or newer, JDBC drivers may
         *  be automatically loaded, making this code optional.
         *
         *  In an embedded environment, this will also start up the Derby
         *  engine (though not any databases), since it is not already
         *  running. In a client environment, the Derby engine is being run
         *  by the network server framework.
         *
         *  In an embedded environment, any static Derby system properties
         *  must be set before loading the driver to take effect.
         */
        try {
            Class.forName(driver).newInstance();
            //System.out.println("(OK) \t Loaded the appropriate driver");
        } catch (ClassNotFoundException cnfe) {
            System.err.println("\nUnable to load the JDBC driver " + driver);
            System.err.println("Please check your CLASSPATH.");
            cnfe.printStackTrace(System.err);
        } catch (InstantiationException ie) {
            System.err.println(
                    "\nUnable to instantiate the JDBC driver " + driver);
            ie.printStackTrace(System.err);
        } catch (IllegalAccessException iae) {
            System.err.println(
                    "\nNot allowed to access the JDBC driver " + driver);
            iae.printStackTrace(System.err);
        }
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
}
