package dev.kscott.bonk.bukkit.utils;

import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.checkerframework.checker.nullness.qual.NonNull;

public class Styles {

    public static final @NonNull Style STYLE_TEXT = Style.style()
            .color(Colours.GRAY_LIGHT)
            .decoration(TextDecoration.BOLD, false)
            .decoration(TextDecoration.ITALIC, false)
            .decoration(TextDecoration.UNDERLINED, false)
            .decoration(TextDecoration.OBFUSCATED, false)
            .decoration(TextDecoration.STRIKETHROUGH, false)
            .build();

    public static final @NonNull Style STYLE_TEXT_DARK = Style.style()
            .color(Colours.GRAY_DARK)
            .decoration(TextDecoration.BOLD, false)
            .decoration(TextDecoration.ITALIC, false)
            .decoration(TextDecoration.UNDERLINED, false)
            .decoration(TextDecoration.OBFUSCATED, false)
            .decoration(TextDecoration.STRIKETHROUGH, false)
            .build();

    public static final @NonNull Style STYLE_EMPHASIS = Style.style()
            .color(Colours.BLUE_LIGHT)
            .decoration(TextDecoration.BOLD, true)
            .merge(STYLE_TEXT, Style.Merge.Strategy.IF_ABSENT_ON_TARGET)
            .build();

}
