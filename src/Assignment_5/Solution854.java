import java.util.*;

public class Solution854 {
    private Map<String, Integer> memo = new HashMap<>();

    public int kSimilarity(String s1, String s2) {
        if (s1.equals(s2)) return 0;
        if (memo.containsKey(s1)) return memo.get(s1);

        int i = 0;
        while (i < s1.length() && s1.charAt(i) == s2.charAt(i)) i++;

        int min = Integer.MAX_VALUE;

        for (int j = i + 1; j < s1.length(); j++) {
            if (s1.charAt(j) == s2.charAt(i) && s1.charAt(j) != s2.charAt(j)) {
                String next = swap(s1, i, j);
                min = Math.min(min, 1 + kSimilarity(next, s2));
            }
        }

        if (min == Integer.MAX_VALUE) {
            for (int j = i + 1; j < s1.length(); j++) {
                if (s1.charAt(j) == s2.charAt(i)) {
                    String next = swap(s1, i, j);
                    min = Math.min(min, 1 + kSimilarity(next, s2));
                }
            }
        }

        memo.put(s1, min);
        return min;
    }

    private String swap(String s, int i, int j) {
        char[] arr = s.toCharArray();
        char temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
        return new String(arr);
    }

    public static void main(String[] args) {
        Solution854 sol = new Solution854();

        String s1a = "ab", s2a = "ba";
        String s1b = "abc", s2b = "bca";
        String s1c = "aabbccd", s2c = "abcabcd";

        System.out.println("Example 1:");
        System.out.println("Input: s1 = \"" + s1a + "\", s2 = \"" + s2a + "\"");
        System.out.println("Output: " + sol.kSimilarity(s1a, s2a));
        System.out.println();

        System.out.println("Example 2:");
        System.out.println("Input: s1 = \"" + s1b + "\", s2 = \"" + s2b + "\"");
        System.out.println("Output: " + sol.kSimilarity(s1b, s2b));
        System.out.println();

        System.out.println("Example 3:");
        System.out.println("Input: s1 = \"" + s1c + "\", s2 = \"" + s2c + "\"");
        System.out.println("Output: " + sol.kSimilarity(s1c, s2c));
    }
}
