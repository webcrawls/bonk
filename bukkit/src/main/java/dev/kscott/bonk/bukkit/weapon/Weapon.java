package dev.kscott.bonk.bukkit.weapon;

import broccolai.corn.adventure.AdventureItemBuilder;
import broccolai.corn.paper.PaperItemBuilder;
import dev.kscott.bluetils.core.text.Styles;
import dev.kscott.bonk.bukkit.game.Constants;
import dev.kscott.bonk.bukkit.weapon.sound.WeaponSoundDefinition;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;
import java.util.function.Consumer;

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
        @NonNull List<WeaponSoundDefinition> sounds,
        @NonNull Consumer<EntityDamageByEntityEvent> hitHandler
) {
    /**
     * {@return the ItemStack of this weapon}
     */
    public @NonNull ItemStack itemStack() {
        return PaperItemBuilder.paper(this.material)
                .name(Component.text()
                        .append(this.name)
                        .style(Styles.TEXT)
                        .build())
                .loreComponents(this.description)
                .data(Constants.Keys.ITEM_WEAPON_KEY, PersistentDataType.STRING, this.id)
                .build();
    }

    public @NonNull Vector applyVelocity(final @NonNull Vector a, final @NonNull Vector b) {
        return a.subtract(b)
                .normalize()
                .multiply(3);
    }

}