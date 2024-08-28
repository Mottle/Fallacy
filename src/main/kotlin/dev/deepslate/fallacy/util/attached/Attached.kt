package dev.deepslate.fallacy.util.attached

import net.minecraft.network.chat.Component

interface Attached<T> {
    fun attach(value: T): T

    val chatComponent: Component
}