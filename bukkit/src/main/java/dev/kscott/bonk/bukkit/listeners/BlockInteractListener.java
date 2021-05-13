package dev.kscott.bonk.bukkit.listeners;

import com.google.inject.Inject;
import dev.kscott.bonk.bukkit.block.BlockService;
import dev.kscott.bonk.bukkit.entity.EntityService;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.Directional;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Random;

/**
 * Handles interactions with blocks.
 */
public class BlockInteractListener implements Listener {

    /**
     * The block service.
     */
    private final @NonNull BlockService blockService;

    /**
     * The entity service.
     */
    private final @NonNull EntityService entityService;

    /**
     * The plugin.
     */
    private final @NonNull JavaPlugin plugin;

    /**
     * Random instance.
     */
    private final @NonNull Random random;

    /**
     * Constructs {@code BlockInteractListener}.
     *
     * @param plugin       the plugin
     * @param blockService the block service
     */
    @Inject
    public BlockInteractListener(
            final @NonNull JavaPlugin plugin,
            final @NonNull BlockService blockService,
            final @NonNull EntityService entityService
    ) {
        this.plugin = plugin;
        this.random = new Random();
        this.blockService = blockService;
        this.entityService = entityService;
    }

    /**
     * Handles exploding pumpkin mechanics.
     *
     * @param event PlayerInteractEvent
     */
    @EventHandler
    public void pumpkinInteract(final @NonNull PlayerInteractEvent event) {
        final @Nullable Block block = event.getClickedBlock();

        if (block == null) {
            return;
        }

        if (block.getType() != Material.JACK_O_LANTERN) {
            return;
        }

        event.setCancelled(true);

        final @NonNull BlockState state = block.getState(true);

        final @NonNull BlockFace pumpkinFace = ((Directional) block.getBlockData()).getFacing();

        block.setType(Material.CARVED_PUMPKIN);

        final @NonNull Directional pumpkinBlockData = ((Directional) block.getBlockData());
        pumpkinBlockData.setFacing(pumpkinFace);

        block.setBlockData(pumpkinBlockData);

        // Regen between 2-4 seconds
        final int regenTime = random.nextInt(80) + 40;

        this.entityService.launchEntities(block.getLocation(), 4, 2, false);

        new BukkitRunnable() {
            @Override
            public void run() {
                state.update(true);
            }
        }.runTaskLater(this.plugin, regenTime);

    }

}
