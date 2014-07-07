/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.umu.uikm.argbuilder.control;

import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.graph.Forest;
import java.awt.FileDialog;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.SetUtils;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.alg.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.DepthFirstIterator;
import se.umu.uikm.argbuilder.control.derby.ArgumentSQL;
import se.umu.uikm.argbuilder.control.entities.ComponentEntity;
import se.umu.uikm.argbuilder.control.entities.ProgramEntity;

/**
 *
 * @author Esteban
 */
public class ArgBuildManager {

    private static String AtomSplitter = ",";
    private static String HeadSplitter = "-";
    private static List S = new ArrayList();
    private static List C = new ArrayList();
    private static Forest<String, Integer> graphJung;// = new DelegateForest<>();

    public ArgBuildManager() {
    }

    Factory<Integer> edgeFactory = new Factory<Integer>() {
        int i = 0;

        public Integer create() {
            return i++;
        }
    };

    /**
     * Creates the subgraph files and generates the graph for the GUI
     *
     * @param URLFile
     * @param NameFile
     * @return
     */
    public List GraphFileRules(String URLFile, String NameFile) {

        /**
         * because it's load a new program, it's necessary to delete all data
         * from database, AKA the previous generated arguments + the files of
         * the subgraphs.
         */
        graphJung = new DelegateForest<>();

        List<String> filenames = new ArrayList<>();
        ArgumentSQL manage = new ArgumentSQL();
        Connection conn = null;
        conn = manage.Connect();
        filenames = manage.SelectFilenames(conn);

        for (Iterator<String> it = filenames.iterator(); it.hasNext();) {
            String string = it.next();
            try {
                /*
                 try {
                 System.out.println(new File(".").getCanonicalPath());
                 System.out.println("DIR IN DELETE:" + new File("../examples/").getCanonicalPath());

                 } catch (java.io.IOException ex) {
                 System.out.println("ERROR IN DELETE." + ex.getLocalizedMessage());
                 }
                 */

                String filetodel = new File("../examples/" + string).getCanonicalPath();
                //System.out.println("Name of file:"+clause_s);

                String xwam = string.replace(".P", ".xwam");

                File fileD = new File(filetodel);
                fileD.delete();

                fileD = new File(xwam);
                fileD.delete();

                /*System.out.println(new File(".").getCanonicalPath());
                 System.out.println("DIR:" + new File("../examples/").getCanonicalPath());
                 System.out.println("DIR:" + new File("../examples/" + clause_s).getCanonicalPath());
                 */
            } catch (java.io.IOException ex) {
                System.out.println("ERROR deleting files:" + ex.getLocalizedMessage());
            }
        }

        manage.deleteRecords(conn);
        manage.CloseConnection(conn);

        int NumTotRules = 0;

        UndirectedGraph<String, DefaultEdge> graph = new SimpleGraph<String, DefaultEdge>(DefaultEdge.class);

//System.out.println("(OK)\t file to load:" + URLFile + NameFile);
        File file = new File(URLFile + NameFile);

        // Standard Map
        Map<Integer, String> mapNumRuleHead = new HashMap<Integer, String>();
        Map<Integer, String> mapNumRuleFull = new HashMap<Integer, String>();
        Map<String, Integer> mapAtomCompon = new HashMap<String, Integer>();

        /*    
         map.put("Lars", 1);
         map.put("Lars", 2);
         map.put("Lars", 1);
    
         assertEquals(map.get("Lars"), 1);

         for (int i = 0; i < 100; i++) {
         map.put(String.valueOf(i), i);
         }
         assertEquals(map.size(), 101);

         assertEquals(map.get("51"), 51);
         map.keySet();
         */
        //List 
        try {
            FileReader reader = new FileReader(file);
            BufferedReader buffReader = new BufferedReader(reader);
            // define el numero de componentes, solo hay 1 porque se abre un solo
            // archivo .P, en el futuro debería ser dinámico
            int numComponents = 0;
            numComponents = 1;
            int numofComponents, numofRules = 0;

            //Este es el numero (id) del componente! modificarlo
            numofComponents = 1;
            //read every rule of the program
            String ruleLine;
            while ((ruleLine = buffReader.readLine()) != null) {
                int x = 0;
                numofRules++;
                String headx = "";

//                System.out.println("(OK)\t------------------------------------------------------------- Num of rule:" + numofRules);
//                System.out.println("(OK)\t ruleLine:" + ruleLine);
                StringTokenizer headToken = new StringTokenizer(ruleLine, HeadSplitter, false);
                String temporalRule = "";

                String temporalHead = "";
                String temporalAtomBody = "";

                while (headToken.hasMoreTokens()) {

                    x++;
//System.out.println("(OK)\t ++++++++++++++++++++++++++++++++++++++++++++++ x:" + x);
//System.out.println("countTokens::"+headToken.countTokens());
                    temporalRule = headToken.nextToken();

                    mapNumRuleFull.put(numofRules, ruleLine);

                    if (x == 1) {//esta es la cabeza
                        if (temporalRule.isEmpty() || temporalRule.startsWith(":")) {
                            //System.out.println("(OK)\t Esta no es una regla");// con esto se soluciona el problema de la regla "auto_table"
                        } else {
                            if (temporalRule.contains(".")) {
                                temporalHead = HeadCleaner(temporalRule);

                                graph.addVertex(temporalHead);//Head as a vertex
                                mapNumRuleHead.put(numofRules, temporalHead);//mapping [#of the rule(index), head of the rule(value)]

                                ArgumentSQL manage2 = new ArgumentSQL();
                                Connection conn2 = null;
                                conn2 = manage2.Connect();
                                manage2.InsertClauses(temporalHead, temporalHead + ".", "U", conn2);
                                //System.out.println("\t\t\t======INSERT FACT=========> head:"+temporalHead+"___fullclause:"+temporalHead);
//INSERT CLAU                               
                                manage2.CloseConnection(conn2);
                                //jung graph for GUI purposes
                                graphJung.addVertex(temporalHead);
                            } else {
                                temporalHead = HeadCleaner(temporalRule);
                                graph.addVertex(temporalHead);//Head as a vertex
                                mapNumRuleHead.put(numofRules, temporalHead);//mapping [#of the rule(index), head of the rule(value)]
                                System.out.println("(OK)\t HEAD_" + temporalHead + "_");

                                ArgumentSQL manage2 = new ArgumentSQL();
                                Connection conn2 = null;
                                conn2 = manage2.Connect();
                                manage2.InsertClauses(temporalHead, ruleLine, "U", conn2);
                                // System.out.println("\t\t\t=====INSERT CLAUSE=========> head:" + temporalHead + "___fullclause:"+ruleLine);                                
//INSERT CLAU                                
                                manage2.CloseConnection(conn2);

                                //jung graph for GUI purposes
                                graphJung.addVertex(temporalHead);
                            }

                        }

                    }
                    if (x > 1) {
                        //                      System.out.println("(OK)\t  BODY>" + temporalRule + "<");
                        StringTokenizer bodyToken = new StringTokenizer(temporalRule, AtomSplitter, false);
                        String temporalAtom = "";

                        while (bodyToken.hasMoreTokens()) {
                            temporalAtom = bodyToken.nextToken();

                            // System.out.println("");
                            if (temporalAtom.contains("auto_table") || temporalAtom.isEmpty()) {
                                //System.out.println("(OK)\tel body no tiene atoms");
                            } else {
                                temporalAtomBody = AtomCleaner(temporalAtom);
                                graph.addVertex(temporalAtomBody);
                                //System.out.println("(OK)\t EDGES:" + temporalHead + "-----" + temporalAtomBody);
                                //DefaultEdge edgeClause = new DefaultEdge();

                                graph.addEdge(temporalHead, temporalAtomBody);//,edgeClause);

                                //graphJung is for GUI purposes
                                graphJung.addEdge(edgeFactory.create(), temporalHead, temporalAtomBody);
                                /**
                                 * *
                                 * graph.addVertex("K0");
                                 * graph.addEdge(edgeFactory.create(), "K0",
                                 * "K1");
                                 *
                                 */
                                C.add(temporalHead);
                                S.add(temporalAtomBody);

                                //System.out.println(">>>temporalAtomBody:" + temporalAtomBody + "   >>> temporaHead:" + temporalHead);
//                                System.out.println("temporaHead:" + temporalHead + "  ruleLine:"+ruleLine);
                                //System.out.println("graph.getEdgeTarget(edgeClause):"+graph.getEdgeTarget(edgeClause));
                                //System.out.println("graph.getEdgeSource(edgeClause):"+graph.getEdgeSource(edgeClause));
                                /*
                                 ArgumentSQL manage2 = new ArgumentSQL();
                                 Connection conn2 = null;
                                 conn2 = manage2.Connect();
                                 manage2.InsertClauses(temporalHead, ruleLine, "U", conn2);
                                 System.out.println("\t\t\t=====INSERT CLAUSE # 2 =========> head:" + temporalHead + "___fullclause:"+ruleLine);                                
                                 manage2.CloseConnection(conn2);
                                 */
                            }
                        }
                    }
                }

                NumTotRules = numofRules;
            }
        } catch (IOException e) {
            //handle exception
        }

        mapAtomCompon = ConnectivityAnalysis(graph);

        List nameList = new ArrayList();

        for (Map.Entry<String, Integer> entry : mapAtomCompon.entrySet()) {
            String atom = entry.getKey();
            Integer comp = entry.getValue();
            for (Map.Entry<Integer, String> entry1 : mapNumRuleFull.entrySet()) {
                String fullRule = entry1.getValue();
                Integer ruleMap1 = entry1.getKey();

                if (fullRule.contains(atom)) {
                    //System.out.println("string0:" + atom + "  string1:" + fullRule + "  en el componente:" + comp + "  file:" + comp + NameFile);
                    //WriteSubGraph(URLFile, comp + NameFile, fullRule);
                    /**
                     * *
                     * CREATES SUBGRAPH FILES COMPONENT-SUBPROGRAM
                     */
//                    nameList.add(WriteSubGraph(URLFile, comp + NameFile, fullRule + "\n"));
                }
            }
        }
        //System.out.println("ANTES-> Csize" + C.size() + "  Ssize:" + S.size());

        return nameList;

    }

