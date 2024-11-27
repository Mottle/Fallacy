package dev.deepslate.fallacy.weather.current

//用于模拟天气的环流
//strength: 0.0 ~ 1.0
open class Current<P>(val type: Type, val strength: Float, val position: P) {

    enum class Type {
        WARM, COLD
    }

    //是否相交
//    fun intersects(other: Current): Boolean {
//        val dx = centerX - other.centerX
//        val dz = centerZ - other.centerZ
//        val distance = dx * dx + dz * dz
//        return distance <= (RADIUS * RADIUS)
//    }
}