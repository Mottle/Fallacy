package dev.deepslate.fallacy.common.registrate

import com.tterrag.registrate.Registrate
import dev.deepslate.fallacy.Fallacy


object Registration {
    private var REGISTRATE: Registrate? = null

    fun get() = REGISTRATE

    fun init() {
        REGISTRATE = Registrate.create(Fallacy.MOD_ID)
    }
}

val REG: Registrate = Registration.get()!!