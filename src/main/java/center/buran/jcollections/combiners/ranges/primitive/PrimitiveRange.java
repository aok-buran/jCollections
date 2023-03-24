package center.buran.jcollections.combiners.ranges.primitive;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import center.buran.jcollections.combiners.ranges.Range;

import java.util.Objects;


/**
 * Класс диапазонов примитивных типов
 */
public abstract class PrimitiveRange extends Range {
    /**
     * Шаг диапазона
     */
    @JsonIgnore
    protected Object step;
    /**
     * Минимальное значение диапазона
     */
    private final Object min;
    /**
     * Максимальное значение диапазона
     */
    private final Object max;

    /**
     * Конструктор диапазона примитивного типа
     *
     * @param min            минимальное значение
     * @param max            максимальное значение
     * @param stepCnt        кол-во шагов диапазона
     * @param name           название диапазона
     * @param enabled        флаг, разрешено ли изменение значения интервала
     * @param canRepeatValue флаг, могут ли повторяться значения интервала
     */
    PrimitiveRange(
            @JsonProperty("min") Object min, @JsonProperty("max") Object max,
            @JsonProperty("stepCnt") Integer stepCnt,
            @JsonProperty("name") String name, @JsonProperty("enabled") Boolean enabled,
            @JsonProperty("canRepeatValue") Boolean canRepeatValue
    ) {
        super(name, stepCnt, enabled, canRepeatValue);
        this.min = Objects.requireNonNull(min);
        this.max = Objects.requireNonNull(max);
    }


    /**
     * Конструктор диапазона примитивного типа
     *
     * @param range диапазон
     */
    PrimitiveRange(PrimitiveRange range) {
        super(Objects.requireNonNull(range));
        this.min = range.min;
        this.max = range.max;
        this.step = range.step;
    }

    /**
     * Получить  максимум
     *
     * @return максимум
     */
    @Override
    public Object getMax() {
        return max;
    }

    /**
     * Получить минимум
     *
     * @return минимум
     */
    @Override
    public Object getMin() {
        return min;
    }


    /**
     * Обрезать значение по границам интервала
     *
     * @param object значение, которое нужно обрезать
     * @return обрезанное значение
     */
    public Object trunc(Object object) {
        if (!object.getClass().equals(getMin().getClass())) {
            throw new AssertionError(
                    "can not trunc(): object class " + object.getClass() + " is not equal to getMin() class "
                            + getMin().getClass()
            );
        }
        if (object.getClass().equals(Character.class)) {
            if ((char) object < (char) getMin())
                return getMin();
            if ((char) object > (char) getMax())
                return getMax();
        }
        if (object.getClass().equals(Double.class)) {
            if ((double) object < (double) getMin())
                return getMin();
            if ((double) object > (double) getMax())
                return getMax();
        }
        if (object.getClass().equals(Float.class)) {
            if ((float) object < (float) getMin())
                return getMin();
            if ((float) object > (float) getMax())
                return getMax();
        }
        if (object.getClass().equals(Integer.class)) {
            if ((int) object < (int) getMin())
                return getMin();
            if ((int) object > (int) getMax())
                return getMax();
        }
        if (object.getClass().equals(Long.class)) {
            if ((long) object < (long) getMin())
                return getMin();
            if ((long) object > (long) getMax())
                return getMax();
        }
        return object;
    }


    /**
     * Получить значение по  номеру шага
     *
     * @param stepNum номер шага
     * @return значение интервала
     */
    @Override
    public Object getValue(int stepNum) {
        if (getMin().getClass().equals(Character.class)) {
            return (char) ((char) getMin() + stepNum * (int) step);
        }
        if (getMin().getClass().equals(Double.class)) {
            return stepNum * (double) step + (double) getMin();
        }
        if (getMin().getClass().equals(Float.class)) {
            return stepNum * (float) step + (float) getMin();
        }
        if (getMin().getClass().equals(Integer.class)) {
            return stepNum * (int) step + (int) getMin();
        }
        if (getMin().getClass().equals(Long.class)) {
            return stepNum * (long) step + (long) getMin();
        }
        throw new AssertionError("getValue(): unexpected getMin() class" + getMin().getClass());
    }


    /**
     * Получить номер шага по значению
     *
     * @param object значение
     * @return номер шага
     */
    @Override
    public int getStepNum(Object object) {
        if (!getMin().getClass().equals(object.getClass())) {
            throw new AssertionError(getMin().getClass() + " " + object.getClass());
        }
        if (getMin().getClass().equals(Character.class)) {
            return (((char) object) - (char) getMin()) / (int) step;
        }
        if (getMin().getClass().equals(Double.class)) {
            return (int) Math.round(((double) object - (double) getMin()) / (double) step);
        }
        if (getMin().getClass().equals(Float.class)) {
            return Math.round(((float) object - (float) getMin()) / (float) step);
        }
        if (getMin().getClass().equals(Integer.class)) {
            return (((int) object) - (int) getMin()) / (int) step;
        }
        if (getMin().getClass().equals(Long.class)) {
            return (int) ((((long) object) - (long) getMin()) / (long) step);
        }
        throw new AssertionError("getValue(): unexpected getMin() class" + getMin().getClass());
    }

    /**
     * Строковое представление диапазона
     *
     * @return строковое представление диапазона
     */
    @Override
    public String toString() {
        return "PrimitiveRange{" + getString() + "}";
    }

    /**
     * Строковое представление диапазона
     *
     * @return строковое представление диапазона
     */
    @JsonIgnore
    public String getString() {
        return super.getString() + "[" + min + "," + max + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        PrimitiveRange that = (PrimitiveRange) o;

        if (!Objects.equals(step, that.step)) return false;
        if (!Objects.equals(min, that.min)) return false;
        return Objects.equals(max, that.max);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (step != null ? step.hashCode() : 0);
        result = 31 * result + (min != null ? min.hashCode() : 0);
        result = 31 * result + (max != null ? max.hashCode() : 0);
        return result;
    }
}

