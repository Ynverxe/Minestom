package net.minestom.server.entity.serialization;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.instance.Chunk;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jglrxavpok.hephaistos.nbt.NBT;
import org.jglrxavpok.hephaistos.nbt.NBTCompound;
import org.jglrxavpok.hephaistos.nbt.mutable.MutableNBTCompound;

public class TestEntitySerializer implements EntitySerializer {

    @Override
    public @Nullable NBTCompound serializeChunkEntity(@NotNull Chunk chunk, @NotNull Entity entity) {
        MutableNBTCompound compound = new MutableNBTCompound();

        Pos pos = entity.getPosition();
        MutableNBTCompound positionCompound = new MutableNBTCompound()
                .set("x", NBT.Double(pos.x()))
                .set("y", NBT.Double(pos.x()))
                .set("z", NBT.Double(pos.z()))
                .set("yaw", NBT.Float(pos.yaw()))
                .set("pitch", NBT.Float(pos.pitch()));

        return compound.set("position", positionCompound.toCompound()).toCompound();
    }
}