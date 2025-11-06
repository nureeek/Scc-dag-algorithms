package graph.util;

public class Metrics {
    public long dfsVisits = 0;
    public long edgesProcessed = 0;
    public long relaxations = 0;
    public long pushes = 0;
    public long pops = 0;
    public long timeNs = 0;

    public void start() { timeNs = System.nanoTime(); }
    public void stop() { timeNs = System.nanoTime() - timeNs; }

    @Override
    public String toString() {
        return String.format("Time: %.3f ms, DFS=%d, edges=%d, relax=%d",
                timeNs / 1e6, dfsVisits, edgesProcessed, relaxations);
    }
}
