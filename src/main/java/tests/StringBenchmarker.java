package tests;

public class StringBenchmarker extends Benchmarker<String> {
    @Override
    protected String mapper(long val) {
        int stringLength = 4; // enough for 26^4 = ~400k strings
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < stringLength; ++i) {
            sb.append((char) ('a' + (val % 26)));
            val /= 26;
        }
        return sb.toString();
    }
}
