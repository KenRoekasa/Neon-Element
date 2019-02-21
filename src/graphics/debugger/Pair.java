package graphics.debugger;

// following code adapted from https://stackoverflow.com/a/521235

class Pair<L,R> {
    private final L left;
    private final R right;

    Pair(L left, R right) {
        this.left = left;
        this.right = right;
    }

     L getLeft() {
        return left;
    }

     R getRight() {
        return right;
    }
}
