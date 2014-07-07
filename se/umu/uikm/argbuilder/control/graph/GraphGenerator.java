/* 
 * Copyright (c) 2013, Tim Boudreau
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package se.umu.uikm.argbuilder.control.graph;

import edu.uci.ics.jung.graph.Forest;
import org.apache.commons.collections15.Factory;

/**
 * Demonstrates the visualization of a Tree using TreeLayout and BalloonLayout.
 * An examiner lens performing a hyperbolic transformation of the view is also
 * included.
 *
 * @author Tom Nelson
 */
@SuppressWarnings("serial")
public class GraphGenerator {

    // Code borrowed from JUNG's demos

    Factory<Integer> edgeFactory = new Factory<Integer>() {
        int i = 0;
        public Integer create() {
            return i++;
        }
    };

    public Forest<String, Integer> createTree(Forest<String, Integer> graph) {

        graph.addVertex("A0");
        graph.addEdge(edgeFactory.create(), "A0", "B0");
        graph.addEdge(edgeFactory.create(), "A0", "B1");
        graph.addEdge(edgeFactory.create(), "A0", "B2");

        graph.addVertex("B0");
        graph.addEdge(edgeFactory.create(), "B0", "C0");
        graph.addEdge(edgeFactory.create(), "B0", "C1");
        graph.addEdge(edgeFactory.create(), "B0", "C2");
        graph.addEdge(edgeFactory.create(), "B0", "C3");

        graph.addVertex("HA3");
       	graph.addEdge(edgeFactory.create(), "HA3", "I1");
       	graph.addEdge(edgeFactory.create(), "HA3", "I2");
        
       

        return graph;
    }
    

    public Forest<String, Integer> createTree2(Forest<String, Integer> graph) {

        graph.addVertex("Subg1");
        graph.addEdge(100, "Subg1", "p(a)");
        graph.addEdge(200, "Subg1", "p(b)");
        
        graph.addVertex("Subg2");
        graph.addEdge(300, "Subg2", "p(a)");
        graph.addEdge(400, "Subg2", "p(c)");
        graph.addEdge(500, "Subg2", "p(d)");
        
        graph.addVertex("Subg3");
        graph.addEdge(600, "Subg3", "p(e)");
        graph.addEdge(900, "Subg3", "p(f)");

        return graph;
    }
    
    
    public Forest<String, Integer> createTree3(Forest<String, Integer> graph) {

        graph.addVertex("Activ1");
        graph.addEdge(101, "Activ1", "Goal_a");
        graph.addEdge(201, "Activ1", "Goal_b");
        
        graph.addVertex("Activ2");
        graph.addEdge(301, "Activ2", "Goal_a");
        graph.addEdge(401, "Activ2", "Goal_b");
        graph.addEdge(501, "Activ2", "Goal_c");
        
        graph.addVertex("Activ3");
        graph.addEdge(601, "Activ3", "Goal_e");

        graph.addVertex("Activ4");
        graph.addEdge(701, "Activ4", "Goal_a");
        graph.addEdge(801, "Activ4", "Goal_b");
        graph.addEdge(901, "Activ4", "Goal_d");

        return graph;
    }
    
}
