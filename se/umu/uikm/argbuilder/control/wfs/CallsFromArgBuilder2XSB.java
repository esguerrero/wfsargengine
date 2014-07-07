/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.umu.uikm.argbuilder.control.wfs;

import com.declarativa.interprolog.AbstractPrologEngine;
import com.declarativa.interprolog.util.IPAbortedException;
import com.declarativa.interprolog.util.IPInterruptedException;
import java.io.File;
//import com.sun.org.apache.bcel.internal.generic.AALOAD;
import java.sql.Connection;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import javax.print.DocFlavor;
import se.umu.uikm.argbuilder.control.derby.ArgumentSQL;

/**
 *
 * @author Esteban
 */
public class CallsFromArgBuilder2XSB {

    public String File2Consult(String nameFileProgram) {
        String file2Consult = nameFileProgram;
        //remueve el .P del archivo
        StringTokenizer st2 = new StringTokenizer(file2Consult, ".");
        String nameFile = st2.nextElement().toString();
        //sendCallToProlog("consult("+nameFile+").");
        return "consult(" + nameFile + ").";
    }

    /**
     * Find the tabled atoms when get_calls_for_table from XSB is called, it's
     * useful when it's necessary to know the False values.
     *
     * @param engine
     * @return
     */
    public String FindCallsForTable(AbstractPrologEngine engine) {

        String atomGoal = "p(X)";
        String atomGoalA = "p(X)";
        String atomGoal2 = "findall(Y,p(Y), L)";
        String atomGoal3 = "findall(Call,get_calls_for_table(p/1,Call),List)";
        String calls_for_table = "";

        try {
            String G = "(X=a;X=b)";
            String T = "X";

            String GG = "findall(TM, (" + atomGoal3 + ",buildTermModel(List,TM)), L), ipObjectSpec('ArrayOfObject',L,LM)";

            Object[] bindings0 = engine.deterministicGoal(atomGoal, null);
            Object[] bindings = engine.deterministicGoal(atomGoal3, null);

            Object[] get_calls_for_table = (Object[]) engine.deterministicGoal(GG, "[LM]")[0];

            //System.out.println("Number of solutions:"+get_calls_for_table.length);
            for (int I = 0; I < get_calls_for_table.length; I++) {
                System.out.println("Solution " + I + ":" + get_calls_for_table[I]);
            }

            calls_for_table = (String) get_calls_for_table.toString();

        } catch (IPAbortedException exc) {
            exc.printStackTrace();
            System.out.println("Exception!: " + exc.getLocalizedMessage());
        } catch (IPInterruptedException ex) {
            ex.printStackTrace();
            System.out.println("Exception!: " + ex.getLocalizedMessage());
        }
        return calls_for_table;

    }

    /**
     * Find all the True values of each atom in a rule this is made using XSB
     * call to get_returns_for_call
     *
     * @param engine
     * @return
     */
    public String FindReturnsForCall(AbstractPrologEngine engine, String filename) {

        String atomGoal = "p(X)";
        String atomGoal2 = "findall(Y,p(Y), L)";
        String atomGoal4 = "findall(Answer,get_returns_for_call(p(_),Answer),List2)";
        String returns_for_call = "";

        try {
            String G = "(X=a;X=b)";
            String T = "X";

            String GG2 = "findall(TM, (" + atomGoal4 + ",buildTermModel(List2,TM)), L), ipObjectSpec('ArrayOfObject',L,LM)";

            Object[] bindings0 = engine.deterministicGoal(atomGoal, null);

            Object[] get_returns_for_call = (Object[]) engine.deterministicGoal(GG2, "[LM]")[0];

            //System.out.println("GG2:"+GG2+ "  #atomGoal14:"+atomGoal4+"  #bindings:"+bindings0+ "  #atomGoal:"+atomGoal);
            //System.out.println("Number of bindings:"+get_returns_for_call.length);
            List returnList = new ArrayList();
            for (int I = 0; I < get_returns_for_call.length; I++) {
                System.out.println("Solution2 " + I + ":" + get_returns_for_call[I]);
                returnList.add(get_returns_for_call[I]);
            }

            Object truthRes = null;
            for (Iterator it = returnList.iterator(); it.hasNext();) {
                truthRes = it.next();
            }

            String truthV = new String(truthRes.toString());
            truthV.replace('[', ' ');
            System.out.println("---truthV_>" + truthV);
            String temporalValue = "";
            StringTokenizer valuesT = new StringTokenizer(truthV, ",", false);
            while (valuesT.hasMoreTokens()) {

                //                System.out.println("(OK)\t ++++++++++++++++++++++++++++++++++++++++++++++ x:" + x);
                //  System.out.println("countTokens::"+headToken.countTokens());
                temporalValue = valuesT.nextToken();
                if (temporalValue.contains("[")) {
                    temporalValue = temporalValue.replace("[", "");
                }
                if (temporalValue.contains("]")) {
                    temporalValue = temporalValue.replace("]", "");
                }
                /**
                 * AQUI GRABAR CADA UNA DE LAS TEMPORALvALUE aka LAS CABEZAS
                 * COMO TRUE
                 *
                 *
                 */
                ArgumentSQL manage = new ArgumentSQL();
                Connection conn = null;
                conn = manage.Connect();
                manage.UpdateInferredValues(filename, temporalValue, conn);
                //manage.UpdateTruthValues(temporalValue, conn);
                manage.CloseConnection(conn);
            }

            returns_for_call = (String) get_returns_for_call.toString();

        } catch (IPAbortedException exc) {
            exc.printStackTrace();
            System.out.println("Exception!: " + exc.getLocalizedMessage());
        } catch (IPInterruptedException ex) {
            ex.printStackTrace();
            System.out.println("Exception!: " + ex.getLocalizedMessage());
        }
        return returns_for_call;

    }

