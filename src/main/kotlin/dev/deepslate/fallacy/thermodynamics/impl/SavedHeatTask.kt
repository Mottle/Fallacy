package dev.deepslate.fallacy.thermodynamics.impl

import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.FloatTag
import net.minecraft.nbt.IntTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.LongTag
import net.minecraft.nbt.Tag
import net.minecraft.world.level.saveddata.SavedData

//用于存储服务器关闭时没有完成的温度计算任务以便服务器下次启动时再续
class SavedHeatTask(private val remainedTasks: MutableList<HeatMaintainer.MaintainTask>): SavedData() {

    val tasks: List<HeatMaintainer.MaintainTask>
        get() = remainedTasks

    companion object {
        fun create() = SavedHeatTask(mutableListOf())

        fun load(tag: CompoundTag, provider: HolderLookup.Provider): SavedHeatTask {
            val data = create()
            val positions = tag.getList("positions", Tag.TAG_LONG.toInt()).map { (it as LongTag).asLong }
            val heats = tag.getList("heats", Tag.TAG_FLOAT.toInt()).map { (it as FloatTag).asFloat }
            val types = tag.getList("types", Tag.TAG_INT.toInt()).map { (it as IntTag).asInt }

            for(i in positions.indices) {
                val packedPos = positions[i]
                val heat = heats[i]
                val typeFlag = types[i]
                if(typeFlag == 0) {
                    data.add(HeatMaintainer.MaintainTask.Increase(BlockPos.of(packedPos), heat))
                } else {
                    data.add(HeatMaintainer.MaintainTask.Decrease(BlockPos.of(packedPos), heat))
                }
            }

            return data
        }
    }

    override fun save(
        tag: CompoundTag,
        registries: HolderLookup.Provider
    ): CompoundTag {
        val positions = ListTag()
        val heats = ListTag()
        val types = ListTag()

        remainedTasks.forEach { task ->
            positions.add(LongTag.valueOf(task.pos.asLong()))
            heats.add(FloatTag.valueOf(task.heat))
            if(task is HeatMaintainer.MaintainTask.Increase) {
                types.add(IntTag.valueOf(0))
            } else {
                types.add(IntTag.valueOf(1))
            }
        }

        tag.put("positions", positions)
        tag.put("heats", heats)
        tag.put("types", types)
        return tag
    }

    fun add(task: HeatMaintainer.MaintainTask) {
        remainedTasks.add(task)
    }
}