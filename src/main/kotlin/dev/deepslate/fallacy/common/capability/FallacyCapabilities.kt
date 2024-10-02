package dev.deepslate.fallacy.common.capability

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.common.capability.diet.DietProvider
import dev.deepslate.fallacy.common.capability.diet.IDiet
import dev.deepslate.fallacy.common.capability.hydration.HydrationProvider
import dev.deepslate.fallacy.common.capability.hydration.IHydration
import dev.deepslate.fallacy.common.capability.skeleton.ISkeleton
import dev.deepslate.fallacy.common.capability.skeleton.SkeletonProvider
import dev.deepslate.fallacy.common.capability.thirst.IThirst
import dev.deepslate.fallacy.common.capability.thirst.ThirstProvider
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.entity.EntityType
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.capabilities.EntityCapability
import net.neoforged.neoforge.capabilities.ItemCapability
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent

@EventBusSubscriber(modid = Fallacy.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
object FallacyCapabilities {

    val THIRST: EntityCapability<IThirst, Void?> =
        EntityCapability.createVoid(Fallacy.id("thirst"), IThirst::class.java)

    val HYDRATION: ItemCapability<IHydration, Void?> =
        ItemCapability.createVoid(Fallacy.id("hydration"), IHydration::class.java)

    val DIET: EntityCapability<IDiet, Void?> = EntityCapability.createVoid(Fallacy.id("diet"), IDiet::class.java)

    val SKELETON: EntityCapability<ISkeleton, Void?> =
        EntityCapability.createVoid(Fallacy.id("skeleton"), ISkeleton::class.java)

    @SubscribeEvent
    fun registerAll(event: RegisterCapabilitiesEvent) {
        event.registerEntity(THIRST, EntityType.PLAYER, ThirstProvider())

        event.registerItem(
            HYDRATION,
            HydrationProvider(),
            *BuiltInRegistries.ITEM.filter { it.defaultInstance.has(DataComponents.FOOD) }.toTypedArray()
        )

        event.registerEntity(
            DIET,
            EntityType.PLAYER,
            DietProvider(),
        )

        event.registerEntity(
            SKELETON,
            EntityType.PLAYER,
            SkeletonProvider(),
        )
    }
}