package center.buran.jcollections.combiners.ranges.primitive;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * Вещественный диапазон
 */
public class DoubleRange extends PrimitiveRange {


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
    @JsonCreator
    public DoubleRange(
            @JsonProperty("min") Double min, @JsonProperty("max") Double max,
            @JsonProperty("stepCnt") Integer stepCnt,
            @JsonProperty("name") String name, @JsonProperty("enabled") Boolean enabled,
            @JsonProperty("canRepeatValue") Boolean canRepeatValue
    ) {
        super(
                Objects.requireNonNull(min), Objects.requireNonNull(max),
                Objects.requireNonNullElse(stepCnt, 10), name, enabled, canRepeatValue
        );
        this.size = max - min;
        this.step = (double) this.size / this.stepCnt;
        if (min >= max)
            throw new AssertionError(this + " min>=max");
        setCurrentValue(min);
    }

    /**
     * Конструктор символьного диапазона
     *
     * @param range интервал
     */
    public DoubleRange(DoubleRange range) {
        super(Objects.requireNonNull(range));
    }

    /**
     * Строковое представление диапазона
     *
     * @return строковое представление диапазона
     */
    @Override
    public String toString() {
        return "DoubleRange{" + getString() + "}";
    }
}