    /**
     * Find all the True values of each atom in a rule this is made using XSB
     * call to get_returns_for_call
     *
     * @param engine
     * @return
     */
    public void WFSEvaluation(AbstractPrologEngine engine, String filename) {

        //System.out.println("\t\t\t\t ******** Start WFS Evaluation");
        String atomGoal = "p(X)";
        String atomGoal2 = "findall(Y,p(Y), L)";
        //String atomGoal4 = "findall(Answer,get_returns_for_call(p(_),Answer),List2)";
        String atomGoal4 = "findall(Answer,get_returns_for_call(p(_),Answer),List2)";
        String atomGoal4B = "setof(X,p(X),List2)";
        String returns_for_call = "";
        List<String> goalList = new ArrayList<>();

        String atomGoal3 = "findall(Call,get_calls_for_table(p/1,Call),List)";
        String calls_for_table = "";

        try {
            String G = "(X=a;X=b)";
            String T = "X";

            /**
             * Evaluation of True values
             */
            String GG2 = "findall(TM, (" + atomGoal4 + ",buildTermModel(List2,TM)), L), ipObjectSpec('ArrayOfObject',L,LM)";
            String GG2B = "findall(TM, (" + atomGoal4B + ",buildTermModel(List2,TM)), L), ipObjectSpec('ArrayOfObject',L,LM)";

            Object[] bindings0 = engine.deterministicGoal(atomGoal, null);
            if (bindings0 == null) {
            } else {
                //   System.out.println("bindingsLength:"+bindings0.length);
                for (int i = 0; i < bindings0.length; i++) {
                    System.out.println("IN2_>>   i:" + i + "  _bind" + bindings0[i].toString());
                }
            }

            /*
             Object[] get_goals_for_call = (Object[]) engine.deterministicGoal(GG2B, "[LM]")[0];
             for (int j = 0; j < get_goals_for_call.length; j++) {
             String trueset = (String) get_goals_for_call[j].toString();
             System.out.println("\n\t\t ==============FILE:"+filename+"===========================NEW GOAL:"+trueset);
             }
             */
            Object[] get_returns_for_call = (Object[]) engine.deterministicGoal(GG2, "[LM]")[0];

            String GG = "findall(TM, (" + atomGoal3 + ",buildTermModel(List,TM)), L), ipObjectSpec('ArrayOfObject',L,LM)";
            Object[] bindings = engine.deterministicGoal(atomGoal3, null);

            Object[] get_calls_for_table = (Object[]) engine.deterministicGoal(GG, "[LM]")[0];
            //Object[] get_calls_for_table = (Object[]) engine.deterministicGoal(GG, "[]")[0];

            //System.out.println("GG2:"+GG2+ "  #atomGoal14:"+atomGoal4+"  #bindings:"+bindings0+ "  #atomGoal:"+atomGoal);
            for (int I = 0; I < get_calls_for_table.length; I++) {
                //System.out.println("False Values" + I + ":" + get_calls_for_table[I]);
            }
            calls_for_table = (String) get_calls_for_table.toString();
            //System.out.println("Number of bindings:"+get_returns_for_call.length);
            List returnList = new ArrayList();
            for (int I = 0; I < get_returns_for_call.length; I++) {
                System.out.println("True Values " + I + ":" + get_returns_for_call[I]);
                returnList.add(get_returns_for_call[I]);
            }
            List<String> trueList = new ArrayList<>();
            List<String> falseList = new ArrayList<>();
            for (int I = 0; I < get_calls_for_table.length; I++) {
                String falseset = (String) get_calls_for_table[I].toString();
                falseList = Cleaner(falseset);
            }

            for (int j = 0; j < get_returns_for_call.length; j++) {
                String trueset = (String) get_returns_for_call[j].toString();
                trueList = Cleaner(trueset);
            }

            List<String> undefvalues = new ArrayList<>();
            for (int i = 0; i < trueList.size(); i++) {
                for (int j = 0; j < falseList.size(); j++) {
                    String truevalue = trueList.get(i);
                    String falsevalue = falseList.get(j);
                    //System.out.println("falsevalue:>" + falsevalue + "<  truevalue:>" + truevalue + "<");
                    if (truevalue.contentEquals(falsevalue)) {
                        //trueList.remove(i);
                        //falseList.remove(j);
                        //System.out.println("IGUALES: falsevalue:>" + falsevalue + "<  truevalue:>" + truevalue + "<");
                        undefvalues.add(truevalue);
                    }
                }
            }

            for (int j = 0; j < undefvalues.size(); j++) {
                //System.out.println("****UNDEFI:"+undefvalues.get(j));
                trueList.remove(undefvalues.get(j));
            }

            ArgumentSQL manage = new ArgumentSQL();
            Connection conn = null;
            conn = manage.Connect();
            String inferredHead = "";
            for (Iterator<String> it = trueList.iterator(); it.hasNext();) {
                String string = it.next();
                //fullclause = fullclause + "," + string;
                inferredHead = string;
                /**
                 * *
                 * aqui debería ser cada cláusula, con su archivo y su cabeza
                 * por separado quitar: fullclause = fullclause + "," + string;
                 */

                System.out.println("(OK) Atoms inferred:" + inferredHead + "  from file:" + filename);

                //manage.InsertRowSubpClauses(filename, string, conn);
//                manage.UpdateInferredValues(filename, inferredHead, conn);
                manage.InsertHeads(inferredHead, filename, conn);

                //manage.UpdateTruthValues(temporalValue, conn);
            }
            manage.CloseConnection(conn);

            Object truthRes = null;
            for (Iterator it = returnList.iterator(); it.hasNext();) {
                truthRes = it.next();
            }
            /**
             * String truthV = new String(truthRes.toString());
             * truthV.replace('[', ' '); System.out.println("---truthV_>" +
             * truthV); String temporalValue = ""; StringTokenizer valuesT = new
             * StringTokenizer(truthV, ",", false); while
             * (valuesT.hasMoreTokens()) {
             *
             * // System.out.println("(OK)\t
             * ++++++++++++++++++++++++++++++++++++++++++++++ x:" + x); //
             * System.out.println("countTokens::"+headToken.countTokens());
             * temporalValue = valuesT.nextToken(); if
             * (temporalValue.contains("[")) { temporalValue =
             * temporalValue.replace("[", ""); } if
             * (temporalValue.contains("]")) { temporalValue =
             * temporalValue.replace("]", ""); }
             *
             * AQUI GRABAR CADA UNA DE LAS TEMPORALvALUE aka LAS CABEZAS COMO
             * TRUE
             *
             *
             *
             *
             * ArgumentSQL manage = new ArgumentSQL(); Connection conn = null;
             * conn = manage.Connect(); manage.UpdateInferredValues(filename,
             * temporalValue, conn); //manage.UpdateTruthValues(temporalValue,
             * conn); manage.CloseConnection(conn);
             *
             * }
             */
            returns_for_call = (String) get_returns_for_call.toString();

        } catch (IPAbortedException exc) {
            exc.printStackTrace();
            System.out.println("Exception!: " + exc.getLocalizedMessage());
        } catch (IPInterruptedException ex) {
            ex.printStackTrace();
            System.out.println("Exception!: " + ex.getLocalizedMessage());
        }

    }

