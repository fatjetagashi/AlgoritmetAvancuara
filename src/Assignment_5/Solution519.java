import java.util.*;

class Solution519 {
    private final int m, n;
    private int remaining;
    private final Random rand = new Random();
    private final Map<Integer, Integer> map = new HashMap<>();

    public Solution519(int m, int n) {
        this.m = m;
        this.n = n;
        this.remaining = m * n;
    }

    public int[] flip() {
        int r = rand.nextInt(remaining);
        int x = map.getOrDefault(r, r);
        remaining--;
        int last = map.getOrDefault(remaining, remaining);
        map.put(r, last);
        map.remove(remaining);
        return new int[] { x / n, x % n };
    }

    public void reset() {
        map.clear();
        remaining = m * n;
    }

    public static void main(String[] args) {
        System.out.println("Input");
        System.out.println("[\"Solution\", \"flip\", \"flip\", \"flip\", \"reset\", \"flip\"]");
        System.out.println("[[3, 1], [], [], [], [], []]");

        Solution519 solution = new Solution519(3, 1);

        List<String> output = new ArrayList<>();
        output.add("null");
        output.add(Arrays.toString(solution.flip()));
        output.add(Arrays.toString(solution.flip()));
        output.add(Arrays.toString(solution.flip()));
        solution.reset();
        output.add("null");
        output.add(Arrays.toString(solution.flip()));

        System.out.println("\nOutput");
        System.out.println(output);
    }
}
