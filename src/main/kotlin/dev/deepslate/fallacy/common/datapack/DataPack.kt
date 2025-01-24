package dev.deepslate.fallacy.common.datapack

import net.minecraft.core.BlockPos
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import kotlin.jvm.optionals.getOrNull

object DataPack {

    private fun access(level: LevelAccessor) = level.registryAccess()

    private fun <T> datapack(level: LevelAccessor, registry: ResourceKey<Registry<T>>, key: ResourceKey<T>) =
        access(level).lookup(registry).getOrNull()?.get(key)?.getOrNull()?.value()

    fun crop(level: LevelAccessor, namespacedId: ResourceLocation): CropsConfiguration.CropConfiguration = datapack(
        level, DataPacks.CROP_REGISTRY_KEY,
        CropsConfiguration.CONFIGURATION_KEY
    )?.query(namespacedId) ?: CropsConfiguration.DEFAULT

    fun crop(level: LevelAccessor, block: Block) = BuiltInRegistries.BLOCK.getKey(block).let { crop(level, it) }

    fun crop(level: LevelAccessor, state: BlockState) = crop(level, state.block)

    fun crop(level: LevelAccessor, pos: BlockPos) = crop(level, level.getBlockState(pos))

    fun biome(level: LevelAccessor, namespacedId: ResourceLocation): BiomesConfiguration.BiomeConfiguration = datapack(
        level, DataPacks.BIOME_REGISTRY_KEY,
        BiomesConfiguration.CONFIGURATION_KEY
    )?.query(namespacedId) ?: BiomesConfiguration.DEFAULT

    fun biome(level: LevelAccessor, pos: BlockPos) =
        level.getBiome(pos).key?.location()?.let { biome(level, it) } ?: BiomesConfiguration.DEFAULT

    fun biomes(level: LevelAccessor): BiomesConfiguration = datapack(
        level, DataPacks.BIOME_REGISTRY_KEY,
        BiomesConfiguration.CONFIGURATION_KEY
    ) ?: BiomesConfiguration(mapOf())
}