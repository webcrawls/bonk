package dev.kscott.bonk.bukkit.powerup;

import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;

public class PowerupEntry {

    private final @NonNull String powerupId;
    private final @NonNull ItemStack itemStack;

    public PowerupEntry(final @NonNull String powerupId,
                        final @NonNull ItemStack itemStack) {
        this.powerupId = powerupId;
        this.itemStack = itemStack;
    }

    public String id() {
        return powerupId;
    }

    public ItemStack itemStack() {
        return itemStack;
    }
}
