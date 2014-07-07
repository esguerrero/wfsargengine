/* 
 ** Author(s): Miguel Calejo
 ** Contact:   interprolog@declarativa.com, http://www.declarativa.com
 ** Copyright (C) Declarativa, Portugal, 2000-2002
 ** Use and distribution, without any warranties, under the terms of the 
 ** GNU Library General Public License, readable in http://www.fsf.org/copyleft/lgpl.html
 */
package com.declarativa.interprolog.gui;

import com.declarativa.interprolog.*;
import com.declarativa.interprolog.util.*;
import com.timboudreau.vl.jung.JungScene;
import com.timboudreau.vl.jung.extensions.BaseJungScene;
import com.xsb.interprolog.NativeEngine;
import edu.uci.ics.jung.algorithms.layout.BalloonLayout;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.DAGLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout2;
import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.RadialTreeLayout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout2;
import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.ObservableGraph;
import edu.uci.ics.jung.graph.util.Context;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.Shape;
import java.awt.Toolkit;
import java.util.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.awt.dnd.*;
import java.awt.datatransfer.*;
import javax.swing.border.BevelBorder;
import org.apache.commons.collections15.Transformer;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import se.umu.uikm.argbuilder.control.ArgBuildManager;
import se.umu.uikm.argbuilder.control.DerbyConnect;
import se.umu.uikm.argbuilder.control.graph.GraphGenerator;
import se.umu.uikm.argbuilder.control.wfs.CallsFromArgBuilder2XSB;
import se.umu.uikm.argbuilder.gui.layout.XLayout;

/**
 * A simple ProlographJung listener, with a consult menu and an history
 * mechanism. This should be sub-classed, in order to define
 * sendToProlographJung()
 */
public abstract class ListenerWindow extends JFrame implements WindowListener {

    //Boton para cargar
    // Variables declaration - do not modify
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel PanelRight;
    private String nameOfFileToLoad = "";
    // End of variables declaration
    public JTextArea prologOutput, prologInput;
    JMenu historyMenu, fileMenu, loadRules;
    Vector loadedFiles;
    private static int topLevelCount = 0;
    public AbstractPrologEngine engine = null;

    /**
     * NEW GUI
     */
    private javax.swing.JFileChooser fileChooser;
    // private javax.swing.JButton jButton1;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JLayeredPane jLayeredPane2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JLayeredPane jLayeredPane3;
    private javax.swing.JScrollPane jScrollPaneX;

    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;

    private javax.swing.JLayeredPane jLayeredPane4;
    private javax.swing.JLayeredPane jLayeredPane5;
    private javax.swing.JLayeredPane jLayeredPane6;
    private javax.swing.JLayeredPane jLayeredPane7;
    private javax.swing.JLayeredPane jLayeredPane8;
    private javax.swing.JLayeredPane jLayeredPane10;

    private javax.swing.JScrollPane jScrollPane3;

    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTable jTable1;

    private Forest<String, Integer> forest = new DelegateForest<>();
    private ObservableGraph graphJung; //= new ObservableGraph(new BalloonLayoutDemo().createTree(forest));

    private Layout layout;// = new BalloonLayout(forest);
    //Layout layout = new TreeLayout(forest, 70, 70);

    //private final BaseJungScene scene2;//, scene2;// = new SceneImpl(graphJung, layout);
    public ListenerWindow(AbstractPrologEngine e) throws IOException {
        this(e, true);
    }

    public ListenerWindow(AbstractPrologEngine e, boolean autoDisplay) throws IOException {

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ListenerWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ListenerWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ListenerWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ListenerWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

//        constructWindowContents();
        initComponents();

        graphComponents();

        /* 
         Forest<String, Integer> forest = new DelegateForest<>();
         forest = new GraphGenerator().createTree(forest);
        
         UpdategraphComponents(forest);
         //ObservableGraph g = new ObservableGraph(new BalloonLayoutDemo().createTree(forest));
         //ObservableGraph g = new ObservableGraph(new GraphGenerator().createTree(forest));
        
         */
        /// super("PrologEngine listener (Swing)");
        if (e != null) {
            engine = e;
        } else {
            throw new IPException("missing Prolog engine");
        }

        String VF = e.getImplementationPeer().visualizationFilename();
        if (engine.getLoadFromJar()) {
            engine.consultFromPackage(VF, ListenerWindow.class);
        } else {
            engine.consultRelative(VF, ListenerWindow.class);
        }
        engine.teachMoreObjects(guiExamples());

        if (engine == null) {
            dispose(); // no interface object permitted!
        } else {
            topLevelCount++;
        }
        debug = engine.isDebug();

        loadedFiles = new Vector();

        constructMenu();

        addWindowListener(this);

        prologInput.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendToProlog();
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
            focusInput();
        }

