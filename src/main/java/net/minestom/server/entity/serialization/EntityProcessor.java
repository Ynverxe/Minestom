package net.minestom.server.entity.serialization;

import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.Instance;

/**
 * Represents an object that process entity data. This object may be an {@link EntityFactory}
 * or a {@link EntitySerializer}.
 * These implementations were thought to have methods to use during a {@link net.minestom.server.instance.IChunkLoader#saveChunk(Chunk)}
 * or a {@link net.minestom.server.instance.IChunkLoader#loadChunk(Instance, int, int)}, but also they can have optional methods
 * exposed for context out of Chunk loading/saving and will never be used by Minestom.
 * Both implementations can fail during the data processing, and they also can fail silently just returning a null value.
 * Exception handling depends on the caller. Default, in {@link net.minestom.server.instance.AnvilLoader} if this processor fails with an
 * exception during a Chunk loading/saving it will be handled via {@link net.minestom.server.exception.ExceptionManager#handleException(Throwable)}, and
 * whether a processor fails silently or not, all remaining processing of the entity will be carried out in the same way.
 */
public interface EntityProcessor {}