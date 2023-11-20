package tests;

import algos.Sorter;
import java.util.List;
import utils.ArrayGenerator;

/**
 * Benchmark the sorting algorithms with {@link Integer} objects.
 */
public class IntegerBenchmarker extends Benchmarker<Integer> {
    public IntegerBenchmarker() {
        super();
    }

    public IntegerBenchmarker(int[] sizes, ArrayGenerator.Type[] orders, List<Sorter<Integer>> sorters,
                              boolean isWarm) {
        super(sizes, orders, sorters, isWarm);
    }

    /**
     * Map a long value to an Integer object.
     *
     * @param val the long value to map
     * @return the Integer object
     */
    @Override
    protected Integer mapper(long val) {
        return Math.toIntExact(val);
    }
}
