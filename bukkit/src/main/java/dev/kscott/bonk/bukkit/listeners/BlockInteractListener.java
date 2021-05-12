package dev.kscott.bonk.bukkit.listeners;

import com.google.inject.Inject;
import dev.kscott.bonk.bukkit.block.BlockService;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Handles interactions with blocks.
 */
public class BlockInteractListener implements Listener {

    /**
     * The block service.
     */
    private final @NonNull BlockService blockService;

    /**
     * The plugin.
     */
    private final @NonNull JavaPlugin plugin;

    /**
     * Constructs {@code BlockInteractListener}.
     *
     * @param plugin       the plugin
     * @param blockService the block service
     */
    @Inject
    public BlockInteractListener(
            final @NonNull JavaPlugin plugin,
            final @NonNull BlockService blockService
    ) {
        this.plugin = plugin;
        this.blockService = blockService;
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

        final @NonNull BlockData blockData = block.getBlockData().clone();
        final @NonNull Directional directionalBlockData = (Directional) blockData;
        final @NonNull BlockFace face = directionalBlockData.getFacing();

        block.setType(Material.CARVED_PUMPKIN);
    }

}
