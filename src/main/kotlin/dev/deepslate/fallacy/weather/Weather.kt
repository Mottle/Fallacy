package dev.deepslate.fallacy.weather

import dev.deepslate.fallacy.util.TickHelper
import dev.deepslate.fallacy.util.region.Region
import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.Level

/*
 * “可怕的鲸鱼，害怕着天气，它怕悲伤淹死自己。”
 */
abstract class Weather {
    open val tickInterval: Int = TickHelper.second(10)

    open val tickEntityInterval: Int = TickHelper.second(10)

    open val tickWeatherEntityInterval: Int = TickHelper.second(4)

    abstract fun tick(level: Level, region: Region)

    abstract fun tickEntity(entity: Entity, level: Level, pos: BlockPos)

    open fun createWeatherEntity(): Entity? = null

    abstract fun tickWeatherEntity(entity: Entity, level: Level, pos: BlockPos)

    abstract val namespaceId: ResourceLocation
}