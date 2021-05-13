package dev.kscott.bonk.bukkit.weapon;

import broccolai.corn.adventure.AdventureItemBuilder;
import dev.kscott.bonk.bukkit.game.Constants;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

/**
 * Represents a playable Bonk weapon.
 *
 * @param id          id of weapon
 * @param name        display name of weapon
 * @param description display description of weapon
 * @param material    material of weapon
 * @param sounds      sounds to play on weapon hit
 */
public record Weapon(
        @NonNull String id,
        @NonNull Component name,
        @NonNull List<Component> description,
        @NonNull Material material,
        @NonNull List<WeaponSoundDefinition> sounds
) {
    /**
     * {@return the ItemStack of this weapon}
     */
    public @NonNull ItemStack itemStack() {
        return AdventureItemBuilder.adventure(this.material)
                .name(this.name)
                .loreComponents(this.description)
                .data(Constants.Keys.ITEM_WEAPON_KEY, PersistentDataType.STRING, this.id)
                .build();
    }

}