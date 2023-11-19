package tests;

import algos.BubbleSortPassPerItem;
import algos.BubbleSortUntilNoChange;
import algos.BubbleSortWhileNeeded;
import algos.Sorter;
import java.util.List;
import java.util.function.Function;
import utils.ArrayGenerator;

public abstract class Benchmarker<T extends Comparable<T>> {
    static int DEFAULT_ITERATIONS = 200;
    static double SKIP_PERCENTAGE = 0.1; // skip the first 10% of the measurements

    /**
     * Measure the average time per element to sort the array.
     * The sorting operation is executed `iterations` times, and the average time is returned.
     *
     * @param sorter the sorting algorithm to use
     * @param func   the function to generate the array to sort
     * @return the average time per element to sort the array
     */
    private double[] measureSortTimes(Sorter<T> sorter, Function<Void, T[]> func, int length,
                                      int iterations) {
        final double iterations_skip = iterations * SKIP_PERCENTAGE;
        double[] values = new double[iterations];
        for (int i = 0; i < iterations + iterations_skip; ++i) {
            T[] array = func.apply(null);
            long startTime = System.nanoTime();
            sorter.sort(array);
            long endTime = System.nanoTime();
            long delta = endTime - startTime;
            if (i >= iterations_skip) {
                values[i - (int) iterations_skip] = (double) delta / length;
            }
        }
        return values;
    }

    /**
     * Warms up the JVM by executing the sorting algorithms once. This measurement is not
     * counted in the final results.
     */
    private void warmup() {
        int arraySize = 10000;
        measureSortTimes(new BubbleSortPassPerItem<>(),
            __ -> new ArrayGenerator<T>(ArrayGenerator.Type.RANDOM,
                arraySize).build(this::mapper), arraySize, 10);
//        System.out.println("Warmup done.");
    }

    protected abstract T mapper(long val);

    public void benchmark() {
        int[] sizes = {50, 100, 500, 1000, 5000, 10000, 50000, 100000};

        ArrayGenerator.Type[] orders =
            {ArrayGenerator.Type.RANDOM, ArrayGenerator.Type.SORTED, ArrayGenerator.Type.REVERSED};

        List<Sorter<T>> sorters =
            List.of(new BubbleSortPassPerItem<>(), new BubbleSortUntilNoChange<>(),
                new BubbleSortWhileNeeded<>());

        warmup(); // Warm up the JVM


        for (int size : sizes) {
            final int iterations = size <= 1000 ? 2000 : size <= 10000 ? DEFAULT_ITERATIONS : 20;
            for (ArrayGenerator.Type order : orders) {
                for (Sorter<T> sorter : sorters) {
                    double[] times = measureSortTimes(sorter,
                        __ -> new ArrayGenerator<T>(order, size).build(this::mapper), size,
                        iterations);
                    for (double time : times) {
                        System.out.printf("%s,%s,%s,%s%n", size, order,
                            sorter.getClass().getSimpleName(), time);
                    }
                }
            }
        }
    }
}