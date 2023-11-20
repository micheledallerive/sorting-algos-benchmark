package tests;

import algos.BubbleSortPassPerItem;
import algos.BubbleSortUntilNoChange;
import algos.BubbleSortWhileNeeded;
import algos.Sorter;
import java.util.Date;
import java.util.List;
import utils.ArrayGenerator;

public class TypeRatioTest implements Experiment {
    private static double average(long[] arr) {
        double sum = 0;
        for (long d : arr) {
            sum += d;
        }
        return sum / arr.length;
    }

    @Override
    public void run() {
        var ib = new IntegerBenchmarker();
        var sb = new StringBenchmarker();
        var db = new DateBenchmarker();

        List<Sorter<Integer>> isorters =
            List.of(new BubbleSortPassPerItem<>(), new BubbleSortUntilNoChange<>(),
                new BubbleSortWhileNeeded<>());
        List<Sorter<String>> ssorters =
            List.of(new BubbleSortPassPerItem<>(), new BubbleSortUntilNoChange<>(),
                new BubbleSortWhileNeeded<>());
        List<Sorter<Date>> dsorters =
            List.of(new BubbleSortPassPerItem<>(), new BubbleSortUntilNoChange<>(),
                new BubbleSortWhileNeeded<>());

        final int size = 5000;
        final int iterations = 1000;
        final var order = ArrayGenerator.Type.RANDOM;

        double[] iavgs = new double[3];
        double[] savgs = new double[3];
        double[] davgs = new double[3];
        for (int i = 0; i < 3; ++i) {
            long[] results;
            results =
                db.measureSortTimes(dsorters.get(i), () -> db.generateArray(order, size), size,
                    iterations);
            davgs[i] = average(results);

//            results = sb.measureSortTimes(ssorters.get(i),
//                () -> sb.generateArray(order, size), size, iterations);
//            savgs[i] = average(results);

            results =
                ib.measureSortTimes(isorters.get(i), () -> ib.generateArray(order, size), size,
                    iterations);
            iavgs[i] = average(results);

        }

        for (int i = 0; i < 3; ++i) {
            System.out.println("Integer results");
            double ratio = iavgs[i] / iavgs[0];
            System.out.println("Sorter " + i + " is " + ratio + " times slower than sorter 0");
        }

//        for (int i = 0; i < 3; ++i) {
//            System.out.println("String results");
//            double ratio = savgs[i] / savgs[0];
//            System.out.println("Sorter " + i + " is " + ratio + " times slower than sorter 0");
//        }
//
        for (int i = 0; i < 3; ++i) {
            System.out.println("Double results");
            double ratio = davgs[i] / davgs[0];
            System.out.println("Sorter " + i + " is " + ratio + " times slower than sorter 0");
        }
    }
}
