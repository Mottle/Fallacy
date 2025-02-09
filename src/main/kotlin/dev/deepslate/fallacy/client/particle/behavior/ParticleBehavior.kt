package dev.deepslate.fallacy.client.particle.behavior

import dev.deepslate.fallacy.client.particle.RotationEffectParticle
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import net.minecraft.world.phys.Vec3
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import java.util.*
import kotlin.math.*

@OnlyIn(Dist.CLIENT)
open class ParticleBehavior(source: Vec3?) : Iterable<RotationEffectParticle> {

    companion object {
        const val RATE_DARKEN: Float = 0.025f
        const val RATE_BRIGHTNESS: Float = 0.010f
        const val RATE_BRIGHTNESS_SLOWER: Float = 0.003f
        const val RATE_ALPHA: Float = 0.002f
        const val RATE_SCALE: Float = 0.1f
        const val TICK_SMOKIFY_TRIGGER: Int = 40
    }

    protected val particles = ObjectArrayList<RotationEffectParticle>()

    override fun iterator(): Iterator<RotationEffectParticle> = particles.iterator()

    protected val random = Random()

    var coordSource: Vec3? = source

    fun addParticle(particle: RotationEffectParticle) {
        particles.add(particle)
    }

    fun tick() {
        particles.removeIf { p -> !p.isAlive }
        particles.forEach(::tickUpdate)
    }

    fun tickUpdate(particle: RotationEffectParticle) {
        tickAct(particle)
    }

    //default is smoke effect, override for custom
    open fun tickAct(particle: RotationEffectParticle) {
        val centerX = coordSource?.x ?: particle.pos.x
        val centerZ = coordSource?.z ?: particle.pos.z

        val dx = centerX - particle.pos.x
        val dz = centerZ - particle.pos.z
        val distance2Center = sqrt(dx * dx + dz * dz)

        val adjYaw = min(360.0, 45.0 + particle.particleAge)
        val rotYaw = atan2(dz, dx) * 180.0 / PI - adjYaw

        val speed = 0.1

        if (particle.particleAge < 25 && distance2Center > 0.05) {
            particle.motionX = cos(rotYaw * 0.017453) * speed
            particle.motionZ = sin(rotYaw * 0.017453) * speed
        } else {
            var speed2 = 0.008
            val particleSpeed = sqrt(particle.motionX * particle.motionX + particle.motionZ * particle.motionZ)

            //cheap air search code
            if (particleSpeed < 0.2 && particle.motionY < 0.01) {
                speed2 = 0.08
            }

            if (particleSpeed < 0.002 && abs(particle.motionY) < 0.02) {
                particle.motionY = particle.motionY - 0.15
            }

            particle.motionX = particle.motionX + (random.nextDouble() - random.nextDouble()) * speed2
            particle.motionZ = particle.motionX + (random.nextDouble() - random.nextDouble()) * speed2
        }

        var brightnessShiftRate = RATE_DARKEN
        val stateChangeTick = TICK_SMOKIFY_TRIGGER

        if (particle.particleAge < stateChangeTick) {
            particle.particleGravity = -0.2f
            particle.setColor(
                particle.rCol - brightnessShiftRate,
                particle.gCol - brightnessShiftRate,
                particle.bCol - brightnessShiftRate
            )
        } else if (particle.particleAge == stateChangeTick) {
            particle.setColor(0f, 0f, 0f)
        } else {
            brightnessShiftRate = RATE_BRIGHTNESS
            particle.particleGravity = -0.05f

            if (particle.rCol >= 0.3f) {
                brightnessShiftRate = RATE_BRIGHTNESS_SLOWER
            }

            particle.setColor(
                particle.rCol + brightnessShiftRate,
                particle.gCol + brightnessShiftRate,
                particle.bCol + brightnessShiftRate
            )

            if (particle.fullAlpha > 0) {
                particle.setAlpha(particle.particleAlpha - RATE_ALPHA)
            } else {
                particle.remove()
            }
        }

        if (particle.scale < 8f) particle.scale = particle.scale + RATE_SCALE
    }

    open fun initParticle(particle: RotationEffectParticle): RotationEffectParticle {
        particle.prevX = particle.posX
        particle.prevY = particle.posY
        particle.prevZ = particle.posZ

        //减小AABB大小，提升性能
        particle.setSize(0.01f, 0.01f)

        return particle
    }
}