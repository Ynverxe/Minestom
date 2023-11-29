package net.minestom.server.entity.serialization;

import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@ApiStatus.Experimental
public class EntityProcessorManager {

    private volatile @NotNull FallbackEntityProcessor fallbackEntityProcessor = FallbackEntityProcessor.FAIL;
    private final Map<ProcessorKey, EntityProcessor> entityProcessors = new ConcurrentHashMap<>();

    public @NotNull EntityProcessorManager registerProcessor(
            @NotNull NamespaceID key, @NotNull EntityProcessor processor) throws IllegalArgumentException {
        EntityProcessorType<?> type;

        if (processor instanceof EntityFactory) {
            type = EntityProcessorType.FACTORY;
        } else if (processor instanceof EntitySerializer) {
            type = EntityProcessorType.SERIALIZER;
        } else {
            throw new IllegalArgumentException("Invalid Entity Processor");
        }

        this.entityProcessors.put(new ProcessorKey(key, type), processor);
        return this;
    }

    public <T extends EntityProcessor> @Nullable EntityProcessor removeProcessor(
            @NotNull NamespaceID key, @NotNull EntityProcessorType<T> type) {
        return this.entityProcessors.remove(new ProcessorKey(key, type));
    }

    public <T extends EntityProcessor> @Nullable T findProcessor(@NotNull NamespaceID key, @NotNull EntityProcessorType<T> type) {
        return type.cast(entityProcessors.get(new ProcessorKey(key, type)));
    }

    public <T extends EntityProcessor> @NotNull Optional<T> findOptionalProcessor(@NotNull NamespaceID key, @NotNull EntityProcessorType<T> type) {
        return Optional.ofNullable(findProcessor(key, type));
    }

    public @NotNull Map<ProcessorKey, EntityProcessor> registeredProcessors() {
        return entityProcessors;
    }

    public @NotNull EntityProcessorManager setFallbackEntityProcessor(@NotNull FallbackEntityProcessor fallbackEntityProcessor) {
        this.fallbackEntityProcessor = fallbackEntityProcessor;
        return this;
    }

    public @NotNull FallbackEntityProcessor fallbackEntityProcessor() {
        return fallbackEntityProcessor;
    }

    public record ProcessorKey(NamespaceID key, EntityProcessorType<?> type) {
        @Override
        public boolean equals(Object object) {
            if (this == object) return true;
            if (object == null || getClass() != object.getClass()) return false;
            ProcessorKey that = (ProcessorKey) object;
            return Objects.equals(key, that.key) && Objects.equals(type, that.type);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key, type);
        }
    }
}