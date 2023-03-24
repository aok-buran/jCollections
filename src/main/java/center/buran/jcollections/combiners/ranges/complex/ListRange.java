package center.buran.jcollections.combiners.ranges.complex;

import com.fasterxml.jackson.annotation.JsonIgnore;
import center.buran.jcollections.combiners.ranges.Range;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Диапазон списка
 * Его нельзя построить из JSON илипреобразовать к нему
 */
public class ListRange extends Range {
    /**
     * Список элементов диапазона
     */
    private final List<Object> list;
    /**
     * кол-во жлементов в шаге
     */
    private final int step;
    /**
     * Минимальное значение диапазона
     */
    private final Object min;
    /**
     * Максимальное значение диапазона
     */
    private final Object max;

    /**
     * Конструктор диапазона списка
     *
     * @param list           список
     * @param stepCnt        кол-во шагов диапазона, если передаётся ноль, то кол-во задаётся равным
     *                       разнице между максимумом и минимумом
     * @param enabled        флаг, разрешено ли изменение диапазона
     * @param canRepeatValue флаг, могут ли повторяться значения интервала
     */
    public ListRange(List<Object> list, Integer stepCnt, boolean enabled, boolean canRepeatValue) {
        super(null, stepCnt == null ? list.size() - 1 : stepCnt, enabled, canRepeatValue);
        this.list = list;
        min = list.get(0);
        max = list.get(list.size() - 1);
        if (stepCnt == null) {
            step = 1;
        } else {
            step = list.size() / stepCnt;
        }
    }


    /**
     * Конструктор диапазона списка
     *
     * @param list    список
     * @param stepCnt кол-во шагов диапазона, если передаётся ноль, то кол-во задаётся равным
     *                разнице между максимумом и минимумом
     */
    public ListRange(List<Object> list, int stepCnt) {
        this(Objects.requireNonNull(list), stepCnt, true, true);
    }

    /**
     * Конструктор диапазона списка
     *
     * @param list список
     */
    public ListRange(List<Object> list) {
        this(Objects.requireNonNull(list), null, true, true);
    }

    /**
     * Диапазон списка
     *
     * @param listRange диапазон списка
     */
    public ListRange(ListRange listRange) {
        super(Objects.requireNonNull(listRange));
        this.list = new ArrayList<>(listRange.list);

        min = list.get(0);
        max = list.get(list.size() - 1);
        step = listRange.step;
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
     * Получить значение по  номеру шага
     *
     * @param stepNum номер шага
     * @return значение интервала
     */
    @Override
    public Object getValue(int stepNum) {
        return list.get(stepNum * step);
    }

    /**
     * Получить номер шага по значению
     *
     * @param object значение
     * @return номер шага
     */
    @Override
    public int getStepNum(Object object) {
        return list.indexOf(Objects.requireNonNull(object)) / step;
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
        return step + ", " + super.getString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ListRange listRange = (ListRange) o;

        if (step != listRange.step) return false;
        if (!Objects.equals(list, listRange.list)) return false;
        if (!Objects.equals(min, listRange.min)) return false;
        return Objects.equals(max, listRange.max);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (list != null ? list.hashCode() : 0);
        result = 31 * result + step;
        result = 31 * result + (min != null ? min.hashCode() : 0);
        result = 31 * result + (max != null ? max.hashCode() : 0);
        return result;
    }

}
