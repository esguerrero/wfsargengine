/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.declarativa.interprolog.gui;

import com.declarativa.interprolog.AbstractPrologEngine;
import com.declarativa.interprolog.ObjectExamplePair;
import com.declarativa.interprolog.XSBPeer;
import com.declarativa.interprolog.gui.PredicateTableModel;
import com.declarativa.interprolog.gui.TermListModel;
import com.declarativa.interprolog.gui.TermTreeModel;
import com.declarativa.interprolog.gui.XSBTableModel;
import com.declarativa.interprolog.util.IPException;
import com.timboudreau.vl.jung.extensions.BaseJungScene;
import edu.uci.ics.jung.algorithms.layout.BalloonLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.ObservableGraph;
import edu.uci.ics.jung.graph.util.Context;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FileDialog;
import java.awt.HeadlessException;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileFilter;
import org.apache.commons.collections15.Transformer;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import se.umu.uikm.argbuilder.control.ArgBuildManager;
import se.umu.uikm.argbuilder.control.wfs.CallsFromArgBuilder2XSB;

/**
 *
 * @author Esteban
 */
public class Ini extends javax.swing.JFrame implements WindowListener {

    Vector loadedFiles;
    private static int topLevelCount = 0;
    public static boolean debug = false;

    public AbstractPrologEngine engine = null;

