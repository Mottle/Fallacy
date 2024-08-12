package dev.deepslate.fallacy.hydration.thirst

interface IThirst {
    var thirst: Float
    val max: Float

    fun drink(value: Float)

    fun tick()
}