    /**
     * Find all the True values of each atom in a rule this is made using XSB
     * call to get_returns_for_call
     *
     * @param engine
     * @return
     */
    public void WFSEvaluation2(AbstractPrologEngine engine, String filename) {

        //System.out.println("\t\t\t\t ******** Start WFS Evaluation");
        String atomGoal = "p(X)";
        String atomGoal2 = "findall(Y,p(Y), L)";
        //String atomGoal4 = "findall(Answer,get_returns_for_call(p(_),Answer),List2)";
        String atomGoal4 = "findall(Answer,get_returns_for_call(p(_),Answer),List2)";
        String atomGoal4B = "setof(X,p(X),List2)";
        String returns_for_call = "";
        List<String> goalList = new ArrayList<>();

        try {
            String G = "(X=a;X=b)";
            String T = "X";

            /**
             * Evaluation of True values
             */
            String GG2 = "findall(TM, (" + atomGoal4 + ",buildTermModel(List2,TM)), L), ipObjectSpec('ArrayOfObject',L,LM)";
            String GG2B = "findall(TM, (" + atomGoal4B + ",buildTermModel(List2,TM)), L), ipObjectSpec('ArrayOfObject',L,LM)";

            Object[] bindings0 = engine.deterministicGoal(atomGoal, null);
            if (bindings0 == null) {
            } else {
                //   System.out.println("bindingsLength:"+bindings0.length);
                for (int i = 0; i < bindings0.length; i++) {
                    System.out.println("IN2_>>   i:" + i + "  _bind" + bindings0[i].toString());
                }
            }

            Object[] get_returns_for_call = (Object[]) engine.deterministicGoal(GG2, "[LM]")[0];

            List returnList = new ArrayList();
            for (int I = 0; I < get_returns_for_call.length; I++) {
 //               System.out.println("True Values " + I + ":" + get_returns_for_call[I]);
                returnList.add(get_returns_for_call[I]);
            }
            List<String> trueList = new ArrayList<>();

            for (int j = 0; j < get_returns_for_call.length; j++) {
                String trueset = (String) get_returns_for_call[j].toString();
                trueList = Cleaner(trueset);
            }

            ArgumentSQL manage = new ArgumentSQL();
            Connection conn = null;
            conn = manage.Connect();
            String inferredHead = "";
            for (Iterator<String> it = trueList.iterator(); it.hasNext();) {
                String string = it.next();
                //fullclause = fullclause + "," + string;
                inferredHead = string;
                /**
                 * *
                 * aqui debería ser cada cláusula, con su archivo y su cabeza
                 * por separado quitar: fullclause = fullclause + "," + string;
                 */

 //               System.out.println("(OK) Atoms inferred:" + inferredHead + "  from file:" + filename);

                manage.InsertHeads(inferredHead, filename, conn);

//                manage.UpdateInferredValues(filename, inferredHead, conn);
            }
            manage.CloseConnection(conn);

            Object truthRes = null;
            for (Iterator it = returnList.iterator(); it.hasNext();) {
                truthRes = it.next();
            }

            returns_for_call = (String) get_returns_for_call.toString();

        } catch (IPAbortedException exc) {
            exc.printStackTrace();
            System.out.println("Exception!: " + exc.getLocalizedMessage());
        } catch (IPInterruptedException ex) {
            ex.printStackTrace();
            System.out.println("Exception!: " + ex.getLocalizedMessage());
        }

    }

