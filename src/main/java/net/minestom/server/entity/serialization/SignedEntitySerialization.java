package net.minestom.server.entity.serialization;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Entity;
import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.Instance;
import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;
import org.jglrxavpok.hephaistos.nbt.NBT;
import org.jglrxavpok.hephaistos.nbt.NBTCompound;

import java.util.ArrayList;
import java.util.List;

public final class SignedEntitySerialization {

    private SignedEntitySerialization() {}

    public static final String ENTITY_SERIALIZATION_KEY = "SerializationKey";

    public static @NotNull List<Entity> deserializeChunkEntities(@NotNull Chunk chunk, @NotNull List<NBTCompound> entitiesData) {
        List<Entity> entities = new ArrayList<>();
        EntityProcessorManager processorRegistry = MinecraftServer.getEntityProcessorManager();

        for (NBTCompound someEntityData : entitiesData) {
            EntityFactory factory = null;
            String foundKey = someEntityData.getString(ENTITY_SERIALIZATION_KEY);
            NamespaceID serializationKey = null;

            if (foundKey != null) {
                factory = processorRegistry.findProcessor(serializationKey = NamespaceID.from(foundKey), EntityProcessorType.FACTORY);
            }

            if (factory == null) factory = processorRegistry.fallbackEntityProcessor();

            Entity entity;
            try {
                entity = factory.createChunkEntity(chunk, someEntityData);
            } catch (Throwable throwable) {
                MinecraftServer.getExceptionManager().handleException(throwable);
                continue;
            }

            if (entity == null) continue;

            entities.add(entity);

            entity.setSerializationKey(serializationKey);
            if (entity.getInstance() == null) {
                entity.setInstance(chunk.getInstance()); // CompletableFuture#get() to wait for spawning?
            }
        }

        return entities;
    }

    public static @NotNull List<NBTCompound> serializeChunkEntities(@NotNull Chunk chunk) {
        EntityProcessorManager processorRegistry = MinecraftServer.getEntityProcessorManager();

        Instance instance = chunk.getInstance();
        List<NBTCompound> serializedEntities = new ArrayList<>();

        for (Entity entity : instance.getChunkEntities(chunk)) {
            NamespaceID serializationKey = entity.serializationKey();

            EntitySerializer serializer = null;

            if (serializationKey != null) {
                serializer = processorRegistry.findProcessor(serializationKey, EntityProcessorType.SERIALIZER);
            }

            if (serializer == null) serializer = processorRegistry.fallbackEntityProcessor();

            NBTCompound serialized;
            try {
                serialized = serializer.serializeChunkEntity(chunk, entity);
            } catch (Throwable error) {
                MinecraftServer.getExceptionManager().handleException(error);
                continue;
            }

            if (serialized == null) continue;

            if (serializationKey != null) {
                serialized = serialized.toMutableCompound()
                        .set(ENTITY_SERIALIZATION_KEY, NBT.String(serializationKey.asString()))
                        .toCompound();
            }

            serializedEntities.add(serialized);
        }

        return serializedEntities;
    }
}