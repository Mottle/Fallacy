package dev.deepslate.fallacy.thermodynamics

import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.biome.Biomes

open class EnvironmentThermodynamicsEngine(private val level: Level) : ThermodynamicsEngine {

    companion object {
        const val BIOME_BASE_HEAT = 30
    }

    override fun getEpitaxialHeat(pos: BlockPos): Int = 0

    override fun getIntrinsicHeat(pos: BlockPos): Int =
        getBiomeHeat(pos) + getSunlightHeat(pos) + getWeatherHeat(pos) + dimensionHeat

    override fun checkBlock(pos: BlockPos) {
        /* DO NOTHING */
    }

    protected open fun getSunlightHeat(pos: BlockPos): Int {
        if (level.getBiome(pos).`is`(Biomes.DESERT)) {
            return if (level.isDay) 10 else -55
        }

        return if (level.isDay) 5 else 0
    }

    protected open fun getBiomeHeat(pos: BlockPos): Int = (BIOME_BASE_HEAT * level.getBiome(pos)
        .value().baseTemperature).toInt()

    protected open fun getWeatherHeat(pos: BlockPos): Int = 0

    protected open val dimensionHeat: Int = 0
}