    public List GraphFileRulesBCKP(String URLFile, String NameFile) {

        /**
         * because it's load a new program, it's necessary to delete all data
         * from database, AKA the previous generated arguments + the files of
         * the subgraphs.
         */
        graphJung = new DelegateForest<>();

        List<String> filenames = new ArrayList<>();
        ArgumentSQL manage = new ArgumentSQL();
        Connection conn = null;
        conn = manage.Connect();
        filenames = manage.SelectFilenames(conn);

        for (Iterator<String> it = filenames.iterator(); it.hasNext();) {
            String string = it.next();
            try {
                /*
                 try {
                 System.out.println(new File(".").getCanonicalPath());
                 System.out.println("DIR IN DELETE:" + new File("../examples/").getCanonicalPath());

                 } catch (java.io.IOException ex) {
                 System.out.println("ERROR IN DELETE." + ex.getLocalizedMessage());
                 }
                 */

                String filetodel = new File("../examples/" + string).getCanonicalPath();
                //System.out.println("Name of file:"+clause_s);

                String xwam = string.replace(".P", ".xwam");

                File fileD = new File(filetodel);
                fileD.delete();

                fileD = new File(xwam);
                fileD.delete();

                /*System.out.println(new File(".").getCanonicalPath());
                 System.out.println("DIR:" + new File("../examples/").getCanonicalPath());
                 System.out.println("DIR:" + new File("../examples/" + clause_s).getCanonicalPath());
                 */
            } catch (java.io.IOException ex) {
                System.out.println("ERROR deleting files:" + ex.getLocalizedMessage());
            }
        }

        manage.deleteRecords(conn);
        manage.CloseConnection(conn);

        int NumTotRules = 0;

        UndirectedGraph<String, DefaultEdge> graph = new SimpleGraph<String, DefaultEdge>(DefaultEdge.class);

//System.out.println("(OK)\t file to load:" + URLFile + NameFile);
        File file = new File(URLFile + NameFile);

        // Standard Map
        Map<Integer, String> mapNumRuleHead = new HashMap<Integer, String>();
        Map<Integer, String> mapNumRuleFull = new HashMap<Integer, String>();
        Map<String, Integer> mapAtomCompon = new HashMap<String, Integer>();

        /*    
         map.put("Lars", 1);
         map.put("Lars", 2);
         map.put("Lars", 1);
    
         assertEquals(map.get("Lars"), 1);

         for (int i = 0; i < 100; i++) {
         map.put(String.valueOf(i), i);
         }
         assertEquals(map.size(), 101);

         assertEquals(map.get("51"), 51);
         map.keySet();
         */
        //List 
        try {
            FileReader reader = new FileReader(file);
            BufferedReader buffReader = new BufferedReader(reader);
            // define el numero de componentes, solo hay 1 porque se abre un solo
            // archivo .P, en el futuro debería ser dinámico
            int numComponents = 0;
            numComponents = 1;
            int numofComponents, numofRules = 0;

            //Este es el numero (id) del componente! modificarlo
            numofComponents = 1;
            //read every rule of the program
            String ruleLine;
            while ((ruleLine = buffReader.readLine()) != null) {
                int x = 0;
                numofRules++;

                //System.out.println("(OK)\t------------------------------------------------------------- Num of rule:" + numofRules);
                System.out.println("(OK)\t ruleLine:" + ruleLine);
                StringTokenizer headToken = new StringTokenizer(ruleLine, HeadSplitter, false);
                String temporalRule = "";

                String temporalHead = "";
                String temporalAtomBody = "";

                while (headToken.hasMoreTokens()) {

                    x++;
                    //                System.out.println("(OK)\t ++++++++++++++++++++++++++++++++++++++++++++++ x:" + x);
                    //  System.out.println("countTokens::"+headToken.countTokens());
                    temporalRule = headToken.nextToken();

                    mapNumRuleFull.put(numofRules, ruleLine);

                    if (x == 1) {//esta es la cabeza
                        if (temporalRule.isEmpty() || temporalRule.startsWith(":")) {
                            //System.out.println("(OK)\t Esta no es una regla");// con esto se soluciona el problema de la regla "auto_table"
                        } else {
                            temporalHead = HeadCleaner(temporalRule);
                            graph.addVertex(temporalHead);//Head as a vertex
                            mapNumRuleHead.put(numofRules, temporalHead);//mapping [#of the rule(index), head of the rule(value)]
                            //System.out.println("(OK)\t HEAD>" + temporalHead + "<");

                            //jung graph for GUI purposes
                            graphJung.addVertex(temporalHead);

                        }
                    }
                    if (x > 1) {
                        //                      System.out.println("(OK)\t  BODY>" + temporalRule + "<");
                        StringTokenizer bodyToken = new StringTokenizer(temporalRule, AtomSplitter, false);
                        String temporalAtom = "";

                        while (bodyToken.hasMoreTokens()) {
                            temporalAtom = bodyToken.nextToken();
                            if (temporalAtom.contains("auto_table") || temporalAtom.isEmpty()) {
                                //System.out.println("(OK)\tel body no tiene atoms");
                            } else {
                                temporalAtomBody = AtomCleaner(temporalAtom);
                                graph.addVertex(temporalAtomBody);
                                //System.out.println("(OK)\t EDGES:" + temporalHead + "-----" + temporalAtomBody);
                                //DefaultEdge edgeClause = new DefaultEdge();

                                graph.addEdge(temporalHead, temporalAtomBody);//,edgeClause);

                                //graphJung is for GUI purposes
                                graphJung.addEdge(edgeFactory.create(), temporalHead, temporalAtomBody);
                                /**
                                 * *
                                 * graph.addVertex("K0");
                                 * graph.addEdge(edgeFactory.create(), "K0",
                                 * "K1");
                                 *
                                 */
                                C.add(temporalHead);
                                S.add(temporalAtomBody);

                                //System.out.println(">>>temporalAtomBody:" + temporalAtomBody + "   >>> temporaHead:" + temporalHead);
                                //System.out.println("graph.getEdgeTarget(edgeClause):"+graph.getEdgeTarget(edgeClause));
                                //System.out.println("graph.getEdgeSource(edgeClause):"+graph.getEdgeSource(edgeClause));
                                ArgumentSQL manage2 = new ArgumentSQL();
                                Connection conn2 = null;
                                conn2 = manage2.Connect();
                                manage2.InsertClauses(temporalHead, ruleLine, "U", conn2);
//INSERT CLA                                
                                manage2.CloseConnection(conn2);

                            }
                        }
                    }
                }

                NumTotRules = numofRules;
            }
        } catch (IOException e) {
            //handle exception
        }

        mapAtomCompon = ConnectivityAnalysis(graph);

        List nameList = new ArrayList();

        for (Map.Entry<String, Integer> entry : mapAtomCompon.entrySet()) {
            String atom = entry.getKey();
            Integer comp = entry.getValue();
            for (Map.Entry<Integer, String> entry1 : mapNumRuleFull.entrySet()) {
                String fullRule = entry1.getValue();
                Integer ruleMap1 = entry1.getKey();

                if (fullRule.contains(atom)) {
                    //System.out.println("string0:" + atom + "  string1:" + fullRule + "  en el componente:" + comp + "  file:" + comp + NameFile);
                    //WriteSubGraph(URLFile, comp + NameFile, fullRule);
                    /**
                     * *
                     * CREATES SUBGRAPH FILES COMPONENT-SUBPROGRAM
                     */
//                    nameList.add(WriteSubGraph(URLFile, comp + NameFile, fullRule + "\n"));
                }
            }
        }
        //System.out.println("ANTES-> Csize" + C.size() + "  Ssize:" + S.size());

        return nameList;

    }

