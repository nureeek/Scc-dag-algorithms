package graph.topo;

import graph.util.GraphLoader;
import graph.util.Metrics;

import java.util.*;

public class TopologicalSort {

    private GraphLoader.Graph dag;
    private Metrics metrics;

    public TopologicalSort(GraphLoader.Graph dag) {
        this.dag = dag;
        this.metrics = new Metrics();
    }

    public Metrics getMetrics() {
        return metrics;
    }


    public List<Integer> run() {
        metrics.start();

        int n = dag.n;
        int[] indeg = new int[n];
        for (var e : dag.edges) indeg[e.to]++;

        Queue<Integer> q = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            if (indeg[i] == 0) {
                q.add(i);
                metrics.pushes++;
            }
        }

        List<Integer> order = new ArrayList<>();

        while (!q.isEmpty()) {
            int u = q.poll();
            metrics.pops++;
            order.add(u);

            for (var e : dag.adj.get(u)) {
                indeg[e.to]--;
                metrics.edgesProcessed++;
                if (indeg[e.to] == 0) {
                    q.add(e.to);
                    metrics.pushes++;
                }
            }
        }

        metrics.stop();
        return order;
    }
}
