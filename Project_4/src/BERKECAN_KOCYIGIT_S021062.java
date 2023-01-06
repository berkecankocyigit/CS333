import java.util.Scanner;

public class BERKECAN_KOCYIGIT_S021062 {
    static int n;
    static int[] weights;
    static int[] profits;
    static int b;
    static int m;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        n = scanner.nextInt();
        weights = new int[n];
        profits = new int[n];
        for (int i = 0; i < n; i++) {
            weights[i] = scanner.nextInt();
        }
        for (int i = 0; i < n; i++) {
            profits[i] = scanner.nextInt();
        }
        b = scanner.nextInt();
        m = scanner.nextInt();
        boolean found = false;
        for (int subset = 0; subset < (1 << n); subset++) {
            int w = 0;
            int p = 0;
            for (int i = 0; i < n; i++) {
                if ((subset & (1 << i)) != 0) {
                    w += weights[i];
                    p += profits[i];
                }
            }
            if (w <= b && p >= m) {
                found = true;
                break;
            }
        }
        if (found) {
            System.out.println("YES");
        } else {
            System.out.println("NO");
        }
    }
}
