package center.buran.jcollections.combiners.ranges.complex;

import com.fasterxml.jackson.annotation.JsonIgnore;
import center.buran.jcollections.combiners.Combiner;
import center.buran.jcollections.combiners.ranges.Range;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

/**
 * Диапазон комбайнера
 */
public class CombinerRange extends Range {
    /**
     * Комбайнер
     */
    private final Combiner combiner;
    /**
     * Минимальное значение диапазона
     */
    private final Object min;
    /**
     * Максимальное значение диапазона
     */
    private final Object max;

    /**
     * Конструктор диапазона комбайнера
     *
     * @param combiner       комбайнер
     * @param enabled        флаг, разрешён ли диапазон
     * @param canRepeatValue флаг, могут ли повторяться значения интервала
     */
    public CombinerRange(Combiner combiner, boolean enabled, boolean canRepeatValue) {
        super(null, combiner.getCombinationCnt().intValue(), enabled, canRepeatValue);
        this.min = combiner.deconv(BigInteger.ZERO);
        this.max = combiner.deconv(combiner.getCombinationCnt().subtract(BigInteger.ONE));
        this.combiner = combiner;
    }

    /**
     * Конструктор диапазона комбайнера
     *
     * @param combinerRange диапазон комбайнера
     */
    public CombinerRange(CombinerRange combinerRange) {
        super(Objects.requireNonNull(combinerRange));
        this.min = combinerRange.min;
        this.max = combinerRange.max;
        this.combiner = combinerRange.combiner;
    }

    /**
     * Конструктор диапазона комбайнера
     *
     * @param combiner комбайнер
     */
    public CombinerRange(Combiner combiner) {
        this(Objects.requireNonNull(combiner), true, true);
    }

    /**
     * Получить значение по  номеру шага
     *
     * @param stepNum номер шага
     * @return значение интервала
     */
    @Override
    public Object getValue(int stepNum) {
        return combiner.deconv(BigInteger.valueOf(stepNum));
    }

    /**
     * Получить номер шага по значению
     *
     * @param object значение
     * @return номер шага
     */
    @Override
    public int getStepNum(Object object) {
        return combiner.conv((List<Object>) Objects.requireNonNull(object)).intValue();
    }

    /**
     * Получить  максимум
     *
     * @return максимум
     */
    @Override
    public Object getMax() {
        return min;
    }

    /**
     * Получить минимум
     *
     * @return минимум
     */
    @Override
    public Object getMin() {
        return max;
    }

    /**
     * Строковое представление диапазона
     *
     * @return строковое представление диапазона
     */
    @Override
    public String toString() {
        return "ListRange{" + getString() + "}";
    }

    /**
     * Строковое представление диапазона
     *
     * @return строковое представление диапазона
     */
    @JsonIgnore
    public String getString() {
        return combiner + ", " + super.getString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        CombinerRange that = (CombinerRange) o;

        if (!Objects.equals(combiner, that.combiner)) return false;
        if (!Objects.equals(min, that.min)) return false;
        return Objects.equals(max, that.max);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (combiner != null ? combiner.hashCode() : 0);
        result = 31 * result + (min != null ? min.hashCode() : 0);
        result = 31 * result + (max != null ? max.hashCode() : 0);
        return result;
    }
}
