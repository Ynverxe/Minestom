package net.minestom.server.entity.serialization;

import net.minestom.server.entity.Entity;
import net.minestom.server.instance.Chunk;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jglrxavpok.hephaistos.nbt.NBTCompound;

public interface EntityFactory extends EntityProcessor {

    /**
     * Create and configure a new {@link Entity} with the provided data during a Chunk loading.
     *
     * @param chunk The loaded Chunk
     * @param entityData The data to parse
     * @return a new Entity or null if the factory was failed silently.
     */
    @Nullable Entity createChunkEntity(@NotNull Chunk chunk, @NotNull NBTCompound entityData);

    /**
     * The difference with the {@link #createChunkEntity(Chunk, NBTCompound)} method is that this method
     * will be called in a context where there is no chunk loading.
     * This method is absolutely optional and was thought to provide a way to deserialize entities
     * for third party uses and is not used by Minestom by default.
     *
     * @param entityData The data to parse
     * @return a new Entity or null if the factory was failed silently.
     */
    default @Nullable Entity createEntity(@NotNull NBTCompound entityData) {
        return null;
    }
}