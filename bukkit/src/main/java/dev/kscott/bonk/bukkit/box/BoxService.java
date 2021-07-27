package dev.kscott.bonk.bukkit.box;

import com.destroystokyo.paper.ParticleBuilder;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import dev.kscott.bluetils.core.text.Colours;
import dev.kscott.bluetils.core.text.Styles;
import dev.kscott.bonk.bukkit.BukkitBonkPlugin;
import dev.kscott.bonk.bukkit.player.PlayerService;
import dev.kscott.bonk.bukkit.powerup.PowerupService;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Singleton
public class BoxService {

    private final @NonNull PlayerService playerService;
    private final @NonNull BukkitBonkPlugin plugin;
    private final @NonNull Random random;
    private final @NonNull List<Location> boxLocations;
    private final @NonNull PowerupService powerupService;
    private final @NonNull List<SpawnedBox> spawnedBoxes;

    @Inject
    public BoxService(final @NonNull PlayerService playerService,
                      final @NonNull BukkitBonkPlugin plugin,
                      final @NonNull PowerupService powerupService,
                      final @NonNull @Named("gameWorld") World world) {
        this.playerService = playerService;
        this.plugin = plugin;
        this.powerupService = powerupService;

        this.boxLocations = List.of(
                new Location(world, -79, 103, -126),
                new Location(world, -79, 105, -91)
        );

        this.random = new Random();
        this.spawnedBoxes = new ArrayList<>();
    }

    public void init() {
        for (final @NonNull Location location : boxLocations) {
            spawnBox(location);
        }
    }

    public void cleanup() {
        for (final @NonNull SpawnedBox spawnedBox : spawnedBoxes) {
            despawn(spawnedBox);
        }
    }

    private void spawnBox(final @NonNull Location location) {
        final @NonNull List<BoxType> types = Arrays.asList(BoxType.values());

        final int index = this.random.nextInt(BoxType.values().length);

        var type = types.get(index);

        final @NonNull Block block = location.getBlock();

        final @NonNull BlockState blockState = block.getState();

        block.setType(type.material);

        new ParticleBuilder(Particle.CLOUD)
                .location(location)
                .offset(1.5, 1.5, 1.5)
                .count(15)
                .spawn();

        this.spawnedBoxes.add(new SpawnedBox(location, blockState, type));
    }

    public boolean isBox(final @NonNull Location location) {
        return box(location) != null;
    }

    public @Nullable SpawnedBox box(final @NonNull Location location) {
        for (final @NonNull SpawnedBox box : spawnedBoxes) {
            if (location.getBlockX() == box.location().getBlockX() &&
                    location.getBlockY() == box.location().getBlockY() &&
                    location.getBlockZ() == box.location().getBlockZ()
            ) {
                return box;
            }
        }

        return null;
    }

    public void openBox(final @NonNull Player player, final @NonNull SpawnedBox box) {
        final @NonNull Location location = box.location();

        // TODO Play sound
        location.getWorld().playSound(
                Sound.sound(
                        org.bukkit.Sound.ENTITY_FIREWORK_ROCKET_LAUNCH,
                        Sound.Source.PLAYER,
                        1,
                        1
                ),
                location.getX(),
                location.getY(),
                location.getZ()
        );

        despawn(box);

        new ParticleBuilder(Particle.REDSTONE)
                .offset(0.7, 0.7, 0.5)
                .location(location)
                .color(12, 14, 133)
                .count(10)
                .spawn()
                .color(224, 166, 237)
                .count(20)
                .spawn();
        // TODO Display particles

        if (box.type() == BoxType.POWERUP) {
            this.powerupService.addRandomPowerup(player);
        } else if (box.type() == BoxType.AMMO) {
            final int count = this.random.nextInt(3) + 2;

            this.playerService.spirit(player).addRocket(count);

            player.sendMessage(Component.text()
                    .append(Component.text("GET ", Styles.EMPHASIS).color(Colours.GREEN_DARK))
                    .append(Component.text("+" + count + " ").color(Colours.BLUE_LIGHT))
                    .append(Component.text("rockets", Styles.TEXT)));
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                spawnBox(location);
            }
        }.runTaskLater(this.plugin, this.random.nextInt(20 * 10) + (20 * 10));
    }

    private void despawn(final @NonNull SpawnedBox box) {
        box.oldState().update(true);
        box.location().getBlock().setType(Material.AIR);
        this.spawnedBoxes.remove(box);
    }


    public enum BoxType {
        POWERUP(Component.text("Powerup Box"), Material.BLUE_SHULKER_BOX),
        AMMO(Component.text("Ammo box"), Material.GREEN_SHULKER_BOX);

        private final @NonNull Component title;
        private final @NonNull Material material;

        BoxType(final @NonNull Component title,
                final @NonNull Material material) {
            this.title = title;
            this.material = material;
        }

        public Component title() {
            return title;
        }

        public Material material() {
            return material;
        }
    }

    public static class SpawnedBox {

        private final @NonNull BlockState oldState;
        private final @NonNull Location location;
        private final @NonNull BoxType type;

        public SpawnedBox(final @NonNull Location location,
                          final @NonNull BlockState oldState,
                          final @NonNull BoxType type) {
            this.location = location;
            this.oldState = oldState;
            this.type = type;
        }

        public @NonNull Location location() {
            return this.location;
        }

        public @NonNull BoxType type() {
            return this.type;
        }

        public @NonNull BlockState oldState() {
            return this.oldState;
        }
    }

}
