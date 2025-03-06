package dev.deepslate.fallacy.permission

import net.minecraft.server.level.ServerPlayer
import java.util.*

interface PermissionProvider {
    fun query(player: ServerPlayer, key: String): PermissionValue = query(player.uuid, key)

    fun query(uuid: UUID, key: String): PermissionValue

    fun queryAsync(uuid: UUID, key: String, callback: (PermissionValue) -> Unit)

    val name: String
}