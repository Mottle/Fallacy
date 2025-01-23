package dev.deepslate.fallacy.common.datapack

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.common.block.data.NPK
import dev.deepslate.fallacy.thermodynamics.ThermodynamicsEngine
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation

data class CropsConfiguration(val cropMap: Map<ResourceLocation, CropConfiguration>) {
    companion object {
        private val rangeCodec = RecordCodecBuilder.create { instance ->
            instance.group(
                Codec.INT.fieldOf("min").forGetter(Range::min),
                Codec.INT.fieldOf("max").forGetter(Range::max),
            ).apply(instance, ::Range)
        }

        private val configurationCodec = RecordCodecBuilder.create { instance ->
            instance.group(
                NPK.CODEC.fieldOf("npk_require").forGetter(CropConfiguration::npkRequire),
                rangeCodec.fieldOf("celsius_require").forGetter(CropConfiguration::celsiusRequire),
                rangeCodec.fieldOf("brightness_require").forGetter(CropConfiguration::brightnessRequire)
            ).apply(instance, ::CropConfiguration)
        }

        val CODEC: Codec<CropsConfiguration> =
            Codec.unboundedMap(ResourceLocation.CODEC, configurationCodec)
                .xmap(::CropsConfiguration, CropsConfiguration::cropMap)

        val CONFIG_KEY: ResourceKey<CropsConfiguration> =
            ResourceKey.create(DataPacks.CROP_REGISTRY_KEY, Fallacy.id("configuration"))

        val DEFAULT: CropConfiguration = CropConfiguration(
            NPK(2, 2, 2),
            Range(10, 40),
            Range(9, Int.MAX_VALUE)
        )

        private val defaultCropMap = mutableMapOf<ResourceLocation, CropConfiguration>()

        fun crop(namespacedId: ResourceLocation, configuration: CropConfiguration) {
            defaultCropMap[namespacedId] = configuration
        }

        fun npk(namespacedId: ResourceLocation, npk: NPK) {
            if (defaultCropMap.containsKey(namespacedId)) {
                defaultCropMap[namespacedId] = defaultCropMap[namespacedId]!!.copy(npkRequire = npk)
            } else {
                defaultCropMap[namespacedId] = DEFAULT.copy(npkRequire = npk)
            }
        }

        fun celsius(namespacedId: ResourceLocation, minCelsius: Int, maxCelsius: Int) {
            if (defaultCropMap.containsKey(namespacedId)) {
                defaultCropMap[namespacedId] =
                    defaultCropMap[namespacedId]!!.copy(celsiusRequire = Range(minCelsius, maxCelsius))
            } else {
                defaultCropMap[namespacedId] = DEFAULT.copy(celsiusRequire = Range(minCelsius, maxCelsius))
            }
        }

        fun brightness(namespacedId: ResourceLocation, minBrightness: Int, maxBrightness: Int) {
            if (defaultCropMap.containsKey(namespacedId)) {
                defaultCropMap[namespacedId] =
                    defaultCropMap[namespacedId]!!.copy(brightnessRequire = Range(minBrightness, maxBrightness))
            } else {
                defaultCropMap[namespacedId] = DEFAULT.copy(brightnessRequire = Range(minBrightness, maxBrightness))
            }
        }

        internal fun generateDefaultDataPack(): CropsConfiguration {
            val generated = CropsConfiguration(defaultCropMap.toMap())
            defaultCropMap.clear()
            return generated
        }
    }

    data class CropConfiguration(val npkRequire: NPK, val celsiusRequire: Range, val brightnessRequire: Range) {
        val heatRange: IntRange
            get() = ThermodynamicsEngine.fromFreezingPoint(celsiusRequire.min)..
                    ThermodynamicsEngine.fromFreezingPoint(celsiusRequire.max)
    }

    data class Range(val min: Int, val max: Int) : Iterable<Int> {
        override fun iterator(): Iterator<Int> = (min..max).iterator()
    }

    fun query(namespacedId: ResourceLocation): CropConfiguration = cropMap[namespacedId] ?: DEFAULT
}