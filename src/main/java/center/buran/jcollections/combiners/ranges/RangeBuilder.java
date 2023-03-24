package center.buran.jcollections.combiners.ranges;


import center.buran.jcollections.combiners.ranges.primitive.*;
import center.buran.jcollections.combiners.ranges.vector.Vector2iRange;
import center.buran.jcollections.combiners.ranges.vector.Vector3dRange;
import center.buran.jmath.vector.Vector2i;
import center.buran.jmath.vector.Vector3d;

import java.util.Objects;

/**
 * Строитель интервалов
 */
public class RangeBuilder {
    /**
     * кол-во созданных безымянных интервалов
     */
    private static int createdRangeCnt = 0;
    /**
     * Имя интервала
     */
    private final String name;
    /**
     * флаг, разрешён ли интервал
     */
    private Boolean enabled;
    /**
     * флаг, может ли  повторяться значение интервала
     */
    private Boolean canRepeatValue;
    /**
     * кол-во шагов
     */
    private Integer stepCnt;
    /**
     * минимальное значение интервала
     */
    private Object min;
    /**
     * максимальное значение интервала
     */
    private Object max;

    /**
     * Конструктор строителя интервалов
     */
    public RangeBuilder() {
        this("Range" + (createdRangeCnt++));
    }

    /**
     * Конструктор строителя интервалов
     *
     * @param name название интервала
     */
    public RangeBuilder(String name) {
        this.name = Objects.requireNonNull(name);
        enabled = true;
        canRepeatValue = false;
    }

    /**
     * Построить интервал
     *
     * @return интервал
     */
    public Range build() {
        if (min == null || max == null)
            return new EmptyRange(name, enabled, canRepeatValue);
        if (min.getClass().equals(Character.class))
            return new CharRange((char) min, (char) max, stepCnt, name, enabled, canRepeatValue);
        if (min.getClass().equals(Double.class))
            return new DoubleRange((double) min, (double) max, stepCnt, name, enabled, canRepeatValue);
        if (min.getClass().equals(Float.class))
            return new FloatRange((float) min, (float) max, stepCnt, name, enabled, canRepeatValue);
        if (min.getClass().equals(Integer.class))
            return new IntRange((int) min, (int) max, stepCnt, name, enabled, canRepeatValue);
        if (min.getClass().equals(Long.class))
            return new LongRange((long) min, (long) max, stepCnt, name, enabled, canRepeatValue);
        if (min.getClass().equals(Vector2i.class))
            return new Vector2iRange((Vector2i) min, (Vector2i) max, name, enabled, canRepeatValue);
        if (min.getClass().equals(Vector3d.class))
            return new Vector3dRange((Vector3d) min, (Vector3d) max, stepCnt, name, enabled, canRepeatValue);
        throw new AssertionError("unexpected min class " + min.getClass());
    }

    /**
     * Задать флаг, можно ли изменять интервал
     *
     * @param enabled флаг, можно ли изменять интервал
     * @return строитель
     */
    public RangeBuilder enabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    /**
     * Задать флаг,  может ли  повторяться значение интервала
     *
     * @param canRepeatValue флаг,  может ли  повторяться значение интервала
     * @return строитель
     */
    public RangeBuilder canRepeatValue(boolean canRepeatValue) {
        this.canRepeatValue = canRepeatValue;
        return this;
    }

    /**
     * Задать кол-во шагов
     *
     * @param stepCnt кол-во шагов
     * @return строитель
     */
    public RangeBuilder setStepCnt(Integer stepCnt) {
        this.stepCnt = Objects.requireNonNull(stepCnt);
        return this;
    }

    /**
     * Задать минимальное и максимальное значения интервала
     *
     * @param min минимальное значение интервала
     * @param max максимальное значение интервала
     * @return строитель
     */
    public RangeBuilder setMinMax(Object min, Object max) {
        this.min = Objects.requireNonNull(min);
        this.max = Objects.requireNonNull(max);
        return this;
    }
}
