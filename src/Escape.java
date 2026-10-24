import java.util.Arrays;
public class Escape {

    public record Result(int[] path, int visited, int deadEnds) {}
    public static Result escape(Input.Walls w, int[] broken) {
        int n = w.cells();
        int cols = w.cols();
        int[][] graph = Graphs.open(w, broken);

        // Count cells with exactly one open neighbour
        int deadEnds = 0;
        for (int i = 0; i < n; i++) {
            if (graph[i].length == 1) {
                deadEnds++;
            }
        }

        boolean[] visited = new boolean[n];
        int[] parent = new int[n];
        Arrays.fill(parent, -1);

        // Store the current DFS path and the next neighbour to try
        int[] stack = new int[n];
        int[] nextNeighbour = new int[n];
        int top = 0;
        int visitedCount = 1;

        stack[top] = 0;
        visited[0] = true;

        int exit = n - 1;
        while (top >= 0) {
            int current = stack[top];
            // Stop when the exit is found
            if (current == exit) {
                break;
            }
            int[] directions = {
                    current - cols,
                    current + 1,
                    current + cols,
                    current - 1
            };
            boolean moved = false;
            // Try neighbours in the order: up, right, down, left
            while (nextNeighbour[top] < 4) {
                int neighbour = directions[nextNeighbour[top]];
                nextNeighbour[top]++;

                // Skip cells outside the maze
                if (neighbour < 0 || neighbour >= n) {
                    continue;
                }
                // Prevent moving across the edge between rows
                if (Math.abs(current / cols - neighbour / cols) > 1) {
                    continue;
                }
                if (Math.abs(current % cols - neighbour % cols) > 1) {
                    continue;
                }
                // Check whether this is an open neighbour
                boolean open = false;
                for (int cell : graph[current]) {
                    if (cell == neighbour) {
                        open = true;
                        break;
                    }
                }

                if (open && !visited[neighbour]) {
                    visited[neighbour] = true;
                    visitedCount++;
                    parent[neighbour] = current;

                    top++;
                    stack[top] = neighbour;
                    nextNeighbour[top] = 0;
                    moved = true;
                    break;
                }
            }

            // Backtrack when there are no more neighbours to try
            if (!moved && nextNeighbour[top] == 4) {
                top--;
            }
        }        // Rebuild the path from the exit to the start
        if (!visited[exit]) {
            return new Result(new int[0], visitedCount, deadEnds);
        }
        int length = 0;
        for (int cell = exit; cell != -1; cell = parent[cell]) {
            length++;
        }
        int[] path = new int[length];
        int cell = exit;
        for (int i = length - 1; i >= 0; i--) {
            path[i] = cell;
            cell = parent[cell];
        }
        return new Result(path, visitedCount, deadEnds);
    }
}
