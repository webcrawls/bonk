package dev.kscott.bonk.bukkit.powerup;

import broccolai.corn.paper.item.PaperItemBuilder;
import dev.kscott.bonk.bukkit.game.Constants;
import dev.kscott.bonk.bukkit.utils.Colours;
import dev.kscott.bonk.bukkit.utils.Styles;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;


public class LevitationPowerup implements Powerup {

    // Lightfoot
    private static final @NonNull Component NAME = Component.text("Levitation").color(Colours.GRAY_LIGHT);

    @Override
    public boolean activate(@NonNull Player player) {
        player.removePotionEffect(Constants.Potions.LEVITATION.getType());
        player.addPotionEffect(Constants.Potions.LEVITATION);

        return true;
    }

    @Override
    public void deactivate(@NonNull Player player) {
    }

    @Override
    public @NonNull Component name() {
        return LevitationPowerup.NAME;
    }

    @Override
    public @NonNull ItemStack itemStack() {
        final @NonNull List<Component> lore = new ArrayList<>();

        lore.add(Component.empty());
        lore.add(Component.text("A strange object that makes you hover.", Styles.STYLE_TEXT));
        lore.add(Component.empty());

        lore.addAll(Powerup.CONSUMABLE_LORE_FOOTER);

        return PaperItemBuilder.ofType(Material.PHANTOM_MEMBRANE)
                .name(NAME)
                .lore(lore)
                .addFlag(ItemFlag.values())
                .setData(Powerup.POWERUP_ITEM_ID_KEY, PersistentDataType.STRING, "levitation")
                .build();
    }
}
