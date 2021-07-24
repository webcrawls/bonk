package dev.kscott.bonk.bukkit.lobby;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import org.jetbrains.annotations.NotNull;

public class LobbyAudience implements ForwardingAudience {
    @Override
    public @NotNull Iterable<? extends Audience> audiences() {
        return null;
    }
}
