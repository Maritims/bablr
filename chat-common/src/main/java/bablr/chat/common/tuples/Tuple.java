package bablr.chat.common.tuples;

public record Tuple<T1, T2>(T1 first, T2 second) {
    public Tuple {
        if (first == null) {
            throw new IllegalArgumentException("first must not be null");
        }
        if (second == null) {
            throw new IllegalArgumentException("second must not be null");
        }
    }

    public static <T1, T2> Tuple<T1, T2> tuple(T1 first, T2 second) {
        if (first == null) {
            throw new IllegalArgumentException("first must not be null");
        }
        if (second == null) {
            throw new IllegalArgumentException("second must not be null");
        }

        return new Tuple<>(first, second);
    }
}
