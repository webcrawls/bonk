package dev.kscott.bonk.bukkit.player;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import dev.kscott.bonk.bukkit.game.Constants;
import dev.kscott.bonk.bukkit.position.PositionService;
import dev.kscott.bonk.bukkit.utils.ArrayHelper;
import dev.kscott.bonk.bukkit.weapon.WeaponService;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.GameMode;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manages the players of Bonk.
 */
public final class PlayerService {

    /**
     * An array of components to send to players upon joining Bonk.
     */
    private static final Component[] MOTD_COMPONENTS = ArrayHelper.create(
            MiniMessage.get().parse("<gradient:#b01e13:#de5a50:#b01e13><st>                                        </st></gradient>"),
            MiniMessage.get().parse("<gray>Welcome to <color:#de5a50>Bonk!</color:#de5a50></gray>"),
            MiniMessage.get().parse("<gray>Developed by bluely, with lots of help from Bing.</gray>"),
            MiniMessage.get().parse("<gray>Check out the code on <dark_aqua>GitHub!</dark_aqua></gray>"),
            Component.empty(),
            MiniMessage.get().parse("<gray>To change your Bonk weapon, run <aqua>/weapons</aqua>."),
            MiniMessage.get().parse("<gradient:#b01e13:#de5a50:#b01e13><st>                                        </st></gradient>")
    );

    /**
     * Controls whether or not the MOTD should be sent to the player.
     */
    private static final boolean SEND_MOTD = true;

    /**
     * The PositionService dependency.
     */
    private final @NonNull PositionService positionService;

    /**
     * The WeaponService dependency.
     */
    private final @NonNull WeaponService weaponService;

    /**
     * The logger.
     */
    private final @NonNull Logger logger;

    /**
     * Stores all online Bonk players.
     */
    private final @NonNull Set<BonkPlayer> players;

    /**
     * Constructs {@code PlayerService}.
     *
     * @param positionService the PositionService dependency
     * @param weaponService   the WeaponService dependency
     * @param logger          the plugin logger
     */
    @Inject
    public PlayerService(
            final @NonNull PositionService positionService,
            final @NonNull WeaponService weaponService,
            final @NonNull @Named("pluginLogger") Logger logger
            ) {
        this.positionService = positionService;
        this.weaponService = weaponService;
        this.logger = logger;
        this.players = new HashSet<>();
    }

    /**
     * Returns the {@link BonkPlayer} associated with {@code player}.
     *
     * @param player player
     * @return the {@link BonkPlayer}
     */
    public @Nullable BonkPlayer player(final @NonNull Player player) {
        for (final @NonNull BonkPlayer bonkPlayer : players) {
            if (bonkPlayer.uuid().equals(player.getUniqueId())) {
                return bonkPlayer;
            }
        }

        return null;
    }

    /**
     * Returns the {@link BonkPlayer} associated with {@code event#getPlayer}.
     * <p>
     * If there is no {@link BonkPlayer} associated with {@code event#getPlayer},
     * and {@code player} is online, then a new {@link BonkPlayer} will
     * be created and {@code player} will be initialized into the game.
     *
     * @param event {@link PlayerJoinEvent}
     * @return the {@link BonkPlayer}
     */
    public @NonNull BonkPlayer joined(final @NonNull PlayerJoinEvent event) {
        final @NonNull Player player = event.getPlayer();

        if (!player.isOnline()) {
            throw new RuntimeException("Tried to create a player that is not online!");
        }

        boolean ingame = playing(player);

        if (ingame) {
            for (final @NonNull BonkPlayer bonkPlayer : players) {
                if (bonkPlayer.uuid().equals(player.getUniqueId())) {
                    return bonkPlayer;
                }
            }
        }

        final @NonNull BonkPlayer bonkPlayer = createNewBonkPlayer(player);

        return bonkPlayer;
    }

    /**
     * Removes a player from Bonk.
     *
     * @param event {@link PlayerQuitEvent}
     */
    public void quit(final @NonNull PlayerQuitEvent event) {
        // TODO restore inventory
        // TODO teleport back to previous location
        players.removeIf(bonkPlayer -> bonkPlayer.uuid().equals(event.getPlayer().getUniqueId()));
    }

    /**
     * Handles a player's death.
     *
     * @param event {@link PlayerDeathEvent}
     */
    public void died(final @NonNull PlayerDeathEvent event) {
        final @NonNull Player player = event.getEntity();
        final @Nullable BonkPlayer bonkPlayer = this.player(player);

        if (bonkPlayer == null) {
            this.logger.log(Level.INFO, "Tried to call PlayerService#died with a player who is not playing Bonk!");
            return;
        }

        // TODO Set killstreak to 0
        // TODO Score subtract
        event.setCancelled(true);

        // Reset player
        this.reset(bonkPlayer);
    }

    /**
     * Returns {@code true} if {@code player} is playing Bonk; {@code false} if otherwise.
     *
     * @param player player
     * @return {@code true} if {@code player} is playing Bonk; {@code false} if otherwise
     */
    public boolean playing(final @NonNull Player player) {
        return this.player(player) != null;
    }

    /**
     * {@return a collection containing all bonk players}
     */
    public @NonNull Collection<BonkPlayer> players() {
        return Collections.unmodifiableSet(this.players);
    }

    /**
     * Resets a player: attributes, inventory, and position.
     *
     * @param bonkPlayer player to reset
     */
    public void reset(final @NonNull BonkPlayer bonkPlayer) {
        final @NonNull Player player = bonkPlayer.player();

        bonkPlayer.position(this.positionService.spawnPosition());

        player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.25);
        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(24);
        player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
        player.setGameMode(GameMode.SURVIVAL);

        player.removePotionEffect(Constants.Potions.JUMP_BOOST.getType());
        player.addPotionEffect(Constants.Potions.JUMP_BOOST);

        player.getInventory().clear();
        player.getInventory().addItem(bonkPlayer.weapon().itemStack());
    }


    /**
     * Creates (and initializes) a new {@link BonkPlayer} associated with {@code player}.
     *
     * @param player player
     * @return the {@link BonkPlayer}
     */
    private @NonNull BonkPlayer createNewBonkPlayer(final @NonNull Player player) {
        final @NonNull BonkPlayer bonkPlayer = new BonkPlayer(player, this.weaponService.defaultWeapon());

        // TODO: Save inventory/attributes before joining to reuse after player leaves Bonk

        if (SEND_MOTD) {
            for (final @NonNull Component component : MOTD_COMPONENTS) {
                player.sendMessage(component);
            }
        }

        players.add(bonkPlayer);

        reset(bonkPlayer);

        return bonkPlayer;
    }

}
