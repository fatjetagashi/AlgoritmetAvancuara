package Assignment_9;
import java.util.*;

public class NPProblemKnapsack {

    static class Item {
        String name;
        int weight;
        int value;

        Item(String name, int weight, int value) {
            this.name = name;
            this.weight = weight;
            this.value = value;
        }
    }

    public static void main(String[] args) {
        Item[] items = {
                new Item("Laptop", 3, 6),
                new Item("Tablet", 4, 7),
                new Item("DSLR_Camera", 7, 12),
                new Item("NoiseCancelHeadphones", 4, 8),
                new Item("Smartwatch", 2, 5),
                new Item("PortableSSD", 3, 4),
                new Item("PowerBank", 2, 6),
                new Item("WirelessMouse", 1, 3),
                new Item("USBHub", 1, 2),
                new Item("TravelAdapter", 2, 3)
        };


        int n = items.length;
        int W = 25;

        int[][] dp = new int[n + 1][W + 1];

        for (int i = 1; i <= n; i++) {
            int w_i = items[i - 1].weight;
            int v_i = items[i - 1].value;
            for (int w = 0; w <= W; w++) {
                dp[i][w] = dp[i - 1][w];
                if (w_i <= w) {
                    int candidate = dp[i - 1][w - w_i] + v_i;
                    if (candidate > dp[i][w]) {
                        dp[i][w] = candidate;
                    }
                }
            }
        }

        int maxValue = dp[n][W];
        System.out.println("Maximum value = " + maxValue);

        List<Item> chosen = new ArrayList<>();
        int w = W;
        for (int i = n; i >= 1; i--) {
            if (dp[i][w] != dp[i - 1][w]) {
                chosen.add(items[i - 1]);
                w -= items[i - 1].weight;
            }
        }

        Collections.reverse(chosen);

        System.out.println("Chosen items:");
        int totalWeight = 0;
        int totalValue = 0;
        for (Item it : chosen) {
            System.out.println(it.name + " (weight=" + it.weight + ", value=" + it.value + ")");
            totalWeight += it.weight;
            totalValue += it.value;
        }
        System.out.println("Total weight = " + totalWeight);
        System.out.println("Total value = " + totalValue);
    }
}
