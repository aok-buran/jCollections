import center.buran.jcollections.combiners.Combiner;
import center.buran.jcollections.combiners.ranges.Range;
import center.buran.jcollections.combiners.ranges.complex.CombinerRange;
import center.buran.jcollections.combiners.ranges.complex.ListRange;
import center.buran.jcollections.combiners.ranges.primitive.DoubleRange;
import center.buran.jcollections.combiners.ranges.primitive.IntRange;
import center.buran.jcollections.combiners.ranges.vector.Vector3dRange;
import center.buran.jmath.vector.Vector2i;
import center.buran.jmath.vector.Vector3d;
import org.junit.Test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TestRanges {

    @Test
    public void testRanges1() {
        Combiner combiner = new Combiner(3);
        combiner.setRange(0, Range.of(10, 20));
        combiner.setRange(1, Range.of(-15.0, 20.0, 5));
        combiner.setRange(2, Range.of(new Vector3d(1, 2, 3), new Vector3d(4, 5, 7), 5));
        testCombiner(combiner);
    }

    @Test
    public void testRanges2() {
        Combiner combiner = new Combiner(3);
        combiner.setRange(0, Range.of('C', 'G'));
        combiner.setRange(1, Range.of(20L, 25L));
        combiner.setRange(2, Range.of(new Vector2i(1, 2), new Vector2i(4, 5)));
        testCombiner(combiner);
    }

    @Test
    public void testRanges3() {
        Combiner combiner = new Combiner(6);
        combiner.setRange(0, Range.of('A', 'Z', 5));
        combiner.setRange(1, Range.of(20, 150, 10));
        combiner.setRange(2, Range.of(45, 150, false));
        combiner.setRange(3, Range.of(5L, 1005L, 15));
        combiner.setRange(4, Range.of('B', 'C', false));
        combiner.setRange(5, Range.of(0.5, 1.5, 10));
        testCombiner(combiner);
    }

    @Test
    public void testRanges4() {
        Combiner combiner = new Combiner(
                Range.of(0, 100, 20),
                Range.of(-15.0, 20.0, 5),
                Range.of(new Vector3d(1, 2, 3), new Vector3d(4, 5, 7), 5)
        );
        testCombiner(combiner);
    }

    private void testCombiner(Combiner combiner) {
        for (BigInteger i = BigInteger.ZERO; i.compareTo(combiner.getCombinationCnt()) < 0; i = i.add(BigInteger.ONE)) {
            List<Object> decoved = combiner.deconv(i);
//            for (Object o : decoved)
//                System.out.print(o + " ");
//            System.out.println();
            BigInteger conValue = combiner.conv(decoved);
            assert conValue.equals(i);
        }
    }

    @Test
    public void testIntDeconv() {
        Combiner combiner = new Combiner(
                Range.of(0, 100, 20),
                Range.of(-15.0, 20.0, 5),
                new Vector3dRange(new Vector3d(1, 2, 3), new Vector3d(4, 5, 7), 5)
        );
        for (int i = 0; i < combiner.getCombinationCnt().intValue(); i++) {
            LinkedList<Object> decoved = combiner.deconv(i);
//            for (Object o : decoved)
//                System.out.print(o + " ");
//            System.out.println();
            int conValue = combiner.simpleConv(decoved);
            assert conValue == i;
        }
    }

    @Test
    public void testCombinerRange() {
        Combiner combiner = new Combiner(2);
        combiner.setRange(0, Range.of( 5, 10));
        combiner.setRange(1,  Range.of('A', 'C'));

        Combiner parentCombiner = new Combiner(2);
        parentCombiner.setRange(0,  Range.of(1.0, 7.0, 10));
        parentCombiner.setRange(1, new CombinerRange(combiner));
        for (BigInteger i = BigInteger.ZERO; i.compareTo(parentCombiner.getCombinationCnt()) < 0; i = i.add(BigInteger.ONE)) {
            List<Object> decoved = parentCombiner.deconv(i);
//            for (Object combination : decoved) {
//                System.out.print(combination + " ");
//            }
//            System.out.println();
            BigInteger conValue = parentCombiner.conv(decoved);
            assert conValue.equals(i);
        }
    }

    @Test
    public void testListRange() {
        Combiner combiner = new Combiner(2);
        ArrayList<Object> lst1 = new ArrayList<>();
        lst1.add(1);
        lst1.add(3);
        lst1.add(5);
        lst1.add(15);
        lst1.add(25);
        combiner.setRange(0, new ListRange(lst1));
        LinkedList<Object> lst2 = new LinkedList<>();
        lst2.add(1.0);
        lst2.add(5.0);
        lst2.add(8.9);
        lst2.add(12.4);
        lst2.add(5.7);
        lst2.add(12.1);
        lst2.add(11.1);
        lst2.add(6);
        lst2.add(7);
        combiner.setRange(1, new ListRange(lst2, 3));
        for (BigInteger i = BigInteger.ZERO; i.compareTo(combiner.getCombinationCnt()) < 0; i = i.add(BigInteger.ONE)) {
            List<Object> decoved = combiner.deconv(i);
//            for (Object combination : decoved) {
//                System.out.print(combination + " ");
//            }
//            System.out.println();
            BigInteger conValue = combiner.conv(decoved);
            assert conValue.equals(i);
        }
    }


}
