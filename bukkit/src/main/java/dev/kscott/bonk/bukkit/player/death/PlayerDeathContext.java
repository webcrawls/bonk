package dev.kscott.bonk.bukkit.player.death;

import dev.kscott.bluetils.core.text.Colours;
import dev.kscott.bluetils.core.text.Styles;
import dev.kscott.bonk.bukkit.player.BonkSpirit;
import dev.kscott.bonk.bukkit.player.PlayerService;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a death caused by an Entity.
 */
public class PlayerDeathContext extends EntityDeathContext {

    public static @NonNull PlayerDeathContext of(final @NonNull EntityDamageByEntityEvent event,
                                                 final @NonNull PlayerService service) {
        final @NonNull Player player = (Player) event.getEntity();
        final @NonNull Player killer = (Player) event.getDamager();

        if (!service.registered(player)) {
            throw new UnsupportedOperationException("Cannot create PlayerDeathContext with '"+player.getName()+"', who is not registered.");
        }


        if (!service.registered(killer)) {
            throw new UnsupportedOperationException("Cannot create PlayerDeathContext with '"+killer.getName()+"', who is not registered.");
        }

        final @NonNull BonkSpirit playerSpirit = service.spirit(player);
        final @NonNull BonkSpirit killerSpirit = service.spirit(killer);

        return new PlayerDeathContext(
                playerSpirit,
                killerSpirit,
                event.getEntity().getLocation(),
                System.currentTimeMillis()
        );
    }

    private final @NonNull BonkSpirit playerSpirit;
    private final @NonNull BonkSpirit killerSpirit;

    public PlayerDeathContext(final @NonNull BonkSpirit playerSpirit,
                              final @NonNull BonkSpirit killerSpirit,
                              final @NonNull Location location,
                              final long time) {
        super(playerSpirit.player(), killerSpirit.player(), location, time);

        this.playerSpirit = playerSpirit;
        this.killerSpirit = killerSpirit;
    }

    public @NonNull BonkSpirit playerSpirit() {
        return this.playerSpirit;
    }

    public @NonNull BonkSpirit killerSpirit() {
        return this.killerSpirit;
    }

    @Override
    public @NonNull Component message() {
        return Component.text()
                .append(
                        this.playerSpirit.player().displayName()
                                .style(Styles.TEXT)
                                .color(Colours.BLUE_LIGHT)
                )
                .append(Component.text(" ⚔ "))
                .append(
                        this.killerSpirit.player().displayName()
                                .style(Styles.TEXT)
                                .color(Colours.RED_LIGHT))
                .build();
    }

}
