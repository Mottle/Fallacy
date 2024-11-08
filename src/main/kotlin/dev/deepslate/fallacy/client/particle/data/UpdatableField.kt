package dev.deepslate.fallacy.client.particle.data

data class UpdatableField<T>(var value: T, var previous: T) {
    fun update() {
        previous = value
    }
}