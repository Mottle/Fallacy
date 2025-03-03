package dev.deepslate.fallacy

import dev.deepslate.fallacy.common.network.packet.CalendarSyncPacket
import dev.deepslate.fallacy.util.TickHelper
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.level.saveddata.SavedData
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import net.neoforged.bus.api.EventPriority
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.loading.FMLEnvironment
import net.neoforged.neoforge.client.event.ClientTickEvent
import net.neoforged.neoforge.event.entity.player.PlayerEvent
import net.neoforged.neoforge.event.server.ServerStartedEvent
import net.neoforged.neoforge.event.server.ServerStoppingEvent
import net.neoforged.neoforge.event.tick.ServerTickEvent
import net.neoforged.neoforge.network.PacketDistributor
import net.neoforged.neoforge.network.handling.IPayloadContext
import net.neoforged.neoforge.server.ServerLifecycleHooks

sealed class Calendar {
    companion object {
        val INSTANCE: Calendar = if (FMLEnvironment.dist.isClient) ClientCalendar() else ServerCalendar()

        val time: Long
            get() = INSTANCE.time
    }

    @Volatile
    protected var count: Long = Long.MIN_VALUE

    val time: Long
        get() = count

    fun update(modifier: (Long) -> Long) {
        synchronized(this) {
            updateUnsafe(modifier)
        }
    }

    fun updateUnsafe(modifier: (Long) -> Long) {
        count = modifier(count)
    }

    open fun tick() { count++ }

    @OnlyIn(Dist.DEDICATED_SERVER)
    class ServerCalendar : Calendar() {
        override fun tick() {
            super.tick()
            if (TickHelper.checkServerSecondRate(10)) {
                ServerLifecycleHooks.getCurrentServer()?.playerList?.players?.forEach {
                    PacketDistributor.sendToPlayer(it, CalendarSyncPacket(time))
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    class ClientCalendar : Calendar()

    @EventBusSubscriber(modid = Fallacy.MOD_ID)
    object Handler {
        @SubscribeEvent(priority = EventPriority.HIGHEST)
        fun onTick(event: ServerTickEvent.Pre) {
            INSTANCE.tick()
        }

        private const val KEY = "fallacy_calendar"

        private val factory = SavedData.Factory(SavedTime::create, SavedTime::load)

        @SubscribeEvent(priority = EventPriority.LOWEST)
        fun onServerStop(event: ServerStoppingEvent) {
            val server = event.server
            val overworld = server.overworld()
            overworld.dataStorage.set(KEY, SavedTime.create())
        }

        @SubscribeEvent
        fun onServerStart(event: ServerStartedEvent) {
            val overworld = event.server.overworld()
            val data = overworld.dataStorage.computeIfAbsent(factory, KEY)
            INSTANCE.update { data.count }
        }

        @SubscribeEvent
        fun onPlayerLogin(event: PlayerEvent.PlayerLoggedInEvent) {
            PacketDistributor.sendToPlayer(event.entity as ServerPlayer, CalendarSyncPacket(time))
        }
    }

    @EventBusSubscriber(modid = Fallacy.MOD_ID)
    object ClientHandler {
        //client
        fun handleTimeSync(data: CalendarSyncPacket, context: IPayloadContext) {
            INSTANCE.update { data.currentTime }
        }

        @SubscribeEvent
        fun onClientTick(event: ClientTickEvent.Pre) {
            INSTANCE.tick()
        }
    }

    private data class SavedTime(val count: Long) : SavedData() {
        companion object {
            const val TAG_KEY = "calendar_time"

            fun create() = SavedTime(if (INSTANCE.time < 0) 0 else INSTANCE.time)

            fun load(tag: CompoundTag, registries: HolderLookup.Provider): SavedTime {
                val count = tag.getLong(TAG_KEY)
                return SavedTime(count)
            }
        }

        override fun save(tag: CompoundTag, registries: HolderLookup.Provider): CompoundTag {
            tag.putLong(TAG_KEY, INSTANCE.time)
            return tag
        }
    }
}