        /**
         * *******************************************************************************************
         */
        this.setVisible(true);

    }

    private void graphComponents() throws IOException {

        Forest<String, Integer> forest = new DelegateForest<>();
        Forest<String, Integer> forest2 = new DelegateForest<>();
        Forest<String, Integer> forest3 = new DelegateForest<>();
        //ObservableGraph g = new ObservableGraph(new BalloonLayoutDemo().createTree(forest));
        ObservableGraph g = new ObservableGraph(new GraphGenerator().createTree(forest));
        ObservableGraph g2 = new ObservableGraph(new GraphGenerator().createTree2(forest2));
        ObservableGraph g3 = new ObservableGraph(new GraphGenerator().createTree3(forest3));


        //Layout layout = new BalloonLayout(forest);
        Layout layout = new BalloonLayout(forest);
        Layout layout2 = new BalloonLayout(forest2);
        Layout layout3 = new TreeLayout(forest3, 70, 70);

        final BaseJungScene scene = new SceneImpl(g, layout);
        final BaseJungScene scene2 = new SceneImpl(g2, layout2);
        final BaseJungScene scene3 = new SceneImpl(g3, layout3);

        jLayeredPane1.setLayout(new BorderLayout());
        //jf.setLayout(new BorderLayout());

        jLayeredPane5.setLayout(new BorderLayout());
        jLayeredPane8.setLayout(new BorderLayout());

        jLayeredPane1.add(new JScrollPane(scene.createView()), BorderLayout.CENTER);
        //jf.add(new JScrollPane(scene2.createView()), BorderLayout.CENTER);

        jLayeredPane5.add(new JScrollPane(scene2.createView()), BorderLayout.CENTER);
        jLayeredPane8.add(new JScrollPane(scene3.createView()), BorderLayout.CENTER);

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
        mdl.addElement(new edu.uci.ics.jung.algorithms.layout.SpringLayout(g));
        mdl.addElement(new SpringLayout2(g));
        mdl.addElement(new DAGLayout(g));
        mdl.addElement(new XLayout(g));
        mdl.setSelectedItem(layout);
        final JCheckBox checkbox = new JCheckBox("Animate iterative layouts");

        scene.setLayoutAnimationFramesPerSecond(48);
        scene2.setLayoutAnimationFramesPerSecond(48);
        scene3.setLayoutAnimationFramesPerSecond(48);

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
        bar.add(new ListenerWindow.MinSizePanel(scene.createSatelliteView()));
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

        jLayeredPane6.setLayout(new BorderLayout());

        jLayeredPane3.add(bar);

        jLayeredPane6.add(bar);

//        jf.setSize(jf.getGraphicsConfiguration().getBounds().width - 120, 700);
//        jf.setSize(new Dimension(1280, 720));
//        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent we) {
                scene.relayout(true);
                scene.validate();
                scene2.relayout(true);
                scene2.validate();
                scene3.relayout(true);
                scene3.validate();
            }
        });

    }

    private void UpdategraphComponents() throws IOException {
        jLayeredPane1.removeAll();

        List<String> heads = new ArrayList<>();
        List<String> subgrph = new ArrayList<>();
        ArgBuildManager manag = new ArgBuildManager();
        Forest<String, Integer> graphGUI = new DelegateForest<>();
        graphGUI = manag.getGraphJung();

        System.out.println("\t \t NUM UF VERT:" + graphGUI.getVertexCount());

        Forest<String, Integer> forest = new DelegateForest<>();
        //ObservableGraph g = new ObservableGraph(new BalloonLayoutDemo().createTree(forest));
        ObservableGraph g = new ObservableGraph(graphGUI);

        //Layout layout = new BalloonLayout(forest);
        Layout layout = new BalloonLayout(graphGUI);
        //Layout layout = new TreeLayout(forest, 70, 70);

        final BaseJungScene scene2 = new SceneImpl(g, layout);

        //  jLayeredPane1.setLayout(new BorderLayout());     
        //jf.setLayout(new BorderLayout());
        jLayeredPane1.add(new JScrollPane(scene2.createView()), BorderLayout.CENTER);
        //jf.add(new JScrollPane(scene2.createView()), BorderLayout.CENTER);

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
        mdl.addElement(new edu.uci.ics.jung.algorithms.layout.SpringLayout(g));
        mdl.addElement(new SpringLayout2(g));
        mdl.addElement(new DAGLayout(g));
        mdl.addElement(new XLayout(g));
        mdl.setSelectedItem(layout);
        final JCheckBox checkbox = new JCheckBox("Animate iterative layouts");

        scene2.setLayoutAnimationFramesPerSecond(48);

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
                scene2.setGraphLayout(layout, true);
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
                scene2.setConnectionEdgeShape(xform);
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
        bar.add(new ListenerWindow.MinSizePanel(scene2.createSatelliteView()));
        bar.setFloatable(false);
        bar.setRollover(true);

        final JLabel selectionLabel = new JLabel("<html>&nbsp;</html>");
        System.out.println("LOOKUP IS " + scene2.getLookup());
        Lookup.Result<String> selectedNodes = scene2.getLookup().lookupResult(String.class);
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
                scene2.setAnimateIterativeLayouts(checkbox.isSelected());
            }
        });
        bar.add(checkbox);
        jLayeredPane3.setLayout(new BorderLayout());

        jLayeredPane3.add(bar);
