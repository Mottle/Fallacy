package dev.deepslate.fallacy.weather

import dev.deepslate.fallacy.util.TickHelper
import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

sealed class Weather {

    open val tickInterval: Int = TickHelper.second(10)

    open val tickEntityInterval: Int = TickHelper.second(10)

    open val randomTickRegionInterval: Int = TickHelper.second(10)

    open val randomTickStateCountRange = 5..10

    open val tickWeatherEntityInterval: Int = TickHelper.second(4)

    abstract fun tick()

    abstract fun tickEntity(entity: Entity, level: Level, pos: BlockPos)

    abstract fun randomTickRegion(state: BlockState, level: Level, pos: BlockPos)

    open fun getWeatherEntity(): Entity? = null

    abstract fun tickWeatherEntity(entity: Entity, level: Level, pos: BlockPos)

    abstract val namespaceId: ResourceLocation
}