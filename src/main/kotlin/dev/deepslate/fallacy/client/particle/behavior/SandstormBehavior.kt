package dev.deepslate.fallacy.client.particle.behavior

import dev.deepslate.fallacy.client.particle.RotationEffectParticle
import dev.deepslate.fallacy.util.extension.windEngine
import net.minecraft.client.Minecraft
import net.minecraft.core.BlockPos
import net.minecraft.util.Mth
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.Vec3
import kotlin.math.min

class SandstormBehavior(source: Vec3?) : ParticleBehavior(source) {

    override fun initParticle(particle: RotationEffectParticle): RotationEffectParticle {
        super.initParticle(particle)

        //fog
        particle.rotation.yaw = random.nextInt(360).toFloat()
        particle.rotation.pitch = (random.nextInt(50) - random.nextInt(50)).toFloat()

        //cloud
        particle.setLifetime(450 + random.nextInt(10))
        val randFloat: Float = (random.nextFloat() * 0.6f)
        val baseBright = 0.7f
        val finalBright = min(1.0, (baseBright + randFloat).toDouble()).toFloat()
        particle.setColor(finalBright, finalBright, finalBright)

//        particle.brightness = 1f
        particle.setAlpha(1f)

        val sizeBase = (30f + (random.nextDouble() * 4f)).toFloat()

        particle.scale = sizeBase
        particle.particlePhysics = true

        particle.renderDistance = 2048

        particle.facePlayer = true
        particle.particleGravity = 0.03f

        return particle
    }


    fun initParticleSandstormDust(particle: RotationEffectParticle) {
        val windForce: Vec3 = wind() ?: return

        particle.motionX = windForce.x
        particle.motionZ = windForce.z

        particle.facePlayer = false
        particle.transparent = true

        particle.rotation.yaw = random.nextInt(360).toFloat()
        particle.rotation.pitch = random.nextInt(360).toFloat()

        particle.lifetime = 40
        particle.particleGravity = 0.09f
        particle.setAlpha(0f)

        val brightnessMulti: Float = 1f - (random.nextFloat() * 0.5f)

        particle.setColor(0.65f * brightnessMulti, 0.6f * brightnessMulti, 0.3f * brightnessMulti)
        particle.scale = 40 * 0.15f
        particle.aboveGroundHeight = 0.2

        particle.killOnCollision = true
        particle.killWhenFarFromCameraDistance = 15

        particle.maxFadingInTick = 5
        particle.maxFadingOutTick = 5
        particle.fadingOutOnDeathTick = 5

        particle.windWeight = 1f
    }

    fun initParticleSandstormTumbleweed(particle: RotationEffectParticle) {
        val windForce: Vec3 = wind() ?: return

        particle.motionX = windForce.x
        particle.motionZ = windForce.z

        particle.facePlayer = false
        particle.facePlayerYaw = false
        particle.spinTowardsMotionDirection = true

        particle.transparent = true

        particle.rotation.yaw = random.nextInt(360).toFloat()
        particle.rotation.pitch = random.nextInt(360).toFloat()

        particle.lifetime = 80
        particle.particleGravity = 0.3f
        particle.setAlpha(0f)

        val brightnessMulti: Float = 1f - (random.nextFloat() * 0.2f)

        particle.setColor(1f * brightnessMulti, 1f * brightnessMulti, 1f * brightnessMulti)
        particle.scale = 8 * 0.15f
        particle.aboveGroundHeight = 0.5

        particle.collisionSpeedDampen = false

        particle.bounceSpeed = 0.03
        particle.bounceSpeedAhead = 0.03

        particle.killOnCollision = false
        particle.killWhenFarFromCameraDistance = 30

        particle.maxFadingInTick = 5
        particle.maxFadingOutTick = 5
        particle.fadingOutOnDeathTick = 5

        particle.windWeight = 1f
    }

    private fun wind(): Vec3? {
        val minecraft = Minecraft.getInstance()
        val level = minecraft.level ?: return null
        val engine = level.windEngine ?: return null
        val pos = minecraft.player?.blockPosition() ?: return null
        val windForce: Vec3 = engine.getWindAt(pos)
        return windForce
    }


    override fun tickAct(particle: RotationEffectParticle) {

        if (!particle.isAlive) {
            particles.remove(particle)
        } else {
            //random rotation yaw adjustment
            if (particle.particleID % 2 == 0) {
                particle.rotation.yaw -= 0.1f
            } else {
                particle.rotation.yaw += 0.1f
            }

            val ticksFadeInMax = 10f
            val ticksFadeOutMax = 10f

            //fade in and fade out near age edges
            if (particle.particleAge < ticksFadeInMax) {
                particle.setAlpha(1f.coerceAtMost(particle.particleAge.toFloat() / ticksFadeInMax))
            } else if (particle.particleAge > particle.getLifetime() - ticksFadeOutMax) {
                val count: Float = particle.particleAge.toFloat() - (particle.getLifetime() - ticksFadeOutMax)
                val `val` = (ticksFadeOutMax - (count)) / ticksFadeOutMax
                particle.setAlpha(`val`)
            }

            //get pos a bit under particle
            val pos: BlockPos = BlockPos(
                particle.blockPos.x,
                Mth.floor((particle.posY - particle.aboveGroundHeight)),
                particle.blockPos.z,
            )
            val state: BlockState = particle.world.getBlockState(pos)
            //if particle is near ground, push it up to keep from landing
            if (!state.isAir) {
                if (particle.motionY < particle.bounceSpeedMax) {
                    particle.motionY = particle.motionY + particle.bounceSpeed
                }
                //check ahead for better flowing over large cliffs
            } else {
                val aheadMultiplier = 20.0
                val posAhead: BlockPos = BlockPos(
                    Mth.floor(particle.posX + (particle.motionX * aheadMultiplier)),
                    Mth.floor(particle.posY - particle.aboveGroundHeight),
                    Mth.floor(particle.posZ + (particle.motionZ * aheadMultiplier))
                )
                val stateAhead: BlockState = particle.world.getBlockState(posAhead)
                if (!stateAhead.isAir) {
                    if (particle.motionY < particle.bounceSpeedMaxAhead) {
                        particle.motionY = particle.motionY + particle.bounceSpeedAhead
                    }
                }
            }

            val moveSpeedRand = 0.005

            particle.motionX =
                particle.motionX + (random.nextDouble() * moveSpeedRand - random.nextDouble() * moveSpeedRand)
            particle.motionZ =
                particle.motionZ + (random.nextDouble() * moveSpeedRand - random.nextDouble() * moveSpeedRand)

            if (particle.spawnY != -1f) {
                particle.setPos(particle.posX, particle.spawnY.toDouble(), particle.posZ)
            }
        }
    }
}