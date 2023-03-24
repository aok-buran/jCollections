package center.buran.jcollections.combiners.ranges.vector;

import center.buran.jmath.coordinateSystem.CoordinateSystem2i;
import center.buran.jmath.vector.Vector2i;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import center.buran.jcollections.combiners.ranges.Range;

import java.util.Objects;

/**
 * Диапазон двумерных целочисленных векторов, перебирается по свёртке, шаг всегда равен 1
 */
public class Vector2iRange extends Range {
    /**
     * Минимальное значение диапазона
     */
    private final Object min;
    /**
     * Максимальное значение диапазона
     */
    private final Object max;
    /**
     * СК диапазона
     */
    private final CoordinateSystem2i ownCS;

    /**
     * Конструктор диапазона примитивного типа
     *
     * @param min            минимальное значение
     * @param max            максимальное значение
     * @param name           название интервала
     * @param enabled        флаг, разрешено ли изменение значения интервала
     * @param canRepeatValue флаг, могут ли повторяться значения интервала
     */
    @JsonCreator
    public Vector2iRange(
            @JsonProperty("min") Vector2i min, @JsonProperty("max") Vector2i max,
            @JsonProperty("name") String name, @JsonProperty("enabled") Boolean enabled,
            @JsonProperty("canRepeatValue") Boolean canRepeatValue
    ) {
        super(name, null, enabled, canRepeatValue);
        this.min = new Vector2i(min);
        this.max = new Vector2i(max);
        this.size = Vector2i.subtract(max, min);
        this.ownCS = new CoordinateSystem2i(min, max);
        this.stepCnt = ownCS.conv(max);
        this.setCurrentValue(min);
        if (Vector2i.biggerOrEqual(min, max))
            throw new AssertionError(this + " min>=max");
    }

    /**
     * Конструктор диапазона двумерных целочисленных векторов
     *
     * @param range интервал
     */
    public Vector2iRange(Vector2iRange range) {
        super(Objects.requireNonNull(range));
        this.min = new Vector2i((Vector2i) range.min);
        this.max = new Vector2i((Vector2i) range.max);
        this.size = new Vector2i((Vector2i) range.size);
        this.ownCS = new CoordinateSystem2i(range.ownCS);
        this.stepCnt = range.stepCnt;
        this.setCurrentValue(min);
    }

    /**
     * Получить значение по номеру шага
     *
     * @param stepNum номер шага
     * @return значение интервала
     */
    @Override
    public Object getValue(int stepNum) {
        return ownCS.deconv(stepNum);
    }

    /**
     * Получить номер шага по значению
     *
     * @param object значение
     * @return номер шага
     */
    @Override
    public int getStepNum(Object object) {
        return ownCS.conv((Vector2i) Objects.requireNonNull(object));
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
     * Строковое представление диапазона
     *
     * @return строковое представление диапазона
     */
    @Override
    public String toString() {
        return "Vector2iRange{" + getString() + "}";
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

        Vector2iRange that = (Vector2iRange) o;

        if (!Objects.equals(min, that.min)) return false;
        if (!Objects.equals(max, that.max)) return false;
        return Objects.equals(ownCS, that.ownCS);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (min != null ? min.hashCode() : 0);
        result = 31 * result + (max != null ? max.hashCode() : 0);
        result = 31 * result + (ownCS != null ? ownCS.hashCode() : 0);
        return result;
    }
}
