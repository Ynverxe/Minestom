package net.minestom.server.world.biome;

import net.minestom.server.color.Color;
import net.minestom.server.coordinate.Point;
import net.minestom.server.registry.DynamicRegistry;
import net.minestom.server.registry.ProtocolObject;
import net.minestom.server.registry.Registry;
import net.minestom.server.utils.nbt.BinaryTagSerializer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public sealed interface Biome extends Biomes, ProtocolObject permits BiomeImpl {

    static @NotNull Builder builder() {
        return new Builder();
    }

    /**
     * <p>Creates a new registry for biomes, loading the vanilla trim biomes.</p>
     *
     * @see net.minestom.server.MinecraftServer to get an existing instance of the registry
     */
    @ApiStatus.Internal
    static @NotNull DynamicRegistry<Biome> createDefaultRegistry() {
        return DynamicRegistry.create(
                "minecraft:worldgen/biome", BiomeImpl.REGISTRY_NBT_TYPE, Registry.Resource.BIOMES,
                (namespace, props) -> new BiomeImpl(Registry.biome(namespace, props)),
                // We force plains to be first because it allows convenient palette initialization.
                // Maybe worth switching to fetching plains in the palette in the future to avoid this.
                (a, b) -> a.equals("minecraft:plains") ? -1 : b.equals("minecraft:plains") ? 1 : 0
        );
    }

    float temperature();

    float downfall();

    @NotNull BiomeEffects effects();

    boolean hasPrecipitation();

    @NotNull TemperatureModifier temperatureModifier();

    @Nullable Registry.BiomeEntry registry();

    enum TemperatureModifier {
        NONE, FROZEN;

        public static final BinaryTagSerializer<TemperatureModifier> NBT_TYPE = BinaryTagSerializer.fromEnumStringable(TemperatureModifier.class);
    }

    interface Setter {
        void setBiome(int x, int y, int z, @NotNull DynamicRegistry.Key<Biome> biome);

        default void setBiome(@NotNull Point blockPosition, @NotNull DynamicRegistry.Key<Biome> biome) {
            setBiome(blockPosition.blockX(), blockPosition.blockY(), blockPosition.blockZ(), biome);
        }
    }

    interface Getter {
        @NotNull DynamicRegistry.Key<Biome> getBiome(int x, int y, int z);

        default @NotNull DynamicRegistry.Key<Biome> getBiome(@NotNull Point point) {
            return getBiome(point.blockX(), point.blockY(), point.blockZ());
        }
    }

    final class Builder {
        private static final BiomeEffects DEFAULT_EFFECTS = BiomeEffects.builder()
                .fogColor(new Color(0xC0D8FF))
                .skyColor(new Color(0x78A7FF))
                .waterColor(new Color(0x3F76E4))
                .waterFogColor(new Color(0x50533))
                .build();

        private float temperature = 0.25f;
        private float downfall = 0.8f;
        private BiomeEffects effects = DEFAULT_EFFECTS;
        private boolean hasPrecipitation = false;
        private TemperatureModifier temperatureModifier = TemperatureModifier.NONE;

        private Builder() {
        }

        @Contract(value = "_ -> this", pure = true)
        public @NotNull Builder temperature(float temperature) {
            this.temperature = temperature;
            return this;
        }

        @Contract(value = "_ -> this", pure = true)
        public @NotNull Builder downfall(float downfall) {
            this.downfall = downfall;
            return this;
        }

        @Contract(value = "_ -> this", pure = true)
        public @NotNull Builder effects(@NotNull BiomeEffects effects) {
            this.effects = effects;
            return this;
        }

        @Contract(value = "_ -> this", pure = true)
        public @NotNull Builder hasPrecipitation(boolean precipitation) {
            this.hasPrecipitation = precipitation;
            return this;
        }

        @Contract(value = "_ -> this", pure = true)
        public @NotNull Builder temperatureModifier(@NotNull TemperatureModifier temperatureModifier) {
            this.temperatureModifier = temperatureModifier;
            return this;
        }

        @Contract(pure = true)
        public @NotNull Biome build() {
            return new BiomeImpl(temperature, downfall, effects, hasPrecipitation, temperatureModifier, null);
        }
    }
}
