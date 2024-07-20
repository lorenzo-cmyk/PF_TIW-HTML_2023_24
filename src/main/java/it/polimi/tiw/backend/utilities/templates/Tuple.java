package it.polimi.tiw.backend.utilities.templates;

/**
 * Represents a generic tuple of two elements.
 * This class is designed to store a pair of objects, potentially of different types.
 * It provides a simple structure to access these two elements.
 *
 * @param <T1>       The type of the first element in the tuple.
 * @param <T2>       The type of the second element in the tuple.
 * @param firstItem  The first item of the tuple
 * @param secondItem The second item of the tuple
 */
public record Tuple<T1, T2>(T1 firstItem, T2 secondItem) {
    /**
     * Constructs a new Tuple with specified items.
     *
     * @param firstItem  The first item of the tuple, of type T1.
     * @param secondItem The second item of the tuple, of type T2.
     */
    public Tuple {
    }

    /**
     * Retrieves the first item of the tuple.
     *
     * @return The first item of the tuple.
     */
    @Override
    public T1 firstItem() {
        return firstItem;
    }

    /**
     * Retrieves the second item of the tuple.
     *
     * @return The second item of the tuple.
     */
    @Override
    public T2 secondItem() {
        return secondItem;
    }
}