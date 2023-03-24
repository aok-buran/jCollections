package center.buran.jcollections.combiners;

import center.buran.jcollections.combiners.ranges.Range;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Класс переборщика комбинаций перебирает наборы комбинаций из всех диапазонов
 * Иными словами, комбинации комбинаций. В каждой комбинации первого уровня заданное количество элементов, равное
 * количеству значений в первом диапазоне.  В каждом наборе элементы из первого диапазона в каждой комбинации
 * стоят на первом месте и вставляются по очереди.
 * При этом не может быть одинаковых значений в указанных диапазонах, а в  остальных может
 * в указанных диапазонах, а в остальных - может.
 */
public class ForEachCombiner extends FixedSizeCombiner {
    /**
     * Главный диапазон
     */
    public Range mainRange;

    /**
     * Конструктор хранителя интервалов
     *
     * @param ranges список интервалов
     */
    public ForEachCombiner(Range... ranges) {
        super(ranges.length - 1, Arrays.asList(ranges).subList(1, ranges.length));
        mainRange = ranges[0];
    }

    /**
     * Конструктор хранителя интервалов
     *
     * @param lst список интервалов
     */
    public ForEachCombiner(List<Range> lst) {
        super(lst.size() - 1, lst.subList(1, lst.size()));
        mainRange = ranges.get(0);
    }

    /**
     * Узнать номер набора комбинаций по его значению
     *
     * @param combinationSets комбинации
     * @return номер комбинации
     */
    @Override
    public BigInteger gammaConv(List<List<?>> combinationSets) {
        List<List<?>> reducedCombinationSet = new LinkedList<>();
        for (List<?> combination : combinationSets) {
            reducedCombinationSet.add(new LinkedList<>(combination.subList(1, combination.size())));
        }
        // получаем номер набора в списке номеров наборов
        return super.gammaConv(reducedCombinationSet);
    }

    /**
     * Преобразование номера набора комбинаций в набор комбинаций
     *
     * @param value номер комбинации
     * @return следующая комбинация
     */
    @Override
    public List<List<?>> gammaDeconv(BigInteger value) {
        List<List<?>> gammaDeconved = super.gammaDeconv(value);
        List<List<?>> addedCombinationSet = new LinkedList<>();
        for (int i = 0; i < mainRange.getStepCnt(); i++) {
            LinkedList<Object> newCombination = new LinkedList<>(gammaDeconved.get(i));
            newCombination.add(0, mainRange.getValue(i));
            addedCombinationSet.add(newCombination);
        }
        return addedCombinationSet;
    }

    /**
     * Строковое представление объекта вида:
     *
     * @return "ForEachCombiner{getString()}"
     */
    @Override
    public String toString() {
        return "ForEachCombiner{" + getString() + '}';
    }

    /**
     * Строковое представление объекта вида:
     * "mainRange, super.getString()"
     *
     * @return строковое представление объекта
     */
    protected String getString() {
        return mainRange + ", " + super.getString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ForEachCombiner that = (ForEachCombiner) o;

        return Objects.equals(mainRange, that.mainRange);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + mainRange.hashCode();
        return result;
    }
}