    public List GraphFragment(String URLFile, String NameFile) {

        /**
         * because it's load a new program, it's necessary to delete all data
         * from database, AKA the previous generated arguments + the files of
         * the subgraphs.
         */
        graphJung = new DelegateForest<>();

        List<String> filenames = new ArrayList<>();
        ArgumentSQL manage = new ArgumentSQL();
        Connection conn = null;
        conn = manage.Connect();
        filenames = manage.SelectFilenames(conn);

        for (Iterator<String> it = filenames.iterator(); it.hasNext();) {
            String string = it.next();
            try {
                /*
                 try {
                 System.out.println(new File(".").getCanonicalPath());
                 System.out.println("DIR IN DELETE:" + new File("../examples/").getCanonicalPath());

                 } catch (java.io.IOException ex) {
                 System.out.println("ERROR IN DELETE." + ex.getLocalizedMessage());
                 }
                 */

                String filetodel = new File("../examples/" + string).getCanonicalPath();
                //System.out.println("Name of file:"+clause_s);

                String xwam = string.replace(".P", ".xwam");

                File fileD = new File(filetodel);
                fileD.delete();

                fileD = new File(xwam);
                fileD.delete();

                /*System.out.println(new File(".").getCanonicalPath());
                 System.out.println("DIR:" + new File("../examples/").getCanonicalPath());
                 System.out.println("DIR:" + new File("../examples/" + clause_s).getCanonicalPath());
                 */
            } catch (java.io.IOException ex) {
                System.out.println("ERROR deleting files:" + ex.getLocalizedMessage());
            }
        }

        manage.deleteRecords(conn);
        manage.CloseConnection(conn);

        int NumTotRules = 0;

        UndirectedGraph<String, DefaultEdge> graph = new SimpleGraph<String, DefaultEdge>(DefaultEdge.class);

//System.out.println("(OK)\t file to load:" + URLFile + NameFile);
        File file = new File(URLFile + NameFile);

        // Standard Map
        Map<Integer, String> mapNumRuleHead = new HashMap<Integer, String>();
        Map<Integer, String> mapNumRuleFull = new HashMap<Integer, String>();
        Map<String, Integer> mapAtomCompon = new HashMap<String, Integer>();

        /*    
         map.put("Lars", 1);
         map.put("Lars", 2);
         map.put("Lars", 1);
    
         assertEquals(map.get("Lars"), 1);

         for (int i = 0; i < 100; i++) {
         map.put(String.valueOf(i), i);
         }
         assertEquals(map.size(), 101);

         assertEquals(map.get("51"), 51);
         map.keySet();
         */
        //List 
        try {
            FileReader reader = new FileReader(file);
            BufferedReader buffReader = new BufferedReader(reader);
            // define el numero de componentes, solo hay 1 porque se abre un solo
            // archivo .P, en el futuro debería ser dinámico
            int numComponents = 0;
            numComponents = 1;
            int numofComponents, numofRules = 0;

            //Este es el numero (id) del componente! modificarlo
            numofComponents = 1;
            //read every rule of the program
            String ruleLine;
            while ((ruleLine = buffReader.readLine()) != null) {
                int x = 0;
                numofRules++;

                //System.out.println("(OK)\t------------------------------------------------------------- Num of rule:" + numofRules);
//                System.out.println("(OK)\t ruleLine:" + ruleLine);
                StringTokenizer headToken = new StringTokenizer(ruleLine, HeadSplitter, false);
                String temporalRule = "";

                String temporalHead = "";
                String temporalAtomBody = "";

                while (headToken.hasMoreTokens()) {

                    x++;
                    //                System.out.println("(OK)\t ++++++++++++++++++++++++++++++++++++++++++++++ x:" + x);
                    //  System.out.println("countTokens::"+headToken.countTokens());
                    temporalRule = headToken.nextToken();

                    mapNumRuleFull.put(numofRules, ruleLine);

                    if (x == 1) {//esta es la cabeza
                        if (temporalRule.isEmpty() || temporalRule.startsWith(":")) {
                            //System.out.println("(OK)\t Esta no es una regla");// con esto se soluciona el problema de la regla "auto_table"
                        } else {
                            temporalHead = HeadCleaner(temporalRule);
                            graph.addVertex(temporalHead);//Head as a vertex
                            mapNumRuleHead.put(numofRules, temporalHead);//mapping [#of the rule(index), head of the rule(value)]
                            //System.out.println("(OK)\t HEAD>" + temporalHead + "<");

                            //jung graph for GUI purposes
                            graphJung.addVertex(temporalHead);

                        }
                    }
                    if (x > 1) {
                        //                      System.out.println("(OK)\t  BODY>" + temporalRule + "<");
                        StringTokenizer bodyToken = new StringTokenizer(temporalRule, AtomSplitter, false);
                        String temporalAtom = "";

                        while (bodyToken.hasMoreTokens()) {
                            temporalAtom = bodyToken.nextToken();
                            if (temporalAtom.contains("auto_table") || temporalAtom.isEmpty()) {
                                //System.out.println("(OK)\tel body no tiene atoms");
                            } else {
                                temporalAtomBody = AtomCleaner(temporalAtom);
                                graph.addVertex(temporalAtomBody);
                                //System.out.println("(OK)\t EDGES:" + temporalHead + "-----" + temporalAtomBody);
                                //DefaultEdge edgeClause = new DefaultEdge();

                                graph.addEdge(temporalHead, temporalAtomBody);//,edgeClause);

                                //graphJung is for GUI purposes
                                graphJung.addEdge(edgeFactory.create(), temporalHead, temporalAtomBody);
                                /**
                                 * *
                                 * graph.addVertex("K0");
                                 * graph.addEdge(edgeFactory.create(), "K0",
                                 * "K1");
                                 *
                                 */
                                C.add(temporalHead);
                                S.add(temporalAtomBody);

                                //System.out.println(">>>temporalAtomBody:" + temporalAtomBody + "   >>> temporaHead:" + temporalHead);
                                //System.out.println("graph.getEdgeTarget(edgeClause):"+graph.getEdgeTarget(edgeClause));
                                //System.out.println("graph.getEdgeSource(edgeClause):"+graph.getEdgeSource(edgeClause));
                                ArgumentSQL manage2 = new ArgumentSQL();
                                Connection conn2 = null;
                                conn2 = manage2.Connect();
                                manage2.InsertClauses(temporalHead, ruleLine, "U", conn2);
//INSERT CLAU                                
                                manage2.CloseConnection(conn2);

                            }
                        }
                    }
                }

                NumTotRules = numofRules;
            }
        } catch (IOException e) {
            //handle exception
        }

        mapAtomCompon = ConnectivityAnalysis(graph);

        List nameList = new ArrayList();

        for (Map.Entry<String, Integer> entry : mapAtomCompon.entrySet()) {
            String atom = entry.getKey();
            Integer comp = entry.getValue();
            for (Map.Entry<Integer, String> entry1 : mapNumRuleFull.entrySet()) {
                String fullRule = entry1.getValue();
                Integer ruleMap1 = entry1.getKey();

                if (fullRule.contains(atom)) {
                    //System.out.println("string0:" + atom + "  string1:" + fullRule + "  en el componente:" + comp + "  file:" + comp + NameFile);
                    //WriteSubGraph(URLFile, comp + NameFile, fullRule);
                    /**
                     * *
                     * CREATES SUBGRAPH FILES COMPONENT-SUBPROGRAM
                     */
//                    nameList.add(WriteSubGraph(URLFile, comp + NameFile, fullRule + "\n"));
                }
            }
        }
        //System.out.println("ANTES-> Csize" + C.size() + "  Ssize:" + S.size());

        return nameList;

    }

    private String HeadCleaner(String dirtyRule) {
        String Splitter1 = ":";
        StringTokenizer bodyToken = new StringTokenizer(dirtyRule, Splitter1);
        String temporalAtom = "";
        temporalAtom = bodyToken.nextToken();
        if (temporalAtom.contains(".")) {
            temporalAtom = temporalAtom.replace(".", "");
        }
        return temporalAtom;
    }

    private String ClauseCleaner1(String dirtyRule) {
        String temporalAtom = "";

        if (dirtyRule.contains("[")) {
            temporalAtom = dirtyRule.replace("[", "");
        }
        if (temporalAtom.contains("]")) {
            temporalAtom = temporalAtom.replace("]", "");
        }
        if (temporalAtom.contains(" ")) {
            temporalAtom = temporalAtom.replace(" ", "");
        }
        if (temporalAtom.contains(".,")) {
            temporalAtom = temporalAtom.replace(".,", ".\r\n");
        }
        return temporalAtom;
    }

    private String ClauseCleaner2(String dirtyRule) {
        String temporalAtom = "";

        if (dirtyRule.contains("\n")) {
            dirtyRule = dirtyRule.replace("\n", "");
        }
        if (dirtyRule.contains(" ")) {
            dirtyRule = dirtyRule.replace(" ", "");
        }
        temporalAtom = dirtyRule;
        return temporalAtom;
    }

