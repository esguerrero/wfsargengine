/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.declarativa.interprolog.gui;

import com.declarativa.interprolog.AbstractPrologEngine;
import com.declarativa.interprolog.ObjectExamplePair;
import com.declarativa.interprolog.gui.PredicateTableModel;
import com.declarativa.interprolog.gui.TermListModel;
import com.declarativa.interprolog.gui.TermTreeModel;
import com.declarativa.interprolog.gui.XSBTableModel;
import com.declarativa.interprolog.util.IPException;
import com.timboudreau.vl.jung.extensions.BaseJungScene;
import edu.uci.ics.jung.algorithms.layout.BalloonLayout;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.DAGLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout2;
import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.RadialTreeLayout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout2;
import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.graph.DelegateTree;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.ObservableGraph;
import edu.uci.ics.jung.graph.Tree;
import edu.uci.ics.jung.graph.util.Context;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.VisualizationServer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalLensGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.layout.LayoutTransition;
import edu.uci.ics.jung.visualization.transform.LensSupport;
import edu.uci.ics.jung.visualization.transform.MutableTransformer;
import edu.uci.ics.jung.visualization.transform.MutableTransformerDecorator;
import edu.uci.ics.jung.visualization.transform.shape.HyperbolicShapeTransformer;
import edu.uci.ics.jung.visualization.transform.shape.ViewLensSupport;
import edu.uci.ics.jung.visualization.util.Animator;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Insets;
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
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.functors.ConstantTransformer;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import se.umu.uikm.argbuilder.control.ArgBuildManager;
import se.umu.uikm.argbuilder.control.wfs.CallsFromArgBuilder2XSB;
import se.umu.uikm.argbuilder.gui.layout.XLayout;

/**
 *
 * @author Esteban
 */
public class Ini2 extends javax.swing.JFrame implements WindowListener {

    Vector loadedFiles;
    private static int topLevelCount = 0;
    public static boolean debug = false;

    public AbstractPrologEngine engine = null;

    
    /********************/
    
        /**
     * the graph
     */
    Forest<String,Integer> graph;
 
    Factory<DirectedGraph<String,Integer>> graphFactory =    new Factory<DirectedGraph<String,Integer>>() {
 
        public DirectedGraph<String, Integer> create() {
            return new DirectedSparseMultigraph<String,Integer>();
        }
    };
 
    Factory<Tree<String,Integer>> treeFactory =
        new Factory<Tree<String,Integer>> () {
 
        public Tree<String, Integer> create() {
            return new DelegateTree<String,Integer>(graphFactory);
        }
    };
 
    Factory<Integer> edgeFactory = new Factory<Integer>() {
        int i=0;
        public Integer create() {
            return i++;
        }};
     
    Factory<String> vertexFactory = new Factory<String>() {
        int i=0;
        public String create() {
            return "V"+i++;
        }};
 
    /**
     * the visual component and renderer for the graph
     */
    VisualizationViewer<String,Integer> vv;
     
    VisualizationServer.Paintable rings;
     
    String root;
     
    TreeLayout<String,Integer> layout;
     
    BalloonLayout<String,Integer> radialLayout;
    /**
     * provides a Hyperbolic lens for the view
     */
    LensSupport hyperbolicViewSupport;
    
    /*********************/
    
    public Ini2(AbstractPrologEngine e, boolean autoDisplay) {
        /// super("PrologEngine listener (Swing)");
        System.out.println("ENTRA");
        if (e != null) {
            engine = e;
        } else {
            throw new IPException("missing Prolog engine");
        }

        String VF = e.getImplementationPeer().visualizationFilename();
        if (engine.getLoadFromJar()) {
            engine.consultFromPackage(VF, Ini2.class);
        } else {
            engine.consultRelative(VF, Ini2.class);
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

        /*
        prologInput.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
//                    sendToProlog();
                    e.consume();
                }
            }
        });
        
        */
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
    public Ini2() throws IOException {
        
        
        
        
        
        
        
        
        
        
        initComponents();

        graphComponents();

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
        testButton = new javax.swing.JButton();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel5 = new javax.swing.JPanel();
        jLayeredPane2 = new javax.swing.JLayeredPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        prologOutput = new javax.swing.JTextArea();
        jButton1 = new javax.swing.JButton();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        jLayeredPane3 = new javax.swing.JLayeredPane();
        jLayeredPane9 = new javax.swing.JLayeredPane();
        jPanel6 = new javax.swing.JPanel();
        jLayeredPane4 = new javax.swing.JLayeredPane();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jLayeredPane5 = new javax.swing.JLayeredPane();
        jLayeredPane6 = new javax.swing.JLayeredPane();
        jLayeredPane7 = new javax.swing.JLayeredPane();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLayeredPane8 = new javax.swing.JLayeredPane();

        fileChooser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileChooserActionPerformed(evt);
            }
        });

