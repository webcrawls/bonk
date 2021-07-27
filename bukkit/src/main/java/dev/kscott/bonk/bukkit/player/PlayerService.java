package dev.kscott.bonk.bukkit.player;

import com.google.inject.Inject;
import dev.kscott.bluetils.core.text.Colours;
import dev.kscott.bluetils.core.text.Styles;
import dev.kscott.bonk.bukkit.BonkInterfaceProvider;
import dev.kscott.bonk.bukkit.game.Constants;
import dev.kscott.bonk.bukkit.game.DeathfeedService;
import dev.kscott.bonk.bukkit.game.score.ScoringService;
import dev.kscott.bonk.bukkit.lobby.LobbyService;
import dev.kscott.bonk.bukkit.log.LoggingService;
import dev.kscott.bonk.bukkit.player.damage.DamageContext;
import dev.kscott.bonk.bukkit.player.damage.FallDamageContext;
import dev.kscott.bonk.bukkit.player.damage.PlayerDamageContext;
import dev.kscott.bonk.bukkit.player.death.DeathContext;
import dev.kscott.bonk.bukkit.player.death.PlayerDeathContext;
import dev.kscott.bonk.bukkit.player.death.PlayerLaunchDeathContext;
import dev.kscott.bonk.bukkit.position.PositionService;
import dev.kscott.bonk.bukkit.utils.ArrayHelper;
import dev.kscott.bonk.bukkit.utils.PlayerUtils;
import dev.kscott.bonk.bukkit.weapon.Weapon;
import dev.kscott.bonk.bukkit.weapon.WeaponService;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.incendo.interfaces.paper.PlayerViewer;

