package tests;

public class IntegerBenchmarker extends Benchmarker<Integer> {
    @Override
    protected Integer mapper(long val) {
        return Math.toIntExact(val);
    }
}
