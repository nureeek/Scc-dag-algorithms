package graph;

import graph.util.GraphLoader;
import graph.scc.SCC;
import graph.topo.TopologicalSort;
import graph.dagsp.DAGShortestPath;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;


public class GraphTests {

    @Test
    public void testSCCSmallGraph() {
        GraphLoader.Graph g = new GraphLoader.Graph(4, true);
        g.addEdge(0, 1, 1);
        g.addEdge(1, 0, 1);
        g.addEdge(2, 3, 1);
        g.addEdge(3, 2, 1);
        g.addEdge(1, 2, 2);

        SCC scc = new SCC(g);
        var comps = scc.run();

        assertEquals(2, comps.size());
        assertTrue(comps.get(0).contains(0) || comps.get(1).contains(0));
    }

    @Test
    public void testTopologicalOrder() {
        GraphLoader.Graph dag = new GraphLoader.Graph(5, true);
        dag.addEdge(0, 1, 1);
        dag.addEdge(0, 2, 1);
        dag.addEdge(1, 3, 1);
        dag.addEdge(2, 3, 1);
        dag.addEdge(3, 4, 1);

        TopologicalSort topo = new TopologicalSort(dag);
        List<Integer> order = topo.run();

        assertTrue(order.indexOf(0) < order.indexOf(4));
        assertEquals(5, order.size());
    }

    @Test
    public void testDAGShortestAndLongest() {
        GraphLoader.Graph dag = new GraphLoader.Graph(4, true);
        dag.addEdge(0, 1, 2);
        dag.addEdge(1, 2, 3);
        dag.addEdge(0, 2, 10);
        dag.addEdge(2, 3, 1);

        TopologicalSort topo = new TopologicalSort(dag);
        List<Integer> order = topo.run();

        DAGShortestPath dsp = new DAGShortestPath(dag);
        double[] shortest = dsp.shortestPaths(0, order);
        double[] longest = dsp.longestPaths(0, order);

        assertEquals(0.0, shortest[0]);
        assertEquals(6.0, shortest[3]);  // 0→1→2→3 (2+3+1)
        assertEquals(11.0, longest[3]);  // 0→2→3 (10+1)
    }
}
