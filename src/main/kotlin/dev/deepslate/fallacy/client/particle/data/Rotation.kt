package dev.deepslate.fallacy.client.particle.data

class Rotation {
    // 横滚角度
    private val yawField = UpdatableField(0f, 0f)

    var previousYaw: Float
        get() = yawField.previous
        set(value) {
            yawField.previous = value
        }

    var yaw: Float
        get() = yawField.value
        set(value) {
            yawField.value = value
        }

    // 俯仰角度
    private val pitchField = UpdatableField(0f, 0f)

    var previousPitch: Float
        get() = pitchField.previous
        set(value) {
            pitchField.previous = value
        }

    var pitch: Float
        get() = pitchField.value
        set(value) {
            pitchField.value = value
        }

    fun updateRotation() {
        yawField.update()
        pitchField.update()
    }
}