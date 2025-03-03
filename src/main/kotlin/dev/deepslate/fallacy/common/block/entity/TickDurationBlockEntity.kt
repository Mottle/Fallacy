package dev.deepslate.fallacy.common.block.entity

import dev.deepslate.fallacy.Calendar
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

open class TickDurationBlockEntity(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) :
    FallacyBlockEntity(type, pos, state) {

    constructor(pos: BlockPos, state: BlockState) : this(FallacyBlockEntities.TICK_COUNTER.get(), pos, state)

    protected var internalLastUpdatedTick = -1L

    var lastUpdatedTick: Long
        get() = internalLastUpdatedTick
        set(value) {
            internalLastUpdatedTick = value
            setChanged()
        }

    val durationTicks: Long
        get() = Calendar.time - lastUpdatedTick

    fun reset() {
        internalLastUpdatedTick = Calendar.time
        setChanged()
    }

    override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        internalLastUpdatedTick = tag.getLong("last_updated_tick")
        super.loadAdditional(tag, registries)
    }

    override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        tag.putLong("last_updated_tick", internalLastUpdatedTick)
        super.saveAdditional(tag, registries)
    }
}