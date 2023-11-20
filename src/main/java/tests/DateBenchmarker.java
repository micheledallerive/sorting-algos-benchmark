package tests;

import algos.Sorter;
import java.util.Date;
import java.util.List;
import utils.ArrayGenerator;

/**
 * Benchmark the sorting algorithms with {@link Date} objects.
 */
public class DateBenchmarker extends Benchmarker<Date> {
    public DateBenchmarker() {
        super();
    }

    public DateBenchmarker(int[] sizes, ArrayGenerator.Type[] orders, List<Sorter<Date>> sorters,
                           boolean isWarm) {
        super(sizes, orders, sorters, isWarm);
    }

    /**
     * Map a long value to a Date object.
     *
     * @param val the long value to map
     * @return the Date object
     */
    @Override
    protected Date mapper(long val) {
        return new Date(val);
    }
}