//        jf.setSize(jf.getGraphicsConfiguration().getBounds().width - 120, 700);
//        jf.setSize(new Dimension(1280, 720));
//        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.repaint();
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent we) {
                scene2.relayout(true);
                scene2.validate();
            }
        });

    }

    private void UpdategraphComponents(Forest<String, Integer> graph) throws IOException {
        jLayeredPane1.removeAll();

        /*     
             
         List<String> heads = new ArrayList<>();
         List<String> subgrph = new ArrayList<>();
         ArgBuildManager manag = new ArgBuildManager();
         Forest<String, Integer> graphGUI = new DelegateForest<>();
        
        
         graphGUI = manag.getGraphJung();
         */
        Forest<String, Integer> graphGUI = new DelegateForest<>();

        graphGUI = graph;

        System.out.println("\t \t NUM UF VERT:" + graphGUI.getVertexCount());

        //ObservableGraph g = new ObservableGraph(new BalloonLayoutDemo().createTree(forest));
        ObservableGraph g = new ObservableGraph(graphGUI);

        //Layout layout = new BalloonLayout(forest);
        Layout layout = new BalloonLayout(graphGUI);
        //Layout layout = new TreeLayout(forest, 70, 70);

        final BaseJungScene scene2 = new SceneImpl(g, layout);

        jLayeredPane1.setLayout(new BorderLayout());
        //jf.setLayout(new BorderLayout());

        jLayeredPane1.add(new JScrollPane(scene2.createView()), BorderLayout.CENTER);
        //jf.add(new JScrollPane(scene2.createView()), BorderLayout.CENTER);

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
        mdl.addElement(new edu.uci.ics.jung.algorithms.layout.SpringLayout(g));
        mdl.addElement(new SpringLayout2(g));
        mdl.addElement(new DAGLayout(g));
        mdl.addElement(new XLayout(g));
        mdl.setSelectedItem(layout);
        final JCheckBox checkbox = new JCheckBox("Animate iterative layouts");

        scene2.setLayoutAnimationFramesPerSecond(48);

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
                scene2.setGraphLayout(layout, true);
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
                scene2.setConnectionEdgeShape(xform);
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
        bar.add(new ListenerWindow.MinSizePanel(scene2.createSatelliteView()));
        bar.setFloatable(false);
        bar.setRollover(true);

        final JLabel selectionLabel = new JLabel("<html>&nbsp;</html>");
        System.out.println("LOOKUP IS " + scene2.getLookup());
        Lookup.Result<String> selectedNodes = scene2.getLookup().lookupResult(String.class);
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
                scene2.setAnimateIterativeLayouts(checkbox.isSelected());
            }
        });
        bar.add(checkbox);
        jLayeredPane3.setLayout(new BorderLayout());

        jLayeredPane3.add(bar);
//        jf.setSize(jf.getGraphicsConfiguration().getBounds().width - 120, 700);
//        jf.setSize(new Dimension(1280, 720));
//        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // this.repaint();
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent we) {
                scene2.relayout(true);
                scene2.validate();
            }
        });

    }

    private void UpdategraphComponents2(Forest<String, Integer> graph) throws IOException {
        jLayeredPane5.removeAll();

        /*     
             
         List<String> heads = new ArrayList<>();
         List<String> subgrph = new ArrayList<>();
         ArgBuildManager manag = new ArgBuildManager();
         Forest<String, Integer> graphGUI = new DelegateForest<>();
        
        
         graphGUI = manag.getGraphJung();
         */
        Forest<String, Integer> graphGUI = new DelegateForest<>();

        graphGUI = graph;

        System.out.println("\t \t NUM UF VERT:" + graphGUI.getVertexCount());

        //ObservableGraph g = new ObservableGraph(new BalloonLayoutDemo().createTree(forest));
        ObservableGraph g = new ObservableGraph(graphGUI);

        //Layout layout = new BalloonLayout(forest);
        Layout layout = new BalloonLayout(graphGUI);
        //Layout layout = new TreeLayout(forest, 70, 70);

        final BaseJungScene scene2 = new SceneImpl(g, layout);

        jLayeredPane5.setLayout(new BorderLayout());
        //jf.setLayout(new BorderLayout());

        jLayeredPane5.add(new JScrollPane(scene2.createView()), BorderLayout.CENTER);
        //jf.add(new JScrollPane(scene2.createView()), BorderLayout.CENTER);

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
        mdl.addElement(new edu.uci.ics.jung.algorithms.layout.SpringLayout(g));
        mdl.addElement(new SpringLayout2(g));
        mdl.addElement(new DAGLayout(g));
        mdl.addElement(new XLayout(g));
        mdl.setSelectedItem(layout);
        final JCheckBox checkbox = new JCheckBox("Animate iterative layouts");

        scene2.setLayoutAnimationFramesPerSecond(48);

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
                scene2.setGraphLayout(layout, true);
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
                scene2.setConnectionEdgeShape(xform);
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
        bar.add(new ListenerWindow.MinSizePanel(scene2.createSatelliteView()));
        bar.setFloatable(false);
        bar.setRollover(true);

        final JLabel selectionLabel = new JLabel("<html>&nbsp;</html>");
        System.out.println("LOOKUP IS " + scene2.getLookup());
        Lookup.Result<String> selectedNodes = scene2.getLookup().lookupResult(String.class);
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
                scene2.setAnimateIterativeLayouts(checkbox.isSelected());
            }
        });
        bar.add(checkbox);
        jLayeredPane6.setLayout(new BorderLayout());

        jLayeredPane6.add(bar);
