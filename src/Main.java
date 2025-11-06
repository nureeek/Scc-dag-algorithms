package graph;

import graph.util.GraphLoader;
import graph.scc.SCC;
import graph.topo.TopologicalSort;
import graph.dagsp.DAGShortestPath;

import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        File dataDir = new File("data");
        File[] files = dataDir.listFiles((dir, name) -> name.endsWith(".json"));

        if (files == null || files.length == 0) {
            System.err.println("No JSON files found in /data");
            return;
        }

        File outFile = new File("results.csv");

        try (PrintWriter writer = new PrintWriter(new FileWriter(outFile))) {
            writer.println("dataset,nodes,edges,scc_count,time_scc_ms,time_topo_ms,time_dag_ms");

            System.out.println("=== Running analysis for all datasets ===");
            System.out.println("Found " + files.length + " dataset(s)\n");

            for (File file : files) {
                System.out.println("============================================");
                System.out.println("Processing file: " + file.getName());
                System.out.println("============================================");

                try {
                    GraphLoader.Graph g = GraphLoader.loadGraph(file.getPath());
                    System.out.println("Nodes: " + g.n + ", Edges: " + g.edges.size());

                    SCC scc = new SCC(g);
                    var components = scc.run();

                    GraphLoader.Graph dag = scc.buildCondensationGraph();
                    TopologicalSort topo = new TopologicalSort(dag);
                    List<Integer> order = topo.run();

                    DAGShortestPath dagsp = new DAGShortestPath(dag);
                    int source = 0;
                    dagsp.shortestPaths(source, order);
                    dagsp.longestPaths(source, order);

                    double sccTime = scc.getMetrics().timeNs / 1e6;
                    double topoTime = topo.getMetrics().timeNs / 1e6;
                    double dagTime = dagsp.getMetrics().timeNs / 1e6;

                    writer.printf("%s,%d,%d,%d,%.3f,%.3f,%.3f%n",
                            file.getName(), g.n, g.edges.size(),
                            components.size(), sccTime, topoTime, dagTime);

                    System.out.printf("→ Done: %s (SCC=%d, total time≈%.3f ms)%n%n",
                            file.getName(), components.size(), sccTime + topoTime + dagTime);

                } catch (IOException e) {
                    System.err.println("Error loading graph: " + file.getName());
                }
            }

            System.out.println("\n✅ Results saved to: " + outFile.getAbsolutePath());

        } catch (IOException e) {
            System.err.println("Error writing results.csv: " + e.getMessage());
        }
    }
}
