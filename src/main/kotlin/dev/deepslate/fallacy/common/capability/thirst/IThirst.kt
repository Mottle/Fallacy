package dev.deepslate.fallacy.common.capability.thirst

interface IThirst {
    var value: Float
    val max: Float

    fun drink(value: Float)

    fun tick()
}