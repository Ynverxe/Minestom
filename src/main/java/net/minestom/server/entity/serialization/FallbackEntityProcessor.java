package net.minestom.server.entity.serialization;

import net.minestom.server.entity.Entity;
import net.minestom.server.instance.Chunk;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jglrxavpok.hephaistos.nbt.NBTCompound;

/**
 * A Fallback processor to be used if a {@link EntityProcessor} is missing during a Chunk loading/saving.
 */
public interface FallbackEntityProcessor extends EntityFactory, EntitySerializer {
    FallbackEntityProcessor FAIL = new FallbackEntityProcessor() {
        @Override
        public @Nullable Entity createChunkEntity(@NotNull Chunk chunk, @NotNull NBTCompound entityData) {
            return null;
        }

        @Override
        public @Nullable NBTCompound serializeChunkEntity(@NotNull Chunk chunk, @NotNull Entity entity) {
            return null;
        }
    };
}