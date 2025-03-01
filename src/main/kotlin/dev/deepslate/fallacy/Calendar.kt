package dev.deepslate.fallacy

import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.level.saveddata.SavedData
import net.neoforged.bus.api.EventPriority
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.server.ServerStartedEvent
import net.neoforged.neoforge.event.server.ServerStoppingEvent
import net.neoforged.neoforge.event.tick.ServerTickEvent
import net.neoforged.neoforge.server.ServerLifecycleHooks

object Calendar {
    @Volatile
    private var count = Int.MIN_VALUE

    val time: Int
        get() = count

    fun update(modifier: (Int) -> Int) {
        synchronized(this) {
            updateUnsafe(modifier)
        }
    }

    fun updateUnsafe(modifier: (Int) -> Int) {
        count = modifier(count)
    }

    @EventBusSubscriber(modid = Fallacy.MOD_ID)
    object Handler {
        @SubscribeEvent(priority = EventPriority.HIGHEST)
        fun onTick(event: ServerTickEvent.Pre) {
            count++
        }

        private const val KEY = "fallacy_calendar"

        private val factory = SavedData.Factory(SavedTime::create, SavedTime::load)

        @SubscribeEvent(priority = EventPriority.LOWEST)
        fun onServerStop(event: ServerStoppingEvent) {
            val server = ServerLifecycleHooks.getCurrentServer()
            val overworld = server?.overworld()
            overworld?.dataStorage?.set(KEY, SavedTime.create())
        }

        @SubscribeEvent
        fun onServerStart(event: ServerStartedEvent) {
            val data = event.server.overworld().dataStorage.computeIfAbsent(factory, KEY)
            count = data.count
        }
    }

    private data class SavedTime(val count: Int) : SavedData() {
        companion object {
            const val TAG_KEY = "calendar_time"

            fun create() = SavedTime(if (count < 0) 0 else count)

// 函数load，用于加载CompoundTag中的SavedTime
            fun load(tag: CompoundTag, registries: HolderLookup.Provider): SavedTime {
                // 从CompoundTag中获取TAG_KEY对应的整数值
                val count = tag.getInt(TAG_KEY)
                // 返回SavedTime对象，参数为count
                return SavedTime(count)
            }
        }

        override fun save(tag: CompoundTag, registries: HolderLookup.Provider): CompoundTag {
            tag.putInt(TAG_KEY, Calendar.count)
            return tag
        }
    }
}