package com.lvxc.enc.util;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * <p>Functional class.</p>
 *
 * @author Sergio.U.Bocchio
 * @version $Id: $Id
 */
public class Functional {
    /**
     * <p>tap.</p>
     *
     * @param consumer a {@link Consumer} object
     * @param <T> a T class
     * @return a {@link Function} object
     */
    public static <T> Function<T, T> tap(Consumer<T> consumer) {
        return t -> {
            consumer.accept(t);
            return t;
        };
    }

    /**
     * <p>notNull.</p>
     *
     * @param <T> a T class
     * @return a {@link Predicate} object
     */
    public static <T> Predicate<T> notNull() {
        return Objects::nonNull;
    }
}
