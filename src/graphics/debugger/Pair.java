package graphics.debugger;

/**
 * A data type class that stores a pair of objects
 * Adapted from https://stackoverflow.com/a/521235
 * @param <L>  The first object
 * @param <R>   The second object
 */
class Pair<L,R> {

    /**
     * The first object
     */
    private final L left;

    /**
     * The second object
     */
    private final R right;

    /**
     *  Constructor for the pair class
     * @param left The first object to store in the pair
     * @param right The second object to store in the pair
     */
    Pair(L left, R right) {
        this.left = left;
        this.right = right;
    }

    /**
     * Gets the first object in the pair
     * @return Returns the first object
     */
     L getLeft() {
        return left;
    }

    /**
     * Gets the second object in the pair
     * @return Returns the second object
     */
     R getRight() {
        return right;
    }
}
