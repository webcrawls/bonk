package dev.kscott.bonk.bukkit.powerup;

import broccolai.corn.paper.item.PaperItemBuilder;
import com.google.inject.Inject;


import dev.kscott.bonk.bukkit.player.BonkSpirit;
import dev.kscott.bonk.bukkit.player.PlayerService;
import dev.kscott.bonk.bukkit.player.damage.DamageContext;
import dev.kscott.bonk.bukkit.player.damage.PlayerDamageContext;
import dev.kscott.bonk.bukkit.utils.Colours;
import dev.kscott.bonk.bukkit.utils.Styles;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import javax.print.attribute.standard.MediaSize;

public class ReversePowerup implements Powerup {

    private static final @NonNull Component NAME = Component.text("Reverse").color(Colours.PURPLE_LIGHT);

    private final @NonNull PlayerService playerService;

    @Inject
    public ReversePowerup(final @NonNull PlayerService playerService) {
        this.playerService = playerService;
    }

    @Override
    public boolean activate(@NonNull Player player) {
        final @NonNull BonkSpirit spirit = this.playerService.spirit(player);

        final @Nullable Player attacker = lastAttacker(spirit);

        System.out.println("last attacker: "+attacker);

        if (attacker == null) {
            return false;
        }

        final @NonNull Location playerLocation = player.getLocation();
        final @NonNull Location attackerLocation = attacker.getLocation();

        final float playerDistance = player.getFallDistance();
        final float attackerDistance = attacker.getFallDistance();

        player.teleport(attackerLocation);
        player.setFallDistance(attackerDistance);

        attacker.teleport(playerLocation);
        attacker.setFallDistance(playerDistance);

        final @NonNull Component attackerMessage = Component.text()
                .append(Component.text("REVERSE! ", Styles.STYLE_EMPHASIS).color(Colours.PINK_LIGHT))
                .append(Component.text("You switched places with "))
                .append(
                        Component.text()
                                .append(attacker.displayName())
                                .style(Styles.STYLE_TEXT)
                )
                .append(Component.text("."))
                .style(Styles.STYLE_TEXT)
                .build();

        final @NonNull Component victimMessage = Component.text()
                .append(Component.text("REVERSE! ", Styles.STYLE_EMPHASIS).color(Colours.PINK_LIGHT))
                .append(Component.text("You switched places with "))
                .append(
                        Component.text()
                                .append(attacker.displayName())
                                .style(Styles.STYLE_TEXT)
                )
                .append(Component.text("."))
                .style(Styles.STYLE_TEXT)
                .build();

        attacker.sendMessage(attackerMessage);
        player.sendMessage(victimMessage);

        return true;
    }

    private @Nullable Player lastAttacker(final @NonNull BonkSpirit spirit) {
        for (final @NonNull DamageContext damage : spirit.damageList()) {
            System.out.println(damage);
            if (damage instanceof PlayerDamageContext playerDamageContext) {
                return playerDamageContext.attacker();
            }
        }

        return null;
    }

    @Override
    public void deactivate(@NonNull Player player) {

    }

    @Override
    public @NonNull Component name() {
        return NAME;
    }

    @Override
    public @NonNull ItemStack itemStack() {
        return PaperItemBuilder.ofType(Material.MAGENTA_GLAZED_TERRACOTTA)
                .name(NAME.style(Styles.STYLE_TEXT))
                .addFlag(ItemFlag.values())
                .setData(Powerup.POWERUP_ITEM_ID_KEY, PersistentDataType.STRING, "reverse")
                .build();
    }
}
