package dev.deepslate.fallacy.util.attached

class CachedAttachedValue<T>(private var chain: AttachedChain<T>) {

    private var cached = chain.apply()

    val base: T
        get() = chain.base

    val value: T
        get() = cached

    fun attach(attached: Attached<T>) {
        chain = chain.addAttachment(attached)
    }

    fun rebuild() {
        cached = chain.apply()
    }

    fun attachAndRebuild(attached: Attached<T>) {
        attach(attached)
        rebuild()
    }

    val chatComponents = chain.chatComponents
}