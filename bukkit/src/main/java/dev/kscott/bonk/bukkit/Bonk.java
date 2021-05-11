package dev.kscott.bonk.bukkit;

import com.google.inject.Inject;
import com.google.inject.Injector;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * The main Bonk class.
 */
public final class Bonk {

    /**
     * The parent injector passed into the {@code Bonk} constructor.
     */
    private final @NonNull Injector parentInjector;

    /**
     * The injector used by {@code Bonk} to inject its classes.
     */
    private @MonotonicNonNull Injector injector;

    /**
     * Constructs {@code Bonk}.
     *
     * @param parentInjector the parent injector
     */
    @Inject
    public Bonk(final @NonNull Injector parentInjector) {
        this.parentInjector = parentInjector;
    }

    /**
     * Loads Bonk.
     *
     * @return the {@code Bonk} injector.
     */
    public @NonNull Injector load() {
        this.injector = this.parentInjector.createChildInjector();

        return this.injector;
    }

}
