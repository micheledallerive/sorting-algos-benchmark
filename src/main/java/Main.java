import algos.BubbleSortPassPerItem;
import algos.BubbleSortUntilNoChange;
import algos.BubbleSortWhileNeeded;
import algos.Sorter;
import java.util.List;
import java.util.function.Function;
import tests.IntegerBenchmarker;
import utils.ArrayGenerator;

public class Main {

    public static void main(String[] args) {
        new IntegerBenchmarker().benchmark();
    }
}