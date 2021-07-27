package dev.kscott.bonk.bukkit.player;

import broccolai.corn.paper.PaperItemBuilder;
import dev.kscott.bluetils.core.text.Colours;
import dev.kscott.bluetils.core.text.Styles;
import dev.kscott.bonk.bukkit.BonkInterfaceProvider;
import dev.kscott.bonk.bukkit.player.damage.DamageContext;
import dev.kscott.bonk.bukkit.position.GamePosition;
import dev.kscott.bonk.bukkit.powerup.Powerup;
import dev.kscott.bonk.bukkit.powerup.PowerupEntry;
import dev.kscott.bonk.bukkit.weapon.Weapon;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.persistence.PersistentDataType;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.incendo.interfaces.paper.PlayerViewer;
import org.incendo.interfaces.paper.element.ItemStackElement;
import org.incendo.interfaces.paper.pane.ChestPane;
import org.incendo.interfaces.paper.transform.PaperTransform;
import org.incendo.interfaces.paper.type.PlayerInterface;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * The core bonk player class.
 */
public final class BonkSpirit {

    public static final @NonNull NamespacedKey CROSSLAUNCHER_DATA_KEY = NamespacedKey.fromString("crosslauncher");
    public static final @NonNull NamespacedKey AMMO_DATA_KEY = NamespacedKey.fromString("ammo");

    private final int POWERUP_LIMIT = 5;

    private final @NonNull Player player;
    private final PowerupEntry[] powerupEntries;
    private @NonNull Weapon weapon;
    private @Nullable Entity lastAttacker;
    private final @NonNull PlayerInterface playerInterface;
    private final @NonNull List<DamageContext> damageList;
    private long lastAttackTime;
    private int rockets;

    /**
     * Constructs {@code BonkPlayer}.
     *
     * @param player the player to associate with this {@code BonkPlayer}
     * @param weapon the weapon to give the player
     */
    public BonkSpirit(
            final @NonNull Player player,
            final @NonNull Weapon weapon
    ) {
        this.player = player;
        this.powerupEntries = new PowerupEntry[POWERUP_LIMIT];
        this.weapon = weapon;
        this.lastAttacker = null;
        this.lastAttackTime = 0;
        this.rockets = 0;
        this.damageList = new ArrayList<>();
        this.playerInterface = PlayerInterface.builder()
                .updates(true, 1)
//                .addTransform(PaperTransform.playerFillAll(ItemStackElement.of(BonkInterfaceProvider.MENU_BG)))
                .addTransform((pane, view) -> {
                    final int POWERUP_ITEM_START_INDEX = 2;

                    for (int i = 0; i < ChestPane.MINECRAFT_CHEST_WIDTH; i++) {
                        pane = pane.hotbar(i, ItemStackElement.of(BonkInterfaceProvider.MENU_DARK_BG));
                    }

                    pane = pane.hotbar(0, ItemStackElement.of(this.weapon.itemStack()));

                    for (int i = 0; i < POWERUP_LIMIT; i++) {
                        final @Nullable PowerupEntry entry = this.powerupEntries[i];

                        if (entry == null) {
                            pane = pane.hotbar(POWERUP_ITEM_START_INDEX+i, ItemStackElement.empty());
                        } else {
                            pane = pane.hotbar(POWERUP_ITEM_START_INDEX+i, ItemStackElement.of(entry.itemStack()));
                        }
                    }

                    // Add crossbow
                    if (this.rockets <= 0) {
                        pane = pane.hotbar(8, ItemStackElement.of(BonkInterfaceProvider.MENU_DARK_BG));
                    } else {
                        pane = pane.hotbar(8, ItemStackElement.of(
                                PaperItemBuilder.paper(Material.FIREWORK_ROCKET)
                                        .name(Component.text(this.rockets+" rockets", Styles.TEXT).color(Colours.GREEN_LIGHT))
                                        .loreComponents(
                                                List.of(
                                                        Component.empty(),
                                                        Component.text()
                                                                .append(Component.keybind("key.use").color(Colours.BLUE_LIGHT))
                                                                .append(Component.text(" while holding your weapon"))
                                                                .style(Styles.TEXT)
                                                                .build(),
                                                        Component.text("to fire a rocket.", Styles.TEXT)
                                                )
                                        )
                                        .flags(ItemFlag.values())
                                        .amount(this.rockets)
                                        .build()
                        ));
                    }

                    return pane;
                })
                .clickHandler((ctx) -> {
                    ctx.cancel(true);
                })
                .build();
    }

