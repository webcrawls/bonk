package dev.kscott.bonk.bukkit.utils;

import net.kyori.adventure.text.format.TextColor;
import org.checkerframework.checker.nullness.qual.NonNull;

import static net.kyori.adventure.text.format.TextColor.color;
import static net.kyori.adventure.util.HSVLike.of;

/**
 * Static values for text colours.
 */
public class Colours {

    public static final @NonNull TextColor WHITE = color(255, 255, 255);
    public static final @NonNull TextColor BLACK = color(0);

    // Light colours (pastels)
    public static final @NonNull TextColor GRAY_LIGHT = color(185, 185,185);
    public static final @NonNull TextColor RED_LIGHT = color(of(0, 26, 93));
    public static final @NonNull TextColor YELLOW = color(of(55, 28, 93));
    public static final @NonNull TextColor GREEN_LIGHT = color(of(114, 28, 94));
    public static final @NonNull TextColor BLUE_LIGHT = color(of(55, 28, 93));
    public static final @NonNull TextColor PURPLE_LIGHT = color(of(281, 28, 93));
    public static final @NonNull TextColor PINK_LIGHT = color(of(336, 28, 93));

    // Dark colours
    public static final @NonNull TextColor GRAY_DARK = color(65, 65, 65);
    public static final @NonNull TextColor RED_DARK = color(of(0, 73, 52));
    public static final @NonNull TextColor ORANGE = color(of(33, 89, 88));
    public static final @NonNull TextColor GREEN_DARK = color(of(114, 73, 52));
    public static final @NonNull TextColor BLUE_DARK = color(of(55, 73, 52));
    public static final @NonNull TextColor PURPLE_DARK = color(of(281, 73, 52));
    public static final @NonNull TextColor PINK_DARK = color(of(336, 73, 52));

}
