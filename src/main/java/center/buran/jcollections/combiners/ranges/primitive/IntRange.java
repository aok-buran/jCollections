package center.buran.jcollections.combiners.ranges.primitive;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * Integer диапазон
 */
public class IntRange extends PrimitiveRange {

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
    public IntRange(
            @JsonProperty("min") Integer min, @JsonProperty("max") Integer max,
            @JsonProperty("stepCnt") Integer stepCnt,
            @JsonProperty("name") String name, @JsonProperty("enabled") Boolean enabled,
            @JsonProperty("canRepeatValue") Boolean canRepeatValue
    ) {
        super(
                Objects.requireNonNull(min), Objects.requireNonNull(max),
                Objects.requireNonNullElse(stepCnt, max - min), name, enabled, canRepeatValue
        );
        this.size = max - min;
        this.step = (int) this.size / this.stepCnt;
        if (min >= max)
            throw new AssertionError(this + " min>=max");
        if (this.stepCnt > (int) this.size)
            throw new AssertionError(this + " stepCnt>=size: step is zero");
        setCurrentValue(min);
    }

    /**
     * Конструктор символьного диапазона
     *
     * @param range интервал
     */
    public IntRange(IntRange range) {
        super(Objects.requireNonNull(range));
    }

    /**
     * Строковое представление диапазона
     *
     * @return строковое представление диапазона
     */
    @Override
    public String toString() {
        return "IntRange{" + getString() + "}";
    }
}