    private String AtomCleaner(String dirtyAtom) {
        String Splitter1 = ".";
        String Splitter2 = ")";
        StringTokenizer bodyToken = new StringTokenizer(dirtyAtom, Splitter1);
        String temporalAtom = "";
        temporalAtom = bodyToken.nextToken();

        if (temporalAtom.contains("tnot")) {
            temporalAtom = temporalAtom.replace("tnot(", "");
            temporalAtom = temporalAtom.replace("))", ")");
        }
        return temporalAtom;
    }

    private Map<String, Integer> ConnectivityAnalysis(UndirectedGraph<String, DefaultEdge> graphConnected) {

        Map<String, Integer> map = new HashMap<String, Integer>();

        /*
         Iterator<String> iter =
         new DepthFirstIterator<String, DefaultEdge>(graphConnected);
         Object vertex;
         String verto;
         while (iter.hasNext()) {
         vertex = iter.next();
         //System.out.println(" () Vertex " + vertex.toString() + " is connected to: " + graphConnected.edgesOf(vertex.toString()));
            
         }
         */
        ConnectivityInspector<String, DefaultEdge> conn = new ConnectivityInspector<String, DefaultEdge>(graphConnected);
        List<Set<String>> conVertSets = conn.connectedSets();
        for (Integer j = 0; j < conVertSets.size(); j++) {
            Iterator<String> tai = conVertSets.get(j).iterator();
            while (tai.hasNext()) {
                String atom = tai.next();
                map.put(atom, j);
                //System.out.println("str:" + atom + " of comp:" + j);
                ArgumentSQL manage2 = new ArgumentSQL();
                Connection conn2 = null;
                conn2 = manage2.Connect();
                manage2.UpdateSubgraphValues(atom, j, conn2);
                manage2.CloseConnection(conn2);
            }

        }
        return map;
    }

    public List<String> CleanHead() {
        List<String> list1 = new ArrayList<>();
        ArgumentSQL manage2 = new ArgumentSQL();
        Connection conn2 = manage2.Connect();
        list1 = manage2.SelectHEADS(conn2);
        manage2.CloseConnection(conn2);
        return list1;
    }

    private String WriteSubGraph(String URL, String nameFile, String content) {
        String subProg = "";
        //       if (!content.isEmpty()) {
        try {

            //String content = "This is the content to write into file";
            //List filenames =  new ArrayList();
            File file = new File(URL + nameFile);
            //System.out.println("\t URL+nameFile:" + URL + nameFile);
            //filenames.add("subg_" +nameFile);
            subProg = "subg_" + nameFile;

            String auto = ":-auto_table. \n";

            content = auto + content;

            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();

            //System.out.println("(OK) \t Writing subgraph in a new file " + subProg);
        } catch (IOException e) {
            e.printStackTrace();
        }

//        }
        return subProg;
    }

    public List<String> RetrieveArguments() {
        List<String> args = new ArrayList<>();
        ArgumentSQL manage2 = new ArgumentSQL();
        Connection conn2 = null;
        conn2 = manage2.Connect();
        args = manage2.SelectArguments(conn2);
        manage2.CloseConnection(conn2);
        return args;
    }

    public List<String> RetrieveFragments() {
        List<String> args = new ArrayList<>();
        ArgumentSQL manage2 = new ArgumentSQL();
        Connection conn2 = null;
        conn2 = manage2.Connect();

        args = manage2.SelectArguments(conn2);

        manage2.CloseConnection(conn2);
        return args;
    }

    public List<String> RetrieveHeads() {
        List<String> args = new ArrayList<>();
        ArgumentSQL manage2 = new ArgumentSQL();
        Connection conn2 = null;
        conn2 = manage2.Connect();
        args = manage2.SelectHEADS(conn2);
        manage2.CloseConnection(conn2);
        return args;
    }

    public List<String> RetrieveSubGraphs() {
        List<String> args = new ArrayList<>();
        ArgumentSQL manage2 = new ArgumentSQL();
        Connection conn2 = null;
        conn2 = manage2.Connect();
        args = manage2.SelectSUBGRAPH(conn2);
        manage2.CloseConnection(conn2);
        return args;
    }

    /**
     *
     * @return
     */
    public List<String> ArgumentComposer() {

        int maxNumComp = 0;
        List<String> clauseSubgr = new ArrayList();
        List<String> powerSettedCl = new ArrayList();
        List<String> powerSettedCl2 = new ArrayList();
        List<String> fileNames = new ArrayList<>();

        ArgumentSQL manage3 = new ArgumentSQL();
        Connection conn3 = null;
        conn3 = manage3.Connect();
        //manage3.GetSupports(conn3);
        maxNumComp = manage3.GetMaxSubgraph(conn3);
        //         System.out.println("\t\t ******* number of generated subgrpahs"+maxNumComp);
        int j = 0;

        for (int i = 0; i <= maxNumComp; i++) {
            clauseSubgr = manage3.GetSubgraphs(conn3, i);//obtiene todas las clausulas de un dado subgrafo
 //           System.out.println("------------------SUBGRAPH -----:" + "  i::" + i + "\n");
            powerSettedCl = PowerSet(clauseSubgr);

            LinkedHashSet myPowerSet = powerset(clauseSubgr);//power set de todas las clausulas para un dado subgrafo

            Iterator iter = myPowerSet.iterator();

            while (iter.hasNext()) {
                String clause_s, clause_t, tempCla = "";

                tempCla = iter.next().toString();

                clause_s = ClauseCleaner1(tempCla);
                clause_t = ClauseCleaner2(clause_s);

//                System.out.println("tempCla:" + tempCla); //todas las clausulas de un dado subgrafo
//                System.out.println("clause_s:_i1_" + clause_s + "_f1_");
 //               System.out.println("clause_t:_i2_" + clause_t + "_f2_");

                tempCla = tempCla.replace("[", "");
                tempCla = tempCla.replace("]", "");
                tempCla = tempCla.replace(",", "");
                tempCla = tempCla.replace(" ", "");
  //              System.out.println("new tempCla:" + tempCla); //todas las clausulas de un dado subgrafo

                /**
                 * *
                 * si esta vacio o si es un fact o conjunto de facts, entonces
                 * se genera manualmente el argument
                 *
                 * pero si tiene clausulas se deja que XSB las evalue
                 *
                 */
                //if (!clause_s.contains(":")) {
                if (!tempCla.contains(":")) {

                    j = j + 1;
//                    System.out.println("\t///ini///////////FACT____:" + clause_t + ":___");
//                    System.out.println("\t///ini///////////FACT__tempCla__:" + tempCla + ":___");
                    manage3.InsertSubpClauses("Sbg" + i + "set" + j + ".P", conn3);// add a new record of 
                    int idsub = manage3.SelectIDFromSubprog("Sbg" + i + "set" + j + ".P", conn3);

                    StringTokenizer clauseINToken = new StringTokenizer(clause_t, ".", false);
                    while (clauseINToken.hasMoreTokens()) {
                        String clau = clauseINToken.nextToken().toString();

                        int idcla = manage3.SelectIDFromClauses(clau + ".", conn3);
//                        System.out.println("\t\t CLAXX   :" + clau + "  index:" + idcla + "  idsub:" + idsub + "   clause_s:" + clause_t);
                        manage3.UpdateSubprValues(idcla, idsub, conn3);

                    }

                    WriteSubGraph("../examples/", "Sbg" + i + "set" + j + ".P", clause_s);
 //                   System.out.println("(OK) Atoms inferred from a FACT:" + clause_t + "  in file :" + "Sbg" + i + "set" + j + ".P" + "  IN SUB:" + i);
//                    System.out.println("\t///end///////////FACT//////// \n");
                    //manage3.UpdateInferredValues("Sbg" + i + "set" + j + ".P", clause_s, conn3);
                    manage3.InsertHeads(clause_s, "Sbg" + i + "set" + j + ".P", conn3);

                } else {

                    j = j + 1;
//                    System.out.println("\t///ini##############CLAUSE########" + clause_t);
//                    System.out.println("\t///ini##############CLAUSE######tempCla##" + tempCla);
//
//                    System.out.println("\t\t\t -->clause_s:" + clause_s);
//                    System.out.println("\t\t\t -->clause_t:" + clause_t);
//                    System.out.println("\t\t\t -->tempCla:" + tempCla);
//                    System.out.println("\t\t\t -->subgraph_file:" + "Sbg" + i + "set" + j + ".P");

                    manage3.InsertSubpClauses("Sbg" + i + "set" + j + ".P", conn3);// add a new record of 
                    int idsub = manage3.SelectIDFromSubprog("Sbg" + i + "set" + j + ".P", conn3);
//                    System.out.println("\t\t\t -->id de subgraph_file inserted_idsub:" + idsub + "___file:" + "Sbg" + i + "set" + j + ".P");

                    //StringTokenizer clauseINToken = new StringTokenizer(clause_t, ".", false);
                    StringTokenizer clauseINToken = new StringTokenizer(tempCla, ".", false);

                    //System.out.println("NUMERO DE TOKENS de:" + clause_t + "___#:" + clauseINToken.countTokens());
//                    System.out.println("NUMERO DE TOKENS de tempCla:" + tempCla + "___#:" + clauseINToken.countTokens());
                    while (clauseINToken.hasMoreTokens()) {
                        String clau = clauseINToken.nextToken().toString();
//                        System.out.println("\t\t\t -->primer token:" + clau);
                        int idcla = manage3.SelectIDFromClauses(clau + ".", conn3);
//                        System.out.println("\t\t\t -->id del primer token:" + idcla);
//                        System.out.println("\t\t CLAXX2   :" + clau + "  index:" + idcla + "  idsub:" + idsub + "   clause_s:" + clause_t);
                        manage3.UpdateSubprValues(idcla, idsub, conn3);
                    }

                    WriteSubGraph("../examples/", "Sbg" + i + "set" + j + ".P", clause_s);
                    fileNames.add("Sbg" + i + "set" + j + ".P");
//                    System.out.println("(OK2) Atoms inferred from CLAUSE:" + clause_s + "  in file :" + "Sbg" + i + "set" + j + ".P" + "  IN SUB:" + i);
//                    System.out.println("\t///end##############CLAUSE######## \n");
                }
            }
        }

        manage3.CloseConnection(conn3);

        return fileNames;
    }

