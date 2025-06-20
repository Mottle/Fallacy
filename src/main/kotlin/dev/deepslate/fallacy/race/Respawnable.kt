package dev.deepslate.fallacy.race

import net.minecraft.server.level.ServerPlayer

interface Respawnable {
    //Clone发生在Respawn之前，在此期间向Client发送的数据包会在客户端指向已死亡的Player，因此不可以在Clone期间发送数据包同步Attachment
    fun onPreRespawn(player: ServerPlayer, origin: ServerPlayer) {}

    fun onRespawn(player: ServerPlayer)
}