//        jf.setSize(jf.getGraphicsConfiguration().getBounds().width - 120, 700);
//        jf.setSize(new Dimension(1280, 720));
//        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // this.repaint();
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent we) {
                scene2.relayout(true);
                scene2.validate();
            }
        });

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        fileChooser = new javax.swing.JFileChooser();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel5 = new javax.swing.JPanel();
        jLayeredPane2 = new javax.swing.JLayeredPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        prologOutput = new javax.swing.JTextArea();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        prologInput = new javax.swing.JTextArea();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        jLayeredPane3 = new javax.swing.JLayeredPane();
        jPanel6 = new javax.swing.JPanel();
        jLayeredPane4 = new javax.swing.JLayeredPane();
        jButton2 = new javax.swing.JButton();
        jLayeredPane5 = new javax.swing.JLayeredPane();
        jLayeredPane6 = new javax.swing.JLayeredPane();
        jLayeredPane7 = new javax.swing.JLayeredPane();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLayeredPane8 = new javax.swing.JLayeredPane();

        jLayeredPane10 = new javax.swing.JLayeredPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        jScrollPaneX = new javax.swing.JScrollPane();

        fileChooser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileChooserActionPerformed(evt);
            }
        });

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

        jButton1.setText("Load Program"); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    jButton1ActionPerformed(evt);
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        });

        prologInput.setColumns(20);
        prologInput.setRows(5);
        jScrollPane2.setViewportView(prologInput);

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
                .addGap(0, 0, Short.MAX_VALUE)
        );

        jLayeredPane3.setBorder(javax.swing.BorderFactory.createTitledBorder("Controls")); // NOI18N

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
                        .addGroup(jLayeredPane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLayeredPane3)
                                .addComponent(jLayeredPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                        .addGap(18, 18, 18)
                        .addComponent(jLayeredPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jLayeredPane2.setLayer(jScrollPane1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane2.setLayer(jButton1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane2.setLayer(jScrollPane2, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane2.setLayer(jLayeredPane1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane2.setLayer(jLayeredPane3, javax.swing.JLayeredPane.DEFAULT_LAYER);

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

        jTabbedPane2.addTab("Argument Engine", jPanel5); // NOI18N

        jPanel6.setPreferredSize(new java.awt.Dimension(1100, 476));

        jLayeredPane4.setBorder(javax.swing.BorderFactory.createTitledBorder("Local Selection of Fragments")); // NOI18N

        jButton2.setText("Load Program"); // NOI18N
        jButton2.setPreferredSize(new java.awt.Dimension(133, 23));

        jButton3.setText("Local Selection"); // NOI18N
        jButton3.setPreferredSize(new java.awt.Dimension(133, 23));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        
        
        jButton4.setText("Global Selection"); // NOI18N
        jButton4.setPreferredSize(new java.awt.Dimension(133, 23));

        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    jButton2ActionPerformed(evt);
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        });

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

//        jLayeredPane4Layout.setHorizontalGroup(
//                jLayeredPane4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                .addGroup(jLayeredPane4Layout.createSequentialGroup()
//                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
//                        .addGap(0, 0, Short.MAX_VALUE))
//        );
//        jLayeredPane4Layout.setVerticalGroup(
//                jLayeredPane4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jLayeredPane4Layout.createSequentialGroup()
//                        .addContainerGap(24, Short.MAX_VALUE)
//                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
//                        .addGap(19, 19, 19))
//        );
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

        jLayeredPane6.setBorder(javax.swing.BorderFactory.createTitledBorder("Controls")); // NOI18N

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

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "Fragments", ""
                }
        ) {
            boolean[] canEdit = new boolean[]{
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        jScrollPaneX.setViewportView(jTable1);

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane3.setViewportView(jTextArea1);
        javax.swing.GroupLayout jLayeredPane10Layout = new javax.swing.GroupLayout(jLayeredPane10);
        jLayeredPane10.setLayout(jLayeredPane10Layout);
        jLayeredPane10Layout.setHorizontalGroup(
                jLayeredPane10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jLayeredPane10Layout.createSequentialGroup()
                        .addComponent(jScrollPaneX, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 416, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jLayeredPane10Layout.setVerticalGroup(
                jLayeredPane10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPaneX, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jScrollPane3)
        );
        jLayeredPane10.setLayer(jScrollPaneX, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane10.setLayer(jScrollPane3, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jLayeredPane7Layout = new javax.swing.GroupLayout(jLayeredPane7);
        jLayeredPane7.setLayout(jLayeredPane7Layout);
        jLayeredPane7Layout.setHorizontalGroup(
                jLayeredPane7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jLayeredPane7Layout.createSequentialGroup()
                        .addComponent(jLayeredPane10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
        );
        jLayeredPane7Layout.setVerticalGroup(
                jLayeredPane7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLayeredPane10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jLayeredPane7.setLayer(jLayeredPane10, javax.swing.JLayeredPane.DEFAULT_LAYER);

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
        /*        
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
         */
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

        jTabbedPane2.addTab("Activity Framework", jPanel6); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 1362, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jTabbedPane2)
        );

        jTabbedPane2.getAccessibleContext().setAccessibleName("tabd"); // NOI18N

        pack();

    }// </editor-fold>    

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

    // WindowListener methods
    public void windowOpened(WindowEvent e) {
    }

    public void windowClosed(WindowEvent e) {
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowClosing(WindowEvent e) {
        dispose();
        engine.shutdown();
        topLevelCount--;
        if (topLevelCount <= 0) {
            System.exit(0);
        }
        // should check whether any relevant windows are changed...
    }

    public void windowActivated(WindowEvent e) {
        prologInput.requestFocus();
    }

    public void windowDeactivated(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
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

    void constructWindowContents() {
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
        jLayeredPane3 = new javax.swing.JLayeredPane();
        jPanel6 = new javax.swing.JPanel();

        fileChooser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileChooserActionPerformed(evt);
            }
        });

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

        jButton1.setText("Load Program XX"); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    jButton1ActionPerformed(evt);
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        });

        prologInput.setColumns(20);
        prologInput.setRows(5);
        jScrollPane2.setViewportView(prologInput);

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
                .addGap(0, 0, Short.MAX_VALUE)
        );

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
                        .addGroup(jLayeredPane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLayeredPane3)
                                .addComponent(jLayeredPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                        .addGap(18, 18, 18)
                        .addComponent(jLayeredPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jLayeredPane2.setLayer(jScrollPane1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane2.setLayer(jButton1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane2.setLayer(jScrollPane2, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane2.setLayer(jLayeredPane1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane2.setLayer(jLayeredPane3, javax.swing.JLayeredPane.DEFAULT_LAYER);

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

        jTabbedPane2.addTab("Argument Engine", jPanel5); // NOI18N

        jPanel6.setPreferredSize(new java.awt.Dimension(1100, 476));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
                jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 1273, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
                jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 565, Short.MAX_VALUE)
        );

        jTabbedPane2.addTab("Activity Framework", jPanel6); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 1278, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jTabbedPane2)
        );

        jLayeredPane3.setBorder(javax.swing.BorderFactory.createTitledBorder("Controls")); // NOI18N

        jTabbedPane2.getAccessibleContext().setAccessibleName("tabX"); // NOI18N

        pack();

    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) throws IOException {
        final CallsFromArgBuilder2XSB calls2XSB = new CallsFromArgBuilder2XSB();

        String calls_for_table = "";//maybe temporal
        String returns_for_call = "";

        GraphAnalysis();//generates the graph partition

        try {
            ReDrawGraph();
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        java.util.List<String> filenames = new ArrayList();
        filenames = printArguments();//write the files for each subgraph rel_rules(P,X)

        /**
         * *
         * With this loop, every subprogram generated is sent to XSB for
         * evaluation.
         *
         */
        for (Iterator<String> it = filenames.iterator(); it.hasNext();) {
            String string = it.next();
//            System.out.println("\n\t ####### WFS EVALUATION STARTS ####### file:" + string);

            FileCleaner(string);//deletes all compiled files xwam
            CleanHeads(); // clean the variables already used

            /**
             * evaluation of each generated subfile using WFS
             */
            consultFile("../examples/" + string);

            String nameFile3 = calls2XSB.File2Consult(string);

            sendCallToProlog(nameFile3);

            // engine.command("setof(X,p(X),List)");
            //calls2XSB.WFSEvaluation2(engine, string);
            //calls2XSB.WFSEvaluation(engine, string);
            calls2XSB.WFSEvaluationSimple(engine, string);
//            System.out.println("\n\t ####### WFS EVALUATION ENDS ####### file:" + string);
        }

        ArgumentWriter();

        drawArguments();

        //UpdategraphComponents();
        ArgBuildManager manag = new ArgBuildManager();
        Forest<String, Integer> graphGUI = new DelegateForest<>();
        graphGUI = manag.getGraphJung();

        UpdategraphComponents(graphGUI);

        /**
         * ********************
         *
         * int returnVal = fileChooser.showOpenDialog(this); if (returnVal ==
         * JFileChooser.APPROVE_OPTION) { File file =
         * fileChooser.getSelectedFile();
         *
         * fileChooser.addChoosableFileFilter(new
         * javax.swing.filechooser.FileFilter() {
         *
         * public String getDescription() { return "Prolog native program
         * (*.P)"; }
         *
         * public boolean accept(File f) { if (f.isDirectory()) { return true; }
         * else { return f.getName().toLowerCase().endsWith(".P"); } } });
         *
         * try { // What to do with the file, e.graphJung. display it in a
         * TextArea prologOutput.read(new FileReader(file.getAbsolutePath()),
         * null); } catch (IOException ex) { System.out.println("problem
         * accessing file" + file.getAbsolutePath()); } } else {
         * System.out.println("File access cancelled by user."); }
         *
         */
        //One file chooser for each buton click
    }

    private void fileChooserActionPerformed(java.awt.event.ActionEvent evt) {

    }

    private void CleanHeads() {
        List<String> headlist = new ArrayList<>();
        ArgBuildManager argB = new ArgBuildManager();
        headlist = argB.CleanHead();
        for (Iterator<String> it = headlist.iterator(); it.hasNext();) {
            String head = it.next();
            engine.command("abolish_all_tables");
            engine.command("retractall(" + head + ")");
        }

    }

    void handlePrologInputDnD(DropTargetDropEvent dtde) {
        //System.out.println("drop:"+dtde);
        try {
            Transferable transferable = dtde.getTransferable();
            /*
             DataFlavor[] flavors = transferable.getTransferDataFlavors();
             for (int f=0;f<flavors.length;f++)
             System.out.println("Flavor:"+flavors[f]);*/
            int action = dtde.getDropAction();
            if (transferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                if (engine.isIdle()) {
                    dtde.acceptDrop(action);
                    final java.util.List files = (java.util.List) transferable.getTransferData(DataFlavor.javaFileListFlavor);
                    dtde.getDropTargetContext().dropComplete(true);
                    boolean allPs = true;
                    for (int f = 0; f < files.size(); f++) {
                        String filename = ((File) files.get(f)).getName();
                        int dot = filename.lastIndexOf('.');
                        if (!filename.endsWith(".P")) {
                            allPs = false;
                            break;
                        }
                    }
                    if (!allPs) {
                        errorMessage("All dragged files must be Prolog source files (with a .P extension)");
                    } else {
                        prologOutput.append("\nReconsulting " + ((files.size() > 1 ? files.size() + " files...\n" : files.size() + " file...\n")));
                        Runnable r = new Runnable() {
                            public void run() {
                                boolean crashed = false;
                                Toolkit.getDefaultToolkit().sync();
                                for (int f = 0; f < files.size() && !crashed; f++) {
                                    File file = (File) files.get(f);
                                    if (!processDraggedFile(file)) {
                                        crashed = true;
                                    }
                                }
                                if (crashed) {
                                    prologOutput.append("...terminated with errors.\n");
                                } else {
                                    prologOutput.append("...done.\n");
                                }
                            }
                        };
                        SwingUtilities.invokeLater(r);
                    }
                } else {
                    dtde.rejectDrop();
                    errorMessage("You can not consult files while Prolog is working");
                }
            } else {
                dtde.rejectDrop();
            }
        } catch (Exception e) {
            throw new IPException("Problem dropping:" + e);
        }
    }

    public boolean processDraggedFile(File f) {
        if (engine.consultAbsolute(f)) {
            addToReloaders(f, "consult");
            return true;
        } else {
            errorMessage("Problems reconsulting " + f.getName());
            return false;
        }
    }

    public void errorMessage(String m) {
        beep();
        JOptionPane.showMessageDialog(this, m, "Error", JOptionPane.ERROR_MESSAGE);
    }

    void constructMenu() {
        JMenuBar mb;
        mb = new JMenuBar();
        setJMenuBar(mb);

        final CallsFromArgBuilder2XSB calls2XSB = new CallsFromArgBuilder2XSB();

        /**
         * ****************************************************************
         * ***************ENTRY POINT FOR CALLING THE ARGXSB ANALYSIS *****
         * ****************************************************************
         *
         * Carga un archivo con las reglas para crear los argumentos
         */
        loadRules = new JMenu("");
        loadRules.setMnemonic('A');
        mb.add(loadRules);
        addItemToMenu(loadRules, "Select a Program", 'S', new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                //jLayeredPane1.setVisible(true);
                //WORKFLOW:
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

                try {
                    ReDrawGraph();
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                    //System.out.println("EXCEPCION EN REDRAW CALL");
                }

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
                    sendCallToProlog(nameFile3);

                    //calls_for_table = calls2XSB.FindCallsForTable(engine);
                    //returns_for_call = calls2XSB.FindReturnsForCall(engine, string);
                    calls2XSB.WFSEvaluation(engine, string);

                    //System.out.println("\t \t *************************************************");
                }

                ArgumentWriter();

                drawArguments();

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
            }
        });

        fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F');
//        mb.add(fileMenu);

        addItemToMenu(fileMenu, "Consult...", 'C', new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                reconsultFile();
            }
        });

        if (engine.getImplementationPeer() instanceof XSBPeer) {
            addItemToMenu(fileMenu, "Load dynamically...", 'L', new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    load_dynFile();
                }
            });
        }

        fileMenu.addSeparator();

        JMenu toolMenu = new JMenu("Tools");
        toolMenu.setMnemonic('T');
        //       mb.add(toolMenu);

        final JCheckBoxMenuItem debugging = new JCheckBoxMenuItem("Engine debugging");
        toolMenu.add(debugging);
        debugging.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                engine.setDebug(debugging.isSelected());
            }
        });

        addItemToMenu(toolMenu, "See Object Specifications", 'S', new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                engine.command("showObjectVariables");
            }
        });

        addItemToMenu(toolMenu, "Interrupt Prolog", 'I', new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                engine.interrupt();
            }
        });
        /*
         addItemToMenu(toolMenu,"Serialize JFrame",new ActionListener(){
         public void actionPerformed(ActionEvent e){
         Object [] toSerialize = {new JFrame("My window")};
         engine.setDebug(true);
         System.out.println(engine.deterministicGoal("true","[Object]",toSerialize));
         }
         });*/

        historyMenu = new JMenu("History", true);
        historyMenu.setMnemonic('H');