    /**
     *
     * @return
     */
    public List<String> FragmentComposer() {

        int maxNumComp = 0;
        List<String> clauseSubgr = new ArrayList();
        List<String> powerSettedCl = new ArrayList();
        List<String> powerSettedCl2 = new ArrayList();
        List<String> fileNames = new ArrayList<>();

        ArgumentSQL manage3 = new ArgumentSQL();
        Connection conn3 = null;
        conn3 = manage3.Connect();
        //manage3.GetSupports(conn3);
        maxNumComp = manage3.GetMaxSubgraph(conn3);
        //    System.out.println("\t\t ******* number of generated subgrpahs"+maxNumComp);
        int j = 0;

        for (int i = 0; i <= maxNumComp; i++) {
            clauseSubgr = manage3.GetSubgraphs(conn3, i);
//            System.out.println("------------------SUBGRAPH -----:" + "  i::" + i + "\n");
            powerSettedCl = PowerSet(clauseSubgr);

            LinkedHashSet myPowerSet = powerset(clauseSubgr);

            Iterator iter = myPowerSet.iterator();

            while (iter.hasNext()) {
                String clause_s, clause_t, tempCla = "";

                tempCla = iter.next().toString();

                clause_s = ClauseCleaner1(tempCla);
                clause_t = ClauseCleaner2(clause_s);

//                System.out.println("clause_s:" + clause_s);
//                System.out.println("clause_t:" + clause_t);
                /**
                 * *
                 * si esta vacio o si es un fact o conjunto de facts, entonces
                 * se genera manualmente el argument
                 *
                 * pero si tiene clausulas se deja que XSB las evalue
                 *
                 */
                if (!clause_s.contains(":")) {

                }
                if (!clause_s.contains(":")) {

                    j = j + 1;
//                    System.out.println("\t\n///ini///////////FACT////////" + clause_t);
                    manage3.InsertSubpClauses("Sbg" + i + "set" + j + ".P", conn3);// add a new record of 
                    int idsub = manage3.SelectIDFromSubprog("Sbg" + i + "set" + j + ".P", conn3);

                    StringTokenizer clauseINToken = new StringTokenizer(clause_t, ".", false);
                    while (clauseINToken.hasMoreTokens()) {
                        String clau = clauseINToken.nextToken().toString();

                        int idcla = manage3.SelectIDFromClauses(clau + ".", conn3);
//                        System.out.println("\t\t CLAXX   :" + clau + "  index:" + idcla + "  idsub:" + idsub + "   clause_s:" + clause_t);
                        manage3.UpdateSubprValues(idcla, idsub, conn3);
                    }

                    WriteSubGraph("../examples/", "Sbg" + i + "set" + j + ".P", clause_s);

//                    System.out.println("(OK) Atoms inferred from a FACT:" + clause_t + "  in file :" + "Sbg" + i + "set" + j + ".P" + "  IN SUB:" + i);

//                    System.out.println("\t///end///////////FACT//////// \n");
                    //manage3.UpdateInferredValues("Sbg" + i + "set" + j + ".P", clause_s, conn3);
                    manage3.InsertHeads(clause_s, "Sbg" + i + "set" + j + ".P", conn3);

                } else {

                    j = j + 1;
//                    System.out.println("\t\n///ini##############CLAUSE########" + clause_t);

                    //System.out.println("\t\t\t -->CLAUSE:"+clause_s);
                    manage3.InsertSubpClauses("Sbg" + i + "set" + j + ".P", conn3);// add a new record of 
                    int idsub = manage3.SelectIDFromSubprog("Sbg" + i + "set" + j + ".P", conn3);
//                    System.out.println("\t++++++++++++CLAUSES FOR UPDATING TABLE++++++\n");
                    StringTokenizer clauseINToken = new StringTokenizer(clause_t, ".", false);
//                    System.out.println("NUMERO DE TOKENS de:" + clause_t + "___#:" + clauseINToken.countTokens());
                    while (clauseINToken.hasMoreTokens()) {
                        String clau = clauseINToken.nextToken().toString();

                        int idcla = manage3.SelectIDFromClauses(clau + ".", conn3);
//                        System.out.println("\t\t CLAXX2   :" + clau + "  index:" + idcla + "  idsub:" + idsub + "   clause_s:" + clause_t);
                        manage3.UpdateSubprValues(idcla, idsub, conn3);
                    }

                    WriteSubGraph("../examples/", "Sbg" + i + "set" + j + ".P", clause_s);
                    fileNames.add("Sbg" + i + "set" + j + ".P");
//                    System.out.println("(OK2) Atoms inferred from CLAUSE:" + clause_s + "  in file :" + "Sbg" + i + "set" + j + ".P" + "  IN SUB:" + i);
//                    System.out.println("\t///end##############CLAUSE######## \n");
                }

            }
        }

        manage3.CloseConnection(conn3);

        return fileNames;
    }

    /**
     * Returns the POower Set of a given List of elements
     *
     * @param dataList
     * @return
     */
    public List PowerSet(List<String> dataList) {

        List<String> output = new ArrayList();
        List<String> data = dataList;

        String all = "";

        for (int i = 0; i < data.size(); i++) {
            for (int j = 0; j < data.size(); j++) {
                // System.out.println("i:" + i + "  j:" + j);
                if (i == j) {
                    // System.out.println("\t i=j: " + i);
                    output.add(data.get(i));
                } else {
                    // System.out.println("\t [" + i + "-" + j + "]:");
                    output.add(data.get(i) + "\n" + data.get(j));
                }

            }
            all = all + (String) data.get(i).toString();
        }
        //System.out.println("all:" + all);
        return output;
    }
    /*
     public static <T> Set<Set<T>> powerSet(Set<T> originalSet) {
     Set<Set<T>> sets = new HashSet<Set<T>>();
     if (originalSet.isEmpty()) {
     sets.add(new HashSet<T>());
     return sets;
     }
     List<T> list = new ArrayList<T>(originalSet);
     T head = list.get(0);
     Set<T> rest = new HashSet<T>(list.subList(1, list.size()));
     for (Set<T> set : powerSet(rest)) {
     Set<T> newSet = new HashSet<T>();
     newSet.add(head);
     newSet.addAll(set);
     sets.add(newSet);
     sets.add(set);
     }
     return sets;
     }

     */

    /**
     * Returns the power set from the given set by using a binary counter
     * Example: S = {a,b,c} P(S) = {[], [c], [b], [b, c], [a], [a, c], [a, b],
     * [a, b, c]}
     *
     * @param set String[]
     * @return LinkedHashSet
     */
    private static LinkedHashSet powerset(String[] set) {

        //create the empty power set
        LinkedHashSet power = new LinkedHashSet();

        //get the number of elements in the set
        int elements = set.length;

        //the number of members of a power set is 2^n
        int powerElements = (int) Math.pow(2, elements);

        //run a binary counter for the number of power elements
        for (int i = 0; i < powerElements; i++) {

            //convert the binary number to a clause_s containing n digits
            String binary = intToBinary(i, elements);

            //create a new set
            LinkedHashSet innerSet = new LinkedHashSet();

            //convert each digit in the current binary number to the corresponding element
            //in the given set
            for (int j = 0; j < binary.length(); j++) {
                if (binary.charAt(j) == '1') {
                    innerSet.add(set[j]);
                }
            }

            //add the new set to the power set
            power.add(innerSet);

        }

        return power;
    }

