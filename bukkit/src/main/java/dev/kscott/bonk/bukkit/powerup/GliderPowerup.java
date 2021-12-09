package dev.kscott.bonk.bukkit.powerup;

import broccolai.corn.paper.item.PaperItemBuilder;
import com.google.inject.Inject;
import dev.kscott.bonk.bukkit.BukkitBonkPlugin;
import dev.kscott.bonk.bukkit.utils.PlayerUtils;
import dev.kscott.bonk.bukkit.utils.Styles;
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

public class GliderPowerup implements Powerup {

    private static final @NonNull Component NAME = Component.text("Glider").color(TextColor.color(91, 192, 235));

    private final @NonNull Set<UUID> glidingPlayers;
    private final @NonNull BukkitBonkPlugin plugin;
    private final @NonNull BukkitRunnable gliderRunnable;

    @Inject
    public GliderPowerup(final @NonNull BukkitBonkPlugin plugin) {
        this.glidingPlayers = new HashSet<>();
        this.plugin = plugin;
        this.gliderRunnable = new BukkitRunnable() {
            @Override
            public void run() {
                final var it = glidingPlayers.iterator();

                while (it.hasNext()) {
                    final @NonNull UUID uuid = it.next();

                    final @Nullable Player player = Bukkit.getPlayer(uuid);

                    if (player == null) {
                        it.remove();
                    }

                    if (PlayerUtils.isNearGround(player)) {
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                deactivate(player);
                            }
                        }.runTaskLater(plugin, 0);
                    }
                }
            }
        };

        this.gliderRunnable.runTaskTimer(this.plugin, 0, 0);
    }

    public boolean isGliding(final @NonNull Player player) {
        return this.glidingPlayers.contains(player.getUniqueId());
    }

    @Override
    public boolean activate(@NonNull Player player) {
//        if (PlayerUtils.isNearGround(player) && !PlayerUtils.movingY(player)) {
//            player.setVelocity(player.getLocation().getDirection()
//                    .add(new Vector(0, 1, 0))
//                    .multiply(2)
//                    .normalize()
//            );
//
//            new BukkitRunnable() {
//                @Override
//                public void run() {
//                    makeGliding(player);
//                }
//            }.runTaskLater(this.plugin, 15);
//        } else {
//            makeGliding(player);
//        }

        makeGliding(player);

        return true;
    }

    private void makeGliding(@NonNull Player player) {
        ItemStack fireworkBoost = new ItemStack(Material.FIREWORK_ROCKET);
        FireworkMeta fireworkMeta = (FireworkMeta) fireworkBoost.getItemMeta();
        fireworkMeta.setPower(2);
        fireworkBoost.setItemMeta(fireworkMeta);
        player.sendMessage(Component.text("Set gliding"));
        glidingPlayers.add(player.getUniqueId());
        player.setGliding(true);
        player.boostElytra(fireworkBoost);
    }

    @Override
    public void deactivate(@NonNull Player player) {
        System.out.println("Deactivate called");
        player.sendMessage(Component.text("Deactivate gliding"));
        this.glidingPlayers.remove(player.getUniqueId());
        player.setGliding(false);
    }

    @Override
    public @NonNull Component name() {
        return GliderPowerup.NAME;
    }

    @Override
    public @NonNull ItemStack itemStack() {
        final @NonNull List<Component> lore = new ArrayList<>();

        lore.add(Component.empty());
        lore.add(Component.text("A portable pair of wings that", Styles.STYLE_TEXT));
        lore.add(Component.text("send you flying through the air.", Styles.STYLE_TEXT));
        lore.add(Component.empty());

        lore.addAll(Powerup.CONSUMABLE_LORE_FOOTER);

        return PaperItemBuilder.ofType(Material.ELYTRA)
                .name(NAME)
                .lore(lore)
                .addFlag(ItemFlag.values())
                .setData(Powerup.POWERUP_ITEM_ID_KEY, PersistentDataType.STRING, "glider")
                .build();
    }
}
