package dev.kscott.bonk.bukkit.weapon;

import broccolai.corn.adventure.AdventureItemBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collections;
import java.util.List;

/**
 * Represents a playable Bonk weapon.
 */
public class Weapon {

    /**
     * The id of this weapon.
     */
    private final @NonNull String id;

    /**
     * The name of the weapon.
     */
    private final @NonNull Component name;

    /**
     * The description of the weapon.
     */
    private final @NonNull List<Component> description;

    /**
     * The material of the weapon.
     */
    private final @NonNull Material material;

    /**
     * The key of the weapon, for use in PDC operations.
     */
    private final @NonNull NamespacedKey weaponKey;

    /**
     * The sounds of the weapon.
     */
    private final @NonNull List<WeaponSoundDefinition> sounds;

    /**
     * Constructs Weapon.
     *
     * @param id          id
     * @param name        name
     * @param description description
     * @param material    material
     * @param sounds      sounds to play when used
     * @param weaponKey   namespaced key
     */
    public Weapon(
            final @NonNull String id,
            final @NonNull Component name,
            final @NonNull List<Component> description,
            final @NonNull Material material,
            final @NonNull List<WeaponSoundDefinition> sounds,
            final @NonNull NamespacedKey weaponKey
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.material = material;
        this.weaponKey = weaponKey;
        this.sounds = sounds;
    }


    /**
     * {@return the id}
     */
    public @NonNull String id() {
        return id;
    }

    /**
     * {@return the name}
     */
    public @NonNull Component name() {
        return name;
    }

    /**
     * {@return the description}
     */
    public @NonNull List<Component> description() {
        return Collections.unmodifiableList(this.description);
    }

    /**
     * {@return the weapon sounds}
     */
    public @NonNull List<WeaponSoundDefinition> sounds() {
        return Collections.unmodifiableList(this.sounds);
    }

    /**
     * {@return the ItemStack of this weapon}
     */
    public @NonNull ItemStack itemStack() {
        return AdventureItemBuilder.adventure(this.material)
                .name(this.name)
                .loreComponents(this.description)
                .data(this.weaponKey, PersistentDataType.STRING, "")
                .build();
    }
}