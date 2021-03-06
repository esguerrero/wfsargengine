/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.declarativa.interprolog.gui;

/**
 *
 * @author Esteban
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

import javax.swing.BorderFactory;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton;

import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.functors.ConstantTransformer;

import edu.uci.ics.jung.algorithms.layout.BalloonLayout;
import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.graph.DelegateTree;
import edu.uci.ics.jung.graph.Tree;
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

/**
 * Demonstrates the visualization of a Tree using TreeLayout and BalloonLayout.
 * An examiner lens performing a hyperbolic transformation of the view is also
 * included.
 *
 * @author Tom Nelsonimport javax.swing.JButton;
 *
 *
 */
@SuppressWarnings("serial")
public class Testo1 extends JApplet {

    /**
     * the graph
     */
    Forest<String, Integer> graph;

    Factory<DirectedGraph<String, Integer>> graphFactory = new Factory<DirectedGraph<String, Integer>>() {

        public DirectedGraph<String, Integer> create() {
            return new DirectedSparseMultigraph<String, Integer>();
        }
    };

    Factory<Tree<String, Integer>> treeFactory
            = new Factory<Tree<String, Integer>>() {

                public Tree<String, Integer> create() {
                    return new DelegateTree<String, Integer>(graphFactory);
                }
            };

    Factory<Integer> edgeFactory = new Factory<Integer>() {
        int i = 0;

        public Integer create() {
            return i++;
        }
    };

    Factory<String> vertexFactory = new Factory<String>() {
        int i = 0;

        public String create() {
            return "V" + i++;
        }
    };

    /**
     * the visual component and renderer for the graph
     */
    VisualizationViewer<String, Integer> vv;

    VisualizationServer.Paintable rings;

    String root;

    TreeLayout<String, Integer> layout;

    BalloonLayout<String, Integer> radialLayout;
    /**
     * provides a Hyperbolic lens for the view
     */
    LensSupport hyperbolicViewSupport;
    private javax.swing.JButton jButton1;