import java.text.DecimalFormat;
import java.util.*;

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
    private final @NonNull ScoringService scoringService;
    private final @NonNull DeathfeedService deathfeedService;
    private final @NonNull Set<BonkSpirit> players;
    private final @NonNull Set<BonkSpirit> gamePlayers;
    private final @NonNull Set<BonkSpirit> frozenPlayers;
    private final @NonNull DecimalFormat decimalFormat;

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
            final @NonNull DeathfeedService deathfeedService,
            final @NonNull ScoringService scoringService,
            final @NonNull JavaPlugin plugin
    ) {
        this.positionService = positionService;
        this.weaponService = weaponService;
        this.deathfeedService = deathfeedService;
        this.loggingService = loggingService;
        this.scoringService = scoringService;
        this.lobbyService = lobbyService;
        this.decimalFormat = new DecimalFormat("#.##");
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

        spirit.openInterface();

        this.reset(spirit);
    }

    public void handlePlayerQuit(final @NonNull Player player) {
        if (!this.registered(player)) {
            throw new UnsupportedOperationException("Tried to act on a player that is not registered!");
        }

        final @NonNull BonkSpirit spirit = this.spirit(player);

        this.gamePlayers.remove(spirit);
        this.lobbyService.add(spirit);
    }

    public void handlePlayerDamage(final @NonNull EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        final @NonNull DamageContext damageContext = this.createDamageContext(event);

        final @NonNull Player player = damageContext.player();

        if (!this.registered(player)) {
            throw new UnsupportedOperationException("Tried to handle player damage on an unregistered player!");
        }

        final @NonNull BonkSpirit playerSpirit = this.spirit(player);

        playerSpirit.addDamage(damageContext);

        player.setNoDamageTicks(0);

        if (damageContext instanceof PlayerDamageContext playerDamageContext) {
            final @NonNull Player attacker = playerDamageContext.attacker();

            if (!this.registered(attacker)) {
                throw new UnsupportedOperationException("Tried to handle player damage on an unregistered player!");
            }

            final @NonNull BonkSpirit attackerSpirit = this.spirit(attacker);

            final @NonNull Weapon weapon = attackerSpirit.weapon();

            final @NonNull Vector a = player.getLocation().toVector();
            final @NonNull Vector b = attacker.getLocation().toVector();

            if (PlayerUtils.movingY(player)) {
                final @NonNull Vector vector = a.subtract(b)
                        .normalize()
                        .multiply(6);
                System.out.println(vector);
                player.setVelocity(vector);
            } else {
                player.setVelocity(a.subtract(b)
                        .add(new Vector(0, 0.4, 0))
                        .normalize()
                        .multiply(3));
            }

        }

        if (damageContext instanceof FallDamageContext fallDamageContext) {
            final double distance = fallDamageContext.distance();

            final @Nullable DamageContext previousDamage = playerSpirit.previousDamage(damageContext);

            if (previousDamage != null) {
                if (previousDamage instanceof PlayerDamageContext playerDamageContext) {
                    final @NonNull Player attacker = playerDamageContext.attacker();

                    attacker.sendMessage(Component.text("You launched a player " + distance + "m!"));
                }
            }

        }

    }

    public void handlePlayerDeathEvent(final @NonNull PlayerDeathEvent event) {
        final @NonNull Player player = event.getEntity();

        if (!this.registered(player)) {
            throw new UnsupportedOperationException("Tried to act on a player that is not registered!");
        }

        this.handlePlayerDeath(this.createDeathContext(event));

    }

    public void handlePlayerDeath(final @NonNull DeathContext ctx) {
        final @NonNull Player player = ctx.player();

        final @NonNull BonkSpirit spirit = this.spirit(player);

        reset(spirit);

//        this.broadcast(ctx.message());

        if (ctx instanceof PlayerDeathContext playerCtx) {
            // Handle killed by another player context
            this.scoringService.addKill(playerCtx.killerSpirit().player().getUniqueId());
        }

        if (ctx instanceof PlayerLaunchDeathContext playerCtx) {
            playerCtx.killerSpirit().player().sendMessage(
                    Component.text()
                            .append(Component.text("NICE HIT! ").style(Styles.EMPHASIS).color(Colours.YELLOW))
                            .append(playerCtx.killerSpirit().player().displayName().color(Colours.BLUE_LIGHT))
                            .append(Component.text(" was launched "))
                            .append(Component.text(this.decimalFormat.format(playerCtx.distance())).color(Colours.RED_LIGHT))
                            .append(Component.text(" meters"))
                            .style(Styles.TEXT)
            );
        }

        this.scoringService.addDeath(ctx.player().getUniqueId());
        this.deathfeedService.handleDeath(ctx);

        // TODO modify killstreak/damage stuff
    }

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

    public boolean inGame(final @NonNull Player player) {
        for (final @NonNull BonkSpirit spirit : this.gamePlayers) {
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
        player.removePotionEffect(Constants.Potions.GLOWING.getType());
        player.addPotionEffect(Constants.Potions.JUMP_BOOST);
        player.addPotionEffect(Constants.Potions.GLOWING);

//        player.getInventory().clear();
//        player.getInventory().addItem(bonkPlayer.weapon().itemStack());

        new BukkitRunnable() {

            @Override
            public void run() {
                bonkPlayer.position(positionService.spawnPosition());
            }

        }.runTaskLater(this.plugin, 1);
    }

    private @NonNull DeathContext createDeathContext(final @NonNull PlayerDeathEvent event) {
        return DeathContext.fromEvent(event, this);
    }

    private @NonNull DamageContext createDamageContext(final @NonNull EntityDamageEvent event) {
        return DamageContext.fromEvent(event, this);
    }

    /**
     * Propels all LivingEntities surrounding a location away from that location.
     * @param location Location of 'explosion'
     * @param radius Radius to get entities (applies to x,y,z)
     * @param explosionPower Strength of knockback
     * @param knockbackDropoff should there be knocback dropoff? (farther the entity is away from location, the less knockback they take)
     * @return A map where the key is a living entity that was effected by knockback, and where the value is how much knockback they took
     */
    public static Map<LivingEntity, Double> propelEntitiesAt(final @NonNull Location location, final int radius, final double explosionPower, final boolean knockbackDropoff) {
        final @NonNull Collection<LivingEntity> entities = location.getNearbyLivingEntities(radius);

        final @NonNull Map<LivingEntity, Double> knockbackMap = new HashMap<>();

        for (final @NonNull LivingEntity livingEntity : entities) {
            final @NonNull Location entityLocation = livingEntity.getLocation();

            final double distance = location.distanceSquared(entityLocation);

            // TODO: better dropoff calculation
            final double knockback = knockbackDropoff ? explosionPower - distance : explosionPower;

            final @NonNull Vector knockbackVector = entityLocation.toVector().subtract(location.toVector());

            knockbackVector.setY(knockback);

            livingEntity.setVelocity(knockbackVector);

            knockbackMap.put(livingEntity, knockback);
        }

        return knockbackMap;
    }
}
