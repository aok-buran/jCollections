package center.buran.jcollections.combiners;


import center.buran.jcollections.Async;
import center.buran.jcollections.combiners.ranges.Range;

import java.math.BigInteger;
import java.util.*;

/**
 * Класс переборщика комбинаций перебирает наборы комбинаций из двух диапазонов
 * Иными словами, комбинации комбинаций. В каждой комбинации первого уровня не может быть одинаковых значений
 * из первого диапазона, а значения из второго диапазона могут повторяться
 */
public class BetaCombiner extends Combiner {
    /**
     * Сколько бит занимает максимальный порядковый номерзначения из дополнительного диапазона
     */
    private int auxiliaryBitSize;
    /**
     * Список чисел, соответствующих устраивающим нас наборам комбинаций
     * Каждое число через последовательность битов определяет подбираемый набор комбинаций.
     * Каждой комбинации соответствует бит равный одному в этом числе. Номера единичных битов
     * определяют порядковые номера комбинаций, содержащихся в наборе.
     */
    private List<BigInteger> combinationSetValuesTable;

    /**
     * Конструктор комбайнера
     *
     * @param mainRange      главный диапазон, значения из которого нельзя повторять вкомбинациях
     * @param auxiliaryRange дополнительный диапазон, значения которого могут повторяться
     */
    public BetaCombiner(Range mainRange, Range auxiliaryRange) {
        super(2);
        ranges.set(0, Objects.requireNonNull(mainRange));
        ranges.set(1, Objects.requireNonNull(auxiliaryRange));
        initCombinationLoop();
    }

    /**
     * Рассчитать количество комбинаций
     */
    @Override
    protected void calculateCombinationCnt() {
        // рассчитываем кол-во бит, которое занимает максимальный порядковый номерзначения из дополнительного диапазона
        auxiliaryBitSize = Integer.highestOneBit(ranges.get(1).getStepCnt());
        // создаём список чисел, соответствующих наборам комбинаций
        List<BigInteger> newCombinationSetValuesList = Collections.synchronizedList(new LinkedList<>());
        // максимальное число, которое надо проверить, подходит ли соответствующий ему набор комбинаций требованиям
        // комбайнера(первое - уникальное, воторое можетповторяться)
        BigInteger maxLoopValue = BigInteger.ONE
                .shiftLeft(ranges.get(0).getStepCnt() * (auxiliaryBitSize + 1))
                .subtract(BigInteger.ONE);
        // перебираем числа
        //System.out.println(auxiliaryBitSize+"  "+ranges.get(0).getStepCnt()+" "+maxLoopValue);
        Async.parallelForEach(maxLoopValue, (i) -> {
            boolean flgAdd = true;
            // перебираем биты числа
            for (int j = 0; j < i.bitLength(); j++) {
                // бит, отвечающий за число из первого диапазона
                int curPos = j * (auxiliaryBitSize + 1);
                // если этот бит равен нулю
                if (!i.testBit(curPos)) {
                    // и хотя бы один из битов, задающий номер значения из второго
                    // интервала равен единице, то такое число не может соответствовать одной из нужных нам комбинаций
                    for (int k = 0; k < auxiliaryBitSize; k++) {
                        if (i.testBit(curPos + auxiliaryBitSize - k)) {
                            flgAdd = false;
                        }
                    }
                }
            }
            if (flgAdd) {
                synchronized (newCombinationSetValuesList) {
                    newCombinationSetValuesList.add(i);
                }
            }
        });
        combinationSetValuesTable = new ArrayList<>(newCombinationSetValuesList);
        combinationCnt = BigInteger.valueOf(combinationSetValuesTable.size());
    }

    /**
     * Узнать номер набора комбинаций по его значению
     *
     * @param combinationSets комбинации
     * @return номер комбинации
     */
    public BigInteger betaConv(List<List<?>> combinationSets) {
        BigInteger result = BigInteger.ZERO;
        for (List<?> combination : combinationSets) {
            Iterator<?> it = combination.iterator();
            // получаем номер первого значения в комбинации
            int num1 = ranges.get(0).getStepNum(it.next());
            // выставляем в числе соответсвтующий бит
            result = result.setBit((auxiliaryBitSize + 1) * num1);
            // получаем номер второго значения в комбинации
            int num2 = ranges.get(1).getStepNum(it.next());
            // задаёт auxiliaryBitSize последующих битов
            // двоичным представлением этого числа
            for (int j = 0; j < auxiliaryBitSize; j++) {
                if (num2 % 2 == 1)
                    result = result.setBit((auxiliaryBitSize + 1) * num1 + j + 1);
                num2 = num2 / 2;
            }
        }
        // получаем номер набора в списке номеров наборов
        return BigInteger.valueOf(combinationSetValuesTable.indexOf(result));
    }

    /**
     * Преобразование номера набора комбинаций в набор комбинаций
     *
     * @param value номер комбинации
     * @return следующая комбинация
     */
    public List<List<?>> betaDeconv(BigInteger value) {
        List<List<?>> lst = new LinkedList<>();
        // получаем число, соответствующее нужнойнам комбинации
        BigInteger realValue = combinationSetValuesTable.get(value.intValue());
        // перебираем номера всех битов,которые отвечают за значения первого диапазона
        int bitCnt = realValue.bitLength();
        for (int i = 0; i <= bitCnt / (auxiliaryBitSize + 1); i++) {
            //получаем номер бита
            int curPos = i * (auxiliaryBitSize + 1);
            // если бит равен единице, значит есть комбинация с этим числом
            if (realValue.testBit(curPos)) {
                LinkedList<Object> combination = new LinkedList<>();
                combination.add(ranges.get(0).getValue(i));
                // из остальных битов формируем номерзначения из второго диапазона
                int auxValue = 0;
                for (int j = 0; j < auxiliaryBitSize; j++) {
                    auxValue = auxValue * 2;
                    if (realValue.testBit(curPos + auxiliaryBitSize - j)) {
                        auxValue++;
                    }
                }
                combination.add(ranges.get(1).getValue(auxValue));
                lst.add(combination);
            }
        }
        return new LinkedList<>(lst);
    }

    /**
     * Получить Сколько бит занимает максимальный порядковый номерзначения из дополнительного диапазона
     *
     * @return Сколько бит занимает максимальный порядковый номерзначения из дополнительного диапазона
     */
    public int getAuxiliaryBitSize() {
        return auxiliaryBitSize;
    }

    /**
     * Строковое представление объекта вида:
     *
     * @return "BetaCombiner{getString()}"
     */
    @Override
    public String toString() {
        return "BetaCombiner{" + getString() + '}';
    }

    /**
     * Строковое представление объекта вида:
     * "auxiliaryBitSize, super.getString()"
     *
     * @return строковое представление объекта
     */
    protected String getString() {
        return auxiliaryBitSize + ", " + super.getString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        BetaCombiner that = (BetaCombiner) o;

        if (auxiliaryBitSize != that.auxiliaryBitSize) return false;
        return Objects.equals(combinationSetValuesTable, that.combinationSetValuesTable);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + auxiliaryBitSize;
        result = 31 * result + combinationSetValuesTable.hashCode();
        return result;
    }
}