        testButton.setText(org.openide.util.NbBundle.getMessage(Ini2.class, "Ini2.testButton.text")); // NOI18N

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

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

        jButton1.setText(org.openide.util.NbBundle.getMessage(Ini2.class, "Ini2.jButton1.text")); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLayeredPane1.setBackground(new java.awt.Color(153, 153, 255));
        jLayeredPane1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLayeredPane1.setPreferredSize(new java.awt.Dimension(600, 0));

        javax.swing.GroupLayout jLayeredPane1Layout = new javax.swing.GroupLayout(jLayeredPane1);
        jLayeredPane1.setLayout(jLayeredPane1Layout);
        jLayeredPane1Layout.setHorizontalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 596, Short.MAX_VALUE)
        );
        jLayeredPane1Layout.setVerticalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 411, Short.MAX_VALUE)
        );

        jLayeredPane3.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(Ini2.class, "Ini2.jLayeredPane3.border.title"))); // NOI18N

        javax.swing.GroupLayout jLayeredPane3Layout = new javax.swing.GroupLayout(jLayeredPane3);
        jLayeredPane3.setLayout(jLayeredPane3Layout);
        jLayeredPane3Layout.setHorizontalGroup(
            jLayeredPane3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jLayeredPane3Layout.setVerticalGroup(
            jLayeredPane3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 77, Short.MAX_VALUE)
        );

        jLayeredPane9.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(Ini2.class, "Ini2.jLayeredPane9.border.title"))); // NOI18N

        javax.swing.GroupLayout jLayeredPane9Layout = new javax.swing.GroupLayout(jLayeredPane9);
        jLayeredPane9.setLayout(jLayeredPane9Layout);
        jLayeredPane9Layout.setHorizontalGroup(
            jLayeredPane9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jLayeredPane9Layout.setVerticalGroup(
            jLayeredPane9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
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
                    .addComponent(jLayeredPane9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jLayeredPane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLayeredPane3)
                    .addComponent(jLayeredPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(240, Short.MAX_VALUE))
        );
        jLayeredPane2Layout.setVerticalGroup(
            jLayeredPane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayeredPane2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jLayeredPane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jLayeredPane2Layout.createSequentialGroup()
                        .addComponent(jLayeredPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 415, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(22, 22, 22)
                        .addComponent(jLayeredPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jLayeredPane2Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 313, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLayeredPane9)))
                .addContainerGap())
        );
        jLayeredPane2.setLayer(jScrollPane1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane2.setLayer(jButton1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane2.setLayer(jLayeredPane1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane2.setLayer(jLayeredPane3, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane2.setLayer(jLayeredPane9, javax.swing.JLayeredPane.DEFAULT_LAYER);

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
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab(org.openide.util.NbBundle.getMessage(Ini2.class, "Ini2.jPanel5.TabConstraints.tabTitle"), jPanel5); // NOI18N

        jPanel6.setPreferredSize(new java.awt.Dimension(1100, 476));

        jButton2.setText(org.openide.util.NbBundle.getMessage(Ini2.class, "Ini2.jButton2.text")); // NOI18N
        jButton2.setPreferredSize(new java.awt.Dimension(133, 23));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText(org.openide.util.NbBundle.getMessage(Ini2.class, "Ini2.jButton3.text")); // NOI18N

        jButton4.setText(org.openide.util.NbBundle.getMessage(Ini2.class, "Ini2.jButton4.text")); // NOI18N

        javax.swing.GroupLayout jLayeredPane4Layout = new javax.swing.GroupLayout(jLayeredPane4);
        jLayeredPane4.setLayout(jLayeredPane4Layout);
        jLayeredPane4Layout.setHorizontalGroup(
            jLayeredPane4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayeredPane4Layout.createSequentialGroup()
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(73, 73, 73)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(89, 89, 89)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jLayeredPane4Layout.setVerticalGroup(
            jLayeredPane4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jLayeredPane4Layout.createSequentialGroup()
                .addContainerGap(24, Short.MAX_VALUE)
                .addGroup(jLayeredPane4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3)
                    .addComponent(jButton4))
                .addGap(19, 19, 19))
        );
        jLayeredPane4.setLayer(jButton2, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane4.setLayer(jButton3, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane4.setLayer(jButton4, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jLayeredPane5.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout jLayeredPane5Layout = new javax.swing.GroupLayout(jLayeredPane5);
        jLayeredPane5.setLayout(jLayeredPane5Layout);
        jLayeredPane5Layout.setHorizontalGroup(
            jLayeredPane5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jLayeredPane5Layout.setVerticalGroup(
            jLayeredPane5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 449, Short.MAX_VALUE)
        );

        jLayeredPane6.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(Ini2.class, "Ini2.jLayeredPane6.border.title"))); // NOI18N

        javax.swing.GroupLayout jLayeredPane6Layout = new javax.swing.GroupLayout(jLayeredPane6);
        jLayeredPane6.setLayout(jLayeredPane6Layout);
        jLayeredPane6Layout.setHorizontalGroup(
            jLayeredPane6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 541, Short.MAX_VALUE)
        );
        jLayeredPane6Layout.setVerticalGroup(
            jLayeredPane6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 88, Short.MAX_VALUE)
        );

        jLayeredPane7.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane3.setViewportView(jTextArea1);

        javax.swing.GroupLayout jLayeredPane7Layout = new javax.swing.GroupLayout(jLayeredPane7);
        jLayeredPane7.setLayout(jLayeredPane7Layout);
        jLayeredPane7Layout.setHorizontalGroup(
            jLayeredPane7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 761, Short.MAX_VALUE)
        );
        jLayeredPane7Layout.setVerticalGroup(
            jLayeredPane7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 237, Short.MAX_VALUE)
        );
        jLayeredPane7.setLayer(jScrollPane3, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jLayeredPane8.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout jLayeredPane8Layout = new javax.swing.GroupLayout(jLayeredPane8);
        jLayeredPane8.setLayout(jLayeredPane8Layout);
        jLayeredPane8Layout.setHorizontalGroup(
            jLayeredPane8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jLayeredPane8Layout.setVerticalGroup(
            jLayeredPane8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLayeredPane4)
                    .addComponent(jLayeredPane7)
                    .addComponent(jLayeredPane8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLayeredPane6)
                    .addComponent(jLayeredPane5))
                .addGap(23, 23, 23))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLayeredPane5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLayeredPane6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLayeredPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLayeredPane7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLayeredPane8)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab(org.openide.util.NbBundle.getMessage(Ini2.class, "Ini2.jPanel6.TabConstraints.tabTitle"), jPanel6); // NOI18N

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

        jTabbedPane2.getAccessibleContext().setAccessibleName(org.openide.util.NbBundle.getMessage(Ini2.class, "Ini2.jTabbedPane2.AccessibleContext.accessibleName")); // NOI18N

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void fileChooserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileChooserActionPerformed


    }//GEN-LAST:event_fileChooserActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
       
      
        
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void prologOutputComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_prologOutputComponentShown
        // TODO add your handling code here:
    }//GEN-LAST:event_prologOutputComponentShown

    private void jTabbedPane2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabbedPane2MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jTabbedPane2MouseEntered

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

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
            java.util.logging.Logger.getLogger(Ini2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Ini2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Ini2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Ini2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                System.out.println("RUN");
                try {
                    new Ini2().setVisible(true);
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFileChooser fileChooser;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JLayeredPane jLayeredPane2;
    private javax.swing.JLayeredPane jLayeredPane3;
    private javax.swing.JLayeredPane jLayeredPane4;
    private javax.swing.JLayeredPane jLayeredPane5;
    private javax.swing.JLayeredPane jLayeredPane6;
    private javax.swing.JLayeredPane jLayeredPane7;
    private javax.swing.JLayeredPane jLayeredPane8;
    private javax.swing.JLayeredPane jLayeredPane9;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea prologOutput;
    private javax.swing.JButton testButton;
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

    private void graphComponents() throws IOException {
             
        
        Forest<String, Integer> forest = new DelegateForest<>();
        ObservableGraph g = new ObservableGraph(new BalloonLayoutDemo().createTree(forest));

        Layout layout = new BalloonLayout(forest);
        //Layout layout = new TreeLayout(forest, 70, 70);

        final BaseJungScene scene = new SceneImpl(g, layout);
        
        jLayeredPane1.setLayout(new BorderLayout());     
        //jf.setLayout(new BorderLayout());
        
        jLayeredPane1.add(new JScrollPane(scene.createView()), BorderLayout.CENTER);
        //jf.add(new JScrollPane(scene.createView()), BorderLayout.CENTER);

        
        
        JToolBar bar = new JToolBar();
        bar.setMargin(new Insets(5, 5, 5, 5));
        bar.setLayout(new FlowLayout(5));
        DefaultComboBoxModel<Layout> mdl = new DefaultComboBoxModel<>();
        mdl.addElement(new KKLayout(g));
        mdl.addElement(layout);
        mdl.addElement(new BalloonLayout(forest));
        mdl.addElement(new RadialTreeLayout(forest));
        mdl.addElement(new CircleLayout(g));
        mdl.addElement(new FRLayout(g));
        mdl.addElement(new FRLayout2(g));
        mdl.addElement(new ISOMLayout(g));
        mdl.addElement(new SpringLayout(g));
        mdl.addElement(new SpringLayout2(g));
        mdl.addElement(new DAGLayout(g));
        mdl.addElement(new XLayout(g));
        mdl.setSelectedItem(layout);
        final JCheckBox checkbox = new JCheckBox("Animate iterative layouts");

        scene.setLayoutAnimationFramesPerSecond(48);

        final JComboBox<Layout> layouts = new JComboBox(mdl);
        layouts.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> jlist, Object o, int i, boolean bln, boolean bln1) {
                o = o.getClass().getSimpleName();
                return super.getListCellRendererComponent(jlist, o, i, bln, bln1); //To change body of generated methods, choose Tools | Templates.
            }
        });
        bar.add(new JLabel(" Layout Type"));
        bar.add(layouts);
        layouts.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                Layout layout = (Layout) layouts.getSelectedItem();
                // These two layouts implement IterativeContext, but they do
                // not evolve toward anything, they just randomly rearrange
                // themselves.  So disable animation for these.
                if (layout instanceof ISOMLayout || layout instanceof DAGLayout) {
                    checkbox.setSelected(false);
                }
                scene.setGraphLayout(layout, true);
            }
        });

        bar.add(new JLabel(" Connection Shape"));
        DefaultComboBoxModel<Transformer<Context<Graph<String, Number>, Number>, Shape>> shapes = new DefaultComboBoxModel<>();
        shapes.addElement(new EdgeShape.QuadCurve<String, Number>());
        shapes.addElement(new EdgeShape.BentLine<String, Number>());
        shapes.addElement(new EdgeShape.CubicCurve<String, Number>());
        shapes.addElement(new EdgeShape.Line<String, Number>());
        shapes.addElement(new EdgeShape.Box<String, Number>());
        shapes.addElement(new EdgeShape.Orthogonal<String, Number>());
        shapes.addElement(new EdgeShape.Wedge<String, Number>(10));

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
        bar.add(shapesBox);
        

        //jf.add(bar, BorderLayout.NORTH);
        
        
        bar.add(new MinSizePanel(scene.createSatelliteView()));
        bar.setFloatable(false);
        bar.setRollover(true);

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

        bar.add(selectionLabel);

        checkbox.setSelected(true);
        checkbox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                scene.setAnimateIterativeLayouts(checkbox.isSelected());
            }
        });
        bar.add(checkbox);
        jLayeredPane3.setLayout(new BorderLayout());     

        jLayeredPane3.add(bar);
