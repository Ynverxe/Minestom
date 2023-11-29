package net.minestom.server.entity.serialization;

import org.jetbrains.annotations.NotNull;

public final class EntityProcessorType<T extends EntityProcessor> {

    public static final EntityProcessorType<EntitySerializer> SERIALIZER = new EntityProcessorType<>();
    public static final EntityProcessorType<EntityFactory> FACTORY = new EntityProcessorType<>();

    private EntityProcessorType() {}

    @SuppressWarnings("unchecked")
    public @NotNull T cast(@NotNull EntityProcessor processor) {
        return (T) processor;
    }
}