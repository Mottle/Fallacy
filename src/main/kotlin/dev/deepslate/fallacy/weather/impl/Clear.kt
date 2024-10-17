package dev.deepslate.fallacy.weather.impl

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.util.region.Region
import dev.deepslate.fallacy.weather.Weather
import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.Level

object Clear : Weather() {
    override fun tick(level: Level, region: Region) {}

    override fun tickEntity(
        entity: Entity,
        level: Level,
        pos: BlockPos
    ) {
    }

    override fun tickWeatherEntity(
        entity: Entity,
        level: Level,
        pos: BlockPos
    ) {
    }

    override val namespaceId: ResourceLocation = Fallacy.id("clear")
}