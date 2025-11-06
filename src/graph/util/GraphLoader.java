package graph.util;

import com.google.gson.*;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class GraphLoader {

    public static class Edge {
        public int from;
        public int to;
        public int weight;
        public Edge(int f, int t, int w) {
            this.from = f;
            this.to = t;
            this.weight = w;
        }
    }

    public static class Graph {
        public int n;
        public boolean directed;
        public List<Edge> edges;
        public Map<Integer, List<Edge>> adj;

        public Graph(int n, boolean directed) {
            this.n = n;
            this.directed = directed;
            this.edges = new ArrayList<>();
            this.adj = new HashMap<>();
            for (int i = 0; i < n; i++) adj.put(i, new ArrayList<>());
        }

        public void addEdge(int u, int v, int w) {
            edges.add(new Edge(u, v, w));
            adj.get(u).add(new Edge(u, v, w));
            if (!directed) adj.get(v).add(new Edge(v, u, w));
        }
    }

    public static Graph loadGraph(String path) throws IOException {
        JsonObject obj = JsonParser.parseReader(new FileReader(path)).getAsJsonObject();
        boolean directed = obj.get("directed").getAsBoolean();
        int n = obj.get("n").getAsInt();
        Graph g = new Graph(n, directed);

        JsonArray arr = obj.get("edges").getAsJsonArray();
        for (JsonElement e : arr) {
            JsonObject o = e.getAsJsonObject();
            g.addEdge(o.get("u").getAsInt(), o.get("v").getAsInt(), o.get("w").getAsInt());
        }
        return g;
    }
}