    public Ini(AbstractPrologEngine e, boolean autoDisplay) {
        /// super("PrologEngine listener (Swing)");
        System.out.println("ENTRA");
        if (e != null) {
            engine = e;
        } else {
            throw new IPException("missing Prolog engine");
        }

        String VF = e.getImplementationPeer().visualizationFilename();
        if (engine.getLoadFromJar()) {
            engine.consultFromPackage(VF, Ini.class);
        } else {
            engine.consultRelative(VF, Ini.class);
        }
        engine.teachMoreObjects(guiExamples());

        if (engine == null) {
            dispose(); // no interface object permitted!
        } else {
            topLevelCount++;
        }
        debug = engine.isDebug();

        loadedFiles = new Vector();

        initComponents();

        //constructWindowContents();
///        constructMenu();
        addWindowListener(this);

        prologInput.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
//                    sendToProlog();
                    e.consume();
                }
            }
        });
        //prologOutput.append("Welcome to an InterProlog top level\n" + e.getPrologVersion() + "\n\n");
        prologOutput.append("\n ARG ENGINE v18 ALPHA ");
        prologOutput.append("\t Argument Engine based on WFS \n\n");
        prologOutput.append("\t UIKM Group - Umea University \n\n");
        prologOutput.append("\t {esteban, jcnieves, helena}@cs.umu.se \n\n");
        if (autoDisplay) {
            setVisible(true);
//            focusInput();
        }
    }

    /**
     * Creates new form Ini
     */
    public Ini() throws IOException {
        
        
        
        
        
        
        
        
        
        
        initComponents();
       

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fileChooser = new javax.swing.JFileChooser();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel5 = new javax.swing.JPanel();
        jLayeredPane2 = new javax.swing.JLayeredPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        prologOutput = new javax.swing.JTextArea();
        jButton1 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        prologInput = new javax.swing.JTextArea();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        jPanel6 = new javax.swing.JPanel();

        fileChooser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileChooserActionPerformed(evt);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1105, 540));

        jTabbedPane2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jTabbedPane2MouseEntered(evt);
            }
        });

        jLayeredPane2.setBackground(new java.awt.Color(0, 0, 0));

        prologOutput.setColumns(20);
        prologOutput.setRows(5);
        prologOutput.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                prologOutputComponentShown(evt);
            }
        });
        jScrollPane1.setViewportView(prologOutput);

        jButton1.setText(org.openide.util.NbBundle.getMessage(Ini.class, "Ini.jButton1.text")); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        prologInput.setColumns(20);
        prologInput.setRows(5);
        jScrollPane2.setViewportView(prologInput);

        jLayeredPane1.setBackground(new java.awt.Color(153, 153, 255));
        jLayeredPane1.setPreferredSize(new java.awt.Dimension(600, 0));

        javax.swing.GroupLayout jLayeredPane1Layout = new javax.swing.GroupLayout(jLayeredPane1);
        jLayeredPane1.setLayout(jLayeredPane1Layout);
        jLayeredPane1Layout.setHorizontalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 600, Short.MAX_VALUE)
        );
        jLayeredPane1Layout.setVerticalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jLayeredPane2Layout = new javax.swing.GroupLayout(jLayeredPane2);
        jLayeredPane2.setLayout(jLayeredPane2Layout);
        jLayeredPane2Layout.setHorizontalGroup(
            jLayeredPane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayeredPane2Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jLayeredPane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton1)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 488, Short.MAX_VALUE)
                    .addComponent(jScrollPane2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLayeredPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jLayeredPane2Layout.setVerticalGroup(
            jLayeredPane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayeredPane2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jLayeredPane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jLayeredPane2Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 313, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLayeredPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 415, Short.MAX_VALUE))
                .addContainerGap(75, Short.MAX_VALUE))
        );
        jLayeredPane2.setLayer(jScrollPane1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane2.setLayer(jButton1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane2.setLayer(jScrollPane2, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane2.setLayer(jLayeredPane1, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addComponent(jLayeredPane2)
                .addGap(1, 1, 1))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jLayeredPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 11, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab(org.openide.util.NbBundle.getMessage(Ini.class, "Ini.jPanel5.TabConstraints.tabTitle"), jPanel5); // NOI18N

        jPanel6.setPreferredSize(new java.awt.Dimension(1100, 476));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1127, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 541, Short.MAX_VALUE)
        );

        jTabbedPane2.addTab(org.openide.util.NbBundle.getMessage(Ini.class, "Ini.jPanel6.TabConstraints.tabTitle"), jPanel6); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane2)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane2)
        );

        jTabbedPane2.getAccessibleContext().setAccessibleName(org.openide.util.NbBundle.getMessage(Ini.class, "Ini.jTabbedPane2.AccessibleContext.accessibleName")); // NOI18N

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void fileChooserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileChooserActionPerformed


    }//GEN-LAST:event_fileChooserActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            Forest<String, Integer> forest = new DelegateForest<>();
            ObservableGraph g = new ObservableGraph(new BalloonLayoutDemo().createTree(forest));
            
            Layout layout = new BalloonLayout(forest);
            //Layout layout = new TreeLayout(forest, 70, 70);
            final BaseJungScene scene = new SceneImpl(g, layout);
            
            jLayeredPane1.setLayout(new BorderLayout());
            jLayeredPane1.add(new JScrollPane(scene.createView()), BorderLayout.CENTER);
            
            final JCheckBox checkbox = new JCheckBox("Animate iterative layouts");
            
            scene.setLayoutAnimationFramesPerSecond(48);
            
            //**********************
            DefaultComboBoxModel<Transformer<Context<Graph<String, Number>, Number>, Shape>> shapes = new DefaultComboBoxModel<>();
            shapes.addElement(new EdgeShape.QuadCurve<String, Number>());
            shapes.addElement(new EdgeShape.BentLine<String, Number>());
            shapes.addElement(new EdgeShape.CubicCurve<String, Number>());
            shapes.addElement(new EdgeShape.Line<String, Number>());
            shapes.addElement(new EdgeShape.Box<String, Number>());
            shapes.addElement(new EdgeShape.Orthogonal<String, Number>());
            shapes.addElement(new EdgeShape.Wedge<String, Number>(10));
            
            /*
            final JComboBox<Transformer<Context<Graph<String, Number>, Number>, Shape>> shapesBox = new JComboBox<>(shapes);
            shapesBox.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent ae) {
            Transformer<Context<Graph<String, Number>, Number>, Shape> xform = (Transformer<Context<Graph<String, Number>, Number>, Shape>) shapesBox.getSelectedItem();
            scene.setConnectionEdgeShape(xform);
            }
            });
            
            
            
            shapesBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> jlist, Object o, int i, boolean bln, boolean bln1) {
            o = o.getClass().getSimpleName();
            return super.getListCellRendererComponent(jlist, o, i, bln, bln1); //To change body of generated methods, choose Tools | Templates.
            }
            });
            shapesBox.setSelectedItem(new EdgeShape.QuadCurve<>());
            
            */
            final JLabel selectionLabel = new JLabel("<html>&nbsp;</html>");
            System.out.println("LOOKUP IS " + scene.getLookup());
            Lookup.Result<String> selectedNodes = scene.getLookup().lookupResult(String.class);
            LookupListener listener = new LookupListener() {
                @Override
                public void resultChanged(LookupEvent le) {
                    System.out.println("RES CHANGED");
                    Lookup.Result<String> res = (Lookup.Result<String>) le.getSource();
                    StringBuilder sb = new StringBuilder("<html>");
                    List<String> l = new ArrayList<>(res.allInstances());
                    Collections.sort(l);
                    for (String s : l) {
                        if (sb.length() != 0) {
                            sb.append(", ");
                        }
                        sb.append(s);
                    }
                    sb.append("</html>");
                    selectionLabel.setText(sb.toString());
                    System.out.println("LOOKUP EVENT " + sb);
                }
            };
            selectedNodes.addLookupListener(listener);
            selectedNodes.allInstances();
            
            checkbox.setSelected(true);
            checkbox.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    scene.setAnimateIterativeLayouts(checkbox.isSelected());
                }
            });

            jLayeredPane1.setSize(456, 458);
            ////        jf.setSize(jf.getGraphicsConfiguration().getBounds().width - 120, 700);
            this.addWindowListener(new WindowAdapter() {
                @Override
                public void windowOpened(WindowEvent we) {
                    scene.relayout(true);
                    scene.validate();
                }
            });
            
            
            
            /*
            
            int returnVal = fileChooser.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            
            fileChooser.addChoosableFileFilter(new FileFilter() {
            
            public String getDescription() {
            return "Prolog native program (*.P)";
            }
            
            public boolean accept(File f) {
            if (f.isDirectory()) {
            return true;
            } else {
            return f.getName().toLowerCase().endsWith(".P");
            }
            }
            });
            
            try {
            // What to do with the file, e.g. display it in a TextArea
            prologOutput.read(new FileReader(file.getAbsolutePath()), null);
            } catch (IOException ex) {
            System.out.println("problem accessing file" + file.getAbsolutePath());
            }
            } else {
            System.out.println("File access cancelled by user.");
            }
            
            */
            
            //One file chooser for each buton click
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
            System.out.println("AUCH");
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void prologOutputComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_prologOutputComponentShown
        // TODO add your handling code here:
    }//GEN-LAST:event_prologOutputComponentShown

    private void jTabbedPane2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabbedPane2MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jTabbedPane2MouseEntered

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Ini.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Ini.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Ini.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Ini.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                System.out.println("RUN");
                try {
                    new Ini().setVisible(true);
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFileChooser fileChooser;
    private javax.swing.JButton jButton1;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JLayeredPane jLayeredPane2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTextArea prologInput;
    private javax.swing.JTextArea prologOutput;
    // End of variables declaration//GEN-END:variables

    @Override
    public void windowOpened(WindowEvent we) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowClosing(WindowEvent we) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowClosed(WindowEvent we) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowIconified(WindowEvent we) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowDeiconified(WindowEvent we) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowActivated(WindowEvent we) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowDeactivated(WindowEvent we) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static ObjectExamplePair[] guiExamples() {
        ObjectExamplePair[] examples = {
            PredicateTableModel.example(),
            TermListModel.example(),
            TermTreeModel.example(),
            new ObjectExamplePair("ArrayOfTermTreeModel", new TermTreeModel[0]),
            XSBTableModel.example(),};
        return examples;
    }

    void constructMenu() {

        final CallsFromArgBuilder2XSB calls2XSB = new CallsFromArgBuilder2XSB();

        /**
         * ****************************************************************
         * ***************ENTRY POINT FOR CALLING THE ARGXSB ANALYSIS *****
         * ****************************************************************
         *
         * Carga un archivo con las reglas para crear los argumentos
         */
    
        //1. Load File
        //2. clause partition
        //3. graph creation
        //4. extract subgraphs
        //5. create a file for each subgraph
        //6. consult via XSB, each sub-file (sub-graph)
        /**
         * deleteing previous files
         */
        String calls_for_table = "";//maybe temporal
        String returns_for_call = "";

        //the file is loaded
        GraphAnalysis();

        /*
         //remueve el .P del archivo 
         String nameFile = calls2XSB.File2Consult(getNameOfFileToLoad());
         System.out.println("=============_" + nameFile);
         //StringTokenizer st2 = new StringTokenizer(getNameOfFileToLoad(), ".");
         //String nameFile = st2.nextElement().toString();
         sendCallToProlog("consult(" + nameFile + ").");


         //retorna los valores de True False de cada atomo
         //estos debeian guardarse en la base de datos para luego
         // obtener los argumentos
         calls_for_table = calls2XSB.FindCallsForTable(engine);
         returns_for_call = calls2XSB.FindReturnsForCall(engine);
         */
        java.util.List<String> filenames = new ArrayList();
        filenames = printArguments();

        for (Iterator<String> it = filenames.iterator(); it.hasNext();) {
            String string = it.next();
            //System.out.println("\t\t --------------> name:" + string);
            //System.out.println("\t \t *************************************************");

            /*
             try {
             System.out.println(new File(".").getCanonicalPath());
             System.out.println("DIR:" + new File("../examples/").getCanonicalPath());
             System.out.println("DIR:" + new File("../examples/"+string).getCanonicalPath());

             } catch (java.io.IOException ex) {
             System.out.println("ex." + ex.getLocalizedMessage());
             }
             */
            consultFile("../examples/" + string);

            String nameFile3 = calls2XSB.File2Consult(string);
            //sendCallToProlog("consult(" + nameFile3 + ").");
//            sendCallToProlog(nameFile3);

            //calls_for_table = calls2XSB.FindCallsForTable(engine);
            //returns_for_call = calls2XSB.FindReturnsForCall(engine, string);
            calls2XSB.WFSEvaluation(engine, string);

            //System.out.println("\t \t *************************************************");
        }

//        ArgumentWriter();

//        drawArguments();

        /*
         String atomGoal ="p(X)";
         String atomGoal2 = "findall(Y,p(Y), L)";
         String atomGoal3 ="findall(Call,get_calls_for_table(p/1,Call),List)";
         String atomGoal4 ="findall(Answer,get_returns_for_call(p(_),Answer),List2)";
                
                
         try {
         String G = "(X=a;X=b)";
         String T = "X";
                   
         String GG = "findall(TM, ("+atomGoal3+",buildTermModel(List,TM)), L), ipObjectSpec('ArrayOfObject',L,LM)";                   
         String GG2 = "findall(TM, ("+atomGoal4+",buildTermModel(List2,TM)), L), ipObjectSpec('ArrayOfObject',L,LM)";

         Object[] bindings0 = engine.deterministicGoal(atomGoal,null);                  
         Object[] bindings = engine.deterministicGoal(atomGoal3,null);
                    
         Object[] get_calls_for_table = (Object[])engine.deterministicGoal(GG,"[LM]")[0];                    
         Object[] get_returns_for_call = (Object[])engine.deterministicGoal(GG2,"[LM]")[0];
              
         //System.out.println("Number of solutions:"+get_calls_for_table.length);
         for(int I=0;I<get_calls_for_table.length;I++)
         System.out.println("Solution "+I+":"+get_calls_for_table[I]);
                    
         //System.out.println("Number of bindings:"+get_returns_for_call.length);
         for(int I=0;I<get_returns_for_call.length;I++)
         System.out.println("Solution2 "+I+":"+get_returns_for_call[I]);
                      
                
         } catch (IPAbortedException exc) {
         exc.printStackTrace();
         System.out.println("Exception!: "+exc.getLocalizedMessage());
         } catch (IPInterruptedException ex) {
         ex.printStackTrace();
         System.out.println("Exception!: "+ex.getLocalizedMessage());
         }

         */
        // }
    }
    
    

    public void GraphAnalysis() {
        String fileToLoad, folder;
        File filetoreconsult = null;
        FileDialog d = new FileDialog(this, "Consult file...");
        d.setVisible(true);
        fileToLoad = d.getFile();
        folder = d.getDirectory();
        if (fileToLoad != null) {
            ArgBuildManager manag = new ArgBuildManager();
            manag.GraphFileRules(folder, fileToLoad);//Call to the ArgBuildManager!
            /*
             filetoreconsult = new File(folder, fileToLoad);
             if (engine.consultAbsolute(filetoreconsult)) {
             addToReloaders(filetoreconsult, "consult");
             }
             */
            //System.out.println("file:"+filetoreconsult.);

        }
    }

    /**
     * *
     * Print arguments
     *
     */
    public java.util.List<String> printArguments() {
        java.util.List<String> filename = new ArrayList();
        //System.out.println("\t END");
        //prologOutput.append("Arguments:");
        ArgBuildManager manag = new ArgBuildManager();
        filename = manag.ArgumentComposer();
        return filename;
    }

    /**
     * *
     * For Arg Builder
     */
    void consultFile(String fileToLoad) {

        File filetoreconsult = null;
        /* 
         FileDialog d = new FileDialog(this, "Consult file...");
         d.setVisible(true);
         fileToLoad = d.getFile();
         folder = d.getDirectory();
         */
        if (fileToLoad != null) {
            filetoreconsult = new File(fileToLoad);
            if (engine.consultAbsolute(filetoreconsult)) {
                addToReloaders(filetoreconsult, "consult");
            }
            //System.out.println("file:"+filetoreconsult.);

        }
    }
    
    static class LoadedFile {

        File file;
        String method;

        LoadedFile(File file, String method) {
            this.file = file;
            this.method = method;
            if (!(method.equals("consult") || method.equals("load_dyn"))) {
                throw new IPException("bad load method");
            }
        }

        public boolean equals(LoadedFile o) {
            return file.equals(o.file) && method.equals(o.method);
        }
    }
    
    
    void addToReloaders(File file, String method) {
        final LoadedFile lf = new LoadedFile(file, method);
        if (!loadedFiles.contains(lf)) {
            loadedFiles.addElement(lf);
            
            
            
            
            /*
            addItemToMenu(fileMenu, file.getName(), new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    engine.command(lf.method + "('" + engine.unescapedFilePath(lf.file.getAbsolutePath()) + "')");

                }
            });
            */        
        }
    }
    

}