//        jf.setSize(jf.getGraphicsConfiguration().getBounds().width - 120, 700);
//        jf.setSize(new Dimension(1280, 720));
//        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        

        
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent we) {
                scene.relayout(true);
                scene.validate();
            }
        });
        
        
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
    

    
    private static class MinSizePanel extends JPanel {

        MinSizePanel(JComponent inner) {
            setLayout(new BorderLayout());
            add(inner, BorderLayout.CENTER);
            setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        }

        public Dimension getPreferredSize() {
            Dimension result = super.getPreferredSize();
            result.height = 40;
            return result;
        }
    }

    
    
         
    class Rings implements VisualizationServer.Paintable {
         
        BalloonLayout<String,Integer> layout;
         
        public Rings(BalloonLayout<String,Integer> layout) {
            this.layout = layout;
        }
         
        public void paint(Graphics g) {
            g.setColor(Color.gray);
         
            Graphics2D g2d = (Graphics2D)g;
 
            Ellipse2D ellipse = new Ellipse2D.Double();
            for(String v : layout.getGraph().getVertices()) {
                Double radius = layout.getRadii().get(v);
                if(radius == null) continue;
                Point2D p = layout.transform(v);
                ellipse.setFrame(-radius, -radius, 2*radius, 2*radius);
                AffineTransform at = AffineTransform.getTranslateInstance(p.getX(), p.getY());
                Shape shape = at.createTransformedShape(ellipse);
                 
                MutableTransformer viewTransformer =
                    vv.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.VIEW);
                 
                if(viewTransformer instanceof MutableTransformerDecorator) {
                    shape = vv.getRenderContext().getMultiLayerTransformer().transform(shape);
                } else {
                    shape = vv.getRenderContext().getMultiLayerTransformer().transform(Layer.LAYOUT,shape);
                }
 
                g2d.draw(shape);
            }
        }
 
        public boolean useTransform() {
            return true;
        }
    }
     
    /**
     * 
     */
    private void createTree() {
         
        graph.addVertex("A0");
        graph.addEdge(edgeFactory.create(), "A0", "B0");
        graph.addEdge(edgeFactory.create(), "A0", "B1");
        graph.addEdge(edgeFactory.create(), "A0", "B2");
         
        graph.addEdge(edgeFactory.create(), "B0", "C0");
        graph.addEdge(edgeFactory.create(), "B0", "C1");
        graph.addEdge(edgeFactory.create(), "B0", "C2");
        graph.addEdge(edgeFactory.create(), "B0", "C3");
 
        graph.addEdge(edgeFactory.create(), "C2", "H0");
        graph.addEdge(edgeFactory.create(), "C2", "H1");
 
        graph.addEdge(edgeFactory.create(), "B1", "D0");
        graph.addEdge(edgeFactory.create(), "B1", "D1");
        graph.addEdge(edgeFactory.create(), "B1", "D2");
 
        graph.addEdge(edgeFactory.create(), "B2", "E0");
        graph.addEdge(edgeFactory.create(), "B2", "E1");
        graph.addEdge(edgeFactory.create(), "B2", "E2");
 
        graph.addEdge(edgeFactory.create(), "D0", "F0");
        graph.addEdge(edgeFactory.create(), "D0", "F1");
        graph.addEdge(edgeFactory.create(), "D0", "F2");
         
        graph.addEdge(edgeFactory.create(), "D1", "G0");
        graph.addEdge(edgeFactory.create(), "D1", "G1");
        graph.addEdge(edgeFactory.create(), "D1", "G2");
        graph.addEdge(edgeFactory.create(), "D1", "G3");
        graph.addEdge(edgeFactory.create(), "D1", "G4");
        graph.addEdge(edgeFactory.create(), "D1", "G5");
        graph.addEdge(edgeFactory.create(), "D1", "G6");
        graph.addEdge(edgeFactory.create(), "D1", "G7");
         
        // uncomment this to make it a Forest:
//          graph.addVertex("K0");
//          graph.addEdge(edgeFactory.create(), "K0", "K1");
//          graph.addEdge(edgeFactory.create(), "K0", "K2");
//          graph.addEdge(edgeFactory.create(), "K0", "K3");
//          
//          graph.addVertex("J0");
//      graph.addEdge(edgeFactory.create(), "J0", "J1");
//      graph.addEdge(edgeFactory.create(), "J0", "J2");
//      graph.addEdge(edgeFactory.create(), "J1", "J4");
//      graph.addEdge(edgeFactory.create(), "J2", "J3");
////        graph.addEdge(edgeFactory.create(), "J2", "J5");
////        graph.addEdge(edgeFactory.create(), "J4", "J6");
////        graph.addEdge(edgeFactory.create(), "J4", "J7");
////        graph.addEdge(edgeFactory.create(), "J3", "J8");
////        graph.addEdge(edgeFactory.create(), "J6", "B9");
 
         
    }
    
}
