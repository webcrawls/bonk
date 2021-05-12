package dev.kscott.bonk.bukkit.weapon;

import com.google.inject.Inject;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.*;

/**
 * Handles weapons.
 */
public class WeaponService {

    /**
     * The id->weapon map.
     */
    private final @NonNull Map<@NonNull String, @NonNull Weapon> weaponMap;

    /**
     * The key to use for weapon ids.
     */
    private final @NonNull NamespacedKey weaponKey;

    /**
     * The default weapon.
     */
    private final @NonNull Weapon defaultWeapon;

    /**
     * Constructs {@link WeaponService}.
     *
     * @param plugin the plugin
     */
    @Inject
    public WeaponService(
            final @NonNull JavaPlugin plugin
    ) {
        this.weaponMap = new HashMap<>();
        this.weaponKey = new NamespacedKey(plugin, "weapon");

        this.registerDefaults();

        this.defaultWeapon = this.weaponMap.entrySet().iterator().next().getValue();
    }

    /**
     * Registers default weapons.
     */
    private void registerDefaults() {
        this.register(new Weapon(
                "stick",
                Component.text("Stick"),
                List.of(Component.text("The original Bonk stick, circa 2019.")),
                Material.STICK,
                List.of(new WeaponSoundDefinition(Sound.BLOCK_BAMBOO_BREAK, 1, 1)),
                this.weaponKey
        )); // Stick

        this.register(new Weapon(
                "blaze",
                Component.text("Blaze"),
                List.of(Component.text("The Bonk stick, with a bit more flare.")),
                Material.BLAZE_ROD,
                List.of(new WeaponSoundDefinition(Sound.ENTITY_BLAZE_HURT, 0.2F, 1)),
                this.weaponKey
        )); // Blaze
    }

    /**
     * Registers a Weapon.
     *
     * @param weapon weapon
     */
    public void register(final @NonNull Weapon weapon) {
        this.weaponMap.put(weapon.id(), weapon);
    }

    /**
     * Returns a weapon with the id {@code id}.
     *
     * @param id id of the weapon
     * @return if there is no weapon with id {@code id}, returns null; otherwise returns the weapon
     */
    public @Nullable Weapon weapon(final @NonNull String id) {
        return this.weaponMap.get(id);
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
    public @NonNull Collection<Weapon> weapons() {
        return Collections.unmodifiableCollection(this.weaponMap.values());
    }

}
