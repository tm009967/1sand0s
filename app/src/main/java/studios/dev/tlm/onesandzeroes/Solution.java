package studios.dev.tlm.onesandzeroes;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by tm009967 on 7/28/2014.
 */

public class Solution {
    public int[][] array;
    private Random generator;
    public static int LENGTH_OF_ROW = 4;// how many numbers in a row
    private int valueCap = 2;// random numbers < valueCap

    public Solution(int numberOfRows){
        /** numberOfRows is 1-4, the number of active rows */
        generator = new Random();
        generateSolution(numberOfRows);
    }

    public void generateSolution(int numberOfRows) {
        array = new int[numberOfRows][LENGTH_OF_ROW];
        for (int i=0; i < numberOfRows; i++) {
            int[] row = new int[LENGTH_OF_ROW];
            for (int j = 0; j < LENGTH_OF_ROW; j++) {
                row[j] = generator.nextInt(valueCap);
            }
            array[i] = row;
        }
    }

    public int getValueCap() {
        return valueCap;
    }

    /** TESTING */
    public static void main(String[] args) {
        Solution s1 = new Solution(1);
        Solution s2 = new Solution(2);
        Solution s3 = new Solution(3);
        Solution s4 = new Solution(4);

        System.out.print(Arrays.deepToString(s1.array) + "\n");
        System.out.print(Arrays.deepToString(s2.array) + "\n");
        System.out.print(Arrays.deepToString(s3.array) + "\n");
        System.out.print(Arrays.deepToString(s4.array) + "\n");
    }
}