//        mb.add(historyMenu);
        historyMenu.addSeparator(); // to avoid Swing bug handling key events
    }

    class HistoryListener implements ActionListener {

        JTextComponent targetText;
        String memory;

        HistoryListener(JTextComponent t, String s) {
            targetText = t;
            memory = s;
        }

        public void actionPerformed(ActionEvent e) {
            targetText.replaceSelection(memory);
        }
    }

    static void addItemToMenu(JMenu menu, String item, ActionListener handler) {
        JMenuItem menuItem = new JMenuItem(item);
        menu.add(menuItem);
        menuItem.addActionListener(handler);
    }

    static void addItemToMenu(JMenu menu, String item, char mnemonics, ActionListener handler) {
        JMenuItem menuItem = new JMenuItem(item);
        menuItem.setMnemonic(mnemonics);
        menu.add(menuItem);
        menuItem.addActionListener(handler);
    }

    public abstract void sendToProlog();

    public abstract void sendCallToProlog(String s);

    protected void addToHistory() {
        JMenuItem item;
        String goal = prologInput.getText();
        if (goal.equals(";")) {
            return; // not worthy remembering
        }
        if (goal.length() > 20) {
            historyMenu.add(item = new JMenuItem(goal.substring(0, 19) + "..."));
        } else {
            historyMenu.add(item = new JMenuItem(goal.substring(0, goal.length())));
        }
        item.addActionListener(new HistoryListener(prologInput, goal));
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
            addItemToMenu(fileMenu, file.getName(), new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    engine.command(lf.method + "('" + engine.unescapedFilePath(lf.file.getAbsolutePath()) + "')");

                }
            });
        }
    }

    public boolean successfulCommand(String s) {
        try {
            return engine.command(s);
        } catch (Exception e) {
        }
        return false;
    }

    void reconsultFile() {
        String nome, directorio;
        File filetoreconsult = null;
        FileDialog d = new FileDialog(this, "Consult file...");
        d.setVisible(true);
        nome = d.getFile();
        directorio = d.getDirectory();
        if (nome != null) {
            filetoreconsult = new File(directorio, nome);
            if (engine.consultAbsolute(filetoreconsult)) {
                addToReloaders(filetoreconsult, "consult");
            }
        }
    }

    public void drawArguments() {
        java.util.List<String> args = new ArrayList<>();
        ArgBuildManager manag = new ArgBuildManager();
        args = manag.RetrieveArguments();
        prologOutput.append("List of built arguments:\n");
        for (Iterator<String> it = args.iterator(); it.hasNext();) {
            String string = it.next();
            prologOutput.append("\t" + string + "\n");
        }
    }

    public void drawFragments() {
        java.util.List<String> args = new ArrayList<>();
        ArgBuildManager manag = new ArgBuildManager();
        args = manag.RetrieveArguments();
        jTextArea1.append("List of built arguments:\n");
        for (Iterator<String> it = args.iterator(); it.hasNext();) {
            String string = it.next();
            jTextArea1.append("\t" + string + "\n");
        }

        DefaultTableModel defaultModel = (DefaultTableModel) jTable1.getModel();

        Object[][] rowData = {{"Hello", "World"}};
        Object[] columnNames = {"A", "B"};

        DefaultTableModel model = new DefaultTableModel(rowData, columnNames);
        //table = new JTable();
        jTable1.setModel(model);

        /*
         Vector newRow = new Vector();
         newRow.add("Total Amount Spend");
         newRow.add("G");
         newRow.add("SDFG");
         newRow.add("KASDPF");
         newRow.add("ÖE9JASK");
         newRow.add("09DFJ SLD JI");
         */
        int sizearg = args.size();

        for (int j = 0; j < args.size(); j++) {
         Vector newRow = new Vector();

         newRow.add(" ");            
         newRow.add(args.get(j));
         
         model.addRow(newRow);
        }

 //       model.addRow(newRow);
    }

    /**
     * **
     * Print argraphJunguments
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
     * **
     * Print argraphJunguments
     *
     */
    public java.util.List<String> PrintFragments() {
        java.util.List<String> filename = new ArrayList();
        //System.out.println("\t END");
        //prologOutput.append("Arguments:");
        ArgBuildManager manag = new ArgBuildManager();
        filename = manag.FragmentComposer();
        return filename;
    }

    void deleteOldFiles() {

        ArgBuildManager manag = new ArgBuildManager();

    }

    /**
     * **
     * For ArgraphJung Builder
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

    /**
     * *
     * Generates the subgraph and splits the Program in Subgraphs (Graph
     * Analysis)
     */
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
     * Generates the subgraph and splits the Program in Subgraphs (Graph
     * Analysis)
     */
    public void GraphAnalysis2() {
        String fileToLoad, folder;
        File filetoreconsult = null;
        FileDialog d = new FileDialog(this, "Consult file...");
        d.setVisible(true);
        fileToLoad = d.getFile();
        folder = d.getDirectory();
        if (fileToLoad != null) {
            ArgBuildManager manag = new ArgBuildManager();
            manag.GraphAnalysis4Frag(folder, fileToLoad);//Call to the ArgBuildManager!
        }
    }

    public void ArgumentWriter() {
        ArgBuildManager manag = new ArgBuildManager();
        //manag.ArgumentWriter();
        manag.ArgumentWriter2();
    }

    public void FragmentWriter() {
        ArgBuildManager manag = new ArgBuildManager();
        //manag.ArgumentWriter();

        manag.FragmentWriter();
    }

    public void ReDrawGraph() throws IOException {
        List<String> heads = new ArrayList<>();
        List<String> subgrph = new ArrayList<>();
        ArgBuildManager manag = new ArgBuildManager();
        Forest<String, Integer> graphGUI = new DelegateForest<>();
        graphGUI = manag.getGraphJung();

        System.out.println("\t \t NUM UF VERT:" + graphGUI.getVertexCount());

    }

    /**
     * For XSB only
     */
    void load_dynFile() {
        String nome, directorio;
        File filetoreconsult = null;
        FileDialog d = new FileDialog(this, "load_dyn file...");
        d.show();
        nome = d.getFile();
        directorio = d.getDirectory();
        if (nome != null) {
            filetoreconsult = new File(directorio, nome);
            if (successfulCommand("load_dyn('" + engine.unescapedFilePath(filetoreconsult.getAbsolutePath()) + "')")) {
                addToReloaders(filetoreconsult, "load_dyn");
            }
        }
    }

    public void focusInput() {
        prologInput.selectAll();
        prologInput.requestFocus();
    }

    public void scrollToBottom() {
        if (prologOutput.isShowing()) {
            prologOutput.setCaretPosition(prologOutput.getDocument().getEndPosition().getOffset() - 1 /* OBOB hack */);
            try {
                // If we're in a JScrollPane, force scrolling to bottom and left
                JScrollBar scrollbarV = ((JScrollPane) ((JViewport) (prologOutput.getParent())).getParent()).getVerticalScrollBar();
                scrollbarV.setValue(scrollbarV.getMaximum());
                JScrollBar scrollbarH = ((JScrollPane) ((JViewport) (prologOutput.getParent())).getParent()).getHorizontalScrollBar();
                scrollbarH.setValue(scrollbarH.getMinimum());
            } catch (Exception e) {/* We're not in a JScrollPane, forget it! */

            };
        }
    }
    public static boolean debug = false;
    public static String prologStartCommand = null;
    public static boolean loadFromJar = true;

    public static void commonMain(String args[]) {
        commonGreeting();

        //el argumento simplemente es la direccion del XSB
        if (args.length >= 1) {
            int i = 0;
            while (i < args.length) {
                if (args[i].toLowerCase().startsWith("-d")) {
                    debug = true;
                    i++;
                } else if (args[i].toLowerCase().startsWith("-nojar")) {
                    loadFromJar = false;
                    i++;
                } else {
                    prologStartCommand = remainingArgs(args, i);
                    break;
                }
            }
        } else {
            throw new IPException("Missing arguments in command line");
        }

    }

    public static void commonGreeting() {
        System.out.println("Welcome " + System.getProperty("user.name") + " to InterProlog " + AbstractPrologEngine.version + " on Java "
                + System.getProperty("java.version") + " ("
                + System.getProperty("java.vendor") + "), "
                + System.getProperty("os.name") + " "
                + System.getProperty("os.version"));
        System.out.println("Test desde Java");

    }

    public static String commandArgs(String[] args) {
        return remainingArgs(args, 0);
    }

    public static String remainingArgs(String[] args, int first) {
        if (args.length == 0) {
            throw new IPException("Missing arguments in command line");
        }
        StringBuffer temp = new StringBuffer();
        for (int i = first; i < args.length; i++) {
            if (i > first) {
                temp.append(" ");
            }
            temp.append(args[i]);
        }
        return temp.toString();
    }

    public static void beep() {
        Toolkit.getDefaultToolkit().beep();
    }

    public String getNameOfFileToLoad() {
        return nameOfFileToLoad;
    }

    public void setNameOfFileToLoad(String nameOfFileToLoad) {
        this.nameOfFileToLoad = nameOfFileToLoad;
    }

    private void jTabbedPane2MouseEntered(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
    }

    private void prologOutputComponentShown(java.awt.event.ComponentEvent evt) {
        // TODO add your handling code here:
    }

    /**
     * Activate the "Load Program" button in Activity Framework tab
     *
     * @param evt
     */
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) throws IOException {

        final CallsFromArgBuilder2XSB calls2XSB = new CallsFromArgBuilder2XSB();

        String calls_for_table = "";//maybe temporal
        String returns_for_call = "";

        GraphAnalysis2();//generates the graph partition

        System.out.println("END");

        try {
            ReDrawGraph();
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        java.util.List<String> filenames = new ArrayList();
        filenames = printArguments();//write the files for each subgraph rel_rules(P,X)

        for (Iterator<String> it = filenames.iterator(); it.hasNext();) {
            String string = it.next();
//            System.out.println("\n\t ####### WFS EVALUATION STARTS ####### file:" + string);

            FileCleaner(string);
            CleanHeads();

            consultFile("../examples/" + string);

            String nameFile3 = calls2XSB.File2Consult(string);

            sendCallToProlog(nameFile3);

            calls2XSB.WFSEvaluationSimple(engine, string);
//            System.out.println("\n\t ####### WFS EVALUATION ENDS ####### file:" + string);
        }

        //ArgumentWriter();
        FragmentWriter();

        drawFragments();

        ArgBuildManager manag = new ArgBuildManager();
        Forest<String, Integer> graphGUI = new DelegateForest<>();
        graphGUI = manag.getGraphJung();

        UpdategraphComponents(graphGUI);

    }

    private void FileCleaner(String filename) {
        try {

            String filetodel = new File("../examples/" + filename).getCanonicalPath();
            System.out.println("File to delete:" + filename);

            String xwam = filename.replace(".P", ".xwam");

            File fileD = new File(xwam);
            fileD.delete();

        } catch (java.io.IOException ex) {
            System.out.println("ERROR deleting files:" + ex.getLocalizedMessage());
        }
    }
    
    
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        
        
        
         try {
         System.out.println(new File(".").getCanonicalPath());
         System.out.println("DIR:" + new File("../").getCanonicalPath());

         } catch (java.io.IOException ex) {
         System.out.println("ex." + ex.getLocalizedMessage());
         }
         
        
        try {
            // TODO add your handling code here:
            Runtime.getRuntime().exec("java -jar ..\\WizArg\\WizArg.jar");
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        
    }                                        

    

}
