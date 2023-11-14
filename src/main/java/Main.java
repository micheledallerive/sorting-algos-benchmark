import algos.BubbleSortPassPerItem;
import algos.BubbleSortUntilNoChange;
import algos.BubbleSortWhileNeeded;
import algos.Sorter;
import utils.ArrayGenerator;
import java.util.List;

public class Main {

    static void MeasureSortTime(Sorter sorter, Byte[] array) {
        long startTime = System.nanoTime();
        sorter.sort(array);
        System.out.println(System.nanoTime() - startTime);
    }

    public static void main(String[] args) {

        // new utils.ArrayGenerator<byte>(utils.ArrayGenerator.Type.RANDOM, 1000000).build();

        int[] sizes = {1000, 10000, 1000000};
        ArrayGenerator.Type[] orders = {ArrayGenerator.Type.RANDOM, ArrayGenerator.Type.SORTED, ArrayGenerator.Type.REVERSED};
        List<Sorter<Byte>> sorters = List.of();

        for (int size : sizes) {
            for (ArrayGenerator.Type order : orders) {
                for (Sorter sorter : sorters) {
                    MeasureSortTime(sorter, new ArrayGenerator<Byte>(order, size).build());            }

            }
        }



    }
}