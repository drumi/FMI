public enum Direction {
    DOWN,
    LEFT,
    UP,
    RIGHT;

    public Direction next() {
        return switch (this) {
            case DOWN -> LEFT;
            case LEFT -> UP;
            case UP -> RIGHT;
            case RIGHT -> DOWN;
        };
    }
}
