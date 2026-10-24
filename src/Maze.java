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
        return java.util.Arrays.copyOf(broken, count);
    }
    // Mission 2: Kruskal algorithm
    public static int[] cheapestMaze(Input.Walls w) {
        // TODO: implement Kruskal
        return new int[0];
    }
    // Mission 2: Prim algorithm
    public static long primCost(Input.Walls w) {
        // TODO: implement Prim
        return 0;
    }
}
