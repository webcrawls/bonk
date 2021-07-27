package dev.kscott.bonk.bukkit.powerup;

import dev.kscott.bluetils.core.text.Styles;
import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

/**
 * Represents a Bonk power up.
 */
public interface Powerup {

    @NonNull List<Component> CONSUMABLE_LORE_FOOTER = List.of(
            Component.text()
                    .append(Component.text("Use "))
                    .append(Component.keybind("key.use", Styles.EMPHASIS))
                    .append(Component.text(" to"))
                    .style(Styles.TEXT)
                    .build(),
            Component.text()
                    .append(Component.text("activate this powerup."))
                    .style(Styles.TEXT)
                    .build()
    );

    @NonNull NamespacedKey POWERUP_ITEM_ID_KEY = NamespacedKey.fromString("bonk_powerup_item_id");

    /**
     * Activates this power up for {@code player}.
     *
     * @param player player
     * @return true if the powerup could activate, false if not
     */
    boolean activate(final @NonNull Player player);

    void deactivate(final @NonNull Player player);

    @NonNull Component name();

    @NonNull ItemStack itemStack();

}
