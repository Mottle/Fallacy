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

    open fun tick(level: Level, region: Region) {}

    open fun tickEntity(entity: Entity, level: Level, pos: BlockPos) {}

    open fun createWeatherEntity(): Entity? = null

    open fun tickWeatherEntity(entity: Entity, level: Level, pos: BlockPos) {}

    open fun isValidIn(level: Level, pos: BlockPos): Boolean = true

    open val isWet: Boolean = false

    abstract val namespaceId: ResourceLocation

    //0..7, 7 is the highest priority
    open val priority: Int = 1

    init {
        //天气的tick以秒为单位
        require(tickInterval % 20 == 0) { "tickInterval must be a multiple of 20." }
        require(tickEntityInterval % 20 == 0) { "tickEntityInterval must be a multiple of 20." }
        require(tickWeatherEntityInterval % 20 == 0) { "tickWeatherEntityInterval must be a multiple of 20." }
        require(priority in 0..7) { "priority must be between 0 and 7." }
    }
}