package dev.kscott.bonk.bukkit.weapon;

import com.google.inject.Inject;
import dev.kscott.bluetils.core.text.Colours;
import dev.kscott.bluetils.core.text.Styles;
import dev.kscott.bonk.bukkit.game.Constants;
import dev.kscott.bonk.bukkit.weapon.sound.WeaponSoundDefinition;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Handles weapons.
 */
public class WeaponService {

    /**
     * The id->weapon map.
     */
    private final @NonNull List<@NonNull Weapon> weapons;

    /**
     * The default weapon.
     */
    private final @NonNull Weapon defaultWeapon;

    /**
     * Constructs {@link WeaponService}.
     */
    @Inject
    public WeaponService() {
        this.weapons = new ArrayList<>();

        this.registerDefaults();

        this.defaultWeapon = Objects.requireNonNull(this.weapon("stick"));
    }

    /**
     * Registers default weapons.
     */
    private void registerDefaults() {
        this.register(new Weapon(
                "stick",
                Component.text("Stick", Colours.GRAY_DARK),
                List.of(Component.text("The original Bonk stick, circa 2019.", Styles.TEXT)),
                Material.STICK,
                List.of(WeaponSoundDefinition.of(Sound.BLOCK_BAMBOO_BREAK, 0.2F, 1))
        )); // Stick

        this.register(new Weapon(
                "blaze",
                Component.text("Blaze", Colours.ORANGE),
                List.of(Component.text()
                                .append(Component.text("⚠").color(Colours.RED_DARK))
                                .append(Component.text(" WARNING ", Styles.EMPHASIS).color(Colours.RED_LIGHT))
                                .append(Component.text("⚠").color(Colours.RED_DARK))
                                .build(),
                        Component.text("May create third-degree burns...", Styles.TEXT)
                ),
                Material.BLAZE_ROD,
                List.of(WeaponSoundDefinition.of(Sound.ENTITY_BLAZE_HURT, 0.2F, 1))
        )); // Blaze

        this.register(new Weapon(
                "emerald",
                Component.text("Emerald", Colours.GREEN_LIGHT),
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
                )
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
