package center.buran.jcollections;



import java.math.BigInteger;
import java.util.function.Consumer;

/**
 * Класс асинхронных методов
 */
public class Async {

    /**
     * Параллельный перебор числа i
     *
     * @param n        кол-во шагов цикла
     * @param loopStep консумер, к отором описано, что делать с каждым значение счётчика цикла
     */
    public static void parallelForEach(int n, Consumer<Integer> loopStep) {
        int threadCnt = Runtime.getRuntime().availableProcessors();
        Thread[] threads = new Thread[threadCnt];
        // заполняем массив предрассчитанных значений
        for (int i = 0; i < threadCnt; i++) {
            int step = n / threadCnt;
            int finalI = i;
            threads[i] = new Thread(() -> {
                for (int j = step * finalI; j < (finalI == threadCnt - 1 ? n : step * (finalI + 1)); j++)
                    loopStep.accept(j);
            });
        }
        // выполняем потоки
        for (Thread thread : threads)
            thread.start();
        for (Thread thread : threads)
            try {
                thread.join();
                //   thread2.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    }

    /**
     * Параллельный перебор числа i
     *
     * @param n        кол-во шагов цикла
     * @param loopStep консумер, к отором описано, что делать с каждым значение счётчика цикла
     */
    public static void parallelForEach(BigInteger n, Consumer<BigInteger> loopStep) {
        int threadCnt = Runtime.getRuntime().availableProcessors();
        BigInteger step = n.divide(BigInteger.valueOf(threadCnt));
        // заполняем массив предрассчитанных значений
        Thread[] threads = new Thread[threadCnt];
        for (int i = 0; i < threadCnt; i++) {
            int finalI = i;
            threads[i] = new Thread(() -> {
                for (
                        BigInteger j = step.multiply(BigInteger.valueOf(finalI));
                        j.compareTo(finalI == threadCnt - 1 ? n : step.multiply(BigInteger.valueOf(finalI + 1))) < 0;
                        j = j.add(BigInteger.ONE)
                )
                    loopStep.accept(j);
            });
        }
        // выполняем потоки
        for (Thread thread : threads)
            thread.start();
        for (Thread thread : threads)
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    }

    /**
     * Конструктор для запрета наследования
     */
    private Async() {
        // Подавление создания конструктора по умолчанию
        // для достижения неинстанцируемости
        throw new AssertionError("constructor is disabled");
    }
}