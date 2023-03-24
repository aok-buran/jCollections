package center.buran.jcollections;


import java.math.BigInteger;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Атомарный BigInteger
 */
public final class AtomicBigInteger {
    /**
     * ссылка на BigInteger
     */
    private final AtomicReference<BigInteger> valueHolder = new AtomicReference<>();

    /**
     * Конструктор атомарного BigInteger
     *
     * @param bigInteger расширенное целое число
     */
    public AtomicBigInteger(BigInteger bigInteger) {
        valueHolder.set(bigInteger);
    }

    /**
     * Увеличить и получить
     *
     * @return увеличенное на 1 значение
     */
    public synchronized BigInteger incrementAndGet() {
        for (; ; ) {
            BigInteger current = valueHolder.get();
            BigInteger next = current.add(BigInteger.ONE);
            if (valueHolder.compareAndSet(current, next)) {
                return next;
            }
        }
    }

    /**
     * Получить значение
     *
     * @return значение
     */
    public synchronized BigInteger get() {
        return valueHolder.get();
    }

    /**
     * Увеличить текущее значение на 1
     */
    public synchronized void increment() {
        BigInteger current = valueHolder.get();
        BigInteger next = current.add(BigInteger.ONE);
        valueHolder.set(next);
    }

    /**
     * Задать текущее значение
     *
     * @param integer новое значение
     */
    public synchronized void set(BigInteger integer) {
        valueHolder.set(integer);
    }

    @Override
    public String toString() {
        return valueHolder.get().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AtomicBigInteger that = (AtomicBigInteger) o;
        return Objects.equals(valueHolder.get(), that.valueHolder.get());
    }

    @Override
    public int hashCode() {
        return Objects.hash(valueHolder.get());
    }
}
