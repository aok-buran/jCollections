package center.buran.jcollections;

import center.buran.jmath.coordinateSystem.CoordinateSystem2i;
import center.buran.jmath.vector.Vector2d;
import center.buran.jmath.vector.Vector2i;
import com.sun.istack.NotNull;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.passay.WhitespaceRule.ERROR_CODE;

/**
 * Вспомогательный класс алгоритмов
 */
public class Algorithms {

    /**
     * Получить значение в точке {x,y} по значениям в 4 точках квадрата(билинейная интерполяция)
     * <a href="https://ru.wikipedia.org/wiki/Bilinear_interpolation">Подробнее</a>
     *
     * @param x1 x координата левой нижней точки квадрата
     * @param y1 y координата левой нижней  точки квадрата
     * @param x2 x координата правой верхней точки квадрата
     * @param y2 y координата правой верхней точки квадрата
     * @param vA значение в точке A
     * @param vB значение в точке B
     * @param vC значение в точке C
     * @param vD значение в точке D
     * @param p  координаты точки, значение в которой нужно получить
     * @return значение в заданной точке
     */
    public static double bilinearInterpolation(
            double x1, double y1, double x2, double y2,
            double vA, double vB, double vC, double vD,
            Vector2d p
    ) {
        return vA * (x2 - p.x) * (y2 - p.y) / ((x2 - x1) * (y2 - y1)) +
                vB * (p.x - x1) * (y2 - p.y) / ((x2 - x1) * (y2 - y1)) +
                vC * (p.x - x1) * (p.y - y1) / ((x2 - x1) * (y2 - y1)) +
                vD * (x2 - p.x) * (p.y - y1) / ((x2 - x1) * (y2 - y1));
    }

    /**
     * Проверить, что в файлах содержится одинаковые json-структуры
     *
     * @param obj1 первый JSON-объект
     * @param obj2 второй JSON-объект
     * @param log  нужно ли выводить лог сравнения
     * @return флаг, совпадают ли json-ситруктуры в указанных файлах
     */
    public static boolean sameJSONContent(@NotNull Object obj1, @NotNull Object obj2, boolean log) {
        if (obj1 instanceof JSONArray) {
            if (((JSONArray) obj1).size() != ((JSONArray) obj2).size()) {
                if (log) {
                    System.out.println(
                            "arr " + " has different sizes: " +
                                    ((JSONArray) obj1).size() + " " +
                                    ((JSONArray) obj2).size()
                    );
                    System.out.println(" obj1: " + obj1);
                    System.out.println(" obj2: " + obj2);
                }
                return false;
            } else {
                for (Object elem1 : ((JSONArray) obj1)) {
                    boolean flgFindElem = false;
                    for (Object elem2 : ((JSONArray) obj2)) {
                        if (sameJSONContent(elem1, elem2, false)) {
                            flgFindElem = true;
                            break;
                        }
                    }
                    if (!flgFindElem) {
                        System.out.println("can not find in second arr " + elem1);
                        return false;
                    }
                }
                for (Object elem2 : (JSONArray) (obj2)) {
                    boolean flgFindElem = false;
                    for (Object elem1 : ((JSONArray) obj1)) {
                        if (sameJSONContent(elem2, elem1, false)) {
                            flgFindElem = true;
                            break;
                        }
                    }
                    if (!flgFindElem) {
                        System.out.println("can not find in first arr " + elem2);
                        return false;
                    }
                }
                return true;
            }
        } else if (obj1 instanceof JSONObject) {
            if (!obj1.equals(obj2)) {
                Set obj1KeySet = new HashSet(((JSONObject) obj1).keySet());
                Set obj2KeySet = new HashSet(((JSONObject) obj2).keySet());
                obj1KeySet.removeAll(obj2KeySet);

                if (!obj1KeySet.isEmpty()) {
                    if (log)
                        System.out.println("can not find keys in json out " + obj1KeySet);
                    return false;
                }
                obj1KeySet = new HashSet(((JSONObject) obj1).keySet());
                obj2KeySet = new HashSet(((JSONObject) obj2).keySet());
                obj2KeySet.removeAll(obj1KeySet);
                if (!obj2KeySet.isEmpty()) {
                    if (log)
                        System.out.println("can not find keys in json in " + obj2KeySet);
                    return false;
                }

                for (Object key : ((JSONObject) obj1).keySet()) {
                    Object valueFromKey1 = ((JSONObject) obj1).get(key);
                    Object valueFromKey2 = ((JSONObject) obj2).get(key);
                    if (!sameJSONContent(valueFromKey1, valueFromKey2, true)) {
                        if (log) {
                            System.out.println("different values in key " + key + ": ");
                            System.out.println(" obj1: " + valueFromKey1);
                            System.out.println(" obj2: " + valueFromKey2);
                        }
                        return false;
                    }
                }
                return true;
            }
        } else {
            if (!obj1.equals(obj2)) {
                if (log) {
                    System.out.println("different values: ");
                    System.out.println(" obj1: " + obj1);
                    System.out.println(" obj2: " + obj2);
                }
                return false;
            }
            return true;
        }
        return true;
    }

