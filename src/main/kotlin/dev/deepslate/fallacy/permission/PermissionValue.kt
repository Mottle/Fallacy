package dev.deepslate.fallacy.permission

enum class PermissionValue {
    ALLOW,
    DENY,
    UNDEFINED;

    fun asBoolean() = when (this) {
        ALLOW -> true
        else -> false
    }
}