    /**
     * Find all the True values of each atom in a rule this is made using a
     * simple call of setof(X, p(X), List).
     *
     *
     * @param engine
     * @return
     */
    public void WFSEvaluationSimple(AbstractPrologEngine engine, String filename) {

        //System.out.println("\t\t\t\t ******** Start WFS Evaluation");
        String atomGoal = "p(X)";
        String atomGoal2 = "findall(Y,p(Y), L)";
        String atomGoal4 = "findall(Answer,get_returns_for_call(p(_),Answer),List2)";
        String atomGoal4B = "setof(X,p(X),List2)";
        String returns_for_call = "";
        List<String> goalList = new ArrayList<>();
        String trueset = "";
        String atomGoal3 = "findall(Call,get_calls_for_table(p/1,Call),List2)";
        String calls_for_table = "";

        try {
            String G = "(X=a;X=b)";
            String T = "X";

            /**
             * Evaluation of True values
             */
            String GG2 = "findall(TM, (" + atomGoal4 + ",buildTermModel(List2,TM)), L), ipObjectSpec('ArrayOfObject',L,LM)";
            String GG2B = "findall(TM, (" + atomGoal4B + ",buildTermModel(List2,TM)), L), ipObjectSpec('ArrayOfObject',L,LM)";
            String GG2BC = "findall(TM, (" + atomGoal3 + ",buildTermModel(List2,TM)), L), ipObjectSpec('ArrayOfObject',L,LM)";

            Object[] bindings0 = engine.deterministicGoal(atomGoal, null);
            if (bindings0 == null) {
            } else {
                //   System.out.println("bindingsLength:"+bindings0.length);
                for (int i = 0; i < bindings0.length; i++) {
 //                   System.out.println("IN2_>>   i:" + i + "  _bind" + bindings0[i].toString());
                }
            }
            // System.out.println("IN_2_out");

            Object[] get_goals_for_call = (Object[]) engine.deterministicGoal(GG2B, "[LM]")[0];
            for (int j = 0; j < get_goals_for_call.length; j++) {
                trueset = (String) get_goals_for_call[j].toString();
 //               System.out.println("TRUE SET;:"+trueset +"__for filename´:"+filename);
            }

            
            
            String tables = "";
            File fileD = new File(filename);
            //if(engine.consultAbsolute(fileD)){
            if (engine.consultAbsolute(fileD)) {
                System.out.println("CONSULTA GENERADA \n");
                engine.deterministicGoal("p(X)");
                
                
  //              System.out.println("CONSULTA 2 GENERADA \n");
                /*
                
                Object[] answers = engine.deterministicGoal("findall(Answer,get_returns_for_call(p(_),Answer),List2)", null);
                System.out.println("CONSULTA 3 GENERADA \n"+ "answ:"+answers.toString());
                for (int j = 0; j < answers.length; j++) {
                    tables = (String) answers[j].toString();

                    System.out.println("TABLED:" + tables);
                }
                Object[] answers2 = engine.deterministicGoal("findall(Call,get_calls_for_table(p/1,Call),List)", null);
                System.out.println("CONSULTA 4 GENERADA \n"+ "answ2:"+answers2.toString());
                for (int j = 0; j < answers2.length; j++) {
                    tables = (String) answers2[j].toString();

                    System.out.println("TABLED:" + tables);
                }
                */
                
            } else {
                System.out.println("CONSULTA NO GENERADA \n");
            }

            
            
            
            
            goalList = Cleaner(trueset);
            for (Iterator<String> it = goalList.iterator(); it.hasNext();) {
                String string = it.next();
//                System.out.println("\n\t\t ==============FILE:" + filename + "===========================NEW GOAL:" + string + "_");
            }

            ArgumentSQL manage = new ArgumentSQL();
            Connection conn = null;
            conn = manage.Connect();
            String inferredHead = "";
            for (Iterator<String> it = goalList.iterator(); it.hasNext();) {
                String string = it.next();
                //fullclause = fullclause + "," + string;
                inferredHead = string;
                /**
                 * *
                 * aqui debería ser cada cláusula, con su archivo y su cabeza
                 * por separado quitar: fullclause = fullclause + "," + string;
                 */

 //               System.out.println("(OK) Atoms inferred:" + inferredHead + "  from file:" + filename);

                manage.InsertHeads("p(" + inferredHead + ")", filename, conn);

                //manage.InsertRowSubpClauses(filename, string, conn);
                //manage.UpdateInferredValues(filename, "p("+inferredHead+").", conn);
                //manage.UpdateTruthValues(temporalValue, conn);
            }
            manage.CloseConnection(conn);

        } catch (IPAbortedException exc) {
            exc.printStackTrace();
            System.out.println("Exception!: " + exc.getLocalizedMessage());
        } catch (IPInterruptedException ex) {
            ex.printStackTrace();
            System.out.println("Exception!: " + ex.getLocalizedMessage());
        }

    }

    /**
     * Removes brackets, and parenthesis from a String
     *
     * @param dirtyClause
     */
    private List<String> Cleaner(String dirtyClause) {
        List<String> values = new ArrayList<>();
        String clause = dirtyClause.replace('[', ' ');
        clause = clause.replace(']', ' ');

        String temporalValue = "";
        clause = clause.replaceAll(" ", "");
        StringTokenizer valuesT = new StringTokenizer(clause, ",", false);
        while (valuesT.hasMoreTokens()) {

            temporalValue = valuesT.nextToken();
            values.add(temporalValue);
        }
        return values;
    }
}
