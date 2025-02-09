package dev.deepslate.fallacy.client.particle.impl

import com.mojang.blaze3d.vertex.VertexConsumer
import com.mojang.math.Axis
import dev.deepslate.fallacy.client.particle.TextureEffectParticle
import net.minecraft.client.Camera
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.util.Mth
import org.joml.Quaternionf
import org.joml.Vector3f

class CrossSectionParticle(
    clientLevel: ClientLevel,
    x: Double, y: Double, z: Double,
    xSpeed: Double, ySpeed: Double, zSpeed: Double,
    texture: TextureAtlasSprite
) : TextureEffectParticle(clientLevel, x, y, z, xSpeed, ySpeed, zSpeed, texture) {

    override fun render(buffer: VertexConsumer, renderInfo: Camera, partialTicks: Float) {
        val position = renderInfo.position

        val fx = (Mth.lerp(partialTicks.toDouble(), this.xo, this.x) - position.x()).toFloat()
        val fy = (Mth.lerp(partialTicks.toDouble(), this.yo, this.y) - position.y()).toFloat()
        val fz = (Mth.lerp(partialTicks.toDouble(), this.zo, this.z) - position.z()).toFloat()

        val quaternion: Quaternionf

        if (this.facePlayer || (this.rotation.pitch == 0f && this.rotation.yaw == 0f)) {
            quaternion = renderInfo.rotation()
        } else {
            // override rotations
            quaternion = Quaternionf(0f, 0f, 0f, 1f)

            if (!facePlayerYaw) {
                quaternion.mul(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, rotation.previousYaw, rotation.yaw)))
            } else {
                quaternion.mul(Axis.YP.rotationDegrees(-renderInfo.yRot))
            }

            quaternion.mul(Axis.XP.rotationDegrees(Mth.lerp(partialTicks, rotation.previousPitch, rotation.pitch)))
        }

        val vertexes0 = arrayOf<Vector3f>(
            Vector3f(-1.0f, -1.0f, 0.0f),
            Vector3f(-1.0f, 1.0f, 0.0f),
            Vector3f(1.0f, 1.0f, 0.0f),
            Vector3f(1.0f, -1.0f, 0.0f)
        )

        val vertexes1 = arrayOf<Vector3f>(
            Vector3f(0.0f, -1.0f, -1.0f),
            Vector3f(0.0f, 1.0f, -1.0f),
            Vector3f(0.0f, 1.0f, 1.0f),
            Vector3f(0.0f, -1.0f, 1.0f)
        )

        val vertexes2 = arrayOf<Vector3f>(
            Vector3f(-1.0f, 0.0f, -1.0f),
            Vector3f(-1.0f, 0.0f, 1.0f),
            Vector3f(1.0f, 0.0f, 1.0f),
            Vector3f(1.0f, 0.0f, -1.0f)
        )

        val quadSize = this.getQuadSize(partialTicks)

        for (i in 0..3) {
            val vector3f = vertexes0[i]
            vector3f.rotate(quaternion)
            vector3f.mul(quadSize)
            vector3f.add(fx, fy, fz)
        }

        for (i in 0..3) {
            val vector3f = vertexes1[i]
            vector3f.rotate(quaternion)
            vector3f.mul(quadSize)
            vector3f.add(fx, fy, fz)
        }

        for (i in 0..3) {
            val vector3f = vertexes2[i]
            vector3f.rotate(quaternion)
            vector3f.mul(quadSize)
            vector3f.add(fx, fy, fz)
        }

        val lightColor = this.getLightColor(partialTicks)

        if (lightColor > 0) {
            lastNonZeroBrightness = lightColor
        }

        addVertexes(buffer, vertexes0)
        addVertexes(buffer, vertexes1)
        addVertexes(buffer, vertexes2)

//        buffer.addVertex(vertexes0[0].x(), vertexes0[0].y(), vertexes0[0].z()).setUv(u1, v1)
//            .setColor(this.rCol, this.gCol, this.bCol, this.alpha).setLight(j)
//        buffer.addVertex(vertexes0[1].x(), vertexes0[1].y(), vertexes0[1].z()).setUv(u1, v0)
//            .setColor(this.rCol, this.gCol, this.bCol, this.alpha).setLight(j)
//        buffer.addVertex(vertexes0[2].x(), vertexes0[2].y(), vertexes0[2].z()).setUv(u0, v0)
//            .setColor(this.rCol, this.gCol, this.bCol, this.alpha).setLight(j)
//        buffer.addVertex(vertexes0[3].x(), vertexes0[3].y(), vertexes0[3].z()).setUv(u0, v1)
//            .setColor(this.rCol, this.gCol, this.bCol, this.alpha).setLight(j)
//
//        buffer.addVertex(vertexes1[0].x(), vertexes1[0].y(), vertexes1[0].z()).setUv(u1, v1)
//            .setColor(this.rCol, this.gCol, this.bCol, this.alpha).setLight(j)
//        buffer.addVertex(vertexes1[1].x(), vertexes1[1].y(), vertexes1[1].z()).setUv(u1, v0)
//            .setColor(this.rCol, this.gCol, this.bCol, this.alpha).setLight(j)
//        buffer.addVertex(vertexes1[2].x(), vertexes1[2].y(), vertexes1[2].z()).setUv(u0, v0)
//            .setColor(this.rCol, this.gCol, this.bCol, this.alpha).setLight(j)
//        buffer.addVertex(vertexes1[3].x(), vertexes1[3].y(), vertexes1[3].z()).setUv(u0, v1)
//            .setColor(this.rCol, this.gCol, this.bCol, this.alpha).setLight(j)
//
//        buffer.addVertex(vertexes2[0].x(), vertexes2[0].y(), vertexes2[0].z()).setUv(u1, v1)
//            .setColor(this.rCol, this.gCol, this.bCol, this.alpha).setLight(j)
//        buffer.addVertex(vertexes2[1].x(), vertexes2[1].y(), vertexes2[1].z()).setUv(u1, v0)
//            .setColor(this.rCol, this.gCol, this.bCol, this.alpha).setLight(j)
//        buffer.addVertex(vertexes2[2].x(), vertexes2[2].y(), vertexes2[2].z()).setUv(u0, v0)
//            .setColor(this.rCol, this.gCol, this.bCol, this.alpha).setLight(j)
//        buffer.addVertex(vertexes2[3].x(), vertexes2[3].y(), vertexes2[3].z()).setUv(u0, v1)
//            .setColor(this.rCol, this.gCol, this.bCol, this.alpha).setLight(j)
    }

    private fun addVertexes(buffer: VertexConsumer, vertexes: Array<Vector3f>) {
        buffer.addVertex(vertexes[0].x(), vertexes[0].y(), vertexes[0].z()).setUv(u1, v1)
            .setColor(this.rCol, this.gCol, this.bCol, this.alpha).setLight(lastNonZeroBrightness)
        buffer.addVertex(vertexes[1].x(), vertexes[1].y(), vertexes[1].z()).setUv(u1, v0)
            .setColor(this.rCol, this.gCol, this.bCol, this.alpha).setLight(lastNonZeroBrightness)
        buffer.addVertex(vertexes[2].x(), vertexes[2].y(), vertexes[2].z()).setUv(u0, v0)
            .setColor(this.rCol, this.gCol, this.bCol, this.alpha).setLight(lastNonZeroBrightness)
        buffer.addVertex(vertexes[3].x(), vertexes[3].y(), vertexes[3].z()).setUv(u0, v1)
            .setColor(this.rCol, this.gCol, this.bCol, this.alpha).setLight(lastNonZeroBrightness)
    }

}