package center.buran.jcollections.combiners.ranges;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;


/**
 * Пустой интервал
 */
public class EmptyRange extends Range {

    /**
     * Конструктор базового диапазона
     *
     * @param name           название диапазона
     * @param enabled        флаг, разрешён ли диапазон
     * @param canRepeatValue флаг, могут ли повторяться значения интервала
     */
    @JsonCreator
    public EmptyRange(
            @JsonProperty("name") String name, @JsonProperty("enabled") Boolean enabled,
            @JsonProperty("canRepeatValue") Boolean canRepeatValue
    ) {
        super(name, null, enabled, canRepeatValue);
    }

    /**
     * Конструктор пустого интервала
     *
     * @param range интервал, на основе которого создаётся новый
     */
    public EmptyRange(Range range) {
        super(Objects.requireNonNull(range));
    }


    /**
     * Получить значение по  номеру шага
     *
     * @param stepNum номер шага
     * @return значение интервала
     */
    @Override
    public Object getValue(int stepNum) {
        return null;
    }

    /**
     * Получить номер шага по значению
     *
     * @param object значение
     * @return номер шага
     */
    @Override
    public int getStepNum(Object object) {
        return 0;
    }

    /**
     * Получить  максимум
     *
     * @return максимум
     */
    @Override
    @JsonIgnore
    public Object getMax() {
        return null;
    }

    /**
     * Получить минимум
     *
     * @return минимум
     */
    @Override
    @JsonIgnore
    public Object getMin() {
        return null;
    }

    /**
     * Проверка, является ли диапазон пустым
     *
     * @return флаг, является ли диапазон пустым
     */
    @Override
    public String toString() {
        return "EmptyRange{" + getString() + "}";
    }

}
