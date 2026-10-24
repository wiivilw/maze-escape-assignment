import java.util.Arrays;
public class Zones {

    // Mission 4: Find zones using Kosaraju algorithm
    public static int[] kosaraju(Input.Slides s) {
        int n = s.n();

        int[][] graph = Graphs.slidesFrom(s);
        int[][] reverse = Graphs.slidesInto(s);

        boolean[] visited = new boolean[n];
        int[] finishOrder = new int[n];
        int finishCount = 0;

        int[] stack = new int[n];
        int[] nextEdge = new int[n];

        // First DFS: record cells when they finish
        for (int start = 0; start < n; start++) {
            if (visited[start]) {
                continue;
            }

            int top = 0;
            stack[top] = start;
            nextEdge[top] = 0;
            visited[start] = true;

            while (top >= 0) {
                int current = stack[top];

                if (nextEdge[top] < graph[current].length) {
                    int slide = graph[current][nextEdge[top]];
                    nextEdge[top]++;

                    int neighbour = s.to()[slide];
                    if (!visited[neighbour]) {
                        visited[neighbour] = true;
                        top++;
                        stack[top] = neighbour;
                        nextEdge[top] = 0;
                    }
                } else {
                    finishOrder[finishCount] = current;
                    finishCount++;
                    top--;
                }
            }
        }

        // Second DFS: use reversed edges to find each zone
        Arrays.fill(visited, false);
        int[] zoneOf = new int[n];
        Arrays.fill(zoneOf, -1);

        int zoneCount = 0;
        for (int i = finishCount - 1; i >= 0; i--) {
            int start = finishOrder[i];
            if (visited[start]) {
                continue;
            }

            int top = 0;
            stack[top] = start;
            visited[start] = true;
            zoneOf[start] = zoneCount;
            while (top >= 0) {
                int current = stack[top];
                top--;
                for (int slide : reverse[current]) {
                    int neighbour = s.from()[slide];
                    if (!visited[neighbour]) {
                        visited[neighbour] = true;
                        zoneOf[neighbour] = zoneCount;
                        top++;
                        stack[top] = neighbour;
                    }
                }
            }
            zoneCount++;
        }
        return zoneOf;
    }

    // Mission 5: Calculate flow stages and cheapest route
    public static long[] routes(
            Input.Slides s, int[] zoneOf, int zoneCount) {

        int n = s.n();

        int[] stages = new int[zoneCount];
        long[] cost = new long[zoneCount];

        Arrays.fill(cost, Long.MAX_VALUE);

        int startZone = zoneOf[0];
        int endZone = zoneOf[n - 1];

        stages[startZone] = 1;
        cost[startZone] = 0;

        // Process zones in topological order
        for (int z = 0; z < zoneCount; z++) {
            if (cost[z] == Long.MAX_VALUE) {
                continue;
            }
            for (int i = 0; i < s.from().length; i++) {
                int fromZone = zoneOf[s.from()[i]];
                int toZone = zoneOf[s.to()[i]];

                // Ignore slides inside the same zone
                if (fromZone != z || fromZone == toZone) {
                    continue;
                }

                // Find the longest chain of zones
                stages[toZone] = Math.max(
                        stages[toZone], stages[z] + 1);
                // Find the cheapest route
                long newCost = cost[z] + s.time()[i];
                if (newCost < cost[toZone]) {
                    cost[toZone] = newCost;
                }
            }
        }
        return new long[]{stages[endZone], cost[endZone]};
    }

}
