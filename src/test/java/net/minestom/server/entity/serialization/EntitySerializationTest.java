package net.minestom.server.entity.serialization;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.Instance;
import net.minestom.server.utils.NamespaceID;
import org.jglrxavpok.hephaistos.nbt.NBTCompound;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static net.minestom.server.entity.serialization.SignedEntitySerialization.*;
import static org.junit.jupiter.api.Assertions.*;

public class EntitySerializationTest {

    private static final Instance TEST_INSTANCE;

    static {
        MinecraftServer.init();

        TEST_INSTANCE = MinecraftServer.getInstanceManager()
                .createInstanceContainer();

        EntityProcessorManager entityProcessorManager = MinecraftServer.getEntityProcessorManager();
        NamespaceID key = NamespaceID.from("minestom:test");
        entityProcessorManager.registerProcessor(key, new TestEntitySerializer())
                .registerProcessor(key, new TestEntityFactory());
    }

    @Test
    public void testSignedSerialization() throws Exception {
        Pos pos = new Pos(10, 10, 10, 5.0f, 7.0f);

        Entity entity = new Entity(EntityType.CREEPER);
        entity.setSerializationKey(NamespaceID.from("minestom:test"));
        entity.setInstance(TEST_INSTANCE, pos).get();

        Chunk entityChunk = entity.getChunk();
        NBTCompound serialized = serializeChunkEntities(entityChunk).get(0);

        Entity deserialized = deserializeChunkEntities(entityChunk, Collections.singletonList(serialized)).get(0);

        assertEquals(pos, deserialized.getPosition());
  }
}