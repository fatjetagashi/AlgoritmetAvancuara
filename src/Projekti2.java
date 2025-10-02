public class Projekti2 {
    public static int gcd(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    public static void main(String[] args) {
        int a = 48, b = 18;
        System.out.println("GCD me Euklid = " + gcd(a, b));
    }
}
