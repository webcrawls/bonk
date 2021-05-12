package dev.kscott.bonk.bukkit.weapon;

import com.google.inject.Inject;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Handles weapons.
 */
public class WeaponService {

    /**
     * The plugin.
     */
    private final @NonNull JavaPlugin plugin;

    /**
     * The id->weapon map.
     */
    private final @NonNull Map<@NonNull String, @NonNull Weapon> weaponMap;

    /**
     * Constructs {@link WeaponService}.
     *
     * @param plugin the plugin
     */
    @Inject
    public WeaponService(
            final @NonNull JavaPlugin plugin
    ) {
        this.plugin = plugin;
        this.weaponMap = new HashMap<>();
    }

    /**
     * Registers a Weapon.
     *
     * @param weapon weapon
     */
    public void register(final @NonNull Weapon weapon) {
        this.weaponMap.put(weapon.id(), weapon);
    }

}
