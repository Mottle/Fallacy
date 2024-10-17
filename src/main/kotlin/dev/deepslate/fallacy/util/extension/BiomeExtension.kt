package dev.deepslate.fallacy.util.extension

import dev.deepslate.fallacy.common.biome.data.BiomeSetting
import dev.deepslate.fallacy.inject.FallacyBiomeExtension
import net.minecraft.world.level.biome.Biome

val Biome.setting: BiomeSetting
    get() = (this as FallacyBiomeExtension).`fallacy$getSetting`()