public class DisjointSet {
    private int[] parent;
    private int[] size;
    private int count;

    public DisjointSet(int n) {
        parent = new int[n];
        size = new int[n];
        count = n;
        // At first, every element is its own group
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            size[i] = 1;
        }
    }

    public int find(int x) {
        // Move closer to the root to make future searches faster
        if (parent[x] != x) {
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }

    public boolean union(int a, int b) {
        int rootA = find(a);
        int rootB = find(b);
        // They are already in the same group
        if (rootA == rootB) {
            return false;
        }
        // Put the smaller group under the bigger one
        if (size[rootA] < size[rootB]) {
            int temp = rootA;
            rootA = rootB;
            rootB = temp;
        }

        parent[rootB] = rootA;
        size[rootA] += size[rootB];
        count--;
        return true;
    }

    public boolean connected(int a, int b) {
        return find(a) == find(b);
    }

    public int count() {
        return count;
    }
}
