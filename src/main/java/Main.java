import algos.BubbleSortPassPerItem;
import algos.BubbleSortUntilNoChange;
import algos.BubbleSortWhileNeeded;
import algos.Sorter;
import java.util.List;
import java.util.function.Function;
import utils.ArrayGenerator;

public class Main {

    static int DEFAULT_ITERATIONS = 200;

    /**
     * Measure the average time per element to sort the array.
     * The sorting operation is executed `iterations` times, and the average time is returned.
     *
     * @param sorter the sorting algorithm to use
     * @param func   the function to generate the array to sort
     * @param <T>    the type of elements in the array
     * @return the average time per element to sort the array
     */
    private static <T extends Comparable<T>> double[] measureSortTimes(Sorter<T> sorter,
                                                                       Function<Void, T[]> func,
                                                                       int length, int iterations) {

        double[] values = new double[iterations];
        for (int i = 0; i < iterations; ++i) {
            T[] array = func.apply(null);
            long startTime = System.nanoTime();
            sorter.sort(array);
            long endTime = System.nanoTime();
            long delta = endTime - startTime;
            values[i] = ((double) delta / length);
        }
        return values;
    }

    public static void main(String[] args) {

        int[] sizes = {50, 100, 500, 1000, 5000, 10000, 50000, 100000};

        ArrayGenerator.Type[] orders =
            {ArrayGenerator.Type.RANDOM, ArrayGenerator.Type.SORTED, ArrayGenerator.Type.REVERSED};

        List<Sorter<Integer>> sorters =
            List.of(new BubbleSortPassPerItem<>(), new BubbleSortUntilNoChange<>(),
                new BubbleSortWhileNeeded<>());

        warmup(); // Warm up the JVM

        for (int size : sizes) {
            for (ArrayGenerator.Type order : orders) {
                for (Sorter<Integer> sorter : sorters) {
                    double[] times = measureSortTimes(sorter,
                        __ -> new ArrayGenerator<Integer>(order, size).build(Math::toIntExact),
                        size, DEFAULT_ITERATIONS);
                    for (double time : times) {
                        System.out.printf("%s,%s,%s,%s%n", size, order,
                            sorter.getClass().getSimpleName(), time);
                    }
                }
            }
        }
    }

    /**
     * Warms up the JVM by executing the sorting algorithms once. This measurement is not
     * counted in the final results.
     */
    private static void warmup() {
        int arraySize = 10000;
        Main.measureSortTimes(new BubbleSortPassPerItem<>(),
            (Function<Void, Integer[]>) __ -> new ArrayGenerator<Integer>(
                ArrayGenerator.Type.RANDOM, arraySize).build(Math::toIntExact), arraySize, 10);
//        System.out.println("Warmup done.");
    }
}