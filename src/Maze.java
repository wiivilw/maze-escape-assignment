import java.util.Arrays;
public class Maze {
    // Mission 1: Build the maze in the order of the file
    public static int[] breakWalls(Input.Walls w) {
        DisjointSet dsu = new DisjointSet(w.cells());
        int[] broken = new int[w.cells() - 1];
        int count = 0;

        // Check walls in the same order as the file
        for (int i = 0; i < w.count(); i++) {
            int a = w.a()[i];
            int b = w.b()[i];

            // Break the wall if the cells are in different groups
            if (dsu.union(a, b)) {
                broken[count] = i;
                count++;
                if (count == w.cells() - 1) {
                    break;
                }
            }
        }
        return Arrays.copyOf(broken, count);
    }
    // Mission 2: Kruskal algorithm
    public static int[] cheapestMaze(Input.Walls w) {
        Integer[] order = new Integer[w.count()];

        // Sort walls by cost
        for (int i = 0; i < w.count(); i++) {
            order[i] = i;
        }
        Arrays.sort(order,
                (i, j) -> Integer.compare(w.cost()[i], w.cost()[j]));
        DisjointSet dsu = new DisjointSet(w.cells());
        int[] broken = new int[w.cells() - 1];
        int count = 0;

        // Take the cheapest walls first
        for (int i : order) {
            if (dsu.union(w.a()[i], w.b()[i])) {
                broken[count] = i;
                count++;
                if (count == w.cells() - 1) {
                    break;
                }
            }
        }
        return Arrays.copyOf(broken, count);
    }

    // Mission 2: Prim algorithm
    public static long primCost(Input.Walls w) {
        int n = w.cells();
        boolean[] visited = new boolean[n];
        int[] minCost = new int[n];

        Arrays.fill(minCost, Integer.MAX_VALUE);
        minCost[0] = 0;

        long totalCost = 0;
        for (int i = 0; i < n; i++) {
            int current = -1;
            // Find the cheapest unvisited cell
            for (int j = 0; j < n; j++) {
                if (!visited[j] &&
                        (current == -1 || minCost[j] < minCost[current])) {
                    current = j;
                }
            }
            if (current == -1 || minCost[current] == Integer.MAX_VALUE) {
                return -1;
            }
            visited[current] = true;
            totalCost += minCost[current];

            // Update costs of neighboring cells
            for (int j = 0; j < w.count(); j++) {
                int a = w.a()[j];
                int b = w.b()[j];
                int cost = w.cost()[j];
                if (a == current && !visited[b] && cost < minCost[b]) {
                    minCost[b] = cost;
                } else if (b == current && !visited[a] && cost < minCost[a]) {
                    minCost[a] = cost;
                }
            }
        }
        return totalCost;
    }

}
