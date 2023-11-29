package net.minestom.server.entity.serialization;

import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;

public record EntityProcessorExpansion(
        @NotNull NamespaceID key,
        @NotNull EntityFactory factory,
        @NotNull EntitySerializer serializer
) {}
