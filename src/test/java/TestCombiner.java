
import center.buran.jcollections.combiners.*;
import center.buran.jcollections.combiners.ranges.Range;
import center.buran.jcollections.combiners.ranges.primitive.CharRange;
import center.buran.jcollections.combiners.ranges.primitive.IntRange;
import org.junit.Test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class TestCombiner {

    @Test
    public void testAlphaDeconv1() {
        AlphaCombiner combiner = new AlphaCombiner(2);
        combiner.setRange(0, Range.of(2, 5));
        combiner.setRange(1, Range.of(3, 4));
        testAlphaCombiner(combiner);

    }

    @Test
    public void testAlphaDeconv2() {
        AlphaCombiner combiner = new AlphaCombiner(3);
        combiner.setRange(0, Range.of('A', 'C'));
        combiner.setRange(1, Range.of(2, 3));
        combiner.setRange(2, Range.of(-3, -1));
        testAlphaCombiner(combiner);
    }


    private void testAlphaCombiner(AlphaCombiner combiner) {
        BigInteger cnt = combiner.getCombinationCnt();
        for (BigInteger i = BigInteger.ZERO; i.compareTo(cnt) < 0; i = i.add(BigInteger.ONE)) {
            List<List<?>> decoved = combiner.alphaDeconv(i);
//            for (LinkedList<Object> combination : decoved) {
//                System.out.print("{");
//                for (Object val : combination)
//                    System.out.print(val + " ");
//                System.out.print("}");
//            }
            // System.out.println();
            BigInteger conValue = combiner.alphaConv(decoved);
            assert conValue.equals(i);
        }

    }

    @Test
    public void testBetaDeconv1() {
        // long start = currentTimeMillis();
        BetaCombiner combiner = new BetaCombiner(
                Range.of(5L, 10L, true),
                Range.of('A', 'D', true)
        );
        testBetaCombiner(combiner);
        // System.out.println((currentTimeMillis() - start));

    }

    private void testBetaCombiner(BetaCombiner combiner) {
        for (BigInteger i = BigInteger.ZERO; i.compareTo(combiner.getCombinationCnt()) < 0; i = i.add(BigInteger.ONE)) {
            List<List<?>> decoved = combiner.betaDeconv(i);
            HashSet<Object> foundValues = new HashSet<>();
            for (List<?> combination : decoved) {
                assert foundValues.add(combination.get(0));
//                System.out.print("{");
//                for (Object val : combination)
//                    System.out.print(val + " ");
//                System.out.print("}");
            }
            //  System.out.println();
            BigInteger conValue = combiner.betaConv(decoved);
            //System.out.println(conValue.toString(2));
            assert conValue.equals(i);
        }

    }

    @Test
    public void testGammaDeconv() {
        // long start = currentTimeMillis();
        GammaCombiner combiner = new GammaCombiner(
                new CharRange('A', 'C', null, null, true, true),
                new IntRange(2, 4, null, null, true, false),
                new IntRange(-3, -1, null, null, true, true)
        );
        testGammaCombiner(combiner);
        // System.out.println((currentTimeMillis() - start));

    }

    @Test
    public void testGammaDeconv2() {
        GammaCombiner combiner = new GammaCombiner(
                new CharRange('A', 'C', null, null, true, false),
                new IntRange(2, 4, null, null, true, true),
                new IntRange(-3, -1, null, null, true, true)
        );
        testGammaCombiner(combiner);

    }

    @Test
    public void testGammaDeconv3() {
        GammaCombiner combiner = new GammaCombiner(
                new CharRange('A', 'C', null, null, true, true),
                new IntRange(2, 4, null, null, true, true),
                new IntRange(-3, -1, null, null, true, false)
        );
        testGammaCombiner(combiner);

    }


    private void testGammaCombiner(GammaCombiner combiner) {
        for (BigInteger i = BigInteger.ZERO; i.compareTo(combiner.getCombinationCnt()) < 0; i = i.add(BigInteger.ONE)) {
            List<List<?>> decoved = combiner.gammaDeconv(i);
            ArrayList<HashSet<Object>> nonRepeatedValues = new ArrayList<>();
            for (int j = 0; j < combiner.getNonRepeatedRangeIndexes().size(); j++) {
                HashSet<Object> values = new HashSet<>();
                nonRepeatedValues.add(values);
            }
            for (List<?> combination : decoved) {
//                System.out.print("{");
//                for (Object val : combination)
//                    System.out.print(val + " ");
//                System.out.print("}");
                for (int k = 0; k < combiner.getNonRepeatedRangeIndexes().size(); k++) {
                    int rangeNum = combiner.getNonRepeatedRangeIndexes().get(k);
                    HashSet<Object> values = nonRepeatedValues.get(k);
                    assert values.add(combination.get(rangeNum));
                }

            }
            // System.out.println();
            BigInteger conValue = combiner.gammaConv(decoved);
            //System.out.println(conValue.toString(2));
            assert conValue.equals(i);

        }
    }

    @Test
    public void testFixedSizeGammaDeconv() {
        //long start = currentTimeMillis();
        FixedSizeCombiner combiner = new FixedSizeCombiner(
                3,
                new CharRange('A', 'C', null, null, true, true),
                new IntRange(2, 4, null, null, true, false),
                new IntRange(-3, -1, null, null, true, true)
        );
        testFixedSizeGammaCombiner(combiner);
        //System.out.println((currentTimeMillis() - start));

    }

    @Test
    public void testFixedSizeGammaDeconv2() {
        // long start = currentTimeMillis();
        FixedSizeCombiner combiner = new FixedSizeCombiner(
                3,
                new CharRange('A', 'C', null, null, true, true),
                new IntRange(-3, -1, null, null, true, true),
                new IntRange(2, 4, null, null, true, false)
        );
        testFixedSizeGammaCombiner(combiner);
        //  System.out.println((currentTimeMillis() - start));

    }

    private void testFixedSizeGammaCombiner(FixedSizeCombiner combiner) {
        for (BigInteger i = BigInteger.ZERO; i.compareTo(combiner.getCombinationCnt()) < 0; i = i.add(BigInteger.ONE)) {
            List<List<?>> decoved = combiner.gammaDeconv(i);
            assert decoved.size() == combiner.getCombinationSetSize();
            ArrayList<HashSet<Object>> nonRepeatedValues = new ArrayList<>();
            for (int j = 0; j < combiner.getNonRepeatedRangeIndexes().size(); j++) {
                HashSet<Object> values = new HashSet<>();
                nonRepeatedValues.add(values);
            }
            for (List<?> combination : decoved) {
//                System.out.print("{");
//                for (Object val : combination)
//                    System.out.print(val + " ");
//                System.out.print("}");
                for (int k = 0; k < combiner.getNonRepeatedRangeIndexes().size(); k++) {
                    int rangeNum = combiner.getNonRepeatedRangeIndexes().get(k);
                    HashSet<Object> values = nonRepeatedValues.get(k);
                    assert values.add(combination.get(rangeNum));
                }

            }
            BigInteger conValue = combiner.gammaConv(decoved);
            //System.out.println(conValue.toString(2));
            assert conValue.equals(i);
        }

    }

    @Test
    public void testForEachDeconv() {
        ForEachCombiner combiner = new ForEachCombiner(
                new CharRange('A', 'C', null, null, true, true),
                new IntRange(-3, -1, null, null, true, true),
                new IntRange(2, 4, null, null, true, false)
        );
        testForEachCombiner(combiner);

    }

    private void testForEachCombiner(ForEachCombiner combiner) {
        for (BigInteger i = BigInteger.ZERO; i.compareTo(combiner.getCombinationCnt()) < 0; i = i.add(BigInteger.ONE)) {
            List<List<?>> decoved = combiner.gammaDeconv(i);
            assert decoved.size() == combiner.getCombinationSetSize();
            ArrayList<HashSet<Object>> nonRepeatedValues = new ArrayList<>();
            for (int j = 0; j < combiner.getNonRepeatedRangeIndexes().size(); j++) {
                HashSet<Object> values = new HashSet<>();
                nonRepeatedValues.add(values);
            }
            for (int j = 0; j < decoved.size(); j++) {
                List<?> combination = decoved.get(j);
                assert combination.get(0).equals(combiner.mainRange.getValue(j));
                for (int k = 0; k < combiner.getNonRepeatedRangeIndexes().size(); k++) {
                    int rangeNum = combiner.getNonRepeatedRangeIndexes().get(k) + 1;
                    HashSet<Object> values = nonRepeatedValues.get(k);
                    assert values.add(combination.get(rangeNum));
                }
            }
            BigInteger conValue = combiner.gammaConv(decoved);
            //System.out.println(conValue.toString(2));
            assert conValue.equals(i);
        }

    }

}
