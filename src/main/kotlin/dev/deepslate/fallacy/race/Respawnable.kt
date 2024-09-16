package dev.deepslate.fallacy.race

import net.minecraft.server.level.ServerPlayer

interface Respawnable {
    fun onRespawn(player: ServerPlayer)
}