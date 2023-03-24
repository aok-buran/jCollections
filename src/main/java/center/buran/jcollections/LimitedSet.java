package center.buran.jcollections;

import java.util.Comparator;
import java.util.Objects;
import java.util.TreeSet;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Класс ограниченного упорядоченного множества
 *
 * @param <T> тип элементов очереди
 */
public class LimitedSet<T> extends TreeSet<T> {

    /**
     * максимальный размер множества
     */
    private final int maxSize;

    /**
     * Конструктор  ограниченного упорядоченного множества
     *
     * @param maxSize    максимальный размер множества
     * @param comparator оператор сравнения
     */
    public LimitedSet(int maxSize, Comparator<T> comparator) {
        super(Objects.requireNonNull(comparator));
        this.maxSize = maxSize;
    }

    /**
     * Метод добавления нового элемента в множество
     *
     * @param t новый элемент множества
     * @return флаг, получилось ли добавить новый элемент
     */
    @Override
    public synchronized boolean add(T t) {
        super.add(t);
        if (size() <= maxSize)
            return true;
        T removedElem = this.pollLast();
        return removedElem != t;
    }

    /**
     * Получить случайный элемент множества
     *
     * @return случайный элемент множества
     */
    public synchronized T getRandomElement() {
        int item = ThreadLocalRandom.current().nextInt(size()); // In real life, the Random object should be rather more shared than this
        int i = 0;
        for (T t : this) {
            if (i == item)
                return t;
            i++;
        }
        throw new AssertionError("error random element generating by number " + item);
    }

    /**
     * Строковое представление объекта вида:
     *
     * @return FIFOFixedLength{length}
     */
    @Override
    public String toString() {
        return "LimitedSet{" + maxSize + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        LimitedSet<?> that = (LimitedSet<?>) o;

        return maxSize == that.maxSize;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + maxSize;
        return result;
    }
}
