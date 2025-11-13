package Assignment_7;

import java.util.*;

public class OrgHierarchy {

    static class Employee {
        int id;
        String name;
        String position;
        int salary;
        Integer supervisorId;

        Employee(int id, String name, String position, int salary, Integer supervisorId) {
            this.id = id;
            this.name = name;
            this.position = position;
            this.salary = salary;
            this.supervisorId = supervisorId;
        }
    }

    public static int getEmployeeDepth(List<Employee> employees, int employeeId) {
        Map<Integer, Employee> byId = new HashMap<>();
        for (Employee e : employees) {
            byId.put(e.id, e);
        }
        Set<Integer> visiting = new HashSet<>();
        return getDepthRecursive(byId, employeeId, visiting);
    }

    private static int getDepthRecursive(Map<Integer, Employee> byId,
                                         int employeeId,
                                         Set<Integer> visiting) {
        Employee emp = byId.get(employeeId);
        if (emp == null) {
            return -1;
        }

        if (visiting.contains(employeeId)) {
            return -1;
        }

        visiting.add(employeeId);

        if (emp.supervisorId == null) {
            visiting.remove(employeeId);
            return 0;
        }

        int supervisorDepth = getDepthRecursive(byId, emp.supervisorId, visiting);
        visiting.remove(employeeId);

        if (supervisorDepth == -1) {
            return -1;
        }

        return supervisorDepth + 1;
    }

    public static void main(String[] args) {
        List<Employee> employees = new ArrayList<>();

        employees.add(new Employee(1, "Alice Johnson", "CEO", 250000, null));
        employees.add(new Employee(2, "Bob Smith", "CTO", 180000, 1));
        employees.add(new Employee(3, "Carol White", "CFO", 175000, 1));
        employees.add(new Employee(4, "David Brown", "Engineering Manager", 140000, 2));
        employees.add(new Employee(5, "Eve Davis", "QA Manager", 130000, 2));
        employees.add(new Employee(6, "Frank Wilson", "Senior Accountant", 95000, 3));
        employees.add(new Employee(7, "Grace Lee", "Senior Developer", 120000, 4));
        employees.add(new Employee(8, "Henry Martinez", "Junior Developer", 85000, 4));
        employees.add(new Employee(9, "Ivy Chen", "QA Engineer", 90000, 5));
        employees.add(new Employee(10, "Jack Thompson", "DevOps Engineer", 110000, 4));
        employees.add(new Employee(11, "Kelly Anderson", "Junior Accountant", 65000, 6));
        employees.add(new Employee(12, "Liam Garcia", "Intern Developer", 50000, 7));

        int[] testIds = {1, 2, 7, 12, 999};
        for (int id : testIds) {
            int depth = getEmployeeDepth(employees, id);
            System.out.println("getEmployeeDepth(employees, " + id + ") = " + depth);
        }
    }
}
