package dev.deepslate.fallacy.permission.integration

import dev.deepslate.fallacy.permission.PermissionProvider
import dev.deepslate.fallacy.permission.PermissionValue
import net.luckperms.api.LuckPermsProvider
import net.luckperms.api.model.user.UserManager
import net.luckperms.api.util.Tristate
import java.util.*

class LuckPermsProvider : PermissionProvider {

    override val name: String = "LuckPerms"

    companion object {
        private fun convertTripleState(state: Tristate): PermissionValue = when (state) {
            Tristate.TRUE -> PermissionValue.ALLOW
            Tristate.FALSE -> PermissionValue.DENY
            Tristate.UNDEFINED -> PermissionValue.UNDEFINED
        }
    }

    private val luckPermsHolder by lazy { LuckPermsProvider.get() }

    val userManager: UserManager
        get() = luckPermsHolder.userManager

    override fun query(uuid: UUID, key: String): PermissionValue {
        val user = userManager.getUser(uuid) ?: return PermissionValue.UNDEFINED

        return user.cachedData.permissionData.checkPermission(key).let(::convertTripleState)
    }

    override fun queryAsync(uuid: UUID, key: String, callback: (PermissionValue) -> Unit) {
        userManager.loadUser(uuid).thenAcceptAsync { user ->
            val result = user.cachedData.permissionData.checkPermission(key).let(::convertTripleState)
            callback(result)
        }
    }
}