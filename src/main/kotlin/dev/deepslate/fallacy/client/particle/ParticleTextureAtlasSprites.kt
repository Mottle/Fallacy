package dev.deepslate.fallacy.client.particle

import dev.deepslate.fallacy.Fallacy
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.resources.ResourceLocation
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.TextureAtlasStitchedEvent

@EventBusSubscriber(modid = Fallacy.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = [Dist.CLIENT])
object ParticleTextureAtlasSprites {
    val PARTICLE_LOCATION: ResourceLocation = ResourceLocation.withDefaultNamespace("textures/atlas/particles.png")

    lateinit var CLOUD_256: TextureAtlasSprite
        private set

    lateinit var TUMBLEWEED: TextureAtlasSprite
        private set

    lateinit var WUGUN: TextureAtlasSprite
        private set

    @SubscribeEvent
    fun onTextureStitch(event: TextureAtlasStitchedEvent) {
        if (event.atlas.location() != PARTICLE_LOCATION) return

        with(event.atlas) {
            CLOUD_256 = getSprite(Fallacy.withID("cloud256"))
            TUMBLEWEED = getSprite(Fallacy.withID("tumbleweed"))
            WUGUN = getSprite(Fallacy.withID("wugun"))
        }
    }

//    private class TextureAtlasSpritesGen(
//        output: PackOutput,
//        lookupProvider: CompletableFuture<HolderLookup.Provider>,
//        existingFileHelper: ExistingFileHelper
//    ) : SpriteSourceProvider(output, lookupProvider, Fallacy.MOD_ID, existingFileHelper) {
//        override fun gather() {
//            atlas(PARTICLES_ATLAS).addSource(SingleFile(Fallacy.id("particle/cloud256"), Optional.empty()))
//        }
//    }
//
//    @SubscribeEvent
//    fun onGatherData(event: GatherDataEvent) {
//        val gen = event.generator
//        val output = gen.packOutput
//        val lookup = event.lookupProvider
//        val existingFileHelper = event.existingFileHelper
//        val textureGen = TextureAtlasSpritesGen(output, lookup, existingFileHelper)
//        gen.addProvider(event.includeClient(), textureGen)
//    }
}