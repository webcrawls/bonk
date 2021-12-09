package dev.kscott.bonk.bukkit.command;

import cloud.commandframework.Command;
import cloud.commandframework.CommandManager;
import cloud.commandframework.arguments.standard.StringArgument;
import cloud.commandframework.context.CommandContext;
import com.google.inject.Inject;
import dev.kscott.bonk.bukkit.lobby.LobbyService;
import dev.kscott.bonk.bukkit.player.BonkSpirit;
import dev.kscott.bonk.bukkit.player.PlayerService;
import dev.kscott.bonk.bukkit.powerup.GliderPowerup;
import dev.kscott.bonk.bukkit.utils.Colours;
import dev.kscott.bonk.bukkit.utils.Styles;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.interfaces.paper.type.BookInterface;

import java.util.Collection;
import java.util.Iterator;

import static net.kyori.adventure.text.Component.*;
import static org.incendo.interfaces.paper.element.TextElement.of;

/**
 * The /bonk command.
 */
public final class BonkCommand implements BaseCommand {

    // "join" mean when a player joins the server, (sent to lobby)
    // "play" means when a player joins the active bonk game (i.e. /bonk play)
    // "leave" is when player leaves server
    // "quit" is when player quits active game

    private final @NonNull PlayerService playerService;
    private final @NonNull LobbyService lobbyService;
    private final @NonNull BookInterface noticeInterface;
    private final @NonNull GliderPowerup gliderPowerup;
    private final @NonNull JavaPlugin plugin;
    private final @NonNull BossBar bossBar;

    /**
     * Constructs {@code BonkCommand}.
     *
     * @param playerService the player service
     */
    @Inject
    public BonkCommand(final @NonNull PlayerService playerService,
                       final @NonNull LobbyService lobbyService,
                       final @NonNull GliderPowerup gliderPowerup,
                       final @NonNull JavaPlugin plugin) {
        this.playerService = playerService;
        this.lobbyService = lobbyService;
        this.gliderPowerup = gliderPowerup;
        this.plugin = plugin;
        this.bossBar = BossBar.bossBar(Component.text("Munch on my balls"), 1, BossBar.Color.BLUE, BossBar.Overlay.NOTCHED_6);
        this.noticeInterface = BookInterface.builder()
                .addTransform((pane, view) -> pane
                        .add(of(text()
                                .append(text("Welcome to Bonk!", Styles.STYLE_TEXT))
                                .append(newline())
                                .append(newline())
                                .append(text()
                                        .append(text("NOTE: Player movement speed is increased in Bonk. Because of this, it is ", Styles.STYLE_TEXT))
                                        .append(text("highly recommended to disable FOV effects in your video settings.", Styles.STYLE_TEXT
                                                .color(Colours.RED_DARK)
                                                .decoration(TextDecoration.BOLD, true)
                                        ))
                                        .build())
                                .append(newline())
                                .append(newline())
                                .append(text("Click here when you're ready to play.")
                                        .style(Styles.STYLE_EMPHASIS)
                                        .hoverEvent(HoverEvent.showText(Component.text("Click to run /bonk play.")))
                                        .clickEvent(ClickEvent.runCommand("/bonk play")))
                                .build()
                        ))
                )
                .build();
    }

    /**
     * Registers /bonk.
     *
     * @param manager CommandManager to register with
     */
    @Override
    public void register(@NonNull CommandManager<@NonNull CommandSender> manager) {
        final Command.Builder<CommandSender> builder = manager.commandBuilder("bonk");

        manager.command(builder.handler(this::handleBonk));

        manager.command(builder.literal("play").handler(this::handleBonkPlay));

        manager.command(builder.literal("leave").handler(this::handleBonkLeave));

        manager.command(builder.literal("players")
                .handler(this::handlePlayers));

        manager.command(builder.literal("minigame")
                .argument(StringArgument.of("minigame"))
                .argument(StringArgument.of("operation"))
        );

        manager.command(builder.literal("on").handler((ctx) -> {
            this.gliderPowerup.activate((Player) ctx.getSender());
        }));

        manager.command(builder.literal("off").handler((ctx) -> {
            ctx.getSender().hideBossBar(bossBar);
        }));
    }

    /**
     * Handles /bonk.
     *
     * @param ctx command context
     */
    private void handleBonk(final @NonNull CommandContext<CommandSender> ctx) {
        final @NonNull CommandSender sender = ctx.getSender();

        if (sender instanceof Player player) {
            playerService.openPlayMenu(player);
        }
    }


    /**
     * Handles /bonk.
     *
     * @param ctx command context
     */
    private void handleBonkPlay(final @NonNull CommandContext<CommandSender> ctx) {
        final @NonNull CommandSender sender = ctx.getSender();

        if (sender instanceof Player player) {
            playerService.handlePlayerPlay(player);
        }
    }

    private void handleBonkLeave(final @NonNull CommandContext<CommandSender> ctx) {
        new BukkitRunnable() {
            @Override
            public void run() {
                final @NonNull CommandSender sender = ctx.getSender();

                if (sender instanceof Player player) {
                    playerService.handlePlayerQuit(player);
                }
            }
        }.runTask(this.plugin);
    }

    /**
     * Handles /bonk players
     *
     * @param ctx command context
     */
    private void handlePlayers(final @NonNull CommandContext<CommandSender> ctx) {
        final @NonNull CommandSender sender = ctx.getSender();

        final @NonNull Collection<BonkSpirit> players = this.playerService.gamePlayers();

        if (players.isEmpty()) {
            sender.sendMessage(text("There are no players playing Bonk!"));
            return;
        }

        @NonNull Component playersComponent = empty();

        final @NonNull Iterator<BonkSpirit> pliterator = players.iterator();

        while (pliterator.hasNext()) {
            final @NonNull BonkSpirit player = pliterator.next();

            playersComponent = playersComponent.append(text(player.name()));

            if (pliterator.hasNext()) {
                playersComponent = playersComponent.append(text(", "));
            }
        }

        sender.sendMessage(text("There are " + players.size() + " player(s) online:"));
        sender.sendMessage(playersComponent);
    }
}
