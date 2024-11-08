package dev.deepslate.fallacy.client.particle

import dev.deepslate.fallacy.client.particle.data.Rotation
import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.ParticleRenderType
import net.minecraft.client.particle.TextureSheetParticle
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.core.BlockPos
import net.minecraft.world.level.levelgen.Heightmap
import kotlin.math.atan2
import kotlin.math.floor
import kotlin.math.sqrt

open class ComplexParticle(
    clientLevel: ClientLevel,
    x: Double, y: Double, z: Double,
    xSpeed: Double, ySpeed: Double, zSpeed: Double
) : TextureSheetParticle(clientLevel, x, y, z, xSpeed, ySpeed, zSpeed) {
    val particleId: Int = clientLevel.random.nextInt(100000)

    open var sprite: TextureAtlasSprite
        get() = super.sprite
        set(value) {
            super.sprite = value
        }

    val blockPos: BlockPos
        get() = BlockPos(floor(x).toInt(), floor(y).toInt(), floor(z).toInt())

    val rotation: Rotation = Rotation()

    //是否存在运动阻尼
    var motionDampen = true

    //是否存在碰撞运动阻尼
    var collisionSpeedDampen = true

    //this is for yaw only
    var useRotationAroundCenter: Boolean = false

    var rotationAroundCenter: Int = 0

    var rotationAroundCenterPrev: Int = 0

    var rotationAroundCenterSpeed: Int = 0

    var rotationDistAroundCenter: Int = 0

    //旋转
    var spin: Boolean = false

    var spinRate: Float = 10f

    var spinTowardsMotionDirection: Boolean = false

    //淡出
    var fadingOut: Boolean = false

    var maxFadingOutOnDeathTick: Int = -1

    var fadingOutOnDeathTick: Int = 0

    var maxFadingInTick: Int = 0

    var maxFadingOutTick: Int = 0

    //最终透明度
    var fullAlpha: Float = 1f

    //碰撞
    var killOnCollision: Boolean = false

    var maxKillOnCollisionAge: Int = 0

    var collidedHorizontally: Boolean = false

    var collidedHorizontallyDownwards: Boolean = false

    var collidedHorizontallyUpwards: Boolean = false

    //是否在最高方块下时消失
    var killWhenUnderTopmostBlock: Boolean = false

    var killWhenUnderTopmostBlockScanAheadRange: Int = 0

    //远离摄像头时消失距离阈值
    var killWhenUnderCameraDistance: Int = 0

    var killWhenFarFromCameraDistance: Int = 0

    fun isCollided() = onGround || collidedHorizontally

    fun die() {
        if (maxFadingOutOnDeathTick > 0) {
            fadingOut = true
            fadingOutOnDeathTick = 0
        } else {
            remove()
        }
    }

    override fun remove() {
        super.remove()
    }

    override fun tick() {
        super.tick()
        rotation.updateRotation()
        updateMotivation()
        handleDeadCase()
        updateMotionAfterCollision()
        handleSpin()
        handleFadingOut()
        updateRotationCenter()
        tickExtraRotation()
    }

    private fun tickExtraRotation() {

    }

    private fun handleSpin() {
        if (spin) {
            val xzMotivation = sqrt(xd * xd + zd * zd).toFloat()
            val spinRateAdjust = spinRate * xzMotivation * 10f
            rotation.pitch += if (particleId % 2 == 0) spinRateAdjust else -spinRateAdjust
            rotation.yaw += if (particleId % 2 == 0) spinRateAdjust else -spinRateAdjust
        }

        if (spinTowardsMotionDirection) {
            val angleToMovement = Math.toDegrees(atan2(xd, zd)).toFloat()
            rotation.yaw = angleToMovement
            rotation.pitch += spinRate
        }
    }

    private fun handleFadingOut() {
        if (!fadingOut) {
            if (maxFadingInTick > 0 && age < maxFadingInTick) {
                val ration = age.toFloat() / maxFadingInTick.toFloat()
                setAlpha(ration * fullAlpha)
            } else if (maxFadingOutTick > 0 && age > lifetime - maxFadingOutTick) {
                val count = age - lifetime + maxFadingOutTick
                val ratio = (maxFadingOutTick - count).toFloat() / maxFadingOutTick.toFloat()

                this.setAlpha(ratio * fullAlpha)
            } else if (maxFadingInTick > 0 || maxFadingOutTick > 0) {
                setAlpha(fullAlpha)
            }
        } else {
            if (fadingOutOnDeathTick < maxFadingOutOnDeathTick) {
                fadingOutOnDeathTick++
            } else {
                this.remove()
            }
            val ration = 1f - (fadingOutOnDeathTick / maxFadingOutOnDeathTick)

            setAlpha(ration * fullAlpha)
        }
    }

    private fun updateMotivation() {
        if (motionDampen) return

        //消除运动阻尼
        this.xd /= 0.9800000190734863
        this.yd /= 0.9800000190734863
        this.zd /= 0.9800000190734863
    }

    private fun updateMotionAfterCollision() {
        if (this.onGround) {
            this.xd /= 0.699999988079071
            this.zd /= 0.699999988079071
        }
    }

    private fun handleDeadCase() {
        if (removed || fadingOut) return
        handleCollision()
        handleUnderTopmostBlock()
        handleCamera()
    }

    private fun handleCollision() {
        if (!killOnCollision) return
        if (maxKillOnCollisionAge != 0 && age < maxKillOnCollisionAge) return
        if (!isCollided()) return
        die()
    }

    private fun handleUnderTopmostBlock() {
        if (!killWhenUnderTopmostBlock) return

        val height = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, blockPos).y

        if (this.y - killWhenUnderTopmostBlockScanAheadRange <= height) {
            die()
        }
    }

    private fun handleCamera() {
        val camera = Minecraft.getInstance().getCameraEntity() ?: return

        if (killWhenUnderCameraDistance != 0 && y + killWhenUnderCameraDistance <= camera.y) {
            die()
        }

        if (killWhenFarFromCameraDistance != 0 && age > 20 && age % 5 == 0
            && camera.distanceToSqr(x, y, z) > killWhenFarFromCameraDistance * killWhenFarFromCameraDistance
        ) {
            die()
        }
    }

    private fun updateRotationCenter() {
        rotationAroundCenter += rotationAroundCenterSpeed
        rotationAroundCenter %= 360
    }

    override fun getRenderType(): ParticleRenderType = ParticleRenderTypes.SORTED_TRANSLUCENT
}