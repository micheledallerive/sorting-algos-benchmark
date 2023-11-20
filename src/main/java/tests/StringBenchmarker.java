package tests;

import algos.Sorter;
import java.util.List;
import utils.ArrayGenerator;

/**
 * Benchmark the sorting algorithms with {@link String} objects.
 */
public class StringBenchmarker extends Benchmarker<String> {
    static int STRING_LENGTH = 6;

    public StringBenchmarker() {
        super();
    }

    public StringBenchmarker(int[] sizes, ArrayGenerator.Type[] orders,
                             List<Sorter<String>> sorters, boolean isWarm) {
        super(sizes, orders, sorters, isWarm);
    }

    /**
     * Map a long value to a String object.
     * The strings have a fixed length of {@link #STRING_LENGTH}.
     * For a sorted array, the strings are in lexicographical order, i.e. "aaaaaa", "aaaaab", etc.
     *
     * @param val the long value to map
     * @return the String object
     */
    @Override
    protected String mapper(long val) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < STRING_LENGTH; ++i) {
            sb.append((char) ('a' + (val % 26)));
            val /= 26;
        }
        sb.reverse();
        return sb.toString();
    }
}
