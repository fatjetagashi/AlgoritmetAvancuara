import java.util.*;

public class MaximizeROI_Simple {

    static class Project {
        String name;
        int cost;
        int roi;
        Project(String name, int cost, int roi) {
            this.name = name; this.cost = cost; this.roi = roi;
        }
    }

    static class Result {
        int maxROI;
        List<String> selectedProjects;
        Result(int maxROI, List<String> selectedProjects) {
            this.maxROI = maxROI; this.selectedProjects = selectedProjects;
        }
        public String toString() { return "{ maxROI: " + maxROI + ", selectedProjects: " + selectedProjects + " }"; }
    }

    public static Result maximizeROI(int budget, List<Project> projects) {
        int n = projects.size();
        int[][] dp = new int[n + 1][budget + 1];

        for (int i = 1; i <= n; i++) {
            Project p = projects.get(i - 1);
            for (int b = 0; b <= budget; b++) {
                dp[i][b] = dp[i - 1][b];
                if (p.cost <= b) {
                    int newROI = dp[i - 1][b - p.cost] + p.roi;
                    if (newROI > dp[i][b]) dp[i][b] = newROI;
                }
            }
        }

        List<String> picked = new ArrayList<>();
        int b = budget;
        for (int i = n; i > 0; i--) {
            if (dp[i][b] != dp[i - 1][b]) {
                Project p = projects.get(i - 1);
                picked.add(p.name);
                b -= p.cost;
            }
        }
        Collections.reverse(picked);

        return new Result(dp[n][budget], picked);
    }


    public static void main(String[] args) {
        int budget1 = 10000;
        List<Project> projects1 = Arrays.asList(
                new Project("AI Chatbot", 5000, 8000),
                new Project("Mobile App", 4000, 6000),
                new Project("Website Redesign", 3000, 4000),
                new Project("Cloud Migration", 6000, 9000)
        );
        System.out.println(maximizeROI(budget1, projects1));

        int budget2 = 15000;
        List<Project> projects2 = Arrays.asList(
                new Project("CRM System", 8000, 12000),
                new Project("Analytics Tool", 5000, 7000),
                new Project("Security Upgrade", 7000, 10000),
                new Project("API Development", 4000, 5000)
        );
        System.out.println(maximizeROI(budget2, projects2));
    }
}
