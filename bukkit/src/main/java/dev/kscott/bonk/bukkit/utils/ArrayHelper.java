package dev.kscott.bonk.bukkit.utils;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Array helper class. Totally stolen from broccolai.
 * <p>
 * Ripped from https://github.com/broccolai/tickets, the best tickets plugin for your server!
 *
 * @author broccolai
 */
public final class ArrayHelper {

    private ArrayHelper() {
    }

    /**
     * Returns an array with the given type {@code T}.
     *
     * @param values the values to return
     * @param <T>    the type of the array
     * @return {@code values}
     */
    @SafeVarargs
    public static <T> @NonNull T @NonNull [] create(final @NonNull T... values) {
        return values;
    }

}