package dev.deepslate.fallacy.common.capability

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.common.block.entity.FallacyBlockEntities
import dev.deepslate.fallacy.common.capability.diet.DietProvider
import dev.deepslate.fallacy.common.capability.diet.IDiet
import dev.deepslate.fallacy.common.capability.heat.IBodyHeat
import dev.deepslate.fallacy.common.capability.hydration.HydrationProvider
import dev.deepslate.fallacy.common.capability.hydration.IHydration
import dev.deepslate.fallacy.common.capability.inventory.CrucibleInventory
import dev.deepslate.fallacy.common.capability.skeleton.ISkeleton
import dev.deepslate.fallacy.common.capability.skeleton.SkeletonProvider
import dev.deepslate.fallacy.common.capability.tank.CrucibleTank
import dev.deepslate.fallacy.common.capability.thirst.IThirst
import dev.deepslate.fallacy.common.capability.thirst.ThirstProvider
import net.minecraft.core.Direction
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.entity.EntityType
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.capabilities.EntityCapability
import net.neoforged.neoforge.capabilities.ItemCapability
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
import java.lang.ref.WeakReference

object FallacyCapabilities {

    val THIRST: EntityCapability<IThirst, Void?> =
        EntityCapability.createVoid(Fallacy.withID("thirst"), IThirst::class.java)

    val HYDRATION: ItemCapability<IHydration, Void?> =
        ItemCapability.createVoid(Fallacy.withID("hydration"), IHydration::class.java)

    val DIET: EntityCapability<IDiet, Void?> = EntityCapability.createVoid(Fallacy.withID("diet"), IDiet::class.java)

    val SKELETON: EntityCapability<ISkeleton, Void?> =
        EntityCapability.createVoid(Fallacy.withID("skeleton"), ISkeleton::class.java)

    val BODY_HEAT: EntityCapability<IBodyHeat, Void?> =
        EntityCapability.createVoid(Fallacy.withID("body_heat"), IBodyHeat::class.java)

    @EventBusSubscriber(modid = Fallacy.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
    object Handler {
        @SubscribeEvent
        fun registerCapabilities(event: RegisterCapabilitiesEvent) {
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

            event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                FallacyBlockEntities.CRUCIBLE.get()
            ) { e, direct ->
                if (direct == Direction.UP) CrucibleInventory(WeakReference(e)) else null
            }

            event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                FallacyBlockEntities.CRUCIBLE.get()
            ) { e, direct ->
                if (direct != Direction.DOWN) CrucibleTank(WeakReference(e)) else null
            }
        }
    }
}