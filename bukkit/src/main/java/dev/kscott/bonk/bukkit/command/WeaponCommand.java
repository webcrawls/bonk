package dev.kscott.bonk.bukkit.command;

import cloud.commandframework.ArgumentDescription;
import cloud.commandframework.Command;
import cloud.commandframework.CommandManager;
import cloud.commandframework.arguments.standard.StringArgument;
import cloud.commandframework.context.CommandContext;
import com.google.inject.Inject;
import dev.kscott.bonk.bukkit.weapon.Weapon;
import dev.kscott.bonk.bukkit.weapon.WeaponService;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collection;

/**
 * A command to get all registered weapons.
 */
public final class WeaponCommand implements BaseCommand {

    private final @NonNull WeaponService weaponService;

    /**
     * Constructs {@code WeaponCommand}.
     *
     * @param weaponService the weapon service
     */
    @Inject
    public WeaponCommand(
            final @NonNull WeaponService weaponService
    ) {
        this.weaponService = weaponService;
    }

    /**
     * Registers /weapons.
     *
     * @param manager CommandManager to register with
     */
    @Override
    public void register(@NonNull CommandManager<@NonNull CommandSender> manager) {
        final Command.Builder<@NonNull CommandSender> builder = manager.commandBuilder("weapons");

        manager.command(builder.handler(this::handleWeapons));

        manager.command(builder.literal("get", ArgumentDescription.of("Get a Bonk weapon"))
                .argument(StringArgument.of("id"))
                .handler(this::handleWeaponsGet)
        );
    }

    /**
     * Handles /weapons.
     *
     * @param ctx the command context
     */
    private void handleWeapons(final @NonNull CommandContext<@NonNull CommandSender> ctx) {
        final @NonNull CommandSender sender = ctx.getSender();

        final @NonNull Collection<Weapon> weapons = this.weaponService.weapons();

        for (final @NonNull Weapon weapon : weapons) {
            sender.sendMessage(
                    Component.text(weapon.id() + ": ")
                            .append(weapon.name())
                            .append(Component.text(", "))
            );
        }
    }

    /**
     * Handles /weapons get.
     *
     * @param ctx the command context
     */
    private void handleWeaponsGet(final @NonNull CommandContext<@NonNull CommandSender> ctx) {
        final @NonNull CommandSender sender = ctx.getSender();

        if (!(sender instanceof final @NonNull Player player)) {
            sender.sendMessage(Component.text("// TODO Must be a player!"));
            return;
        }

        final @NonNull String id = ctx.get("id");

        final @Nullable Weapon weapon = this.weaponService.weapon(id);

        if (weapon == null) {
            sender.sendMessage(Component.text("There is no weapon with the id '" + id + "'. Get all weapon ids with /weapons."));
            return;
        }

        final @NonNull ItemStack itemStack = weapon.itemStack();

        player.getInventory().addItem(itemStack);
    }
}
