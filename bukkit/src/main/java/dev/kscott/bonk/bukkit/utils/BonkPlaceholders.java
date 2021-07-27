package dev.kscott.bonk.bukkit.utils;

import com.google.inject.Inject;
import dev.kscott.bluetils.core.text.Colours;
import dev.kscott.bluetils.core.text.Styles;
import dev.kscott.bonk.bukkit.BukkitBonkPlugin;
import dev.kscott.bonk.bukkit.game.DeathfeedService;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;

public class BonkPlaceholders extends PlaceholderExpansion {

    private final @NonNull BungeeComponentSerializer serializer;
    private final @NonNull DeathfeedService deathfeedService;
    private final @NonNull BukkitBonkPlugin plugin;

    @Inject
    public BonkPlaceholders(final @NonNull BukkitBonkPlugin plugin,
                            final @NonNull DeathfeedService deathfeedService) {
        this.plugin = plugin;
        this.deathfeedService = deathfeedService;
        this.serializer = BungeeComponentSerializer.get();
    }

    @Override
    public @NotNull String getIdentifier() {
        return "bingbonk";
    }

    @Override
    public @NotNull String getAuthor() {
        return "bluely <kscottdev@gmail.com>";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public boolean persist() {
        return true;
    }

    /**
     * Parses the placeholder and returns the requested value.
     *
     * @param player     the player
     * @param identifier the identifier
     * @return the value
     */
    @Override
    public @Nullable String onPlaceholderRequest(final @NonNull Player player, final @NonNull String identifier) {
        if (identifier.equals("server_name")) {
            return TextComponent.toLegacyText(this.serializer.serialize(
                    Component.text("Bonk")
                            .style(Styles.EMPHASIS)
                            .color(Colours.YELLOW)
            ));
        }

        if (identifier.startsWith("kill_feed")) {
            final @NonNull String[] args = identifier.split(":");
            if (args.length == 2) {
                final int line = Integer.parseInt(args[1]);

                return TextComponent.toLegacyText(this.serializer.serialize(this.deathfeedService.message(line)));
            } else {
                return " ";
            }
        }

        if (identifier.startsWith("effects")) {
            final @NonNull String[] args = identifier.split(":");
            if (args.length == 2) {
                final int line = Integer.parseInt(args[1]);

                return " ";
            } else {
                return " ";
            }
        }

        return "";
    }


}
