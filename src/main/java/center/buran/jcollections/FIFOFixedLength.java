package center.buran.jcollections;

import java.util.Arrays;

/**
 * Класс очереди фиксированной длины
 */
public class FIFOFixedLength {
    /**
     * Длина очереди
     */
    private final int length;
    /**
     * Значения
     */
    private final float[] values;

    /**
     * Конструктор очереди фиксированной длины
     *
     * @param length длина очереди
     */
    public FIFOFixedLength(int length) {
        this.length = length;
        values = new float[length];
    }

    /**
     * Конструктор очереди фиксированной длины
     *
     * @param fifoFixedLength очередь фиксированной длины
     */
    public FIFOFixedLength(FIFOFixedLength fifoFixedLength) {
        this.length = fifoFixedLength.length;
        this.values = fifoFixedLength.values.clone();
    }


    /**
     * Добавить значение в очередь
     *
     * @param value значение
     */
    public void addValue(float value) {
        if (length - 1 >= 0) System.arraycopy(values, 1, values, 0, length - 1);
        values[length - 1] = value;
    }

    /**
     * Получить сумму элементов очереди
     *
     * @return сумма элементов очереди
     */
    public float getSum() {
        float sum = 0.0f;
        for (float value : values) {
            sum += value;
        }
        return sum;
    }

    /**
     * Получить среднее значение элемента очереди
     *
     * @return среднее значение элемента очереди
     */
    public float getMeanValue() {
        return getSum() / length;
    }

    /**
     * Строковое представление объекта вида:
     *
     * @return FIFOFixedLength{length}
     */
    @Override
    public String toString() {
        return "FIFOFixedLength{" + length + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FIFOFixedLength that = (FIFOFixedLength) o;

        if (length != that.length) return false;
        return Arrays.equals(values, that.values);
    }

    @Override
    public int hashCode() {
        int result = length;
        result = 31 * result + Arrays.hashCode(values);
        return result;
    }
}
