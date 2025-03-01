package dev.deepslate.fallacy.common.datapack

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.common.block.data.NPK
import dev.deepslate.fallacy.thermodynamics.ThermodynamicsEngine
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.biome.Biomes
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent

data class BiomesConfiguration(val biomeMap: Map<ResourceLocation, BiomeConfiguration>) {

    companion object {
        private val biomeConfigurationCodec = RecordCodecBuilder.create { instance ->
            instance.group(
                NPK.CODEC.fieldOf("npk").forGetter(BiomeConfiguration::npk),
                Codec.INT.fieldOf("heat").forGetter(BiomeConfiguration::celsius)
            ).apply(instance, ::BiomeConfiguration)
        }

        val CODEC: Codec<BiomesConfiguration> = Codec.unboundedMap(ResourceLocation.CODEC, biomeConfigurationCodec)
            .xmap(::BiomesConfiguration, BiomesConfiguration::biomeMap)

        val CONFIGURATION_KEY: ResourceKey<BiomesConfiguration> =
            ResourceKey.create(DataPacks.BIOME_REGISTRY_KEY, Fallacy.withID("configuration"))

        val DEFAULT = BiomeConfiguration(NPK.default(), 15)

        private val defaultMap = mutableMapOf<ResourceLocation, BiomeConfiguration>(
            Biomes.WARM_OCEAN.location() to BiomeConfiguration(NPK.default(), 25),
            Biomes.LUKEWARM_OCEAN.location() to BiomeConfiguration(NPK.default(), 15),
            Biomes.DEEP_LUKEWARM_OCEAN.location() to BiomeConfiguration(NPK.default(), 12),
            Biomes.OCEAN.location() to BiomeConfiguration(NPK.default(), 10),
            Biomes.DEEP_OCEAN.location() to BiomeConfiguration(NPK.default(), 7),
            Biomes.COLD_OCEAN.location() to BiomeConfiguration(NPK.default(), 5),
            Biomes.DEEP_COLD_OCEAN.location() to BiomeConfiguration(NPK.default(), 2),
            Biomes.RIVER.location() to BiomeConfiguration(NPK.default(), 15),
            Biomes.DEEP_FROZEN_OCEAN.location() to BiomeConfiguration(NPK.default(), -23),
            Biomes.THE_VOID.location() to BiomeConfiguration(NPK.zero(), 0),
            Biomes.LUSH_CAVES.location() to BiomeConfiguration(NPK(3, 5, 5), 16),
            Biomes.PLAINS.location() to BiomeConfiguration(NPK.default(), 20),
            Biomes.BEACH.location() to BiomeConfiguration(NPK(1, 3, 3), 20),
            Biomes.SUNFLOWER_PLAINS.location() to BiomeConfiguration(NPK(3, 2, 2), 23),
            Biomes.DEEP_DARK.location() to BiomeConfiguration(NPK(1, 1, 1), 10),
            Biomes.DRIPSTONE_CAVES.location() to BiomeConfiguration(NPK(1, 3, 5), 15),
            Biomes.FROZEN_RIVER.location() to BiomeConfiguration(NPK(1, 1, 1), -15),
            Biomes.FROZEN_OCEAN.location() to BiomeConfiguration(NPK.default(), -20),
            Biomes.SNOWY_PLAINS.location() to BiomeConfiguration(NPK(1, 1, 3), -15),
            Biomes.ICE_SPIKES.location() to BiomeConfiguration(NPK(2, 1, 4), -35),
            Biomes.GROVE.location() to BiomeConfiguration(NPK(2, 4, 2), -25),
            Biomes.FROZEN_PEAKS.location() to BiomeConfiguration(NPK(2, 2, 4), -25),
            Biomes.JAGGED_PEAKS.location() to BiomeConfiguration(NPK(1, 2, 3), -30),
            Biomes.SNOWY_SLOPES.location() to BiomeConfiguration(NPK(2, 3, 3), -20),
            Biomes.SNOWY_TAIGA.location() to BiomeConfiguration(NPK(2, 2, 1), -18),
            Biomes.SNOWY_BEACH.location() to BiomeConfiguration(NPK(1, 4, 3), -12),
            Biomes.MEADOW.location() to BiomeConfiguration(NPK(3, 2, 3), 15),
            Biomes.CHERRY_GROVE.location() to BiomeConfiguration(NPK(4, 2, 3), 20),
            Biomes.DESERT.location() to BiomeConfiguration(NPK(1, 3, 4), 45),
            Biomes.SAVANNA.location() to BiomeConfiguration(NPK(3, 2, 3), 35),
            Biomes.SAVANNA_PLATEAU.location() to BiomeConfiguration(NPK(3, 1, 2), 35),
            Biomes.WINDSWEPT_SAVANNA.location() to BiomeConfiguration(NPK(3, 3, 2), 35),
            Biomes.BADLANDS.location() to BiomeConfiguration(NPK(1, 5, 3), 41),
            Biomes.ERODED_BADLANDS.location() to BiomeConfiguration(NPK(1, 4, 3), 41),
            Biomes.WOODED_BADLANDS.location() to BiomeConfiguration(NPK(1, 4, 3), 40),
            Biomes.FOREST.location() to BiomeConfiguration(NPK(3, 2, 2), 20),
            Biomes.FLOWER_FOREST.location() to BiomeConfiguration(NPK(4, 3, 4), 23),
            Biomes.DARK_FOREST.location() to BiomeConfiguration(NPK(5, 2, 3), 17),
            Biomes.BIRCH_FOREST.location() to BiomeConfiguration(NPK(3, 2, 3), 20),
            Biomes.OLD_GROWTH_BIRCH_FOREST.location() to BiomeConfiguration(NPK(4, 1, 3), 15),
            Biomes.OLD_GROWTH_PINE_TAIGA.location() to BiomeConfiguration(NPK(3, 2, 3), 10),
            Biomes.OLD_GROWTH_SPRUCE_TAIGA.location() to BiomeConfiguration(NPK(3, 2, 3), 5),
            Biomes.TAIGA.location() to BiomeConfiguration(NPK(4, 2, 3), 10),
            Biomes.WINDSWEPT_GRAVELLY_HILLS.location() to BiomeConfiguration(NPK(3, 5, 2), 12),
            Biomes.WINDSWEPT_FOREST.location() to BiomeConfiguration(NPK(4, 2, 3), 15),
            Biomes.WINDSWEPT_HILLS.location() to BiomeConfiguration(NPK(2, 4, 3), 12),
            Biomes.STONY_SHORE.location() to BiomeConfiguration(NPK(1, 3, 2), 20),
            Biomes.JUNGLE.location() to BiomeConfiguration(NPK(5, 3, 5), 30),
            Biomes.BAMBOO_JUNGLE.location() to BiomeConfiguration(NPK(3, 2, 4), 25),
            Biomes.SPARSE_JUNGLE.location() to BiomeConfiguration(NPK(4, 2, 3), 30),
            Biomes.MUSHROOM_FIELDS.location() to BiomeConfiguration(NPK(6, 6, 6), 30),
            Biomes.STONY_PEAKS.location() to BiomeConfiguration(NPK(1, 3, 4), 5),
            Biomes.MANGROVE_SWAMP.location() to BiomeConfiguration(NPK(2, 4, 4), 30),
            Biomes.SWAMP.location() to BiomeConfiguration(NPK(1, 4, 3), 28),
        )

        fun generateDefaultPack() = BiomesConfiguration(defaultMap)
    }

    @EventBusSubscriber(modid = Fallacy.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
    object Handler {
        @SubscribeEvent
        fun onModLoadOver(event: FMLLoadCompleteEvent) {
            defaultMap.clear()
        }
    }

    data class BiomeConfiguration(val npk: NPK, val celsius: Int) {
        val heat: Int
            get() = ThermodynamicsEngine.fromFreezingPoint(celsius)
    }

    fun query(namespacedId: ResourceLocation): BiomeConfiguration = biomeMap[namespacedId] ?: DEFAULT
}