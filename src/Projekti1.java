import java.util.ArrayList;
import java.util.LinkedList;

class ArrayListTest {
    private ArrayList<Integer> arrayList;

    public ArrayListTest(int n) {
        arrayList = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            arrayList.add(i);
        }
    }

    public long removeAtIndex(int index) {
        long start = System.nanoTime();
        arrayList.remove(index);
        long end = System.nanoTime();
        return end - start;
    }

    public int getSize() {
        return arrayList.size();
    }
}

class LinkedListTest {
    private LinkedList<Integer> linkedList;

    public LinkedListTest(int n) {
        linkedList = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            linkedList.add(i);
        }
    }

    public long removeAtIndex(int index) {
        long start = System.nanoTime();
        linkedList.remove(index);
        long end = System.nanoTime();
        return end - start;
    }

    public int getSize() {
        return linkedList.size();
    }
}

public class Projekti1 {
    public static void main(String[] args) {
        int n = 1000;
        int index = n/2;

        ArrayListTest arrayTest = new ArrayListTest(n);
        long arrayTime = arrayTest.removeAtIndex(index);
        System.out.println("Koha ArrayList: " + arrayTime);

        LinkedListTest linkedTest = new LinkedListTest(n);
        long linkedTime = linkedTest.removeAtIndex(index);
        System.out.println("Koha LinkedList: " + linkedTime);
    }
}