    public void openInterface() {
        System.out.println("Opening interface");
        this.playerInterface.open(PlayerViewer.of(player)).open();
    }

    public void addDamage(final @NonNull DamageContext damageContext) {
        this.damageList.add(damageContext);
    }

    /**
     * Returns an immutable list of damages applied to this spirit.
     *
     * The most recent damage is at index 0, with older damages as the index increases.
     *
     * @return the damage list
     */
    public @NonNull List<DamageContext> damageList() {
        final @NonNull List<DamageContext> tempList = new ArrayList<>(this.damageList);

        Collections.reverse(tempList);

        return List.copyOf(tempList);
    }

    public @Nullable DamageContext latestDamage() {
        return this.damageList.get(this.damageList.size()-1);
    }

    public int powerupCount() {
        int count = 0;

        for (final PowerupEntry entry : this.powerupEntries) {
            if (entry != null) {
                count++;
            }
        }

        return count;
    }

    public boolean addPowerup(final @NonNull PowerupEntry entry) {
        if (powerupCount() >= POWERUP_LIMIT) {
            return false;
        }

        for (int i = 0; i < POWERUP_LIMIT; i++) {
            if (powerupEntries[i] == null) {
                powerupEntries[i] = entry;
                return true;
            }
        }

        return false;
    }

    public void removePowerup(final int slot) {
        if (slot > POWERUP_LIMIT) {
            return;
        }

        this.powerupEntries[slot] = null;
    }

    public @Nullable DamageContext previousDamage(final @NonNull DamageContext damageContext) {
        final int index = this.damageList.indexOf(damageContext);

        if (index <= 0) {
            return null;
        }

        return this.damageList.get(index-1);
    }

    /**
     * {@return the player's uuid}
     */
    public @NonNull UUID uuid() {
        return this.player.getUniqueId();
    }

    /**
     * {@return the player's name}
     */
    public @NonNull String name() {
        return this.player.getName();
    }

    /**
     * {@return the player's position}
     */
    public @NonNull GamePosition position() {
        final @NonNull Location location = this.player.getLocation();

        return new GamePosition(
                location.getX(),
                location.getY(),
                location.getZ(),
                location.getPitch(),
                location.getYaw()
        );
    }

    /**
     * Sets the player's position.
     *
     * @param position desired position
     */
    public void position(final @NonNull GamePosition position) {
        this.player.teleport(new Location(
                this.player.getWorld(),
                position.x(),
                position.y(),
                position.z(),
                position.pitch(),
                position.yaw()
        ));
    }

    /**
     * {@return the player's selected weapon}
     */
    public @NonNull Weapon weapon() {
        return this.weapon;
    }

    /**
     * Sets the player's weapon.
     *
     * @param weapon the weapon
     */
    public void weapon(final @NonNull Weapon weapon) {
        this.weapon = weapon;
    }

    /**
     * {@return the player}
     */
    public @NonNull Player player() {
        return this.player;
    }

    /**
     * {@return this player's last attacker}
     */
    public @Nullable Entity lastAttacker() {
        return this.lastAttacker;
    }

    /**
     * {@return when this player was last attacked}
     */
    public long lastAttackTime() {
        return this.lastAttackTime;
    }

    /**
     * Sets this player's last attacker.
     *
     * @param lastAttacker entity
     */
    public void lastAttacker(final @NonNull Entity lastAttacker) {
        this.lastAttacker = lastAttacker;
    }

    /**
     * Sets this player's last attack time.
     *
     * @param lastAttackTime last attack time (in ms, as unix timestamp)
     */
    public void lastAttackTime(final long lastAttackTime) {
        this.lastAttackTime = lastAttackTime;
    }

    public int rockets() {
        return this.rockets;
    }

    public void removeRocket(final int amount) {
        this.rockets = rockets - amount;
    }

    public void addRocket(final int amount) {
        this.rockets = rockets + amount;
    }
}
