package dev.kscott.bonk.bukkit.powerup;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;


import dev.kscott.bonk.bukkit.player.PlayerService;
import dev.kscott.bonk.bukkit.utils.Colours;
import dev.kscott.bonk.bukkit.utils.Styles;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Singleton
public class PowerupService {

    private final @NonNull PlayerService playerService;
    private final @NonNull Map<String, Powerup> powerupMap;
    private final @NonNull Random random;

    @Inject
    public PowerupService(final @NonNull Injector injector,
                          final @NonNull PlayerService playerService) {
        this.playerService = playerService;
        this.random = new Random();
        this.powerupMap = new HashMap<>();

        this.powerupMap.put("glider", injector.getInstance(GliderPowerup.class));
        this.powerupMap.put("reverse", injector.getInstance(ReversePowerup.class));
        this.powerupMap.put("levitation", injector.getInstance(LevitationPowerup.class));
        this.powerupMap.put("feather", injector.getInstance(FeatherPowerup.class));
    }

    public @NonNull Powerup powerup(final @NonNull String powerupId) {
        return this.powerupMap.get(powerupId);
    }

    public @NonNull String id(final @NonNull Powerup powerup) {
        for (final var entry : powerupMap.entrySet()) {
            if (entry.getValue().equals(powerup)) {
                return entry.getKey();
            }
        }

        throw new NullPointerException("No powerup was found");
    }

    public void addPowerup(final @NonNull Player player,
                           final @NonNull String powerupId) {
        final @NonNull Powerup powerup = this.powerupMap.get(powerupId);

        final @NonNull ItemStack itemStack = powerup.itemStack();

        final @NonNull PowerupEntry entry = new PowerupEntry(powerupId, itemStack);

        player.sendMessage(
                Component.text()
                        .append(Component.text("POWERUP! ", Styles.STYLE_EMPHASIS).color(Colours.PURPLE_LIGHT))
                        .append(Component.text("You received "))
                        .append(powerup.name())
                        .append(Component.text("."))
                        .style(Styles.STYLE_TEXT)
        );

        this.playerService.spirit(player).addPowerup(entry);
    }

    public void addRandomPowerup(final @NonNull Player player) {
        final int size = this.powerupMap.size();

        final int index = this.random.nextInt(size);

        int count = 0;

        for (final var entry : this.powerupMap.entrySet()) {
            if (count >= index) {
                this.addPowerup(player, entry.getKey());
                System.out.println("Found "+entry.getKey()+" at index "+index+" (count: "+count+")");
                return;
            }

            count++;
        }
    }

    /**
     * Activates a powerup at the clicked slot
     * @param player the player
     * @param itemStack the item the player was holding
     * @param itemSlot the slot of the item
     */
    public void usePowerup(final @NonNull Player player,
                           final ItemStack itemStack,
                           final int itemSlot) {
        if (!itemStack.hasItemMeta()) {
            return;
        }

        final @NonNull ItemMeta itemMeta = itemStack.getItemMeta();

        if (!itemMeta.getPersistentDataContainer().has(Powerup.POWERUP_ITEM_ID_KEY, PersistentDataType.STRING)) {
            return;
        }

        final @NonNull String powerupId = itemMeta.getPersistentDataContainer().get(Powerup.POWERUP_ITEM_ID_KEY, PersistentDataType.STRING);

        final @NonNull Powerup powerup = this.powerup(powerupId);

        final boolean success = powerup.activate(player);

        if (!success) {
            return;
        }

        this.playerService.spirit(player).removePowerup(itemSlot-2);
    }

}
