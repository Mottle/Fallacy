package dev.deepslate.fallacy.client

//import dev.deepslate.fallacy.Fallacy
//import dev.deepslate.fallacy.common.FallacyFluids
//import net.minecraft.core.BlockPos
//import net.minecraft.resources.ResourceLocation
//import net.minecraft.world.level.BlockAndTintGetter
//import net.minecraft.world.level.material.FluidState
//import net.neoforged.bus.api.SubscribeEvent
//import net.neoforged.fml.common.EventBusSubscriber
//import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions
//import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent
////
//@EventBusSubscriber(modid = Fallacy.Companion.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
//object ClientExtensionsRegisterer {
//    @SubscribeEvent
//    fun onRegisterClientExtension(event: RegisterClientExtensionsEvent) {
//        event.registerFluidType(object : IClientFluidTypeExtensions {
//            override fun getTintColor(
//                state: FluidState,
//                getter: BlockAndTintGetter,
//                pos: BlockPos
//            ): Int {
//                return 0xeeee00
//            }
//
//            override fun getStillTexture(): ResourceLocation = stillTexture
//
//            override fun getFlowingTexture(): ResourceLocation = stillTexture
//        }, FallacyFluids.MOLTEN_COPPER.get().fluidType)
//    }
//}