package dev.deepslate.fallacy.thermodynamics

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap
import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.Level
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.biome.Biomes

object BiomeHeat {
    val biomeMap = Object2ObjectLinkedOpenHashMap<ResourceKey<Biome>, Int>()

    init {
        with(biomeMap) {
            put(Biomes.WARM_OCEAN, 25)
            put(Biomes.LUKEWARM_OCEAN, 15)
            put(Biomes.DEEP_LUKEWARM_OCEAN, 12)
            put(Biomes.OCEAN, 10)
            put(Biomes.DEEP_OCEAN, 7)
            put(Biomes.COLD_OCEAN, 5)
            put(Biomes.DEEP_COLD_OCEAN, 2)
            put(Biomes.RIVER, 15)

            put(Biomes.DEEP_FROZEN_OCEAN, -23)
            put(Biomes.THE_VOID, 0)
            put(Biomes.LUSH_CAVES, 16)

            put(Biomes.PLAINS, 20)
            put(Biomes.BEACH, 20)
            put(Biomes.SUNFLOWER_PLAINS, 20)
            put(Biomes.DEEP_DARK, 10)

            put(Biomes.DRIPSTONE_CAVES, 15)

            put(Biomes.FROZEN_RIVER, -15)
            put(Biomes.FROZEN_OCEAN, -20)

            put(Biomes.SNOWY_PLAINS, -15)

            put(Biomes.ICE_SPIKES, -35)

            put(Biomes.GROVE, -25)

            put(Biomes.FROZEN_PEAKS, -25)
            put(Biomes.JAGGED_PEAKS, -30)

            put(Biomes.SNOWY_SLOPES, -20)

            put(Biomes.SNOWY_TAIGA, -18)

            put(Biomes.SNOWY_BEACH, -12)

            put(Biomes.MEADOW, 15)

            put(Biomes.CHERRY_GROVE, 20)

            put(Biomes.DESERT, 60)

            put(Biomes.SAVANNA, 35)
            put(Biomes.SAVANNA_PLATEAU, 35)
            put(Biomes.WINDSWEPT_SAVANNA, 35)

            put(Biomes.BADLANDS, 48)
            put(Biomes.ERODED_BADLANDS, 45)
            put(Biomes.WOODED_BADLANDS, 48)

            put(Biomes.FOREST, 20)
            put(Biomes.FLOWER_FOREST, 23)

            put(Biomes.DARK_FOREST, 17)

            put(Biomes.BIRCH_FOREST, 20)
            put(Biomes.OLD_GROWTH_BIRCH_FOREST, 15)

            put(Biomes.OLD_GROWTH_PINE_TAIGA, 10)

            put(Biomes.OLD_GROWTH_SPRUCE_TAIGA, 5)
            put(Biomes.TAIGA, 10)

            put(Biomes.WINDSWEPT_GRAVELLY_HILLS, 12)
            put(Biomes.WINDSWEPT_FOREST, 15)

            put(Biomes.WINDSWEPT_HILLS, 12)
            put(Biomes.STONY_SHORE, 20)

            put(Biomes.JUNGLE, 30)
            put(Biomes.BAMBOO_JUNGLE, 25)

            put(Biomes.SPARSE_JUNGLE, 30)

            put(Biomes.MUSHROOM_FIELDS, 30)

            put(Biomes.STONY_PEAKS, 5)

            put(Biomes.MANGROVE_SWAMP, 30)
            put(Biomes.SWAMP, 28)
        }

        for(k in biomeMap.keys) {
            biomeMap[k] = ThermodynamicsEngine.fromFreezingPoint(biomeMap[k]!!)
        }
    }

    val DEFAULT = ThermodynamicsEngine.fromFreezingPoint(15)

    fun getBiomeHeat(level: Level, pos: BlockPos): Int {
        val biome = level.getBiome(pos).key ?: return DEFAULT
        val heat = biomeMap[biome] ?: DEFAULT
        return heat
    }
}