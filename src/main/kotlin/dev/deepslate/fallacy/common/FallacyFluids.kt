package dev.deepslate.fallacy.common

import com.tterrag.registrate.util.entry.FluidEntry
import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.common.registrate.REG
import dev.deepslate.fallacy.common.registrate.defaultModelWithTexture
import net.minecraft.client.renderer.RenderType
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions
import net.neoforged.neoforge.fluids.BaseFlowingFluid
import net.neoforged.neoforge.fluids.FluidType
import java.util.function.Consumer
import java.util.function.Supplier

object FallacyFluids {
    val MOLTEN_COPPER: FluidEntry<BaseFlowingFluid.Flowing> =
        getSimpleMoltenFluidBuilder("molten_copper").properties { p ->
            p.lightLevel(15).viscosity(2000).density(1400)
        }.fluidProperties { p ->
            p.levelDecreasePerBlock(2).slopeFindDistance(3).explosionResistance(100.0f).tickRate(25)
        }
            .block()//.color { Supplier { BlockColor { state, level, pos, tintIndex -> -12012264 } } }
            .build().source(BaseFlowingFluid::Source).bucket().defaultModelWithTexture("molten_bucket").build()
            .renderType { Supplier { RenderType.TRANSLUCENT } }
            .register()

//    //熔融铅
//    val MOLTEN_LEAD: FluidEntry<BaseFlowingFluid.Flowing> =
//        getSimpleMoltenFluidBuilder("molten_lead").properties {
//            it.lightLevel(15).canConvertToSource(false).temperature(
//                Temperature.celsius(327).heat
//            )
//        }.source(BaseFlowingFluid::Source).block().build().bucket().defaultModelWithTexture("molten_bucket").build()
//            .renderType { Supplier { RenderType.TRANSLUCENT } }
//            .register()
//
//    val MOLTEN_IRON: FluidEntry<BaseFlowingFluid.Flowing> = getSimpleMoltenFluidBuilder("molten_iron").properties {
//        it.lightLevel(15).canConvertToSource(false).temperature(
//            Temperature.celsius(1538).heat
//        )
//    }.source(BaseFlowingFluid::Source).block().build().bucket().defaultModelWithTexture("molten_bucket").build()
//        .renderType { Supplier { RenderType.TRANSLUCENT } }
//        .register()

    private fun getDefaultFluidBuilder(name: String) =
        REG.`object`(name).fluid(
            ResourceLocation.fromNamespaceAndPath(Fallacy.MOD_ID, "fluid/${name}_still"),
            ResourceLocation.fromNamespaceAndPath(Fallacy.MOD_ID, "fluid/${name}_flow")
        )

    private fun getSimpleFluidBuilder(name: String) =
        REG.fluid(
            name,
            ResourceLocation.fromNamespaceAndPath(Fallacy.MOD_ID, "fluid/${name}"),
            ResourceLocation.fromNamespaceAndPath(Fallacy.MOD_ID, "fluid/${name}")
        )

    private fun getSimpleMoltenFluidBuilder(name: String) = REG.fluid(
        name,
        Fallacy.withID("fluid/molten"),
        Fallacy.withID("fluid/molten")
    ) { properties, stillTexture, flowTexture ->
        object : FluidType(properties) {
            override fun initializeClient(consumer: Consumer<IClientFluidTypeExtensions>) =
                consumer.accept(object : IClientFluidTypeExtensions {
                    override fun getTintColor(): Int = 0xffeeee00.toInt()

                    override fun getStillTexture(): ResourceLocation = stillTexture

                    override fun getFlowingTexture(): ResourceLocation = flowTexture
                })
        }
    }
}