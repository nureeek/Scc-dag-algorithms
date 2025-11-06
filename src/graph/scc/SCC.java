package graph.scc;

import graph.util.GraphLoader;
import graph.util.Metrics;

import java.util.*;
public class SCC {

    private GraphLoader.Graph g;
    private int time;
    private int[] disc, low, comp;
    private boolean[] inStack;
    private Deque<Integer> stack;
    private List<List<Integer>> components;
    private Metrics metrics;

    public SCC(GraphLoader.Graph g) {
        this.g = g;
        int n = g.n;
        disc = new int[n];
        low = new int[n];
        inStack = new boolean[n];
        comp = new int[n];
        Arrays.fill(disc, -1);
        stack = new ArrayDeque<>();
        components = new ArrayList<>();
        metrics = new Metrics();
    }

    public Metrics getMetrics() { return metrics; }

    public List<List<Integer>> run() {
        metrics.start();
        for (int i = 0; i < g.n; i++) {
            if (disc[i] == -1) dfs(i);
        }
        metrics.stop();
        return components;
    }

    private void dfs(int u) {
        metrics.dfsVisits++;
        disc[u] = low[u] = ++time;
        stack.push(u);
        inStack[u] = true;

        for (GraphLoader.Edge e : g.adj.get(u)) {
            int v = e.to;
            metrics.edgesProcessed++;
            if (disc[v] == -1) {
                dfs(v);
                low[u] = Math.min(low[u], low[v]);
            } else if (inStack[v]) {
                low[u] = Math.min(low[u], disc[v]);
            }
        }

        // Если u — корень SCC
        if (low[u] == disc[u]) {
            List<Integer> scc = new ArrayList<>();
            int v;
            do {
                v = stack.pop();
                inStack[v] = false;
                comp[v] = components.size();
                scc.add(v);
            } while (v != u);
            components.add(scc);
        }
    }

    public List<List<Integer>> getComponents() {
        return components;
    }

    public GraphLoader.Graph buildCondensationGraph() {
        int k = components.size();
        GraphLoader.Graph dag = new GraphLoader.Graph(k, true);
        Set<String> added = new HashSet<>();

        for (GraphLoader.Edge e : g.edges) {
            int a = comp[e.from];
            int b = comp[e.to];
            if (a != b) {
                String key = a + "->" + b;
                if (!added.contains(key)) {
                    dag.addEdge(a, b, e.weight);
                    added.add(key);
                }
            }
        }
        return dag;
    }
}
