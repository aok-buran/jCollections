package center.buran.jcollections.combiners;


import center.buran.jcollections.combiners.ranges.Range;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Класс переборщика комбинаций перебирает все наборы комбинаций из двух диапазонов
 * Каждое число через последовательность битов определяет подбираемый набор комбинаций.
 * Каждой комбинации соответствует бит равный одному в этом числе. Номера единичных битов
 * определяют порядковые номера комбинаций, содержащихся в наборе.
 */
public class AlphaCombiner extends Combiner {
    /**
     * Конструктор хранителя интервалов
     *
     * @param rangeCnt количество интервалов(задаётся точное число для того, чтобы кадлый интервал был строго
     *                 под указанным номерои и чтобы это контроллировать)
     */
    public AlphaCombiner(int rangeCnt) {
        super(rangeCnt);
    }

    /**
     * Конструктор хранителя интервалов
     *
     * @param ranges список интервалов
     */
    public AlphaCombiner(Range... ranges) {
        super(ranges);
    }

    /**
     * Конструктор хранителя интервалов
     *
     * @param lst список интервалов
     */
    public AlphaCombiner(List<Range> lst) {
        super(Objects.requireNonNull(lst));
    }

    /**
     * Рассчитать количество комбинаций
     */
    @Override
    protected void calculateCombinationCnt() {
        combinationCnt = BigInteger.ONE.shiftLeft(simpleConv(getMax())).subtract(BigInteger.ONE);
    }

    /**
     * Узнать номер набора комбинаций по его значению
     *
     * @param combinationSets комбинации
     * @return номер комбинации
     */
    public BigInteger alphaConv(List<List<?>> combinationSets) {
        BigInteger result = BigInteger.ZERO;
        for (List<?> combination : combinationSets) {
            int value = simpleConv(combination);
            result = result.setBit(value);
        }
        return result;
    }

    /**
     * Преобразование номера набора комбинаций в набор комбинаций
     *
     * @param value номер комбинации
     * @return следующая комбинация
     */
    public List<List<?>> alphaDeconv(BigInteger value) {
        List<List<?>> lst = new LinkedList<>();
        int bitCnt = value.bitLength();
        for (int i = 0; i < bitCnt; i++) {
            if (value.testBit(i)) {
                lst.add(deconv(BigInteger.valueOf(i)));
            }
        }
        return lst;
    }

    /**
     * Строковое представление объекта вида:
     *
     * @return "AlphaCombiner{getString()}"
     */
    @Override
    public String toString() {
        return "AlphaCombiner{" + getString() + '}';
    }


}
