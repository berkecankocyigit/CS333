import java.util.Scanner;

public class Berkecan_Kocyigit_S021062 {

    public static void main(String[] args) {
        // read the number of outlets from the user
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();

        // read the outlet codes from the user
        String[] outlets = new String[n];
        for (int i = 0; i < n; i++) {
            outlets[i] = scanner.next();
        }

        // read the lamp codes from the user
        String[] lamps = new String[n];
        for (int i = 0; i < n; i++) {
            lamps[i] = scanner.next();
        }

        // create the DP array and initialize it with zeros
        int[][] DP = new int[n+1][n+1];
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= n; j++) {
                DP[i][j] = 0;
            }
        }

        // loop through the outlets and lamps and update the DP array
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                // if the codes match, update the DP array
                if (outlets[i-1].equals(lamps[j-1])) {
                    DP[i][j] = DP[i-1][j-1] + 1;
                }
                // if the codes do not match, set the value in the DP array to the maximum value of the previous elements in the DP array
                else {
                    DP[i][j] = Math.max(DP[i-1][j], DP[i][j-1]);
                }
            }
        }

        // the maximum number of lamps that can be turned on is the length of the longest common subsequence of the outlet and lamp codes
        int maxLamps = DP[n][n];
        System.out.println( maxLamps);

        // to find the sockets that can be connected, we can trace back from the element with the maximum value in the DP array
        //System.out.print("The sockets that can be connected are: ");

        // initialize the current row and column indexes to the last row and column in the DP array
        int i = n;
        int j = n;

        // loop until the current row or column index is zero
        while (i > 0 && j > 0) {
            // if the codes match, print the current socket/lamp pair and move to the previous row and column in the DP array
            if (outlets[i-1].equals(lamps[j-1])) {
                System.out.print( lamps[j-1] +" ");
                i--;
                j--;
            }
            // if the codes do not match, move to the previous row or column in the DP array depending on the value in the current element in the DP array
            else {
                if (DP[i-1][j] > DP[i][j-1]) {
                    i--;
                } else {
                    j--;
                }
            }
        }

        System.out.println();
    }
}