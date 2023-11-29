package net.minestom.server.entity.serialization;

import net.minestom.server.entity.Entity;
import net.minestom.server.instance.Chunk;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jglrxavpok.hephaistos.nbt.NBTCompound;

public interface EntitySerializer extends EntityProcessor {

    /**
     * Serialize an entity during a Chunk saving and return a new {@link NBTCompound} that can be converted into an entity
     * using a {@link EntityFactory}.
     *
     * @param chunk The saved Chunk
     * @param entity The entity to serialize
     * @return A new NBTCompound or null if the serializer was failed silently.
     */
    @Nullable NBTCompound serializeChunkEntity(@NotNull Chunk chunk, @NotNull Entity entity);

    /**
     * The difference with the {@link #serializeChunkEntity(Chunk, Entity)} is that this method
     * will be called in a context where there is no chunk loading.
     * This method is absolutely optional and was thought to provide a way to serialize entities
     * for third party uses and is not used by Minestom by default.
     *
     * @param entity The entity to serialize
     * @return A new NBTCompound or null if the serializer was failed silently.
     */
    default @Nullable NBTCompound serializeEntity(@NotNull Entity entity) {
        return null;
    }
}