package dev.kscott.bonk.bukkit.listeners;

import com.google.inject.Inject;
import dev.kscott.bonk.bukkit.box.BoxService;
import dev.kscott.bonk.bukkit.game.Constants;
import dev.kscott.bonk.bukkit.player.BonkSpirit;
import dev.kscott.bonk.bukkit.player.PlayerService;
import dev.kscott.bonk.bukkit.powerup.Powerup;
import dev.kscott.bonk.bukkit.powerup.PowerupService;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public class PlayerInteractListeners implements Listener {

    private final @NonNull BoxService boxService;
    private final @NonNull PowerupService powerupService;
    private final @NonNull PlayerService playerService;
    private final @NonNull JavaPlugin plugin;

    @Inject
    public PlayerInteractListeners(final @NonNull BoxService boxService,
                                   final @NonNull PlayerService playerService,
                                   final @NonNull JavaPlugin plugin,
                                   final @NonNull PowerupService powerupService) {
        this.boxService = boxService;
        this.playerService = playerService;
        this.plugin = plugin;
        this.powerupService = powerupService;
    }

    @EventHandler
    public void onInteract(final @NonNull PlayerInteractEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onItemDrop(final @NonNull PlayerDropItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBoxOpen(final @NonNull PlayerInteractEvent event) {
        if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
            final @Nullable Block block = event.getClickedBlock();

            if (block == null) {
                return;
            }

            if (!this.boxService.isBox(block.getLocation())) {
                return;
            }

            final BoxService.@Nullable SpawnedBox box = this.boxService.box(block.getLocation());

            if (box == null) {
                return;
            }

            System.out.println("Opened box method called");

            this.boxService.openBox(event.getPlayer(), box);
        }
    }

    @EventHandler
    public void onHotbarActivate(final @NonNull PlayerInteractEvent event) {
        final @Nullable ItemStack item = event.getItem();

        if (item == null) {
            return;
        }

        if (!item.hasItemMeta()) {
            return;
        }

        final @NonNull ItemMeta meta = item.getItemMeta();

        final @NonNull Action action = event.getAction();

        if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {

            if (meta.getPersistentDataContainer().has(Constants.Keys.ITEM_WEAPON_KEY, PersistentDataType.STRING)) {
                final @NonNull Player player = event.getPlayer();
                final @NonNull BonkSpirit spirit = this.playerService.spirit(player);

                final int rockets = spirit.rockets();

                if (rockets > 0) {
                    shootFirework(player, player.getEyeLocation(), player.getLocation().getDirection(), 15);
                    spirit.removeRocket(1);
                } else {
                    return;
                }

                return;
            }

            if (meta.getPersistentDataContainer().has(Powerup.POWERUP_ITEM_ID_KEY, PersistentDataType.STRING)) {
                this.powerupService.usePowerup(event.getPlayer(), item, event.getPlayer().getInventory().getHeldItemSlot());
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInterfaceClick(final @NonNull InventoryInteractEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onOffhandSwap(final @NonNull PlayerSwapHandItemsEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerInventory(final @NonNull InventoryDragEvent event) {
        if (event.getInventory().getType() != InventoryType.PLAYER) {
            return;
        }

        event.setCancelled(true);
    }

    /**
     * Shoots a firework with the given parameters
     *
     * @param player       Player who shot
     * @param location     Location to spawn firework
     * @param vector       The Firework's vector (direction/speed)
     * @param detonateTime How many ticks to detonate the firework after launching (if the firework hasn't hit anything yet)
     */
    protected void shootFirework(
            final @NonNull Player player,
            final @NonNull Location location,
            final @NonNull Vector vector,
            final int detonateTime
    ) {
        final @NonNull FireworkMeta fireworkMeta = (FireworkMeta) Bukkit.getItemFactory().getItemMeta(Material.FIREWORK_ROCKET);

        player.setCooldown(Material.CROSSBOW, 18);

        // Add effect
        fireworkMeta.addEffect(FireworkEffect
                .builder()
                .with(FireworkEffect.Type.BURST)
                .withColor(Color.AQUA)
                .withFlicker()
                .withTrail()
                .build());

        // Set power
        fireworkMeta.setPower(5);


        // update firework meta
        final @NonNull Firework firework = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
        firework.setFireworkMeta(fireworkMeta);
        firework.setShotAtAngle(true);
        firework.setShooter(player);

        // Load bonk ammo ID into firework entity
        fireworkMeta.getPersistentDataContainer().set(BonkSpirit.AMMO_DATA_KEY, PersistentDataType.STRING, "true");

        firework.setVelocity(vector);

        firework.setFireworkMeta(fireworkMeta);

        new BukkitRunnable() {
            @Override
            public void run() {
                firework.detonate();
            }
        }.runTaskLater(this.plugin, detonateTime);
    }

}
