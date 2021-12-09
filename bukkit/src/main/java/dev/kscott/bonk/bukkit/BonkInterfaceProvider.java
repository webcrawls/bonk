package dev.kscott.bonk.bukkit;

import broccolai.corn.paper.item.PaperItemBuilder;
import dev.kscott.bonk.bukkit.player.PlayerService;
import dev.kscott.bonk.bukkit.utils.Colours;
import dev.kscott.bonk.bukkit.utils.Styles;
import dev.kscott.bonk.bukkit.weapon.Weapon;
import dev.kscott.bonk.bukkit.weapon.WeaponService;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.interfaces.core.arguments.ArgumentKey;
import org.incendo.interfaces.core.arguments.HashMapInterfaceArguments;
import org.incendo.interfaces.core.arguments.InterfaceArguments;
import org.incendo.interfaces.paper.element.ItemStackElement;
import org.incendo.interfaces.paper.pane.ChestPane;
import org.incendo.interfaces.paper.transform.PaperTransform;
import org.incendo.interfaces.paper.type.ChestInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BonkInterfaceProvider {

    public static final @NonNull ItemStack MENU_BG = PaperItemBuilder.ofType(Material.GRAY_STAINED_GLASS_PANE)
            .name(Component.empty())
            .addFlag(ItemFlag.values())
            .build();

    public static final @NonNull ItemStack MENU_DARK_BG = PaperItemBuilder.ofType(Material.BLACK_STAINED_GLASS_PANE)
            .name(Component.empty())
            .addFlag(ItemFlag.values())
            .build();

    public static @NonNull ChestInterface main(final @NonNull PlayerService playerService,
                                               final @NonNull WeaponService weaponService) {
        return ChestInterface.builder()
                .rows(4)
                .title(Component.text("Bonk Menu").color(Colours.BLUE_DARK))
                .addTransform(PaperTransform.chestFill(ItemStackElement.of(MENU_BG)))
                .addTransform((pane, view) -> {
                    for (int i = 0; i < ChestPane.MINECRAFT_CHEST_WIDTH; i++) {
                        pane = pane.element(ItemStackElement.of(MENU_DARK_BG), i, 3);
                    }

                    return pane;
                })
                .addTransform((pane, view) -> {
                    int x = 4;
                    int y = 1;

                    return pane.element(ItemStackElement.of(
                            PaperItemBuilder.ofType(Material.ENDER_EYE)
                                    .addFlag(ItemFlag.values())
                                    .name(Component.text("Play").style(Styles.STYLE_EMPHASIS))
                                    .lore(
                                            Component.empty(),
                                            Component.text("Click to play Bonk.", Styles.STYLE_TEXT),
                                            Component.empty(),
                                            Component.text()
                                                    .append(Component.text("NOTE: ", Styles.STYLE_EMPHASIS.color(Colours.RED_LIGHT)))
                                                    .append(Component.text("It is ", Styles.STYLE_TEXT))
                                                    .append(Component.text("highly recommended ", Styles.STYLE_TEXT).decorate(TextDecoration.BOLD))
                                                    .build(),
                                            Component.text()
                                                    .append(Component.text("to disable ", Styles.STYLE_TEXT))
                                                    .append(Component.text("FOV Effects ", Styles.STYLE_EMPHASIS))
                                                    .append(Component.text("in your ", Styles.STYLE_TEXT))
                                                    .build(),
                                            Component.text()
                                                    .append(Component.text("Video Settings ", Styles.STYLE_EMPHASIS))
                                                    .append(Component.text("before playing Bonk.", Styles.STYLE_TEXT))
                                                    .build(),
                                            Component.empty(),
                                            Component.text()
                                                    .append(Component.text()
                                                            .append(Component.text("There "))
                                                            .append(Component.text(playerService.gamePlayers().size() == 1 ? " is" : " are"))
                                                            .append(Component.text(playerService.gamePlayers().size())
                                                                    .append(Component.text(playerService.gamePlayers().size() == 1 ? " player" : " players")))
                                                            .append(Component.text(" playing."))
                                                            .style(Styles.STYLE_TEXT)
                                                            .build()
                                                    )
                                                    .build()
                                    )
                                    .build(),
                            (ctx) -> playerService.handlePlayerPlay(view.viewer().player())
                    ), x, y);
                })
                .addTransform((pane, view) -> {
                    int x = 4;
                    int y = 3;

                    final @NonNull InterfaceArguments args = view.arguments();
                    final int weaponIndex = args.getOrDefault(ArgumentKey.of("weapon", Integer.class), 0);

//                    final @NonNull Weapon weapon = playerService.player(view.viewer().player()).weapon();
                    final @NonNull List<Weapon> weapons = weaponService.weapons();

                    final @NonNull Weapon weapon = weapons.get(weaponIndex);

                    final @NonNull Component name = Component.text("Your Weapon", Styles.STYLE_EMPHASIS);

                    // TODO Barrier icon for locked weapons
                    final @NonNull Material material = weapon.material();
                    final @NonNull List<Component> lore = new ArrayList<>();

                    lore.add(Component.empty());

                    lore.add(Component.text()
                            .append(Component.text("Selected: ").style(Styles.STYLE_TEXT))
                            .append(weapon.name())
                            .style(Styles.STYLE_TEXT)
                            .color(Colours.GREEN_LIGHT)
                            .build());

                    lore.add(Component.empty());

                    for (final @NonNull Component text : weapon.description()) {
                        lore.add(Component.text()
                                .append(text.style(Styles.STYLE_TEXT))
                                .style(Styles.STYLE_TEXT)
                                .build());
                    }

                    lore.add(Component.empty());
                    lore.add(Component.text("Click to select the next weapon.", Styles.STYLE_TEXT));

                    return pane.element(ItemStackElement.of(
                            PaperItemBuilder.ofType(material)
                                    .addFlag(ItemFlag.values())
                                    .name(name)
                                    .lore(lore)
                                    .build(),
                            (ctx) -> {
                                int tempIndex = weaponIndex + 1;

                                if (tempIndex >= weapons.size()) {
                                    tempIndex = 0;
                                }

                                final int index = tempIndex;

                                playerService.spirit(view.viewer().player()).weapon(weapons.get(index));

                                main(playerService, weaponService).open(view.viewer(), new HashMapInterfaceArguments(Map.of(
                                        ArgumentKey.of("weapon", Integer.class), () -> index
                                )));
                            }
                    ), x, y);
                })
                .addTransform((pane, view) -> {
                    return pane.element(ItemStackElement.of(
                            PaperItemBuilder.ofType(Material.BOOK)
                                    .name(Component.text("Help", Styles.STYLE_EMPHASIS))
                                    .lore(List.of(
                                            Component.empty(),
                                            Component.text("Controls:", Styles.STYLE_TEXT),
                                            Component.text()
                                                    .append(Component.text("- ", Styles.STYLE_TEXT_DARK))
                                                    .append(Component.keybind("key.use", Styles.STYLE_EMPHASIS))
                                                    .append(Component.text(" to fire rockets while", Styles.STYLE_TEXT))
                                                    .build(),
                                            Component.text()
                                                    .append(Component.text("holding your Bonk weapon, or", Styles.STYLE_TEXT))
                                                    .build(),
                                            Component.text()
                                                    .append(Component.text("activate powerups on your hotbar.", Styles.STYLE_TEXT))
                                                    .build(),
                                            Component.empty(),
                                            Component.text()
                                                    .append(Component.text("- ", Styles.STYLE_TEXT_DARK))
                                                    .append(Component.keybind("key.attack", Styles.STYLE_EMPHASIS))
                                                    .append(Component.text(" to attack, or open", Styles.STYLE_TEXT))
                                                    .build(),
                                            Component.text()
                                                    .append(Component.text("item boxes.", Styles.STYLE_TEXT))
                                                    .build(),
                                            Component.empty(),
                                            Component.text()
                                                    .append(Component.text("- ", Styles.STYLE_TEXT_DARK))
                                                    .append(Component.keybind("key.sneak", Styles.STYLE_EMPHASIS))
                                                    .append(Component.text(" while in the air", Styles.STYLE_TEXT))
                                                    .build(),
                                            Component.text()
                                                    .append(Component.text("to double jump.", Styles.STYLE_TEXT))
                                                    .build()
                                    ))
                                    .build()
                    ), 8, 3);
                })
                .clickHandler((ctx) -> ctx.cancel(true))
                .build();
    }

}
