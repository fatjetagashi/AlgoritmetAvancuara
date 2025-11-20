public class MaxSumBST {

    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int v) { val = v; }
    }

    private int maxSum = 0;

    private static class Info {
        boolean isBST;
        int min, max, sum;

        Info(boolean isBST, int min, int max, int sum) {
            this.isBST = isBST;
            this.min = min;
            this.max = max;
            this.sum = sum;
        }
    }

    public int maxSumBST(TreeNode root) {
        dfs(root);
        return maxSum;
    }

    private Info dfs(TreeNode node) {
        if (node == null) {
            return new Info(true, Integer.MAX_VALUE, Integer.MIN_VALUE, 0);
        }

        Info left = dfs(node.left);
        Info right = dfs(node.right);

        if (!left.isBST || !right.isBST) {
            return new Info(false, 0, 0, 0);
        }

        if (node.val <= left.max || node.val >= right.min) {
            return new Info(false, 0, 0, 0);
        }

        int sum = left.sum + right.sum + node.val;
        int min = Math.min(left.min, node.val);
        int max = Math.max(right.max, node.val);

        maxSum = Math.max(maxSum, sum);

        return new Info(true, min, max, sum);
    }

    public static void main(String[] args) {
        MaxSumBST solver = new MaxSumBST();

        TreeNode root1 = new TreeNode(1);
        root1.left = new TreeNode(4);
        root1.right = new TreeNode(3);
        root1.left.left = new TreeNode(2);
        root1.left.right = new TreeNode(4);
        root1.right.left = new TreeNode(2);
        root1.right.right = new TreeNode(5);
        root1.right.right.left = new TreeNode(4);
        root1.right.right.right = new TreeNode(6);
        System.out.println("Example 1 → " + solver.maxSumBST(root1));

        solver = new MaxSumBST();
        TreeNode root2 = new TreeNode(4);
        root2.left = new TreeNode(3);
        root2.left.left = new TreeNode(1);
        root2.left.right = new TreeNode(2);
        System.out.println("Example 2 → " + solver.maxSumBST(root2));

        solver = new MaxSumBST();
        TreeNode root3 = new TreeNode(-4);
        root3.left = new TreeNode(-2);
        root3.right = new TreeNode(-5);
        System.out.println("Example 3 → " + solver.maxSumBST(root3));
    }
}
