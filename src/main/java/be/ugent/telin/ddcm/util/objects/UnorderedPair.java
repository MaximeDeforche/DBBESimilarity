package be.ugent.telin.ddcm.util.objects;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class UnorderedPair<T> {

    private final T first;
    private final T second;

    public UnorderedPair(T first, T second) {
        this.first = first;
        this.second = second;
    }

    public static <T> UnorderedPair<T> of(T first, T second) {
        return new UnorderedPair<>(first, second);
    }

    public Set<T> getElements() {
        return new HashSet<>(Arrays.asList(first, second));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UnorderedPair<?> that = (UnorderedPair<?>) o;

        return Objects.equals(first, that.first) && Objects.equals(second, that.second)
                || Objects.equals(first, that.second) && Objects.equals(second, that.first);
    }

    @Override
    public int hashCode() {
        int hash = 0;

        if (first != null) hash += first.hashCode();
        if (second != null) hash += second.hashCode();

        return hash;
    }

    @Override
    public String toString() {
        return "UnorderedPair{" +
                "elements={" + first +
                ", " + second +
                "}}";
    }
}
