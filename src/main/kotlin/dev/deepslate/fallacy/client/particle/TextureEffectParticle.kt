package dev.deepslate.fallacy.client.particle

import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
open class TextureEffectParticle(
    clientLevel: ClientLevel,
    x: Double, y: Double, z: Double,
    xSpeed: Double, ySpeed: Double, zSpeed: Double,
    texture: TextureAtlasSprite
) : RotationEffectParticle(clientLevel, x, y, z, xSpeed, ySpeed, zSpeed) {
    init {
        setSprite(texture)
        rCol = 1f
        gCol = 1f
        bCol = 1f
        gravity = 1f
        quadSize = 0.15f
        lifetime = 100
        hasPhysics = false
    }
}