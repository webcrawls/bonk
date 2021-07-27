package dev.kscott.bonk.bukkit.powerup;

import broccolai.corn.paper.PaperItemBuilder;
import com.google.inject.Inject;
import dev.kscott.bluetils.core.text.Colours;
import dev.kscott.bluetils.core.text.Styles;
import dev.kscott.bonk.bukkit.BukkitBonkPlugin;
import dev.kscott.bonk.bukkit.game.Constants;
import dev.kscott.bonk.bukkit.utils.PlayerUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.*;


public class FeatherPowerup implements Powerup {

    // Lightfoot
    private static final @NonNull Component NAME = Component.text("Feather").color(Colours.GRAY_LIGHT);

    @Override
    public boolean activate(@NonNull Player player) {
        player.removePotionEffect(Constants.Potions.FEATHER.getType());
        player.addPotionEffect(Constants.Potions.FEATHER);

        return true;
    }

    @Override
    public void deactivate(@NonNull Player player) {
    }

    @Override
    public @NonNull Component name() {
        return FeatherPowerup.NAME;
    }

    @Override
    public @NonNull ItemStack itemStack() {
        final @NonNull List<Component> lore = new ArrayList<>();

        lore.add(Component.empty());
        lore.add(Component.text("A feather that makes you feel lighter", Styles.TEXT));
        lore.add(Component.text("while holding it.", Styles.TEXT));
        lore.add(Component.empty());

        lore.addAll(Powerup.CONSUMABLE_LORE_FOOTER);

        return PaperItemBuilder.paper(Material.FEATHER)
                .name(NAME)
                .loreComponents(lore)
                .flags(ItemFlag.values())
                .data(Powerup.POWERUP_ITEM_ID_KEY, PersistentDataType.STRING, "feather")
                .build();
    }
}
