import java.util.*;

public class KSmallestPairs {

    public static List<List<Integer>> kSmallestPairs(int[] nums1, int[] nums2, int k) {
        List<List<Integer>> result = new ArrayList<>();
        int n1 = nums1.length;
        int n2 = nums2.length;

        if (n1 == 0 || n2 == 0 || k <= 0) return result;

        PriorityQueue<int[]> pq = new PriorityQueue<>(
                (a, b) -> Integer.compare(nums1[a[0]] + nums2[a[1]], nums1[b[0]] + nums2[b[1]])
        );

        int limit = Math.min(k, n1);
        for (int i = 0; i < limit; i++) {
            pq.offer(new int[]{i, 0});
        }

        while (k > 0 && !pq.isEmpty()) {
            int[] cur = pq.poll();
            int i = cur[0];
            int j = cur[1];
            result.add(Arrays.asList(nums1[i], nums2[j]));
            k--;
            if (j + 1 < n2) pq.offer(new int[]{i, j + 1});
        }

        return result;
    }

    public static void main(String[] args) {
        int[] nums1a = {1, 7, 11};
        int[] nums2a = {2, 4, 6};
        int k1 = 3;
        System.out.println("Example 1: " + kSmallestPairs(nums1a, nums2a, k1));

        int[] nums1b = {1, 1, 2};
        int[] nums2b = {1, 2, 3};
        int k2 = 2;
        System.out.println("Example 2: " + kSmallestPairs(nums1b, nums2b, k2));
    }
}
