package utils;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Utility class to generate arrays of a generic type T.
 *
 * @param <T> the type of the elements of the array
 */
public class ArrayGenerator<T> {
    private static final Random random = new Random();
    private final Type arrayType;
    private final int size;

    public ArrayGenerator(Type type, int size) {
        this.arrayType = type;
        this.size = size;
    }

    public T[] build(Mapper<T> mapping) {
        switch (arrayType) {
            case RANDOM:
                return this.buildRandom(mapping);
            case SORTED:
                return this.buildSorted(mapping);
            case REVERSED:
                final T[] array = this.buildSorted(mapping);
                for (int i = 0, j = array.length - 1; i < j; i++, j--) {
                    T tmp = array[i];
                    array[i] = array[j];
                    array[j] = tmp;
                }
                return array;
            default:
                throw new IllegalArgumentException("The array type does not exist!");
        }
    }

    /**
     * Generate a random list of values of type T, using the provided mapping function.
     *
     * @param mapping a function mapping a random-generated long to an instance of T
     * @return an array of random elements
     */
    private T[] buildRandom(Mapper<T> mapping) {
        List<T> objects = Arrays.asList(this.buildSorted(mapping));
        Collections.shuffle(objects, random);
        return objects.toArray((T[]) Array.newInstance(mapping.map(0).getClass(), this.size));
    }

    /**
     * Generate a sorted list of values of type T, using the provided mapping function.
     *
     * @param mapping a function mapping a random-generated long to an instance of T
     * @return an array of sorted elements
     */
    private T[] buildSorted(Mapper<T> mapping) {
        T[] objects = (T[]) Array.newInstance(mapping.map(0).getClass(), this.size);
        for (int i = 0; i < this.size; ++i) {
            objects[i] = mapping.map(i);
        }
        return objects;
    }

    /**
     * Type of array to generate.
     */
    public enum Type {
        // The array contains random elements
        RANDOM, // The values of the array are already sorted (i.e. a[i] <= a[i+1])
        SORTED, // The values of the array are in inversed sorted order (i.e. a[i] >= a[i+1])
        REVERSED
    }

    /**
     * Mapper class, used to associate a number with an object.
     * Refer to {@link #map(long)} for more information.
     *
     * @param <T> the type of object to generate
     */
    @FunctionalInterface
    public interface Mapper<T> {
        /**
         * Mapper function, used to generate a object `T` starting from a long identifier.
         * The idea of "sorted" and "random" array is clear with numbers, but a bit less with Strings and complex objects.
         * For this reason, the user of this class will be required to provide a function that maps a number to a string
         * "representing" it.
         * For example, the mapping with T: String could be 1 -> "aaa", 2 -> "aab", 3 -> "aac" etc.,
         * or 1 -> "a", 2 -> "f", 3 -> "q" etc.
         *
         * @param value The index of the object in a hypothetically unlimited set.
         * @return the object associated with that index.
         */
        T map(long value);
    }
}
