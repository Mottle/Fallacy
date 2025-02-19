package dev.deepslate.fallacy.client.render

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.client.particle.ParticleTextureAtlasSprites
import dev.deepslate.fallacy.client.particle.behavior.SandstormBehavior
import dev.deepslate.fallacy.client.particle.impl.CrossSectionParticle
import dev.deepslate.fallacy.client.particle.impl.SandstormParticle
import dev.deepslate.fallacy.util.extension.weatherEngine
import dev.deepslate.fallacy.weather.Weathers
import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.renderer.LevelRenderer
import net.minecraft.client.renderer.RenderType
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.levelgen.Heightmap
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.ClientTickEvent
import net.neoforged.neoforge.client.event.RenderLevelStageEvent
import kotlin.math.floor

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = Fallacy.MOD_ID, value = [Dist.CLIENT])
object WeatherRenderer {
    private val particleBehavior = SandstormBehavior(null)

    @SubscribeEvent
    fun onTick(event: ClientTickEvent.Pre) {
        val minecraft = Minecraft.getInstance()
        val level = minecraft.level ?: return
        val player = minecraft.player ?: return
        val pos = player.blockPosition()
        val weatherEngine = level.weatherEngine ?: return
        val weatherInstance = weatherEngine.getWeatherAt(pos)

        if (minecraft.isPaused) return

        if (weatherInstance.`is`(Weathers.SANDSTORM)) {
            renderSandstorm(player, level)
        }

        particleBehavior.tick()
    }

    private fun renderSandstorm(player: Player, level: ClientLevel) {
        val spawnAreaSize = 60

        //风沙
        for (x in 0..12) {
            val pos = spawnPosition(player, spawnAreaSize)

            if (!canPrecipitateAt(level, pos)) continue

            val sprite = ParticleTextureAtlasSprites.CLOUD_256
            val part = SandstormParticle(
                level, pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), 0.0, 0.0, 0.0, sprite
            )
            particleBehavior.initParticle(part)
            particleBehavior.initParticleSandstormDust(part)
            particleBehavior.addParticle(part)
            part.particleAlpha = 0f
            part.spawnAsWeatherParticle()
        }

        if (player.random.nextInt(0, 3) != 0) return
        for (x in 0..1) {
            val pos = spawnPosition(player, spawnAreaSize)

            if (!canPrecipitateAt(level, pos)) continue

            val sprite = ParticleTextureAtlasSprites.TUMBLEWEED
            val part = CrossSectionParticle(
                level, pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), 0.0, 0.0, 0.0, sprite
            )
            particleBehavior.initParticle(part)
            particleBehavior.initParticleSandstormTumbleweed(part)
            particleBehavior.addParticle(part)
            part.spawnAsWeatherParticle()
        }
    }

    private fun canPrecipitateAt(level: Level, strikePosition: BlockPos): Boolean =
        level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, strikePosition).y <= strikePosition.y

    private fun spawnPosition(player: Player, spawnAreaSize: Int) = with(player.random) {
        BlockPos(
            floor(player.x + nextInt(spawnAreaSize).toDouble() - (spawnAreaSize.toDouble() / 2.0)).toInt(),
            floor(player.y - 2.0 + nextInt(10).toDouble()).toInt(),
            floor(player.z + nextInt(spawnAreaSize).toDouble() - (spawnAreaSize.toDouble() / 2.0)).toInt()
        )
    }

    @SubscribeEvent
    fun onRenderWorldLast(event: RenderLevelStageEvent) {
        return
        val minecraft = Minecraft.getInstance()
        val level = minecraft.level ?: return
        val pose = event.poseStack

        for (p in particleBehavior) {
            val aabb = p.boundingBoxForRender
            pose.pushPose()


            // Get the camera position to adjust the rendering
            val camX: Double = minecraft.entityRenderDispatcher.camera.position.x()
            val camY: Double = minecraft.entityRenderDispatcher.camera.position.y()
            val camZ: Double = minecraft.entityRenderDispatcher.camera.position.z()

            // Render the AABB
            LevelRenderer.renderLineBox(
                pose,
                minecraft.renderBuffers().bufferSource().getBuffer(RenderType.lines()),
                aabb.move(-camX, -camY, -camZ),
                1.0F, 0.0F, 0.0F, 1.0F // Red color for the box
            )

            // Pop the pose stack to reset transformations
            pose.popPose()
        }
    }

}