    /**
     * Returns the power set from the given set by using a binary counter
     * Example: S = {a,b,c} P(S) = {[], [c], [b], [b, c], [a], [a, c], [a, b],
     * [a, b, c]}
     *
     * @param set String[]
     * @return LinkedHashSet
     */
    private static LinkedHashSet powerset(List<String> setList) {

        String set[] = {};

        for (Iterator<String> it = setList.iterator(); it.hasNext();) {
            String string = it.next();

        }

        //create the empty power set
        LinkedHashSet power = new LinkedHashSet();

        //get the number of elements in the set
//        int elements = set.length;
        int elements = setList.size();

        //the number of members of a power set is 2^n
        int powerElements = (int) Math.pow(2, elements);

        //run a binary counter for the number of power elements
        for (int i = 0; i < powerElements; i++) {

            //convert the binary number to a clause_s containing n digits
            String binary = intToBinary(i, elements);

            //create a new set
            LinkedHashSet innerSet = new LinkedHashSet();

            //convert each digit in the current binary number to the corresponding element
            //in the given set
            for (int j = 0; j < binary.length(); j++) {
                if (binary.charAt(j) == '1') {
                    //innerSet.add(set[j]);
                    innerSet.add(setList.get(j));
                }
            }

            //add the new set to the power set
            power.add(innerSet);

        }

        return power;
    }

    /**
     * Converts the given integer to a String representing a binary number with
     * the specified number of digits For example when using 4 digits the binary
     * 1 is 0001
     *
     * @param binary int
     * @param digits int
     * @return String
     */
    private static String intToBinary(int binary, int digits) {

        String temp = Integer.toBinaryString(binary);
        int foundDigits = temp.length();
        String returner = temp;
        for (int i = foundDigits; i < digits; i++) {
            returner = "0" + returner;
        }

        return returner;
    }

    public void ArgumentWriter() {
        List<String> headList = new ArrayList<>();
        List<Integer> idSubpList = new ArrayList<>();
        List<Integer> REFCLAList = new ArrayList<>();
        List<String> ClauseList = new ArrayList<>();
        ArgumentSQL manage2 = new ArgumentSQL();
        Connection conn2 = null;
        conn2 = manage2.Connect();
        headList = manage2.SelectHEADS(conn2);//se obtienen todas las HEADS


        /*
         for (Iterator<String> it = headList.iterator(); it.hasNext();) {
         String head = it.next();
         idSubpList = manage2.SelectIDFromSubprog2(head, conn2);//se obtiene IDs de subprograms
         for (Iterator<Integer> it1 = idSubpList.iterator(); it1.hasNext();) {
         Integer idSubprg = it1.next();
         REFCLAList = manage2.SelectREFCLA(idSubprg, conn2);
         for (Iterator<Integer> it2 = REFCLAList.iterator(); it2.hasNext();) {
         Integer idClause = it2.next();
         ClauseList = manage2.SelectClause(idClause, conn2);
         for (Iterator<String> it3 = ClauseList.iterator(); it3.hasNext();) {
         String clause_s = it3.next();
         System.out.println("clause;" + clause_s);
         }
         }
         }
         }
         */
        for (int i = 0; i < headList.size(); i++) {
            String head = headList.get(i);
            String clauses = "";
            int idLastArgument = 0;
            List<Integer> idClauses = new ArrayList<Integer>();

//            idSubpList = manage2.SelectIDFromSubprog2(head, conn2);//se obtiene IDs de subprograms donde ya se sabe que "head" ha sido inferida por XSB como True
            idSubpList = manage2.SelectIDSubpFromInferHead(head, conn2);//se obtiene IDs de subprograms donde ya se sabe que "head" ha sido inferida por XSB como True

            for (int j = 0; j < idSubpList.size(); j++) {
                Integer idSubprg = idSubpList.get(j);
                REFCLAList = manage2.SelectREFCLA(idSubprg, conn2);

                for (int k = 0; k < REFCLAList.size(); k++) {
                    Integer idClause = REFCLAList.get(k);
                    ClauseList = manage2.SelectClause(idClause, conn2);// se obtiene la lista de cláusulas pertenecientes a un subgraph (idSubprg) y que se sabe que tienen una HEAD inferida a True

                    for (int l = 0; l < ClauseList.size(); l++) {
                        String individualCla = ClauseList.get(l);

                        idClauses.add(manage2.SelectIDFromClauses(individualCla, conn2));
                        if (!clauses.contains(individualCla)) {
                            clauses = clauses + individualCla;
                        }
                    }
                }
            }

            manage2.InsertArgument(clauses, head, conn2);

            idLastArgument = manage2.GetLastArgument(conn2);

            manage2.InsertClausesArgs(idClauses, idLastArgument, conn2);
//INSERT CLAU
        }

        //manage2.UpdateSubgraphValues(atom, j, conn2);
        manage2.CloseConnection(conn2);
    }

    public void ArgumentWriter2() {
        List<String> headList = new ArrayList<>();
        List<String> headList2 = new ArrayList<>();

        List<Integer> idSubpList = new ArrayList<>();
        List<Integer> REFCLAList = new ArrayList<>();
        List<String> ClauseList = new ArrayList<>();
        ArgumentSQL manage2 = new ArgumentSQL();
        Connection conn2 = null;
        conn2 = manage2.Connect();

        //headList2 = manage2.SelectHeadFromSubprog2(conn2);
        headList2 = manage2.SelectHeadFromInferredHeads(conn2);

        for (Iterator<String> it = headList2.iterator(); it.hasNext();) {
            String inferHead = it.next();

            String clauses = "";
            int idLastArgument = 0;
            List<Integer> idClauses = new ArrayList<Integer>();

            //idSubpList = manage2.SelectIDFromSubprog2(idHeadSub, conn2);//se obtiene IDs de subprograms donde ya se sabe que "head" ha sido inferida por XSB como True
            idSubpList = manage2.SelectIDSubpFromInferHead(inferHead, conn2);//se obtiene IDs de subprograms donde ya se sabe que "head" ha sido inferida por XSB como True

            for (int j = 0; j < idSubpList.size(); j++) {
                Integer idSubprg = idSubpList.get(j);
                REFCLAList = manage2.SelectREFCLA(idSubprg, conn2);

                for (int k = 0; k < REFCLAList.size(); k++) {
                    Integer idClause = REFCLAList.get(k);
                    ClauseList = manage2.SelectClause(idClause, conn2);// se obtiene la lista de cláusulas pertenecientes a un subgraph (idSubprg) y que se sabe que tienen una HEAD inferida a True

                    for (int l = 0; l < ClauseList.size(); l++) {
                        String individualCla = ClauseList.get(l);

 //                       System.out.println("INDIVIDUAL CLAUSE:" + individualCla + "__>>" + inferHead);

                        
                        
                         inferHead = inferHead.replace("\r\n", "").replace("\n", "");
/*                        
                        System.out.println("indCla:"+text);
                        StringTokenizer clawToken = new StringTokenizer(text, ".", false);
                        while (clawToken.hasMoreTokens()) {
                            System.out.println("NUMOFINT TOK:"+clawToken.countTokens());
                            String tok = clawToken.nextToken();
                            System.out.println("INDIVIDUAL CLAUSE2:" + tok + ":END");
                        }
*/
                        idClauses.add(manage2.SelectIDFromClauses(individualCla, conn2));

                        if (!clauses.contains(individualCla)) {
                            clauses = clauses + "  " + individualCla;
                        }
                    }
                }
            }

            manage2.InsertArgument(clauses, inferHead, conn2);

            idLastArgument = manage2.GetLastArgument(conn2);

            manage2.InsertClausesArgs(idClauses, idLastArgument, conn2);

        }

        //manage2.UpdateSubgraphValues(atom, j, conn2);
        manage2.CloseConnection(conn2);
    }

