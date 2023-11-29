package net.minestom.server.entity.serialization;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.instance.Chunk;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jglrxavpok.hephaistos.nbt.NBTCompound;

import java.util.concurrent.ExecutionException;

public class TestEntityFactory implements EntityFactory {
    @Override
    public @Nullable Entity createChunkEntity(@NotNull Chunk chunk, @NotNull NBTCompound entityData) {
        NBTCompound positionData = entityData.getCompound("position");

        double x = positionData.getAsDouble("x");
        double y = positionData.getAsDouble("y");
        double z = positionData.getAsDouble("z");
        float yaw = positionData.getAsFloat("yaw");
        float pitch = positionData.getAsFloat("pitch");

        Pos pos = new Pos(x, y, z, yaw, pitch);
        Entity entity = new Entity(EntityType.CREEPER);
        try {
            entity.setInstance(chunk.getInstance(), pos).get();
            return entity;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}