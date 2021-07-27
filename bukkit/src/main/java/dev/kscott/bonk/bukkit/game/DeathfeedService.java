package dev.kscott.bonk.bukkit.game;

import cloud.commandframework.types.tuples.Pair;
import cloud.commandframework.types.tuples.Triplet;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import dev.kscott.bonk.bukkit.BukkitBonkPlugin;
import dev.kscott.bonk.bukkit.player.death.DeathContext;
import net.kyori.adventure.text.Component;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Singleton
public class DeathfeedService {

    private final long MIN_TIME_BETWEEN_REMOVALS = 5000;
    private final long MAX_LINES = 5;

    private final @NonNull List<DeathfeedEntry> deathfeedMessages;
    private final @NonNull BukkitRunnable feedRunnable;

    @Inject
    public DeathfeedService(final @NonNull BukkitBonkPlugin plugin) {
        this.deathfeedMessages = new ArrayList<>();

        this.feedRunnable = new BukkitRunnable() {
            @Override
            public void run() {
                final long time = System.currentTimeMillis();

                final @NonNull Iterator<DeathfeedEntry> it = deathfeedMessages.iterator();

                while (it.hasNext()) {
                    final var entry = it.next();

                    final @NonNull DeathContext ctx = entry.context();

                    if (entry.visible()) {
                        final long delta = Math.abs(ctx.time() - time);

                        if (delta >= MIN_TIME_BETWEEN_REMOVALS) {
                            it.remove();
                        }
                    }

                }

                for (int i = 0; i < Math.min(deathfeedMessages.size(), MAX_LINES); i++) {
                    deathfeedMessages.get(i).visible(true);
                }
            }
        };

        this.feedRunnable.runTaskTimer(plugin, 0, 20);
    }

    public void handleDeath(final @NonNull DeathContext context) {
        deathfeedMessages.add(new DeathfeedEntry(context));
    }

    public @NonNull Component message(int line) {
        if (line >= this.deathfeedMessages.size()) {
            return Component.empty();
        }
        return this.deathfeedMessages.get(line).message();
    }

    private static final class DeathfeedEntry {

        private final @NonNull DeathContext context;
        private final @NonNull Component message;
        private boolean visible;

        public DeathfeedEntry(
                final @NonNull DeathContext context
        ) {
            this.context = context;
            this.message = context.message();
            this.visible = false;
        }

        public DeathContext context() {
            return context;
        }

        public Component message() {
            return message;
        }

        public boolean visible() {
            return visible;
        }

        public void visible(boolean visibility) {
            this.visible = visibility;
        }
    }

}
