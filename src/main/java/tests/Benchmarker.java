package tests;

import algos.BubbleSortPassPerItem;
import algos.BubbleSortUntilNoChange;
import algos.BubbleSortWhileNeeded;
import algos.Sorter;
import java.util.List;
import java.util.function.Supplier;
import utils.ArrayGenerator;

/**
 * A class to benchmark sorting algorithms.
 *
 * @param <T> the type of the elements to sort
 */
public abstract class Benchmarker<T extends Comparable<T>> implements Experiment {
    static double SKIP_PERCENTAGE = 0.1; // skip the first 10% of the measurements

    private int[] sizes;
    private ArrayGenerator.Type[] orders;
    private List<Sorter<T>> sorters;
    private boolean isWarm;

    /**
     * Default constructor.
     */
    public Benchmarker() {
        this(new int[] {50, 100, 500, 1000, 5000, 10000, 50000, 100000},
            new ArrayGenerator.Type[] {ArrayGenerator.Type.RANDOM, ArrayGenerator.Type.SORTED,
                ArrayGenerator.Type.REVERSED},
            List.of(new BubbleSortPassPerItem<>(), new BubbleSortUntilNoChange<>(),
                new BubbleSortWhileNeeded<>()), false);
    }

    /**
     * Constructor.
     *
     * @param sizes   the sizes of the arrays to sort
     * @param orders  the orders of the arrays to sort
     * @param sorters the sorting algorithms to use
     * @param isWarm  whether to warm up the JVM
     */
    public Benchmarker(int[] sizes, ArrayGenerator.Type[] orders, List<Sorter<T>> sorters,
                       boolean isWarm) {
        this.sizes = sizes;
        this.orders = orders;
        this.sorters = sorters;
        this.isWarm = isWarm;
    }

    /**
     * Set the sizes of the arrays to sort.
     *
     * @param sizes the sizes of the arrays to sort
     * @return this object
     */
    public Benchmarker<T> setSizes(int[] sizes) {
        this.sizes = sizes;
        return this;
    }

    /**
     * Set the orders of the arrays to sort.
     *
     * @param orders the orders of the arrays to sort
     * @return this object
     */
    public Benchmarker<T> setOrders(ArrayGenerator.Type[] orders) {
        this.orders = orders;
        return this;
    }

    /**
     * Set the sorting algorithms to use.
     *
     * @param sorters the sorting algorithms to use
     * @return this object
     */
    public Benchmarker<T> setSorters(List<Sorter<T>> sorters) {
        this.sorters = sorters;
        return this;
    }

    /**
     * Set whether to warm up the JVM before running the experiment.
     *
     * @param isWarm whether to warm up the JVM
     * @return this object
     */
    public Benchmarker<T> setIsWarm(boolean isWarm) {
        this.isWarm = isWarm;
        return this;
    }

    /**
     * Measure the average time per element to sort the array.
     * The sorting operation is executed `iterations` times and the times are returned.
     *
     * @param sorter     the sorting algorithm to use
     * @param func       the function to generate the array to sort
     * @param length     the length of the array to sort
     * @param iterations the number of iterations to run
     * @return the array of times, in nanoseconds
     */
    public long[] measureSortTimes(Sorter<T> sorter, Supplier<T[]> func, int length,
                                   int iterations) {
        final double iterations_skip = iterations * SKIP_PERCENTAGE;
        long[] values = new long[iterations];
        for (int i = 0; i < iterations + iterations_skip; ++i) {
            T[] array = func.get();
            long startTime = System.nanoTime();
            sorter.sort(array);
            long endTime = System.nanoTime();
            long delta = endTime - startTime;
            if (i >= iterations_skip) {
                values[i - (int) iterations_skip] = delta;
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
            () -> generateArray(ArrayGenerator.Type.RANDOM, arraySize), arraySize, 10);
//        System.out.println("Warmup done.");
    }

    /**
     * Map a long value to a T value.
     * <p>
     * Every sub-class of Benchmarker should implement this method. This method is used to
     * generate the input array for the sorting algorithms.
     *
     * @param val the long value to map
     * @return the mapped T value, representing `val` as a T value
     */
    protected abstract T mapper(long val);

    /**
     * Generate an array of T values.
     *
     * @param order the order of the array
     * @param size  the size of the array
     * @return the generated array
     */
    public T[] generateArray(ArrayGenerator.Type order, int size) {
        return new ArrayGenerator<T>(order, size).build(this::mapper);
    }

    /**
     * Benchmark the sorting algorithms with the given input.
     */
    public void benchmark() {

        if (!isWarm) {
            warmup(); // Warm up the JVM
        }

        for (int size : sizes) {

            // The number of iterations depends on the size of the array, because sorting a
            // larger array takes more time, and smaller array are more likely to be influenced
            // by external factors.
            final int iterations = size <= 1000 ? 2000 : size <= 10000 ? 200 : 20;

            for (ArrayGenerator.Type order : orders) {
                for (Sorter<T> sorter : sorters) {
                    long[] times =
                        measureSortTimes(sorter, () -> this.generateArray(order, size), size,
                            iterations);
                    for (var time : times) {
                        System.out.printf("%s,%s,%s,%s%n", size, order,
                            sorter.getClass().getSimpleName(), time);
                    }
                }
            }
        }
    }

    /**
     * Run the experiment.
     */
    public void run() {
        benchmark();
    }
}