    /**
     * Creates subgraph files and generates the graph for the GUI
     *
     * @param URLFile
     * @param NameFile
     * @return
     */
    public List GraphAnalysis4Frag(String URLFile, String NameFile) {

        /**
         * because it's load a new program, it's necessary to delete all data
         * from database, AKA the previous generated arguments + the files of
         * the subgraphs.
         */
        graphJung = new DelegateForest<>();

        List<String> filenames = new ArrayList<>();
        ArgumentSQL manage = new ArgumentSQL();
        Connection conn = null;
        conn = manage.Connect();
        filenames = manage.SelectFilenames(conn);

        for (Iterator<String> it = filenames.iterator(); it.hasNext();) {
            String string = it.next();
            try {

                String filetodel = new File("../examples/" + string).getCanonicalPath();
                String xwam = string.replace(".P", ".xwam");

                File fileD = new File(filetodel);
                fileD.delete();

                fileD = new File(xwam);
                fileD.delete();

            } catch (java.io.IOException ex) {
                System.out.println("ERROR deleting files:" + ex.getLocalizedMessage());
            }
        }

        manage.deleteRecords(conn);
        manage.CloseConnection(conn);

        int NumTotRules = 0;

        UndirectedGraph<String, DefaultEdge> graph = new SimpleGraph<String, DefaultEdge>(DefaultEdge.class);

        File file = new File(URLFile + NameFile);

        // Standard Map
        Map<Integer, String> mapNumRuleHead = new HashMap<Integer, String>();
        Map<Integer, String> mapNumRuleFull = new HashMap<Integer, String>();
        Map<String, Integer> mapAtomCompon = new HashMap<String, Integer>();

        try {
            FileReader reader = new FileReader(file);
            BufferedReader buffReader = new BufferedReader(reader);
            // define el numero de componentes, solo hay 1 porque se abre un solo
            // archivo .P, en el futuro debería ser dinámico
            int numComponents = 0;
            numComponents = 1;
            int numofComponents, numofRules = 0;

            //Este es el numero (id) del componente! modificarlo
            numofComponents = 1;
            //read every rule of the program
            String ruleLine;
            while ((ruleLine = buffReader.readLine()) != null) {
                int x = 0;
                numofRules++;
                String headx = "";

                //               System.out.println("(OK)\t-------------------------RULE:"+ruleLine+"---------------------------------- Num of rule:" + numofRules);
                //               System.out.println("(OK)\t ruleLine:" + ruleLine);
                StringTokenizer headToken = new StringTokenizer(ruleLine, HeadSplitter, false);
                String temporalRule, tempRule2 = "";

                String temporalHead = "";
                String temporalAtomBody = "";
                String tempActiv = "";

                while (headToken.hasMoreTokens()) {

                    x++;

//System.out.println("countTokens::"+headToken.countTokens());
                    temporalRule = headToken.nextToken();
//                    System.out.println("(OK)\t ++++++++++++++++++++++++++++++++++++++++++++++ x:" + x + "___rule:" + temporalRule);
                    mapNumRuleFull.put(numofRules, ruleLine);

                    if (x == 1) {//esta es la cabeza
                        if (temporalRule.isEmpty() || temporalRule.startsWith(":") || temporalRule.startsWith("act")) {
                            //      System.out.println("(OK)\t Esta no es una regla:" + temporalRule);// con esto se soluciona el problema de la regla "auto_table"

                            List<String> actGoals = new ArrayList<>();
                            List<String> actGoals2 = new ArrayList<>();
                            List<String> actActiv = new ArrayList<>();

                            String actTemp, glName = "";
                            String actName = "";

                            temporalRule = temporalRule.replace("act(", "");
                            tempActiv = temporalRule.replace(").", "");
                            //       System.out.println("tempactivity:" + tempActiv);
                            StringTokenizer clauToken = new StringTokenizer(tempActiv, ",", false);
                            ArgumentSQL manage2 = new ArgumentSQL();
                            Connection conn2 = null;
                            conn2 = manage2.Connect();
                            while (clauToken.hasMoreElements()) {
                                actTemp = clauToken.nextToken();
                                //                               System.out.println("\t\t\t SHOW TOKEN:" + actTemp);
                                if (actTemp.contains("p(")) {
                                    glName = actTemp;
                                    actGoals2.add(glName);
                                    actGoals = manage2.SelectGoals(actTemp, conn2);
                                    if (actGoals.isEmpty()) {
                                        //significa que no existe el goal y se debe insertar
                                        manage2.InsertGoals2(actTemp, conn2);
                                        //                                    System.out.println("!!!!!!!!!!!!!!!new goal inserted:" + glName);
                                        //idLastGoal = manage2.SelectLastGoal(conn2);

                                    }
                                }
                                if (actTemp.startsWith("act")) {
                                    actName = actTemp;
                                    actActiv = manage2.SelectActivities(actName, conn2);
                                    if (actActiv.isEmpty()) {
                                        //no existe la activity toca insertarla
                                        manage2.InsertActivity(actName, conn2);
                                        //      System.out.println("new activ inserted:" + actName);
                                    }
                                }

                            }
                            manage2.CloseConnection(conn2);

                            //                          System.out.println("SIZE OF LIST:" + actGoals2.size() + " nombre actividad:" + actName);
                            Connection conn4 = null;
                            ArgumentSQL manage4 = new ArgumentSQL();

                            conn4 = manage4.Connect();
                            for (Iterator<String> it = actGoals2.iterator(); it.hasNext();) {
                                String goalTe = it.next();
                                //                             System.out.println("Goals:" + goalTe + "_part of activ:" + actName);
                                manage2.InsertActivGoals(actName, goalTe, conn4);
                            }
                            manage4.CloseConnection(conn4);

                            //   manage2.InsertActivity(actName, conn2);//inserts a new activity
                        } else {
                            if (temporalRule.contains(".")) {
                                temporalHead = HeadCleaner(temporalRule);
                                //                              System.out.println("(OK)\t temporalHeadHEAD_" + temporalHead + "_");

                                graph.addVertex(temporalHead);//Head as a vertex
                                mapNumRuleHead.put(numofRules, temporalHead);//mapping [#of the rule(index), head of the rule(value)]

                                ArgumentSQL manage5 = new ArgumentSQL();
                                Connection conn5 = null;
                                conn5 = manage5.Connect();
                                manage5.InsertClauses(temporalHead, temporalHead + ".", "U", conn5);
//INSERT CLAU                                

//System.out.println("\t\t\t======INSERT FACT=========> head:" + temporalHead + "___fullclause:" + temporalHead);
                                manage5.CloseConnection(conn5);
                                //jung graph for GUI purposes
                                graphJung.addVertex(temporalHead);
                            } else {
                                temporalHead = HeadCleaner(temporalRule);
                                //                              System.out.println("+++s+s+s+temporalHead:"+temporalHead);
                                graph.addVertex(temporalHead);//Head as a vertex
                                mapNumRuleHead.put(numofRules, temporalHead);//mapping [#of the rule(index), head of the rule(value)]
                                //                             System.out.println("(OK)\t temporalHead" + temporalHead + "_");

                                tempRule2 = ruleLine;
                                ArgumentSQL manage2 = new ArgumentSQL();
                                Connection conn2 = null;
                                conn2 = manage2.Connect();

//                                if (ruleLine.contains(",ha(")) {
                                StringTokenizer haToken = new StringTokenizer(tempRule2, ",", false);
                                while (haToken.hasMoreElements()) {
                                    String satd = haToken.nextElement().toString();
//                                    System.out.println("--->" + satd);
                                    if (satd.contains("ha(")) {

//                                        System.out.println("CHECK:" + satd);
                                        ruleLine = ruleLine.replace("," + satd, ".");
//                                        System.out.println("CHECK:" + satd + "ruleLine:" + ruleLine);
                                    }

                                }

  //                              }
                                manage2.InsertClauses(temporalHead, ruleLine, "U", conn2);

 //                               System.out.println("\t\t\t=====INSERT CLAUSE=========> head:" + temporalHead + "___fullclause:" + ruleLine);
//INSERT CLAU                                
                                manage2.CloseConnection(conn2);

                                //jung graph for GUI purposes
                                graphJung.addVertex(temporalHead);
                            }

                        }
                    }
                    if (x > 1) {
//                                              System.out.println("(OK)\t  BODY>" + temporalRule + "<");
                        StringTokenizer bodyToken = new StringTokenizer(temporalRule, AtomSplitter, false);
                        String temporalAtom = "";
                        List<String> hactions = new ArrayList<>();
                        String haction = "";
                        String hactionTemp = "";

                        while (bodyToken.hasMoreTokens()) {
                            temporalAtom = bodyToken.nextToken();
                            //                         System.out.println(">>>>>>>>>temporalAtom:"+temporalAtom);
                            // System.out.println("");
                            if (temporalAtom.contains("auto_table") || temporalAtom.isEmpty()) {
                                //System.out.println("(OK)\tel body no tiene atoms");
                            } else {
                                temporalAtomBody = AtomCleaner(temporalAtom);
                                graph.addVertex(temporalAtomBody);
                                //                            System.out.println("(OK)\t ZZZEDGES:" + temporalHead + "-----" + temporalAtomBody);
                                //DefaultEdge edgeClause = new DefaultEdge();

                                graph.addEdge(temporalHead, temporalAtomBody);//,edgeClause);

                                //graphJung is for GUI purposes
                                graphJung.addEdge(edgeFactory.create(), temporalHead, temporalAtomBody);
                                /**
                                 * *
                                 * graph.addVertex("K0");
                                 * graph.addEdge(edgeFactory.create(), "K0",
                                 * "K1");
                                 *
                                 */
                                C.add(temporalHead);
                                S.add(temporalAtomBody);

                            }
                            if (temporalAtom.startsWith("ha(")) {

                                ArgumentSQL manage7 = new ArgumentSQL();
                                Connection conn7 = null;
                                conn7 = manage7.Connect();
                                haction = temporalAtom;

//                                System.out.println("(haction) ==>haction:" + haction + "__ruleline:" + ruleLine);
                                /*
                                
                                 if (ruleLine.startsWith("p(")) {
                                 //                                 System.out.println("(XX) RULE:" + ruleLine + "__ACTION:" + haction);                                                                        
                                 //int idclau = manage7.SelectIDFromClauses(ruleLine, conn7);
                                 manage7.InsertClausesActions(ruleLine, haction, conn7);
                                    
                                 }

                                 hactions = manage7.SelectActions(haction, conn7);
                                 */

        //                        if (hactions.isEmpty()) {
                                //no existe la action toca insertarla
                                manage7.InsertAction(haction, conn7);
                                manage7.InsertClausesActions(ruleLine, haction, conn7);

                                    //                                  System.out.println("!!!!!!!!!!!!new ACTION inserted:" + haction);
                                //                        }
                                manage7.CloseConnection(conn7);

                            }
                        }
                    }
                }
                NumTotRules = numofRules;
            }
        } catch (IOException e) {
            //handle exception
        }

        mapAtomCompon = ConnectivityAnalysis(graph);

        List nameList = new ArrayList();

        for (Map.Entry<String, Integer> entry : mapAtomCompon.entrySet()) {
            String atom = entry.getKey();
            Integer comp = entry.getValue();
            for (Map.Entry<Integer, String> entry1 : mapNumRuleFull.entrySet()) {
                String fullRule = entry1.getValue();
                Integer ruleMap1 = entry1.getKey();

                if (fullRule.contains(atom)) {
                    //System.out.println("string0:" + atom + "  string1:" + fullRule + "  en el componente:" + comp + "  file:" + comp + NameFile);
                    //WriteSubGraph(URLFile, comp + NameFile, fullRule);
                    /**
                     * *
                     * CREATES SUBGRAPH FILES COMPONENT-SUBPROGRAM
                     */
//                    nameList.add(WriteSubGraph(URLFile, comp + NameFile, fullRule + "\n"));
                }
            }
        }
        return nameList;
    }