    public Testo1() {

        jButton1 = new javax.swing.JButton();
        jButton1.setText(org.openide.util.NbBundle.getMessage(Ini2.class, "Ini2.jButton1.text")); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }

        });

        // create a simple graph for the demo
        graph = new DelegateForest<String, Integer>();

        graph = createTree();
        
        updateGUI(graph);

    }

    private void jButton1ActionPerformed(ActionEvent evt) {
        System.out.println("dd");
        

        graph = createTree2();
                System.out.println("d2");

        updateGUI(graph);
                System.out.println("d3");

    }

    
    private void updateGUI(Forest<String, Integer> grapho){
    
        
        layout = new TreeLayout<String, Integer>(grapho);
        radialLayout = new BalloonLayout<String, Integer>(grapho);
        radialLayout.setSize(new Dimension(900, 900));
        vv = new VisualizationViewer<String, Integer>(layout, new Dimension(600, 600));
        vv.setBackground(Color.white);
        vv.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line());
        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        // add a listener for ToolTips
        vv.setVertexToolTipTransformer(new ToStringLabeller());
        vv.getRenderContext().setArrowFillPaintTransformer(new ConstantTransformer(Color.lightGray));
        rings = new Rings(radialLayout);

        
        
        Container content = getContentPane();
        final GraphZoomScrollPane panel = new GraphZoomScrollPane(vv);

        content.add(panel);

        final DefaultModalGraphMouse graphMouse = new DefaultModalGraphMouse();

        vv.setGraphMouse(graphMouse);
        vv.addKeyListener(graphMouse.getModeKeyListener());

        hyperbolicViewSupport
                = new ViewLensSupport<String, Integer>(vv, new HyperbolicShapeTransformer(vv,
                                vv.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.VIEW)),
                        new ModalLensGraphMouse());

        graphMouse.addItemListener(hyperbolicViewSupport.getGraphMouse().getModeListener());

        JComboBox modeBox = graphMouse.getModeComboBox();
        modeBox.addItemListener(graphMouse.getModeListener());
        graphMouse.setMode(ModalGraphMouse.Mode.TRANSFORMING);

        final ScalingControl scaler = new CrossoverScalingControl();

        vv.scaleToLayout(scaler);

        JButton plus = new JButton("+");
        plus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                scaler.scale(vv, 1.1f, vv.getCenter());
            }
        });
        JButton minus = new JButton("-");
        minus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                scaler.scale(vv, 1 / 1.1f, vv.getCenter());
            }
        });

        JToggleButton radial = new JToggleButton("Balloon");
        radial.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {

                    LayoutTransition<String, Integer> lt
                            = new LayoutTransition<String, Integer>(vv, layout, radialLayout);
                    Animator animator = new Animator(lt);
                    animator.start();
                    vv.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.LAYOUT).setToIdentity();
                    vv.addPreRenderPaintable(rings);
                } else {

                    LayoutTransition<String, Integer> lt
                            = new LayoutTransition<String, Integer>(vv, radialLayout, layout);
                    Animator animator = new Animator(lt);
                    animator.start();
                    vv.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.LAYOUT).setToIdentity();
                    vv.removePreRenderPaintable(rings);
                }
                vv.repaint();
            }
        });
        final JRadioButton hyperView = new JRadioButton("Hyperbolic View");
        hyperView.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                hyperbolicViewSupport.activate(e.getStateChange() == ItemEvent.SELECTED);
            }
        });

        JPanel scaleGrid = new JPanel(new GridLayout(1, 0));
        scaleGrid.setBorder(BorderFactory.createTitledBorder("Zoom"));

        JPanel controls = new JPanel();
        scaleGrid.add(plus);
        scaleGrid.add(minus);
        controls.add(radial);
        controls.add(scaleGrid);
        controls.add(modeBox);
        controls.add(jButton1);
        content.add(controls, BorderLayout.SOUTH);
    
        
        this.revalidate();
        this.repaint();
        
    }
    
    
    
    
    class Rings implements VisualizationServer.Paintable {

        BalloonLayout<String, Integer> layout;

        public Rings(BalloonLayout<String, Integer> layout) {
            this.layout = layout;
        }

        public void paint(Graphics g) {
            g.setColor(Color.gray);

            Graphics2D g2d = (Graphics2D) g;

            Ellipse2D ellipse = new Ellipse2D.Double();
            for (String v : layout.getGraph().getVertices()) {
                Double radius = layout.getRadii().get(v);
                if (radius == null) {
                    continue;
                }
                Point2D p = layout.transform(v);
                ellipse.setFrame(-radius, -radius, 2 * radius, 2 * radius);
                AffineTransform at = AffineTransform.getTranslateInstance(p.getX(), p.getY());
                Shape shape = at.createTransformedShape(ellipse);

                MutableTransformer viewTransformer
                        = vv.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.VIEW);

                if (viewTransformer instanceof MutableTransformerDecorator) {
                    shape = vv.getRenderContext().getMultiLayerTransformer().transform(shape);
                } else {
                    shape = vv.getRenderContext().getMultiLayerTransformer().transform(Layer.LAYOUT, shape);
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
    private Forest<String, Integer> createTree() {

        Forest<String, Integer> graph1 = new DelegateForest<>();
        graph1.addVertex("A0");
        graph1.addEdge(edgeFactory.create(), "A0", "B0");
        graph1.addEdge(edgeFactory.create(), "A0", "B1");
        graph1.addEdge(edgeFactory.create(), "A0", "B2");

        graph1.addEdge(edgeFactory.create(), "B0", "C0");
        graph1.addEdge(edgeFactory.create(), "B0", "C1");
        graph1.addEdge(edgeFactory.create(), "B0", "C2");
        graph1.addEdge(edgeFactory.create(), "B0", "C3");

        graph1.addEdge(edgeFactory.create(), "C2", "H0");
        graph1.addEdge(edgeFactory.create(), "C2", "H1");

        graph1.addEdge(edgeFactory.create(), "B1", "D0");
        graph1.addEdge(edgeFactory.create(), "B1", "D1");
        graph1.addEdge(edgeFactory.create(), "B1", "D2");

        graph1.addEdge(edgeFactory.create(), "B2", "E0");
        graph1.addEdge(edgeFactory.create(), "B2", "E1");
        graph1.addEdge(edgeFactory.create(), "B2", "E2");

        graph1.addEdge(edgeFactory.create(), "D0", "F0");
        graph1.addEdge(edgeFactory.create(), "D0", "F1");
        graph1.addEdge(edgeFactory.create(), "D0", "F2");

        graph1.addEdge(edgeFactory.create(), "D1", "G0");
        graph1.addEdge(edgeFactory.create(), "D1", "G1");
        graph1.addEdge(edgeFactory.create(), "D1", "G2");
        graph1.addEdge(edgeFactory.create(), "D1", "G3");
        graph1.addEdge(edgeFactory.create(), "D1", "G4");
        graph1.addEdge(edgeFactory.create(), "D1", "G5");
        graph1.addEdge(edgeFactory.create(), "D1", "G6");
        graph1.addEdge(edgeFactory.create(), "D1", "G7");
        return graph1;

    }

    
   private Forest<String, Integer> createTree2() {

        Forest<String, Integer> graph1 = new DelegateForest<>();
        graph1.addVertex("q0");
        graph1.addEdge(edgeFactory.create(), "q0", "B0");
        graph1.addEdge(edgeFactory.create(), "q0", "B1");
        graph1.addEdge(edgeFactory.create(), "q0", "B2");


        return graph1;

    }


    
    /**
     * a driver for this demo
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        Container content = frame.getContentPane();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        content.add(new Testo1());
        frame.pack();
        frame.setVisible(true);

    }
}
