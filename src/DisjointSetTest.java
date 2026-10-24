/**
 * Checks of Mission 0. Run:  java -cp out DisjointSetTest
 * Add your own checks below: a large chain, a union of a set with itself, count after many unions.
 */
public class DisjointSetTest {

    static int failed = 0;

    static void check(boolean ok, String what) {
        System.out.println((ok ? "ok    " : "FAIL  ") + what);
        if (!ok) failed++;
    }

    public static void main(String[] args) {
        DisjointSet d = new DisjointSet(6);
        check(d.count() == 6, "6 sets at the start");
        check(!d.connected(0, 1), "0 and 1 are apart at the start");
        check(d.union(0, 1), "union(0, 1) joins two sets");
        check(d.connected(0, 1), "0 and 1 are together");
        check(!d.union(1, 0), "union(1, 0) again returns false");
        check(d.count() == 5, "5 sets after one union");
        d.union(2, 3); d.union(1, 3);
        check(d.connected(0, 2), "0 and 2 are together through 1 and 3");
        check(d.count() == 3, "3 sets: {0,1,2,3} {4} {5}");
        check(d.find(4) == 4 && d.find(5) == 5, "a single cell is its own root");

        // Union of an element with itself
        check(!d.union(2, 2), "union of a cell with itself returns false");
        check(d.count() == 3, "count does not change after self-union");

        // Count after many unions
        DisjointSet many = new DisjointSet(10);
        many.union(0, 1);
        many.union(2, 3);
        many.union(1, 2);
        many.union(4, 5);
        many.union(5, 6);

        check(many.count() == 5, "count after many unions");
        check(many.connected(0, 3), "elements in the same group are connected");
        check(!many.connected(0, 6), "elements in different groups are not connected");

        // A large chain
        int n = 10000;
        DisjointSet chain = new DisjointSet(n);

        for (int i = 0; i < n - 1; i++) {
            chain.union(i, i + 1);
        }

        check(chain.count() == 1, "large chain has one set");
        check(chain.connected(0, n - 1), "first and last elements are connected");

        System.out.println(failed == 0 ? "All checks passed." : failed + " checks failed.");
    }
}
