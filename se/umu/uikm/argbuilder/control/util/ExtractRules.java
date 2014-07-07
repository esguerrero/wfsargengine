/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.umu.uikm.argbuilder.control.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.StringTokenizer;
import se.umu.uikm.argbuilder.control.DerbyConnect;
import se.umu.uikm.argbuilder.control.entities.ComponentEntity;
import se.umu.uikm.argbuilder.control.entities.ProgramEntity;

/**
 *
 * @author Esteban
 */
public class ExtractRules {

    private static String AtomSplitter = ",";
    private static String HeadSplitter = "-";

    void RuleExtractor() {
        
        
    }

    static public void main(String[] args) {
        
        String URLofFile ="C:/home/interprolog3/windowsScripts/comp0.P";
        File file = new File(URLofFile);
        String[] lines = new String[10];

        //List 
        try {
            FileReader reader = new FileReader(file);
            BufferedReader buffReader = new BufferedReader(reader);
            // Llamado a la base de datos

            DerbyConnect conec = new DerbyConnect();

            ComponentEntity component;
            ProgramEntity program;




            //aqui almacena el programa y tambien el componente
            String fakeFileName="comp0";//REMOVE IT
            program = new ProgramEntity(fakeFileName);
            //program = new ProgramEntity(URLofFile);
            program.InsertRow(conec.Connect());
            int idProgram = program.findIDProgramByURL(conec.Connect());
            
            
            // define el numero de componentes, solo hay 1 porque se abre un solo
            // archivo .P, en el futuro debería ser dinámico
            int numComponents = 0;
            numComponents = 1;
            int numofComponents, numofRules = 0;
            
            
            //Este es el numero (id) del componente! modificarlo
            numofComponents = 1;
            component = new ComponentEntity(numComponents);
            component.InsertRow(conec.Connect(), numComponents, idProgram);


            System.out.println("\t \t ########################################Start the rule analysis######################################");
            //read every rule of the program
            String ruleLine;
            while ((ruleLine = buffReader.readLine()) != null) {
                int x = 0;
                numofRules++;

                //create a new rule in DB with the id = numofRules


                System.out.println("(OK)\t------------------------------------------------------------- Num of rule:" + numofRules);
                System.out.println("(OK)\t ruleLine:" + ruleLine);
                StringTokenizer headToken = new StringTokenizer(ruleLine, HeadSplitter, false);
                String temporalRule = "";



                while (headToken.hasMoreTokens()) {

                    x++;
                    System.out.println("(OK)\t ++++++++++++++++++++++++++++++++++++++++++++++ x:" + x);
                    //  System.out.println("countTokens::"+headToken.countTokens());
                    temporalRule = headToken.nextToken();

                    if (x == 1) {//esta es la cabeza
                        System.out.println("(OK)\tSE DEBE GUARDAR TEMPORALMENTE LA CABEZA>" + temporalRule + "<");
                        if (temporalRule.isEmpty() || temporalRule.startsWith(":")) {
                            System.out.println("(OK)\t Esta no es una regla");// con esto se soluciona el problema de la regla "auto_table"
                        }
                    } else {
                        System.out.println("(OK)\t \t Este es el BODY>" + temporalRule + "<");
                        StringTokenizer bodyToken = new StringTokenizer(temporalRule, AtomSplitter, false);
                        String temporalAtom = "";

                        while (bodyToken.hasMoreTokens()) {
                            temporalAtom = bodyToken.nextToken();
                            System.out.println("(OK)\tnext element body>>" + temporalAtom + "<<");
                            if (temporalAtom.contains("auto_table") || temporalAtom.isEmpty()) {
                                System.out.println("(OK)\tel body no tiene atoms");
                            } else {
                                System.out.println("(OK)\t GUARDAR AQUI CADA ATOMO EN LA TABLA");
                            }
                        }


                    }


                    /*
                     temporalBody = headToken.nextToken();
                     System.out.println("\t temporal body:" + temporalBody);
                     */
                }


                /*
                 if (HeadSplitter.contains(temporalHead)) {
                 System.out.println("contiene head token");
                 } else {
                 System.out.println("NO contiene head token");
                 }
                 */


                //lines[x] = s;
                // x++;
            }
        } catch (IOException e) {
            //handle exception
        }





        /* And just to prove we have the lines right where we want them..
         for (String st : lines) {
         System.out.println(st);
         }
         */
    }
}
