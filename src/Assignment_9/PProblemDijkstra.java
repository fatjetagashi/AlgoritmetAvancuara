package Assignment_9;
import java.util.*;

public class PProblemDijkstra {

    private static final int INF = 1_000_000;

    public static void main(String[] args) {
        int n = 10;
        int[][] graph = new int[n][n];

        for (int i = 0; i < n; i++) {
            Arrays.fill(graph[i], INF);
            graph[i][i] = 0;
        }

        addEdge(graph, 0, 1, 4);
        addEdge(graph, 0, 2, 2);
        addEdge(graph, 1, 2, 1);
        addEdge(graph, 1, 3, 5);
        addEdge(graph, 2, 3, 8);
        addEdge(graph, 2, 4, 10);
        addEdge(graph, 3, 4, 2);
        addEdge(graph, 3, 5, 6);
        addEdge(graph, 4, 5, 3);
        addEdge(graph, 4, 6, 1);
        addEdge(graph, 5, 7, 1);
        addEdge(graph, 6, 7, 4);
        addEdge(graph, 6, 8, 2);
        addEdge(graph, 7, 8, 1);
        addEdge(graph, 7, 9, 7);
        addEdge(graph, 8, 9, 3);

        int source = 0;

        int[] dist = new int[n];
        int[] prev = new int[n];
        boolean[] visited = new boolean[n];

        Arrays.fill(dist, INF);
        Arrays.fill(prev, -1);
        dist[source] = 0;

        for (int i = 0; i < n; i++) {
            int u = -1;
            int best = INF;
            for (int v = 0; v < n; v++) {
                if (!visited[v] && dist[v] < best) {
                    best = dist[v];
                    u = v;
                }
            }
            if (u == -1) break;
            visited[u] = true;

            for (int v = 0; v < n; v++) {
                if (!visited[v] && graph[u][v] < INF) {
                    int nd = dist[u] + graph[u][v];
                    if (nd < dist[v]) {
                        dist[v] = nd;
                        prev[v] = u;
                    }
                }
            }
        }

        System.out.println("Shortest distances from A:");
        for (int i = 0; i < n; i++) {
            System.out.println(nodeName(source) + " -> " + nodeName(i) + " = " + dist[i]);
        }

        int target = 9;
        List<Integer> path = buildPath(prev, target);
        System.out.print("Shortest path from " + nodeName(source) + " to " + nodeName(target) + ": ");
        for (int i = 0; i < path.size(); i++) {
            System.out.print(nodeName(path.get(i)));
            if (i < path.size() - 1) System.out.print(" -> ");
        }
        System.out.println();
        System.out.println("Total distance = " + dist[target]);
    }

    private static void addEdge(int[][] graph, int u, int v, int w) {
        graph[u][v] = w;
        graph[v][u] = w;
    }

    private static List<Integer> buildPath(int[] prev, int target) {
        List<Integer> path = new ArrayList<>();
        int cur = target;
        while (cur != -1) {
            path.add(cur);
            cur = prev[cur];
        }
        Collections.reverse(path);
        return path;
    }

    private static String nodeName(int i) {
        return String.valueOf((char) ('A' + i));
    }
}
