package dev.deepslate.fallacy.client.particle.impl

import dev.deepslate.fallacy.client.particle.TextureEffectParticle
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.renderer.texture.TextureAtlasSprite

class SandstormParticle(
    clientLevel: ClientLevel,
    x: Double, y: Double, z: Double,
    xSpeed: Double, ySpeed: Double, zSpeed: Double,
    texture: TextureAtlasSprite
) : TextureEffectParticle(clientLevel, x, y, z, xSpeed, ySpeed, zSpeed, texture) {

}