package dev.deepslate.fallacy.permission.integration

import dev.deepslate.fallacy.permission.PermissionProvider
import dev.deepslate.fallacy.permission.PermissionValue
import net.minecraft.server.level.ServerPlayer
import java.util.*

class SimplePermissionProvider : PermissionProvider {
    override fun query(player: ServerPlayer, key: String): PermissionValue = PermissionValue.ALLOW

    override fun query(uuid: UUID, key: String): PermissionValue = PermissionValue.ALLOW

    override fun queryAsync(uuid: UUID, key: String, callback: (PermissionValue) -> Unit) =
        callback(PermissionValue.ALLOW)

    override val name: String = "Simple"
}