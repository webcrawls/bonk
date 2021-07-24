package dev.kscott.bonk.bukkit.player;

import com.google.inject.Inject;
import dev.kscott.bonk.bukkit.BonkInterfaceProvider;
import dev.kscott.bonk.bukkit.game.Constants;
import dev.kscott.bonk.bukkit.lobby.LobbyService;
import dev.kscott.bonk.bukkit.log.LoggingService;
import dev.kscott.bonk.bukkit.player.death.DeathCause;
import dev.kscott.bonk.bukkit.position.PositionService;
import dev.kscott.bonk.bukkit.utils.ArrayHelper;
import dev.kscott.bonk.bukkit.weapon.Weapon;
import dev.kscott.bonk.bukkit.weapon.WeaponService;
import dev.kscott.bonk.bukkit.weapon.sound.WeaponSoundDefinition;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.GameMode;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.interfaces.paper.PlayerViewer;
import org.incendo.interfaces.paper.view.PlayerView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    private final @NonNull PositionService positionService;
    private final @NonNull JavaPlugin plugin;
    private final @NonNull WeaponService weaponService;
    private final @NonNull LobbyService lobbyService;
    private final @NonNull LoggingService loggingService;
    private final @NonNull Set<BonkSpirit> players;
    private final @NonNull Set<BonkSpirit> gamePlayers;
    private final @NonNull Set<BonkSpirit> frozenPlayers;

    /**
     * Constructs {@code PlayerService}.
     *
     * @param positionService the PositionService dependency
     * @param weaponService   the WeaponService dependency
     * @param loggingService  the LoggingService dependency
     * @param lobbyService    the lobby service
     * @param plugin          the plugin dependency
     */
    @Inject
    public PlayerService(
            final @NonNull PositionService positionService,
            final @NonNull WeaponService weaponService,
            final @NonNull LoggingService loggingService,
            final @NonNull LobbyService lobbyService,
            final @NonNull JavaPlugin plugin
    ) {
        this.positionService = positionService;
        this.weaponService = weaponService;
        this.loggingService = loggingService;
        this.lobbyService = lobbyService;
        this.plugin = plugin;

        this.players = new HashSet<>();
        this.frozenPlayers = new HashSet<>();
        this.gamePlayers = new HashSet<>();
    }

    /**
     * Handles a player when they join the server.
     *
     * @param player the player
     */
    public void handlePlayerJoin(final @NonNull Player player) {
        final @NonNull BonkSpirit spirit = createSpirit(player);

        this.lobbyService.add(spirit);
    }

    /**
     * Handles a player when they leave the server.
     *
     * @param player the player
     */
    public void handlePlayerLeave(final @NonNull Player player) {
        if (!this.registered(player)) {
            throw new UnsupportedOperationException("Tried to act on a player that is not registered!");
        }

        removeSpirit(player);
    }

    public void openPlayMenu(final @NonNull Player player) {
        BonkInterfaceProvider.main(this, weaponService)
                .open(PlayerViewer.of(player));
    }

    public void handlePlayerPlay(final @NonNull Player player) {
        if (!this.registered(player)) {
            throw new UnsupportedOperationException("Tried to act on a player that is not registered!");
        }

        final @NonNull BonkSpirit spirit = this.spirit(player);

        this.gamePlayers.add(spirit);
        this.lobbyService.remove(spirit);

        this.reset(spirit);
    }

    public void handlePlayerQuit(final @NonNull Player player) {
        if (!this.registered(player)) {
            throw new UnsupportedOperationException("Tried to act on a player that is not registered!");
        }

        final @NonNull BonkSpirit spirit = this.spirit(player);

        this.gamePlayers.remove(spirit);
    }

    public void handlePlayerAttack(final @NonNull EntityDamageByEntityEvent event) {
        final @NonNull Entity attacker = event.getDamager();
        final @NonNull Entity victim = event.getEntity();

        if (attacker instanceof final @NonNull Player attackerPlayer) {
            if (!this.registered(attackerPlayer)) {
                throw new UnsupportedOperationException("Tried to act on a player that is not registered!");
            }

            final @NonNull BonkSpirit attackerSpirit = this.spirit(attackerPlayer);

            final @NonNull Weapon weapon = attackerSpirit.weapon();

            final @NonNull List<WeaponSoundDefinition> sounds = weapon.sounds();

            for (final @NonNull WeaponSoundDefinition definition : sounds) {
                final @NonNull Sound sound = Sound.sound(
                        definition.sound().key(),
                        Sound.Source.AMBIENT,
                        definition.volume(),
                        definition.pitch()
                );

                attacker.getWorld().playSound(sound);
            }

            // TODO this.createAttackContext();
        } else {
            return;
        }
    }

    public void handlePlayerDeath(final @NonNull PlayerDeathEvent event) {
        final @NonNull Player player = event.getEntity();

        if (!this.registered(player)) {
            throw new UnsupportedOperationException("Tried to act on a player that is not registered!");
        }

        final @NonNull BonkSpirit spirit = this.spirit(player);

        event.setCancelled(true);

        reset(spirit);

        // TODO this.createDeathCause();
        // TODO modify killstreak/damage stuff
    }

    public void handlePlayerKill() {};

    private void broadcast(final @NonNull Component message) {
        for (final @NonNull BonkSpirit spirit : players) {
            spirit.player().sendMessage(message);
        }
    }

    public boolean registered(final @NonNull Player player) {
        for (final @NonNull BonkSpirit spirit : this.players) {
            if (spirit.player().getUniqueId().equals(player.getUniqueId())) {
                return true;
            }
        }

        return false;
    }

    public @NonNull BonkSpirit spirit(final @NonNull Player player) {
        for (final @NonNull BonkSpirit spirit : this.players) {
            if (spirit.player().getUniqueId().equals(player.getUniqueId())) {
                return spirit;
            }
        }

        throw new NullPointerException("No spirit with the given player was found.");
    }

    /**
     * Returns a list containing all the registered players.
     *
     * @return the players
     */
    public @NonNull List<BonkSpirit> players() {
        return List.copyOf(this.players);
    }

    /**
     * Returns a list of all the players actually playing bonk.
     *
     * @return the players
     */
    public @NonNull List<BonkSpirit> gamePlayers() {
        return List.copyOf(this.gamePlayers);
    }

    /**
     * Creates a bonk player and returns it.
     *
     * @return the player
     */
    private @NonNull BonkSpirit createSpirit(final @NonNull Player player) {
        if (registered(player)) {
            throw new UnsupportedOperationException("Cannot create a bonk player that already exists.");
        }

        // TODO Load weapon selection choice from datastores

        final @NonNull BonkSpirit bonkSpirit = new BonkSpirit(player, this.weaponService.defaultWeapon());

        this.players.add(bonkSpirit);

        this.lobbyService.add(bonkSpirit);

        return bonkSpirit;
    }

    private void removeSpirit(final @NonNull Player player) {
        final @NonNull BonkSpirit spirit = this.spirit(player);

        this.players.remove(spirit);
        this.gamePlayers.remove(spirit);
        this.frozenPlayers.remove(spirit);

        if (this.lobbyService.inLobby(player)) {
            this.lobbyService.remove(spirit);
        }
    }

    /**
     * Resets a bonkPlayer: attributes, inventory, position and game data.
     *
     * @param bonkPlayer player to reset
     */
    public void reset(final @NonNull BonkSpirit bonkPlayer) {
        final @NonNull Player player = bonkPlayer.player();

        player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.25);
        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(24);
        player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
        player.setGameMode(GameMode.SURVIVAL);
        player.setFoodLevel(20);

        player.setFallDistance(0);

        player.removePotionEffect(Constants.Potions.JUMP_BOOST.getType());
        player.addPotionEffect(Constants.Potions.JUMP_BOOST);

        player.getInventory().clear();
        player.getInventory().addItem(bonkPlayer.weapon().itemStack());

        new BukkitRunnable() {

            @Override
            public void run() {
                bonkPlayer.position(positionService.spawnPosition());
            }

        }.runTaskLater(this.plugin, 1);
    }

    private @NonNull DeathCause createDeathContext(final @NonNull PlayerDeathEvent event) {
        return null;
    }
}
