package dev.deepslate.fallacy.client.particle.data

data class RecordField<T>(var value: T, var previous: T = value) {
    fun update() {
        previous = value
    }
}