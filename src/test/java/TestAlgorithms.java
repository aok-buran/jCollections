import center.buran.jcollections.Algorithms;
import org.junit.Test;

import java.util.Arrays;

public class TestAlgorithms {

    @Test
    public void testConcatenateDoubleArr() {
        double[] a = new double[]{1, 2};
        assert Arrays.equals(Algorithms.concatenate(a), new double[]{1, 2});
        double[] b = new double[]{3, 4, 5};
        assert Arrays.equals(Algorithms.concatenate(a, b), new double[]{1, 2, 3, 4, 5});
        double[] c = new double[]{6, 7};
        assert Arrays.equals(Algorithms.concatenate(a, b, c), new double[]{1, 2, 3, 4, 5, 6, 7});
        double[] d = new double[]{8, 9, 10, 11};
        assert Arrays.equals(Algorithms.concatenate(a, b, c, d), new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11});
    }

    @Test
    public void testConcatenateDoubleArr2() {
        double[][] a = new double[][]{{1}, {2}};
        double[][] b = new double[][]{{3}, {4}, {5}};
        assert Algorithms.isEqual(Algorithms.concatenate(a, b), new double[][]{{1}, {2}, {3}, {4}, {5}});
        double[][] c = new double[][]{{6}, {7}};
        assert Algorithms.isEqual(Algorithms.concatenate(a, b, c), new double[][]{{1}, {2}, {3}, {4}, {5}, {6}, {7}});
        double[][] d = new double[][]{{8}, {9}, {10}, {11}};
        assert Algorithms.isEqual(
                Algorithms.concatenate(a, b, c, d), new double[][]{{1}, {2}, {3}, {4}, {5}, {6}, {7}, {8}, {9}, {10}, {11}}
        );
    }

    @Test
    public void testConcatenateStringArr() {
        String[] a = new String[]{"1", "2"};
        assert Arrays.equals(Algorithms.concatenate(a), new String[]{"1", "2"});
        String[] b = new String[]{"3", "4", "5"};
        assert Arrays.equals(Algorithms.concatenate(a, b), new String[]{"1", "2", "3", "4", "5"});
        String[] c = new String[]{"6", "7"};
        assert Arrays.equals(Algorithms.concatenate(a, b, c), new String[]{"1", "2", "3", "4", "5", "6", "7"});
        String[] d = new String[]{"8", "9", "10", "11"};
        assert Arrays.equals(
                Algorithms.concatenate(a, b, c, d),
                new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11"}
        );
    }

    @Test
    public void testConcatenateStringArr2() {
        String[][] a = new String[][]{{"1"}, {"2"}};
        String[][] b = new String[][]{{"3"}, {"4"}, {"5"}};
        assert Algorithms.isEqual(Algorithms.concatenate(a, b), new String[][]{{"1"}, {"2"}, {"3"}, {"4"}, {"5"}});
        String[][] c = new String[][]{{"6"}, {"7"}};
        assert Algorithms.isEqual(
                Algorithms.concatenate(a, b, c), new String[][]{{"1"}, {"2"}, {"3"}, {"4"}, {"5"}, {"6"}, {"7"}}
        );
        String[][] d = new String[][]{{"8"}, {"9"}, {"10"}, {"11"}};
        assert Algorithms.isEqual(
                Algorithms.concatenate(a, b, c, d),
                new String[][]{{"1"}, {"2"}, {"3"}, {"4"}, {"5"}, {"6"}, {"7"}, {"8"}, {"9"}, {"10"}, {"11"}}
        );
    }

    @Test
    public void testFindCumulativeValue() {
        float[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        assert Algorithms.findCumulativeValue(arr, 0) == 0;
        assert Algorithms.findCumulativeValue(arr, 1) == 0;
        assert Algorithms.findCumulativeValue(arr, 5) == 4;
        assert Algorithms.findCumulativeValue(arr, 12) == 9;

        float[] arr2 = {5, 10, 15, 20, 25};
        assert Algorithms.findCumulativeValue(arr2, 0) == 0;
        assert Algorithms.findCumulativeValue(arr2, 3) == 0;
        assert Algorithms.findCumulativeValue(arr2, 5) == 0;
        assert Algorithms.findCumulativeValue(arr2, 6) == 1;
        assert Algorithms.findCumulativeValue(arr2, 11) == 2;
        assert Algorithms.findCumulativeValue(arr2, 20) == 3;
        assert Algorithms.findCumulativeValue(arr2, 24) == 4;
        assert Algorithms.findCumulativeValue(arr2, 25) == 4;
        assert Algorithms.findCumulativeValue(arr2, 27) == 4;

        float[] arr3 = {1, 2, 3, 5, 5, 5, 7, 8};

        for (int i = 0; i < 20; i++) {
            assert arr3[Algorithms.findCumulativeValue(arr3, 5)] == 5;
        }

        for (int i = 0; i < 20; i++) {
            assert arr3[Algorithms.findCumulativeValue(arr3, 4)] == 5;
        }


    }
}