    public void FragmentWriter() {
        List<String> headList = new ArrayList<>();
        List<String> headList2 = new ArrayList<>();

        List<Integer> idSubpList = new ArrayList<>();
        List<Integer> REFCLAList = new ArrayList<>();
        List<String> ClauseList = new ArrayList<>();
        ArgumentSQL manage2 = new ArgumentSQL();
        Connection conn2 = null;
        conn2 = manage2.Connect();

        //headList2 = manage2.SelectHeadFromSubprog2(conn2);
        headList2 = manage2.SelectHeadFromInferredHeads(conn2);

        for (Iterator<String> it = headList2.iterator(); it.hasNext();) {

//            System.out.println("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * ");

            String HeadOfSubg = it.next();

            String clauses = "";
            int idLastArgument = 0;
            List<Integer> idClauses = new ArrayList<Integer>();
            List<String> actionsList = new ArrayList<String>();
            int idIndivClause = 0;

            //idSubpList = manage2.SelectIDFromSubprog2(idHeadSub, conn2);//se obtiene IDs de subprograms donde ya se sabe que "head" ha sido inferida por XSB como True
            idSubpList = manage2.SelectIDSubpFromInferHead(HeadOfSubg, conn2);//se obtiene IDs de subprograms donde ya se sabe que "head" ha sido inferida por XSB como True

            for (int j = 0; j < idSubpList.size(); j++) {
                Integer idSubprg = idSubpList.get(j);
                REFCLAList = manage2.SelectREFCLA(idSubprg, conn2);

                for (int k = 0; k < REFCLAList.size(); k++) {
                    Integer idClause = REFCLAList.get(k);
                    ClauseList = manage2.SelectClause(idClause, conn2);// se obtiene la lista de cláusulas pertenecientes a un subgraph (idSubprg) y que se sabe que tienen una HEAD inferida a True

                    for (int l = 0; l < ClauseList.size(); l++) {
                        String individualCla = ClauseList.get(l);

 //                       System.out.println("INDIVIDUAL CLAUSE:" + individualCla + "__>>" + HeadOfSubg);

                        //actionsList.add(individualCla);
                        
                        actionsList = manage2.SelectActions(individualCla, conn2);

                        idIndivClause = manage2.SelectIDFromClauses(individualCla, conn2);
                        idClauses.add(idIndivClause);
                        if (!clauses.contains(individualCla)) {
                            clauses = clauses + " " + individualCla;
                            //                          System.out.println("\t clauses:"+clauses);
                        }
                    }
                }
            }
//            System.out.println("## clauses:"+clauses+ "__HEAD:"+idHeadSub);

            //CAMBIAR A INSERT FRAGMENT!!!!
            //manage2.InsertArgument(clauses, idHeadSub, conn2);
            String tempac = "";

            for (Iterator<String> it1 = actionsList.iterator(); it1.hasNext();) {
                String string = it1.next();
                tempac = tempac + string + ", ";

            }
  //          System.out.println("TEMPAC:"+tempac);

            //manage2.InsertFragments(clauses, idHeadSub, conn2);
            //manage2.InsertFragments(clauses + tempac, HeadOfSubg, conn2);
            manage2.InsertFragments(clauses +" ,"+ tempac, HeadOfSubg, conn2);

            int idlastarg = manage2.SelectLastArguments(conn2);
            HeadOfSubg = HeadOfSubg.replace(".", "");
            int idhead = manage2.SelectIDFromGoals(HeadOfSubg, conn2);
            if (idhead == 0) {
                manage2.InsertGoals2(HeadOfSubg, conn2);
                idhead = manage2.SelectIDFromGoals(HeadOfSubg, conn2);
            }

            //          System.out.println("last arg:"+idlastarg +"_from arg:"+clauses+ "__withhead:"+idHeadSub+"_id:"+idhead);
            manage2.InsertArgGoals(idlastarg, idhead, conn2);

            /**
             * *
             * 1. seleccionar el id del ultiimo argumento insertado 2. tomar la
             * cabeza y buscar el id de esa cabeza en Goals 3. actualizar la
             * tabla Goals-Arguments
             */
            idLastArgument = manage2.GetLastArgument(conn2);

            manage2.InsertClausesArgs(idClauses, idLastArgument, conn2);

        }

        //manage2.UpdateSubgraphValues(atom, j, conn2);
        manage2.CloseConnection(conn2);

        /*
         List<String> headList = new ArrayList<>();
         List<Integer> idSubpList = new ArrayList<>();
         List<Integer> REFCLAList = new ArrayList<>();
         List<String> ClauseList = new ArrayList<>();
         ArgumentSQL manage2 = new ArgumentSQL();
         Connection conn2 = null;
         conn2 = manage2.Connect();
         headList = manage2.SelectHEADS(conn2);//se obtienen todas las HEADS


         for (int i = 0; i < headList.size(); i++) {
         String head = headList.get(i);
         String clauses = "";
         int idLastArgument = 0;
         List<Integer> idClauses = new ArrayList<Integer>();

         idSubpList = manage2.SelectIDFromSubprog2(head, conn2);//se obtiene IDs de subprograms donde ya se sabe que "head" ha sido inferida por XSB como True

         for (int j = 0; j < idSubpList.size(); j++) {
         Integer idSubprg = idSubpList.get(j);
         REFCLAList = manage2.SelectREFCLA(idSubprg, conn2);

         for (int k = 0; k < REFCLAList.size(); k++) {
         Integer idClause = REFCLAList.get(k);
         ClauseList = manage2.SelectClause(idClause, conn2);// se obtiene la lista de cláusulas pertenecientes a un subgraph (idSubprg) y que se sabe que tienen una HEAD inferida a True

         for (int l = 0; l < ClauseList.size(); l++) {
         String goalTe = ClauseList.get(l);

         idClauses.add(manage2.SelectIDFromClauses(goalTe, conn2));
         if (!clauses.contains(goalTe)) {
         clauses = clauses + goalTe;
         }
         }
         }
         }

         manage2.InsertArgument(clauses, head, conn2);

         idLastArgument = manage2.GetLastArgument(conn2);

         manage2.InsertClausesArgs(idClauses, idLastArgument, conn2);

         }

         //manage2.UpdateSubgraphValues(atom, j, conn2);
         manage2.CloseConnection(conn2);
         */
    }

    public void GetClausesFromHead(String head) {
        List<Integer> idSubpList = new ArrayList<>();
        List<Integer> REFCLAList = new ArrayList<>();

        ArgumentSQL manage2 = new ArgumentSQL();
        Connection conn2 = null;
        conn2 = manage2.Connect();

        idSubpList = manage2.SelectIDFromSubprog2(head, conn2);//se obtiene IDs de subprograms
        for (Iterator<Integer> it = idSubpList.iterator(); it.hasNext();) {
            Integer idSubprg = it.next();
            REFCLAList = manage2.SelectREFCLA(idSubprg, conn2);
        }

        manage2.CloseConnection(conn2);

    }

    public Forest<String, Integer> getGraphJung() {
        return graphJung;
    }

    public void setGraphJung(Forest<String, Integer> graphJung) {
        this.graphJung = graphJung;
    }

}
