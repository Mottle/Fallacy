package dev.deepslate.fallacy.client.particle

import com.mojang.blaze3d.vertex.VertexConsumer
import com.mojang.math.Axis
import dev.deepslate.fallacy.client.particle.data.Rotation
import net.minecraft.client.Camera
import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.ParticleRenderType
import net.minecraft.client.particle.TextureSheetParticle
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.core.BlockPos
import net.minecraft.util.Mth
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.levelgen.Heightmap
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import org.joml.Quaternionf
import org.joml.Vector3f
import kotlin.math.atan2
import kotlin.math.floor
import kotlin.math.sqrt

@OnlyIn(Dist.CLIENT)
open class RotationEffectParticle(
    clientLevel: ClientLevel,
    x: Double, y: Double, z: Double,
    xSpeed: Double, ySpeed: Double, zSpeed: Double
) : TextureSheetParticle(clientLevel, x, y, z, xSpeed, ySpeed, zSpeed) {

    companion object {
        val INITIAL_AABB = AABB(0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
    }

    var spawnY: Float = -1f

    val particleID = clientLevel.random.nextInt(114514)

    val world: ClientLevel
        get() = level as ClientLevel

    open var particleSprite: TextureAtlasSprite
        get() = super.sprite
        set(value) {
            super.sprite = value
        }

    val blockPos: BlockPos
        get() = BlockPos(floor(x).toInt(), floor(y).toInt(), floor(z).toInt())

//    val pos: Vec3
//        get() = Vec3(x, y, z)

    val particleAge: Int
        get() = age

    var particleGravity: Float
        get() = super.gravity
        set(value) {
            super.gravity = value
        }

    var posX: Double
        get() = x
        set(value) {
            x = value
        }

    var posY: Double
        get() = y
        set(value) {
            y = value
        }

    var posZ: Double
        get() = z
        set(value) {
            z = value
        }

    var motionX: Double
        get() = xd
        set(value) {
            xd = value
        }

    var motionY: Double
        get() = yd
        set(value) {
            yd = value
        }

    var motionZ: Double
        get() = zd
        set(value) {
            zd = value
        }

    var scale: Float
        get() = quadSize
        set(parScale) {
            //do not set the AABB as big as the render scale, otherwise huge performance losses, we'll just use 0.3 in constructor for now
            //super.setSize(parScale, parScale);
            setSizeForRenderCulling(parScale, parScale)
            quadSize = parScale
        }

    var particleAlpha: Float
        get() = alpha
        set(value) {
            alpha = value
        }

    var prevX: Double
        get() = xo
        set(value) {
            xo = value
        }

    var prevY: Double
        get() = yo
        set(value) {
            yo = value
        }

    var prevZ: Double
        get() = zo
        set(value) {
            zo = value
        }

    var particlePhysics: Boolean
        get() = hasPhysics
        set(value) {
            hasPhysics = value
        }

    var renderDistance: Int = 128

    val rotation: Rotation = Rotation()

    var windWeight = 5f

    var transparent = true

    //是否存在运动阻尼
    var motionDampen = true

    //this is for yaw only
    var useRotationAroundCenter: Boolean = false

    var rotationAroundCenter: Int = 0

    var rotationAroundCenterPrev: Int = 0

    var rotationAroundCenterSpeed: Int = 0

    var rotationDistAroundCenter: Int = 0

    var bounceSpeed: Double = 0.05

    var bounceSpeedMax: Double = 0.15

    var bounceSpeedAhead: Double = 0.35

    var bounceSpeedMaxAhead: Double = 0.25

    var bounceOnVerticalImpact: Boolean = false

    var bounceOnVerticalImpactEnergy: Double = 0.3

    //for particle behaviors - 3
    var aboveGroundHeight: Double = 4.5

    var checkAheadToBounce: Boolean = true

    //是否存在碰撞运动阻尼
    var collisionSpeedDampen = true

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

    var facePlayer: Boolean = false

    //facePlayer will override this
    var facePlayerYaw: Boolean = false

    var markCollided: Boolean = false

    var collidedHorizontally: Boolean = false

    var collidedHorizontallyDownwards: Boolean = false

    var collidedHorizontallyUpwards: Boolean = false

    //是否在最高方块下时消失
    var killWhenUnderTopmostBlock: Boolean = false

    var killWhenUnderTopmostBlockScanAheadRange: Int = 0

    //远离摄像头时消失距离阈值
    var killWhenUnderCameraDistance: Int = 0

    var killWhenFarFromCameraDistance: Int = 0

    //workaround for particles that are fading out while partially in the ground, keeps them rendering at previous brightness instead of 0
    var lastNonZeroBrightness: Int = 15728640

    //workaround for avoiding using vanilla bb which causes huge performance issues for large sizes
    var useCustomBBForRenderCulling: Boolean = false

    var boundingBoxForRender = INITIAL_AABB

    fun isCollided() = onGround || collidedHorizontally

    fun spawnAsWeatherParticle() {
        Minecraft.getInstance().particleEngine.add(this)
    }

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

    protected open fun tickExtraRotation() {

    }

    protected open fun handleSpin() {
        if (spin) {
            val xzMotivation = sqrt(xd * xd + zd * zd).toFloat()
            val spinRateAdjust = spinRate * xzMotivation * 10f
            rotation.pitch += if (particleID % 2 == 0) spinRateAdjust else -spinRateAdjust
            rotation.yaw += if (particleID % 2 == 0) spinRateAdjust else -spinRateAdjust
        }

        if (spinTowardsMotionDirection) {
            val angleToMovement = Math.toDegrees(atan2(xd, zd)).toFloat()
            rotation.yaw = angleToMovement
            rotation.pitch += spinRate
        }
    }

    protected open fun handleFadingOut() {
        if (!fadingOut) {
            if (maxFadingInTick > 0 && particleAge < maxFadingInTick) {
                val ration = particleAge.toFloat() / maxFadingInTick.toFloat()
                setAlpha(ration * fullAlpha)
            } else if (maxFadingOutTick > 0 && particleAge > lifetime - maxFadingOutTick) {
                val count = particleAge - lifetime + maxFadingOutTick
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

    protected open fun updateMotivation() {
        if (motionDampen) return

        //消除运动阻尼
        this.xd /= 0.9800000190734863
        this.yd /= 0.9800000190734863
        this.zd /= 0.9800000190734863
    }

    protected open fun updateMotionAfterCollision() {
        if (this.onGround) {
            this.xd /= 0.699999988079071
            this.zd /= 0.699999988079071
        }
    }

    protected open fun handleDeadCase() {
        if (removed || fadingOut) return
        handleCollision()
        handleUnderTopMostBlock()
        handleCamera()
    }

    protected open fun handleCollision() {
        if (!killOnCollision) return
        if (maxKillOnCollisionAge != 0 && particleAge < maxKillOnCollisionAge) return
        if (!isCollided()) return
        die()
    }

    protected open fun handleUnderTopMostBlock() {
        if (!killWhenUnderTopmostBlock) return

        val height = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, blockPos).y

        if (this.y - killWhenUnderTopmostBlockScanAheadRange <= height) {
            die()
        }
    }

    protected open fun handleCamera() {
        val camera = Minecraft.getInstance().getCameraEntity() ?: return

        if (killWhenUnderCameraDistance != 0 && y + killWhenUnderCameraDistance <= camera.y) {
            die()
        }

        if (killWhenFarFromCameraDistance != 0 && particleAge > 20 && particleAge % 5 == 0
            && camera.distanceToSqr(x, y, z) > killWhenFarFromCameraDistance * killWhenFarFromCameraDistance
        ) {
            die()
        }
    }

    protected open fun updateRotationCenter() {
        rotationAroundCenter += rotationAroundCenterSpeed
        rotationAroundCenter %= 360
    }

    override fun getRenderType(): ParticleRenderType = ParticleRenderTypes.SORTED_TRANSLUCENT

    public override fun setSize(width: Float, height: Float) {
        super.setSize(width, height)
        super.setPos(x, y, z)
    }

    override fun setSprite(sprite: TextureAtlasSprite) {
        super.setSprite(sprite)
    }

    open fun getPivotedPosition(partialTicks: Float): Vec3 = Vec3.ZERO

    fun getBoundingBoxForRender(partialTicks: Float): AABB {
        if (useCustomBBForRenderCulling) return boundingBoxForRender
        return boundingBox
    }

    fun setSizeForRenderCulling(width: Float, height: Float) {
        if (width != this.bbWidth || height != this.bbHeight) {
            this.bbWidth = width
            this.bbHeight = height
            val aabb = this.boundingBox
            val d0 = (aabb.minX + aabb.maxX - width.toDouble()) / 2.0
            val d1 = (aabb.minZ + aabb.maxZ - width.toDouble()) / 2.0
            boundingBoxForRender = AABB(
                d0,
                aabb.minY,
                d1,
                d0 + this.bbWidth.toDouble(),
                aabb.minY + this.bbHeight.toDouble(),
                d1 + this.bbWidth.toDouble()
            )
        }
    }

    override fun render(buffer: VertexConsumer, renderInfo: Camera, partialTicks: Float) {
        val position = renderInfo.position
        val pivotedPosition = getPivotedPosition(partialTicks)
        val fx = (Mth.lerp(partialTicks.toDouble(), this.xo, this.x) + pivotedPosition.x - position.x).toFloat()
        val fy = (Mth.lerp(partialTicks.toDouble(), this.yo, this.y) + pivotedPosition.y - position.y).toFloat()
        val fz = (Mth.lerp(partialTicks.toDouble(), this.zo, this.z) + pivotedPosition.z - position.z).toFloat()

        var rotQuaternion: Quaternionf

        if (facePlayer || (rotation.pitch == 0f && rotation.yaw == 0f)) {
            rotQuaternion = renderInfo.rotation()
        } else {
            rotQuaternion = Quaternionf(0f, 0f, 0f, 1f)

            if (facePlayerYaw) {
                rotQuaternion.mul(Axis.YP.rotationDegrees(-renderInfo.yRot))
            } else {
                rotQuaternion.mul(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, rotation.previousYaw, rotation.yaw)))
            }

            rotQuaternion.mul(Axis.XP.rotationDegrees(Mth.lerp(partialTicks, rotation.previousPitch, rotation.pitch)))
        }

        var rollQuaternion: Quaternionf

        if (roll == 0f) {
            rollQuaternion = renderInfo.rotation()
        } else {
            rollQuaternion = Quaternionf(renderInfo.rotation())
            rollQuaternion.rotateZ(Mth.lerp(partialTicks, this.oRoll, this.roll))
        }

        //四边形顶点
        val vertexes = listOf(
            Vector3f(-1.0f, -1.0f, 0.0f),
            Vector3f(-1.0f, 1.0f, 0.0f),
            Vector3f(1.0f, 1.0f, 0.0f),
            Vector3f(1.0f, -1.0f, 0.0f)
        )
        val quadSize = getQuadSize(partialTicks)

        for (v in vertexes) {
            v.rotate(rotQuaternion)
            v.mul(quadSize)
            v.add(fx, fy, fz)
        }

        val lightColor = getLightColor(partialTicks)

        if (lightColor > 0) {
            lastNonZeroBrightness = lightColor
        }

        with(buffer) {
            addVertex(vertexes[0].x(), vertexes[0].y(), vertexes[0].z()).setUv(u1, v1).setColor(rCol, gCol, bCol, alpha)
                .setLight(lastNonZeroBrightness)
            addVertex(vertexes[1].x(), vertexes[1].y(), vertexes[1].z()).setUv(u1, v0).setColor(rCol, gCol, bCol, alpha)
                .setLight(lastNonZeroBrightness)
            addVertex(vertexes[2].x(), vertexes[2].y(), vertexes[2].z()).setUv(u0, v0).setColor(rCol, gCol, bCol, alpha)
                .setLight(lastNonZeroBrightness)
            addVertex(vertexes[3].x(), vertexes[3].y(), vertexes[2].z()).setUv(u0, v1).setColor(rCol, gCol, bCol, alpha)
                .setLight(lastNonZeroBrightness)
        }
    }

    override fun move(x: Double, y: Double, z: Double) {
        var xx = x
        var yy = y
        var zz = z

        //物理碰撞检测
        if (hasPhysics && (xx != 0.0 || yy != 0.0 || zz != 0.0)) {
            val collided = Entity.collideBoundingBox(null, Vec3(xx, yy, zz), boundingBox, level, emptyList())
            xx = collided.x
            yy = collided.y
            zz = collided.z
        }

        if (xx != 0.0 || yy != 0.0 || zz != 0.0) {
            boundingBox = boundingBox.move(xx, yy, zz)
            if (useCustomBBForRenderCulling) {
                boundingBoxForRender = getBoundingBoxForRender(1f).move(xx, yy, zz)
            }
            setLocationFromBoundingbox()
        }

        onGround = y != yy && y < 0.0
        collidedHorizontally = x != xx || z != zz
        collidedHorizontallyDownwards = y < yy
        collidedHorizontallyUpwards = y > yy

        if (x != xx) {
            xd = 0.0
        }

        if (z != zz) {
            zd = 0.0
        }

        handleMotivationCollision()
    }

    //碰撞处理
    private fun handleMotivationCollision() {
        if (!markCollided) {
            if (onGround || collidedHorizontallyDownwards || collidedHorizontally || collidedHorizontallyUpwards) {
                onHit()
                markCollided = true
            }

            if (bounceOnVerticalImpact && (onGround || collidedHorizontallyDownwards)) {
                motionY = -motionY * bounceOnVerticalImpactEnergy
            }
        }
    }

    open fun onHit() {}
}