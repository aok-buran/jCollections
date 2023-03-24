package center.buran.jcollections.combiners;


import center.buran.jcollections.combiners.ranges.Range;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

/**
 * Класс переборщика комбинаций перебирает наборы комбинаций из всех диапазонов
 * Иными словами, комбинации комбинаций. В каждой комбинации первого уровня не может быть одинаковых значений
 * в указанных диапазонах, а в остальных - может. При этом каждый набор имеет заданное кол-во комбинаций.
 */
public class FixedSizeCombiner extends GammaCombiner {

    /**
     * Количество комбинаций в каждом наборе
     */
    private final int combinationSetSize;

    /**
     * Конструктор хранителя интервалов
     *
     * @param combinationSetSize количество элементов в комбинации
     * @param ranges             список интервалов
     */
    public FixedSizeCombiner(int combinationSetSize, Range... ranges) {
        super(ranges);
        this.combinationSetSize = combinationSetSize;
        initFixedSizeCombinationLoop();
        for (int nonRepeatedRangeIndex : nonRepeatedRangeIndexes) {
            if (ranges[nonRepeatedRangeIndex].getStepCnt() + 1 < combinationSetSize)
                throw new AssertionError(ranges[nonRepeatedRangeIndex] +
                        " has stepCnt less than fixed combination length=" + ranges.length
                );
        }
    }

    /**
     * Конструктор хранителя интервалов
     *
     * @param combinationSetSize количество элементов в комбинации
     * @param lst                список интервалов
     */
    public FixedSizeCombiner(int combinationSetSize, List<Range> lst) {
        super(Objects.requireNonNull(lst));
        this.combinationSetSize = combinationSetSize;
        initFixedSizeCombinationLoop();
        for (int nonRepeatedRangeIndex : nonRepeatedRangeIndexes) {
            if (ranges.get(nonRepeatedRangeIndex).getStepCnt() + 1 < combinationSetSize)
                throw new AssertionError(ranges.get(nonRepeatedRangeIndex) +
                        " has stepCnt less than fixed combination length=" + ranges.size()
                );
        }
    }

    /**
     * Проверка комбинации, подходит ли она для данного комбайнера
     *
     * @param combinationNumber номер комбинации
     * @return флаг, подходит ли комбинация для данного комбайнера
     */
    protected boolean checkCombination(BigInteger combinationNumber) {
        return combinationNumber.bitCount() == combinationSetSize && super.checkCombination(combinationNumber);
    }

    /**
     * инициализировать гамма переборщик
     */
    @Override
    public void initGamaCombinationLoop() {
    }

    /**
     * инициализировать переборщик c фиксированной длиной
     */
    public void initFixedSizeCombinationLoop() {
        super.initGamaCombinationLoop();
    }

    /**
     * Получить  Количество комбинаций в каждом наборе
     *
     * @return Количество комбинаций в каждом наборе
     */
    public int getCombinationSetSize() {
        return combinationSetSize;
    }

    /**
     * Строковое представление объекта вида:
     *
     * @return "FixedSizeCombiner{getString()}"
     */
    @Override
    public String toString() {
        return "FixedSizeCombiner{" + getString() + '}';
    }

    /**
     * Строковое представление объекта вида:
     * "combinationSetSize, super.getString()"
     *
     * @return строковое представление объекта
     */
    protected String getString() {
        return combinationSetSize + ", " + super.getString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        FixedSizeCombiner that = (FixedSizeCombiner) o;

        return combinationSetSize == that.combinationSetSize;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + combinationSetSize;
        return result;
    }
}