    /**
     * Проверить, что в файлах содержится одинаковые json-структуры
     *
     * @param file1 путь к первому файлу
     * @param file2 путь к второму файлу
     * @return флаг, совпадают ли json-ситруктуры в указанных файлах
     */
    public static boolean sameJSONContent(@NotNull String file1, @NotNull String file2) {
        try (
                InputStreamReader isr1 = new InputStreamReader(new FileInputStream(file1));
                InputStreamReader isr2 = new InputStreamReader(new FileInputStream(file2))
        ) {
            JSONObject obj1 = (JSONObject) new JSONParser().parse(isr1);
            JSONObject obj2 = (JSONObject) new JSONParser().parse(isr2);
            return sameJSONContent(obj1, obj2, true);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Объединить список массивов вещественных чисел в один
     *
     * @param arrays список массивов вещественных чисел
     * @return объединённый массив вещественных чисел
     */
    @NotNull
    public static double[] concatenate(double[]... arrays) {
        double[] result = new double[Arrays.stream(arrays).mapToInt(array -> array.length).sum()];
        int destPos = 0;
        for (double[] arr : arrays) {
            System.arraycopy(arr, 0, result, destPos, arr.length);
            destPos += arr.length;
        }
        return result;
    }

    /**
     * Объединить список двумерных массивов вещественных чисел в один
     *
     * @param arrays список двумерных массивов вещественных чисел
     * @return объединённый двумерных массив вещественных чисел
     */
    @NotNull
    public static double[][] concatenate(double[][]... arrays) {
        double[][] result = new double[Arrays.stream(arrays).mapToInt(array -> array.length).sum()][];
        int destPos = 0;
        for (double[][] arr : arrays) {
            System.arraycopy(arr, 0, result, destPos, arr.length);
            destPos += arr.length;
        }
        return result;
    }

    /**
     * Проверка на равенство двух двумерных массивов вещественных чисел
     *
     * @param arr1 первый двумерный массив вещественных чисел
     * @param arr2 второй двумерный ммассив вещественных чисел
     * @return флаг, равны ли два двумерных массива вещественных чисел
     */
    public static boolean isEqual(@NotNull final double[][] arr1, @NotNull final double[][] arr2) {
        if (arr1 == null) {
            return (arr2 == null);
        }
        if (arr2 == null) {
            return false;
        }
        if (arr1.length != arr2.length) {
            return false;
        }
        for (int i = 0; i < arr1.length; i++) {
            if (!Arrays.equals(arr1[i], arr2[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * Проверка, что два файла одинановые
     *
     * @param path1 путь к первому файлу
     * @param path2 путь ко второму файлу
     * @return совпдают ли  два файла
     */
    public static boolean areFilesEqual(@NotNull String path1, @NotNull String path2) {
        try (Stream<String> lines1 = Files.lines(Paths.get(path1));
             Stream<String> lines2 = Files.lines(Paths.get(path2))
        ) {
            List<String> list1 = lines1.collect(toList());
            List<String> list2 = lines2.collect(toList());
            if (list1.size() != list2.size()) {
                System.out.println("list1 has size " + list1.size() + " list2 " + list2.size());
                return false;
            }
            return list1.containsAll(list2) && list2.containsAll(list1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * Объединить список массивов строк в один
     *
     * @param arrays список массивов строк
     * @return объединённый массив строк
     */
    @NotNull
    public static String[] concatenate(@NotNull String[]... arrays) {
        String[] result = new String[Arrays.stream(arrays).mapToInt(array -> array.length).sum()];
        int destPos = 0;
        for (String[] arr : arrays) {
            System.arraycopy(arr, 0, result, destPos, arr.length);
            destPos += arr.length;
        }
        return result;
    }

    /**
     * Объединить список двумерных массивов строк в один
     *
     * @param arrays список двумерных массивов строк
     * @return объединённый двумерных массив строк
     */
    @NotNull
    public static String[][] concatenate(@NotNull String[][]... arrays) {
        String[][] result = new String[Arrays.stream(arrays).mapToInt(array -> array.length).sum()][];
        int destPos = 0;
        for (String[][] arr : arrays) {
            System.arraycopy(arr, 0, result, destPos, arr.length);
            destPos += arr.length;
        }
        return result;
    }

    /**
     * Проверка на равенство двух двумерных массивов строк
     *
     * @param arr1 первый двумерный массив строк
     * @param arr2 второй двумерный ммассив строк
     * @return флаг, равны ли два двумерных массива строк
     */
    public static boolean isEqual(@NotNull final String[][] arr1, @NotNull final String[][] arr2) {
        if (arr1 == null) {
            return (arr2 == null);
        }
        if (arr2 == null) {
            return false;
        }
        if (arr1.length != arr2.length) {
            return false;
        }
        for (int i = 0; i < arr1.length; i++) {
            if (!Arrays.equals(arr1[i], arr2[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * Сгенерировать случайную строку
     *
     * @param lowerCaseCharactersCnt количество символов-букв нижнего регистра
     * @param upperCaseCharactersCnt количество символов-букв верхнего регистра
     * @param digitCharactersCnt     количество символов-цифр
     * @param specialCharactersCnt   количество специальных символов
     * @return случайная строка
     */
    public static String generateRandomString(
            int lowerCaseCharactersCnt, int upperCaseCharactersCnt, int digitCharactersCnt, int specialCharactersCnt
    ) {
        PasswordGenerator gen = new PasswordGenerator();
        CharacterData lowerCaseChars = EnglishCharacterData.LowerCase;
        CharacterRule lowerCaseRule = new CharacterRule(lowerCaseChars);
        lowerCaseRule.setNumberOfCharacters(lowerCaseCharactersCnt);

        CharacterData upperCaseChars = EnglishCharacterData.UpperCase;
        CharacterRule upperCaseRule = new CharacterRule(upperCaseChars);
        upperCaseRule.setNumberOfCharacters(upperCaseCharactersCnt);

        CharacterData digitChars = EnglishCharacterData.Digit;
        CharacterRule digitRule = new CharacterRule(digitChars);
        digitRule.setNumberOfCharacters(digitCharactersCnt);

        if (specialCharactersCnt == 0) {
            return gen.generatePassword(
                    lowerCaseCharactersCnt + upperCaseCharactersCnt + digitCharactersCnt, lowerCaseRule,
                    upperCaseRule, digitRule
            );
        } else {
            CharacterData specialChars = new CharacterData() {
                public String getErrorCode() {
                    return ERROR_CODE;
                }

                public String getCharacters() {
                    return "!@#$%^&*()_+";
                }
            };

            CharacterRule splCharRule = new CharacterRule(specialChars);
            splCharRule.setNumberOfCharacters(specialCharactersCnt);

            return gen.generatePassword(
                    lowerCaseCharactersCnt + upperCaseCharactersCnt + digitCharactersCnt + specialCharactersCnt,
                    splCharRule, lowerCaseRule, upperCaseRule, digitRule
            );
        }

    }

    /**
     * Сгенерировать случайную строку с заданным одинаковым количеством символов-букв нижнего регистра,
     * символов-букв верхнего регистра, символов-цифр и специальных символов
     *
     * @param cnt заданное количество символов каждого типа
     * @return случайная строка
     */
    public static String generateRandomString(int cnt) {
        return generateRandomString(
                cnt, cnt, cnt, 0
        );
    }

    /**
     * Сгенерировать случайную строку
     *
     * @return случайная строка
     */
    @NotNull
    public static String generateRandomString() {
        return generateRandomString(5);
    }

    /**
     * Получить индекс минимального элемента отсортированного массива, который больше или равен заданного числа
     *
     * @param arr   отсортированный массив
     * @param value заданное число
     * @return возвращает первый индекс элемента отсортированного массива, если в массиве есть несколько одинаковых
     * элементов, больших или равных заданному,то возвращается один, случайно выбранный из них
     */
    public static int findCumulativeValue(@NotNull float[] arr, float value) {
        // если значение больше максимального элемента
        if (value >= arr[arr.length - 1])
            return arr.length - 1;
        // если значение меньше минимального элемента
        if (value <= arr[0])
            return 0;
        //  изначальныйдиапазон перебора
        int left = 0;
        int right = arr.length - 1;
        // ответ изначально равен -1
        int ans = -1;
        // пока левая граница не перешла правую
        while (left <= right) {
            // получаем середину рассматриваемого диапазона
            int mid = (left + right) / 2;
            // если значение найдено
            if (arr[mid] == value) {
                ans = mid;
                break;
            } else
                // Смещаем левую границу, если значение по середине интервала меньше заданного
                if (arr[mid] > value) {
                    ans = mid;
                    right = mid - 1;
                }
                // Смещаем правую границу
                else {
                    left = mid + 1;
                }
        }
        // получаем диапазон индексов с элементами, равными ans
        int lAns = ans;
        int rAns = ans;
        while (lAns > 0 && arr[lAns - 1] == arr[ans]) lAns--;
        while (rAns < arr.length - 1 && arr[rAns + 1] == arr[ans]) rAns++;
        // если таких элементов несколько, выбираем из них случайный
        if (lAns != rAns) {
            ans = ThreadLocalRandom.current().nextInt(lAns, rAns);
        }
        return ans;
    }


    /**
     * Возвращает множество уникальных целочисленных значений из заданного диапазона
     *
     * @param minValue минимальное значение
     * @param maxValue максимальное значение
     * @param setSize  размер множеста
     * @return множество уникальных значений
     */
    @NotNull
    public static Set<Integer> getUniqueOrderedIntegerSet(int minValue, int maxValue, int setSize) {
        // рассчитываем размер интервала
        int range = maxValue - minValue + 1;
        // если интервал меньше количества элементов множества
        if (range < setSize) {
            throw new AssertionError(
                    "getUniqueSet: range " + maxValue + ":" + minValue + "=" + range +
                            " is less than set size " + setSize
            );
        }
        // если кол-во элементов множества больше половины интервала
        if (setSize > range / 2) {
            // создаём множество со всеми элементами
            TreeSet<Integer> resultSet = new TreeSet<>();
            for (int i = minValue; i <= maxValue; i++) {
                resultSet.add(i);
            }
            // получаем элементы, которые надо из него удалить
            Set<Integer> inverseSet = getUniqueOrderedIntegerSet(minValue, maxValue, range - setSize);
            // удаляем
            resultSet.removeAll(inverseSet);
            return resultSet;
        }
        TreeSet<Integer> uniqueSet = new TreeSet<>();
        // пока в множестве  меньше элементов, чем нам нужно
        while (uniqueSet.size() < setSize)
            // добавляем новые
            uniqueSet.add(ThreadLocalRandom.current().nextInt(minValue, maxValue));
        return uniqueSet;
    }

    /**
     * Возвращает неупорядоченный список уникальных целочисленных значений из заданного диапазона
     *
     * @param minValue минимальное значение
     * @param maxValue максимальное значение
     * @param listSize размер множеста
     * @return множество уникальных значений
     */
    @NotNull
    public static List<Integer> getUniqueUnorderedIntegerList(int minValue, int maxValue, int listSize) {
        // рассчитываем размер интервала
        int range = maxValue - minValue + 1;
        // если интервал меньше количества элементов списка
        if (range < listSize) {
            throw new AssertionError(
                    "getUniqueSet: range " + maxValue + ":" + minValue + "=" + range +
                            " is less than set size " + listSize
            );
        }
        // если кол-во элементов списка больше половины интервала
        if (listSize > range / 2) {
            // создаём множество со всеми элементами
            List<Integer> resultSet = new LinkedList<>();
            for (int i = minValue; i <= maxValue; i++) {
                resultSet.add(i);
            }
            // получаем элементы, которые надо из него удалить
            List<Integer> inverseSet = getUniqueUnorderedIntegerList(minValue, maxValue, range - listSize);
            // удаляем
            resultSet.removeAll(inverseSet);
            // перемешиваем список
            Collections.shuffle(resultSet);
            return resultSet;
        }
        LinkedList<Integer> uniqueList = new LinkedList<>();
        // пока в множестве  меньше элементов, чем нам нужно
        while (uniqueList.size() < listSize) {
            // новое значение
            int newValue = ThreadLocalRandom.current().nextInt(minValue, maxValue);
            // если его ещё нет в списке
            if (!uniqueList.contains(newValue))
                // добавляем
                uniqueList.add(newValue);
        }
        return uniqueList;
    }

    /**
     * Возвращает множество уникальных значений двумерноговектора из заданного диапазона
     *
     * @param cs          система координат
     * @param density     плотность значений
     * @param checkCoords предикат для проеврки, подходят ли значения
     * @return множество уникальных значений
     */
    @NotNull
    public static Set<Vector2i> getUniqueVector2iSet(
            @NotNull CoordinateSystem2i cs, double density, @NotNull Predicate<Vector2i> checkCoords
    ) {
        if (density > 0.5) {
            Set<Vector2i> res = new HashSet<>();
            for (int i = cs.getMin().x; i <= cs.getMax().x; i++)
                for (int j = cs.getMin().y; j <= cs.getMax().y; j++) {
                    Vector2i coords = new Vector2i(i, j);
                    if (checkCoords.test(coords))
                        res.add(coords);
                }
            res.removeAll(getUniqueVector2iSet(cs, 1 - density, checkCoords));
            return res;
        }

        int checkedCount = 0;
        for (int i = cs.getMin().x; i <= cs.getMax().x; i++)
            for (int j = cs.getMin().y; j <= cs.getMax().y; j++) {
                Vector2i coords = new Vector2i(i, j);
                if (checkCoords.test(coords))
                    checkedCount++;
            }
        int setSize = (int) (density * checkedCount);
        Set<Vector2i> uniqueList = new HashSet<>();
        while (uniqueList.size() < setSize) {
            Vector2i coords = cs.getRandomCoords();
            if (!uniqueList.contains(coords) && checkCoords.test(coords))
                // добавляем новые
                uniqueList.add(coords);
        }
        return uniqueList;
    }

    /**
     * Получить текущее время в миллисекундах
     *
     * @return текущее время в миллисекундах
     */
    public static long getTimeInMillis() {
        return System.currentTimeMillis();
    }


    /**
     * Конструктор для запрета наследования
     */
    private Algorithms() {
        // Подавление создания конструктора по умолчанию
        // для достижения неинстанцируемости
        throw new AssertionError("constructor is disabled");
    }
}
