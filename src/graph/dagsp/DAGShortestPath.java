package graph.dagsp;

import graph.util.GraphLoader;
import graph.util.Metrics;

import java.util.*;

public class DAGShortestPath {

    private GraphLoader.Graph dag;
    private Metrics metrics;

    public DAGShortestPath(GraphLoader.Graph dag) {
        this.dag = dag;
        this.metrics = new Metrics();
    }

    public Metrics getMetrics() { return metrics; }

    public double[] shortestPaths(int source, List<Integer> topoOrder) {
        metrics.start();

        int n = dag.n;
        double[] dist = new double[n];
        Arrays.fill(dist, Double.POSITIVE_INFINITY);
        dist[source] = 0.0;

        for (int u : topoOrder) {
            if (dist[u] != Double.POSITIVE_INFINITY) {
                for (GraphLoader.Edge e : dag.adj.get(u)) {
                    double nd = dist[u] + e.weight;
                    if (nd < dist[e.to]) {
                        dist[e.to] = nd;
                        metrics.relaxations++;
                    }
                }
            }
        }

        metrics.stop();
        return dist;
    }


    public double[] longestPaths(int source, List<Integer> topoOrder) {
        metrics.start();

        int n = dag.n;
        double[] dist = new double[n];
        Arrays.fill(dist, Double.NEGATIVE_INFINITY);
        dist[source] = 0.0;

        for (int u : topoOrder) {
            if (dist[u] != Double.NEGATIVE_INFINITY) {
                for (GraphLoader.Edge e : dag.adj.get(u)) {
                    double nd = dist[u] + e.weight;
                    if (nd > dist[e.to]) {
                        dist[e.to] = nd;
                        metrics.relaxations++;
                    }
                }
            }
        }

        metrics.stop();
        return dist;
    }


    public List<Integer> reconstructPath(int source, int target, double[] dist) {
        List<Integer> path = new ArrayList<>();
        path.add(source);
        path.add(target);
        return path;
    }
}
