package center.buran.jcollections.combiners;

import center.buran.jcollections.Async;
import center.buran.jcollections.combiners.ranges.Range;

import java.math.BigInteger;
import java.util.*;

/**
 * Класс переборщика комбинаций перебирает наборы комбинаций из всех диапазонов
 * Иными словами, комбинации комбинаций. В каждой комбинации первого уровня не может быть одинаковых значений
 * в указанных диапазонах, а в остальных - может
 */
public class GammaCombiner extends AlphaCombiner {
    /**
     * Список индексов диапазонов, в которых не должны повторяться значения
     */
    protected List<Integer> nonRepeatedRangeIndexes;
    /**
     * Список чисел, соответствующих устраивающим нас наборам комбинаций
     * Каждое число через последовательность битов определяет подбираемый набор комбинаций.
     * Каждой комбинации соответствует бит равный одному в этом числе. Номера единичных битов
     * определяют порядковые номера комбинаций, содержащихся в наборе.
     */
    private List<BigInteger> combinationSetValuesTable;
    /**
     * Массив предрассчитанных значений диапазонов с неповторяемыми значениями по номеру комбинации
     * (первый индекс - номер комбинации, второй - номер диапазона с неповторяемыми значениями)
     */
    private Integer[][] nonRepeatedRangeValuesFromNumbers;

    /**
     * Конструктор хранителя интервалов
     *
     * @param rangeCnt количество интервалов(задаётся точное число для того, чтобы кадлый интервал был строго
     *                 под указанным номерои и чтобы это контроллировать)
     */
    public GammaCombiner(int rangeCnt) {
        super(rangeCnt);
        initGamaCombinationLoop();
    }

    /**
     * Конструктор хранителя интервалов
     *
     * @param ranges список интервалов
     */
    public GammaCombiner(Range... ranges) {
        super(ranges);
        initGamaCombinationLoop();
    }

    /**
     * Конструктор хранителя интервалов
     *
     * @param lst список интервалов
     */
    public GammaCombiner(List<Range> lst) {
        super(Objects.requireNonNull(lst));
        initGamaCombinationLoop();
    }

    /**
     * инициализировать переборщик
     */
    @Override
    public void initCombinationLoop() {

    }

    /**
     * инициализировать гамма переборщик
     */
    public void initGamaCombinationLoop() {
        combinationLoopPos = BigInteger.ZERO;
        // заполняем индексы диапазонов, в которых не должны повторяться значения
        nonRepeatedRangeIndexes = new ArrayList<>();
        for (int i = 0; i < ranges.size(); i++) {
            if (!ranges.get(i).isCanRepeatValue())
                nonRepeatedRangeIndexes.add(i);
        }
        // получаем кол-во всех комбинаций
        AlphaCombiner allCombinationCombiner = new AlphaCombiner(ranges);
        BigInteger maxValue = allCombinationCombiner.combinationCnt;
        // рассчитываем гаммы
        ArrayList<Integer> gammas = new ArrayList<>();
        int currentGamma = 1;
        gammas.add(currentGamma);
        for (int i = ranges.size() - 1; i >= 1; i--) {
            currentGamma *= ranges.get(i).getStepCnt() + 1;
            gammas.add(currentGamma);
        }
        //инициализируем массив предрассчитанных значений диапазонов с неповторяемыми значениями
        nonRepeatedRangeValuesFromNumbers = new Integer[maxValue.bitLength()][nonRepeatedRangeIndexes.size()];
        Async.parallelForEach(maxValue.bitLength(), (i) -> {
            for (int j = 0; j < nonRepeatedRangeIndexes.size(); j++) {
                int rangeNum = nonRepeatedRangeIndexes.get(j);
                Range nonRepeatedRange = ranges.get(rangeNum);
                nonRepeatedRangeValuesFromNumbers[i][j] = i / gammas.get(rangeNum) % (nonRepeatedRange.getStepCnt() + 1);
            }
        });

        // создаём список чисел, соответствующих наборам комбинаций
        List<BigInteger> newCombinationSetValuesList = Collections.synchronizedList(new LinkedList<>());
        // проверяем каждую комбинацию
        Async.parallelForEach(maxValue.add(BigInteger.ONE), (i) -> {
            if (checkCombination(i))
                synchronized (newCombinationSetValuesList) {
                    newCombinationSetValuesList.add(i);
                }
        });

        combinationSetValuesTable = new ArrayList<>(newCombinationSetValuesList);
        combinationCnt = BigInteger.valueOf(combinationSetValuesTable.size());
    }


