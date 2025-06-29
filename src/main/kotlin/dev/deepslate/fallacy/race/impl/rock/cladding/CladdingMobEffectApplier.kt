package dev.deepslate.fallacy.race.impl.rock.cladding

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.race.impl.rock.Rock
import dev.deepslate.fallacy.util.TickHelper
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.effect.MobEffectInstance
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.tick.PlayerTickEvent

//class CladdingMobEffectApplier(val effectInstance: MobEffectInstance, minNeed: Int) : CladdingEffect {
//    @EventBusSubscriber(modid = Fallacy.MOD_ID)
//    object Handler {
//        @SubscribeEvent
//        fun onPlayerTick(event: PlayerTickEvent.Pre) {
//            if (event.entity.level().isClientSide) return
//            if (!TickHelper.checkServerTickRate(40)) return
//
//            val player = event.entity as ServerPlayer
//            val effect = Rock.CLADDING_LIMIT
//        }
//    }
//}