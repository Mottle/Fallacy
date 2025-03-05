package dev.deepslate.fallacy.datagen.model

import com.tterrag.registrate.providers.DataGenContext
import com.tterrag.registrate.providers.RegistrateBlockstateProvider
import dev.deepslate.fallacy.Fallacy
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.client.model.generators.ConfiguredModel

object ModelHelper {
    fun <T : Block> withTexture(path: String): (DataGenContext<Block, T>, RegistrateBlockstateProvider) -> Unit =
        { context: DataGenContext<Block, T>, provider: RegistrateBlockstateProvider ->
            val texturePath = Fallacy.withID(path)
            val model = provider.models().cubeAll(path, texturePath)
            val configuredModel = model.let(::ConfiguredModel)
            val itemPath = BuiltInRegistries.BLOCK.getKey(context.entry).path

            provider.getVariantBuilder(context.entry).partialState().setModels(configuredModel)
            provider.itemModels().getBuilder(itemPath).parent(model)
        }
}