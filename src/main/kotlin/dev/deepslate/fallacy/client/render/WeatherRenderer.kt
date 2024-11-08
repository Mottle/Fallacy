package dev.deepslate.fallacy.client.render

import dev.deepslate.fallacy.Fallacy
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.levelgen.Heightmap
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.ClientTickEvent

@EventBusSubscriber(modid = Fallacy.MOD_ID, value = [Dist.CLIENT])
object WeatherRenderer {

//    private val particleBehavior = ParticleBehaviorSandstorm(null)

    @SubscribeEvent
    fun onTick(event: ClientTickEvent.Pre) {
//        val player = Minecraft.getInstance().player ?: return
//        val pos = player.blockPosition()
//        val weatherEngine = player.level().weatherEngine ?: return
//        val weather = weatherEngine.getWeatherAt(pos)
//
//        if (weather.`is`(FallacyWeathers.SANDSTORM)) {
//            renderSandstorm(player, player.level())
//        }
//
//        particleBehavior.tickUpdateList()
    }

    private fun renderSandstorm(player: Player, level: Level) {
//        val spawnAreaSize = 60
//
//        //extra dust
//        for (x in 0..80) {
//            val rand = player.random
//            val pos = BlockPos(
//                floor(player.x + rand.nextInt(spawnAreaSize) - (spawnAreaSize / 2)).toInt(),
//                floor(player.y - 2 + rand.nextInt(10)).toInt(),
//                floor(player.z + rand.nextInt(spawnAreaSize) - (spawnAreaSize / 2)).toInt()
//            )
//
//            if (canPrecipitateAt(level, pos)) {
//                val sprite = TextureAtlasSprites.CLOUD_256
//
//                val part = ParticleSandstorm(
//                    level as ClientLevel,
//                    pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(),
//                    0.0, 0.0, 0.0,
//                    sprite
//                )
//                particleBehavior.initParticle(part)
//                particleBehavior.initParticleSnowstormCloudDust(part)
//                particleBehavior.particles.add(part)
//                part.spawnAsWeatherEffect()
//
//            }
//        }
    }

    private fun canPrecipitateAt(level: Level, strikePosition: BlockPos): Boolean =
        level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, strikePosition).y <= strikePosition.y

}