    /**
     * Проверка комбинации, подходит ли она для данного комбайнера
     *
     * @param combinationNumber номер комбинации
     * @return флаг, подходит ли комбинация для данного комбайнера
     */
    protected boolean checkCombination(BigInteger combinationNumber) {
        // создаём список множеств, хранящих найденные значения
        ArrayList<HashSet<Integer>> nonFoundValues = new ArrayList<>();
        for (int k = 0; k < nonRepeatedRangeIndexes.size(); k++) {
            HashSet<Integer> values = new HashSet<>();
            nonFoundValues.add(values);
        }
        // перебираем биты в номере комбинации
        for (int j = 0; j < combinationNumber.bitLength(); j++) {
            // если текущий бит равен единице
            if (combinationNumber.testBit(j)) {
                // перебираем индексы диапазон с запрещёнными повторами
                for (int k = 0; k < nonRepeatedRangeIndexes.size(); k++) {
                    // если в множестве уже есть такое значение, значит,
                    // такая комбинация нам не подходит
                    if (!nonFoundValues.get(k).add(nonRepeatedRangeValuesFromNumbers[j][k])) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Узнать номер набора комбинаций по его значению
     *
     * @param combinationSets комбинации
     * @return номер комбинации
     */
    public BigInteger gammaConv(List<List<?>> combinationSets) {
        // получаем номер набора в списке номеров наборов
        return BigInteger.valueOf(combinationSetValuesTable.indexOf(alphaConv(Objects.requireNonNull(combinationSets))));
    }

    /**
     * Преобразование номера набора комбинаций в набор комбинаций
     *
     * @param value номер комбинации
     * @return следующая комбинация
     */
    public List<List<?>> gammaDeconv(BigInteger value) {
        return alphaDeconv(combinationSetValuesTable.get(value.intValue()));
    }

    /**
     * Получить список индексов диапазонов, в которых не должны повторяться значения
     *
     * @return список индексов диапазонов, в которых не должны повторяться значения
     */
    public List<Integer> getNonRepeatedRangeIndexes() {
        return nonRepeatedRangeIndexes;
    }

    /**
     * Строковое представление объекта вида:
     *
     * @return "GammaCombiner{getString()}"
     */
    @Override
    public String toString() {
        return "GammaCombiner{" + getString() + '}';
    }

    /**
     * Строковое представление объекта вида:
     * "nonRepeatedRangeIndexes, super.getString()"
     *
     * @return строковое представление объекта
     */
    protected String getString() {
        return nonRepeatedRangeIndexes + ", " + super.getString();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        GammaCombiner that = (GammaCombiner) o;

        if (!Objects.equals(nonRepeatedRangeIndexes, that.nonRepeatedRangeIndexes))
            return false;
        if (!Objects.equals(combinationSetValuesTable, that.combinationSetValuesTable))
            return false;
        return Arrays.deepEquals(nonRepeatedRangeValuesFromNumbers, that.nonRepeatedRangeValuesFromNumbers);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (nonRepeatedRangeIndexes != null ? nonRepeatedRangeIndexes.hashCode() : 0);
        result = 31 * result + (combinationSetValuesTable != null ? combinationSetValuesTable.hashCode() : 0);
        result = 31 * result + Arrays.deepHashCode(nonRepeatedRangeValuesFromNumbers);
        return result;
    }
}
