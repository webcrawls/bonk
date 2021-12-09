package dev.kscott.bonk.bukkit.weapon;

import com.destroystokyo.paper.ParticleBuilder;
import com.google.inject.Inject;


import dev.kscott.bonk.bukkit.game.Constants;
import dev.kscott.bonk.bukkit.utils.Colours;
import dev.kscott.bonk.bukkit.utils.Styles;
import dev.kscott.bonk.bukkit.weapon.sound.WeaponSoundDefinition;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * Handles weapons.
 */
public class WeaponService {

    private final @NonNull List<@NonNull Weapon> weapons; // a list of registered weapons
    private final @NonNull Weapon defaultWeapon; // the default weapon
    private final @NonNull Random random;

    @Inject
    public WeaponService() {
        this.weapons = new ArrayList<>();
        this.random = new Random();

        this.registerDefaults();

        this.defaultWeapon = Objects.requireNonNull(this.weapon("stick"));
    }

    /**
     * Registers default weapons.
     */
    private void registerDefaults() {
        // Stick
        // Charged attack: absorb half of all knockback for 5 seconds
        this.register(new Weapon(
                "stick",
                Component.text("Stick", Styles.STYLE_TEXT).color(Colours.GRAY_DARK),
                List.of(Component.text("The original Bonk stick, circa 2019.", Styles.STYLE_TEXT)),
                Material.STICK,
                List.of(WeaponSoundDefinition.of(Sound.BLOCK_BAMBOO_BREAK, 0.2F, 1)),
                (event) -> {
                }
        ));

        // Blaze
        // Charged move: Send firey tunnel in your direction
        this.register(new Weapon(
                "blaze",
                Component.text("Blaze", Styles.STYLE_TEXT).color(Colours.ORANGE),
                List.of(Component.text()
                                .append(Component.text("⚠").color(Colours.RED_DARK))
                                .append(Component.text(" WARNING ", Styles.STYLE_EMPHASIS).color(Colours.RED_LIGHT))
                                .append(Component.text("⚠").color(Colours.RED_DARK))
                                .build(),
                        Component.text("May create third-degree burns...", Styles.STYLE_TEXT)
                ),
                Material.BLAZE_ROD,
                List.of(WeaponSoundDefinition.of(Sound.ENTITY_BLAZE_HURT, 0.2F, 1)),
                event -> {
                    final org.bukkit.entity.Entity entity = event.getEntity();

                    final @NonNull Location location = entity.getLocation();

                    new ParticleBuilder(Particle.LAVA)
                            .location(location)
                            .allPlayers()
                            .offset(0.5, 1, 0.5)
                            .count(10)
                            .spawn()
                            .particle(Particle.REDSTONE)
                            .data(new Particle.DustOptions(Color.RED, 1))
                            .particle(Particle.FLAME)
                            .data(null)
                            .spawn();
                }
        ));

        // Enderpearl (?)
        // Charged attack: Teleport players away nearby randomly

        // Emerald
        // Charged attack: Store a location by shift right-clicking, and when activated, you teleport there.
        this.register(new Weapon(
                "emerald",
                Component.text("Emerald", Styles.STYLE_TEXT).color(Colours.GREEN_LIGHT),
                List.of(
                        Component.text()
                                .append(Component.text("⛏ ", Colours.BLUE_LIGHT))
                                .append(Component.text("⚡ ", Colours.YELLOW))
                                .append(Component.text("♫ ", Colours.GRAY_LIGHT))
                                .append(Component.text("✔ ", Colours.GREEN_LIGHT))
                                .decorate(TextDecoration.BOLD)
                                .decoration(TextDecoration.ITALIC, false)
                                .build()
                ),
                Material.EMERALD,
                List.of(
                        WeaponSoundDefinition.random(Sound.BLOCK_NOTE_BLOCK_BIT, 1F, 0.2F, 1F),
                        WeaponSoundDefinition.random(Sound.BLOCK_NOTE_BLOCK_BIT, 1F, 0.2F, 1F),
                        WeaponSoundDefinition.random(Sound.BLOCK_NOTE_BLOCK_BIT, 1F, 0.2F, 1F),
                        WeaponSoundDefinition.random(Sound.BLOCK_NOTE_BLOCK_BIT, 1F, 0.2F, 1F)
                ),
                (event) -> {
                }
        ));

        // Slime
        // Charged attack: Replaces blocks below you with slime blocks temporarily
        this.register(new Weapon(
                "slimeball",
                Component.text("Slimeball", Styles.STYLE_TEXT).color(Colours.GREEN_LIGHT),
                List.of(),
                Material.SLIME_BALL,
                List.of(
                        WeaponSoundDefinition.of(Sound.ENTITY_SLIME_SQUISH, 1F, 1F)
                ),
                (event) -> {

                }
        ));
    }

    /**
     * Registers a Weapon.
     *
     * @param weapon weapon
     */
    public void register(final @NonNull Weapon weapon) {
        this.weapons.add(weapon);
    }

    /**
     * Returns a weapon with the id {@code id}.
     *
     * @param id id of the weapon
     * @return if there is no weapon with id {@code id}, returns null; otherwise returns the weapon
     */
    public @Nullable Weapon weapon(final @NonNull String id) {
        for (final @NonNull Weapon weapon : weapons) {
            if (weapon.id().equals(id)) {
                return weapon;
            }
        }

        throw new NullPointerException("No weapon with that id could be found!");
    }

    /**
     * {@return the default weapon}
     */
    public @NonNull Weapon defaultWeapon() {
        return this.defaultWeapon;
    }

    /**
     * {@return a Collection containing all registered Bonk weapons}
     */
    public @NonNull List<Weapon> weapons() {
        return List.copyOf(this.weapons);
    }

    /**
     * Checks if {@code itemStack} is a Bonk weapon. If it is, return the Weapon instance.
     *
     * @param itemStack itemStack
     * @return weapon
     */
    public @Nullable Weapon weaponFromItemStack(final @NonNull ItemStack itemStack) {
        if (!itemStack.hasItemMeta()) {
            return null;
        }

        final @NonNull ItemMeta itemMeta = itemStack.getItemMeta();

        final @NonNull PersistentDataContainer container = itemMeta.getPersistentDataContainer();

        if (!container.has(Constants.Keys.ITEM_WEAPON_KEY, PersistentDataType.STRING)) {
            return null;
        }

        final @NonNull String id = Objects.requireNonNull(container.get(Constants.Keys.ITEM_WEAPON_KEY, PersistentDataType.STRING));

        return this.weapon(id);
    }

}
